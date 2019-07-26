package day.nice.happyvideo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import day.nice.happyvideo.widget.FabToggle;
import day.nice.happyvideo.widget.InsetDividerDecoration;
import day.nice.happyvideo.widget.ParallaxScrimageView;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import day.nice.happyvideo.R;
import day.nice.happyvideo.api.ReplyApi;
import day.nice.happyvideo.model.ItemList;
import day.nice.happyvideo.model.Replies;
import day.nice.happyvideo.model.ReplyList;
import day.nice.happyvideo.utils.CircleTransform;
import day.nice.happyvideo.utils.TimeUtils;

import static day.nice.happyvideo.MainActivity.PROVIDER_ITEM;


/**
 * @author zsj
 */

public class MovieDetailActivity extends RxAppCompatActivity implements View.OnClickListener {

    private ReplyApi replyApi;
    private List<ReplyList> datas = new ArrayList<>();
    private ItemList item;

    private FabToggle play;
    private ReplyAdapter adapter;
    private View movieDescription;

    private int fabOffset;
    private int lastId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.movie_detail_activity);
        RecyclerView replies = (RecyclerView) findViewById(R.id.recycler_replies);
        final ParallaxScrimageView backdrop = (ParallaxScrimageView) findViewById(R.id.backdrop);
        final ImageButton back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(this);

        item = getIntent().getParcelableExtra(PROVIDER_ITEM);

        movieDescription = LayoutInflater.from(this)
                .inflate(R.layout.item_movie_detail_header, replies, false);
        final TextView title = (TextView) movieDescription.findViewById(R.id.movie_title);
        final TextView type = (TextView) movieDescription.findViewById(R.id.movie_type);
        final TextView description = (TextView) movieDescription.findViewById(R.id.movie_desc);
        final ImageView author = (ImageView) movieDescription.findViewById(R.id.author);
        author.setOnClickListener(this);
        final TextView name = (TextView) movieDescription.findViewById(R.id.name);
        final LinearLayout authorContent = (LinearLayout) movieDescription.findViewById(R.id.author_content);

        title.setText(item.data.title);
        type.setText(item.data.category + " | " + TimeUtils.secToTime((int) item.data.duration));
        description.setText(item.data.description);

        if (item.data.author != null) {
            authorContent.setVisibility(View.VISIBLE);
            name.setText(item.data.author.name);
            Glide.with(this)
                    .load(item.data.author.icon)
                    .transform(new CircleTransform(this))
                    .into(author);
        }

        play = (FabToggle) findViewById(R.id.fab_play);
        play.setOnClickListener(this);

        backdrop.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                backdrop.getViewTreeObserver().removeOnPreDrawListener(this);
                fabOffset = backdrop.getHeight() - play.getHeight() / 2 + title.getMeasuredHeight();
                play.setOffset(fabOffset);
                return true;
            }
        });

        Picasso.with(this)
                .load(item.data.cover.detail)
                .into(backdrop);

        replyApi = RetrofitFactory.getRetrofit().createApi(ReplyApi.class);

        adapter = new ReplyAdapter(datas, movieDescription);
        replies.addItemDecoration(new InsetDividerDecoration(
                ReplyAdapter.Holder.class,
                getResources().getDimensionPixelSize(R.dimen.divider_height),
                getResources().getDimensionPixelSize(R.dimen.keyline_1),
                ContextCompat.getColor(this, R.color.divider)
        ));
        replies.setAdapter(adapter);

        final LinearLayoutManager layoutManager = (LinearLayoutManager) replies.getLayoutManager();

        replies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                backdrop.setOffset(movieDescription.getTop());
                play.setOffset(fabOffset + movieDescription.getTop());
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (layoutManager.findFirstVisibleItemPosition() != 0
                        && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (lastId != 0) loadReplies();
                }
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadReplies(true);
    }

    private void loadReplies() {
        loadReplies(false /*Load more data.*/);
    }

    private void loadReplies(boolean clean) {
        Flowable<Replies> result;
        if (clean) result = replyApi.fetchReplies(item.data.id);
        else result = replyApi.fetchReplies(item.data.id, lastId);

        result.compose(bindToLifecycle())
                .map(replies -> {
                    getLastId(replies.replyList);
                    return replies.replyList;
                })
                .flatMap(Flowable::fromIterable)
                .doOnNext(replyList -> datas.add(replyList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> adapter.notifyDataSetChanged())
                .subscribe(replyList -> {}, Throwable::printStackTrace);
    }

    private void getLastId(List<ReplyList> replies) {
        if (replies.size() == 0) return;
        lastId = replies.get(replies.size() - 1).sequence;
    }

    @Override
    public void onBackPressed() {
        play.animate()
                .scaleX(0)
                .scaleY(0)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finishAfterTransition();
                    }
                })
                .start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finishAfterTransition();
                break;
            case R.id.fab_play:
                Intent intent = new Intent(this, PlayActivity.class);
                intent.putExtra("item", item);
                startActivity(intent);
                break;
            case R.id.author:
                Intent relatedIntent = new Intent(this, RelatedActivity.class);
                relatedIntent.putExtra(RelatedActivity.ID, item.data.id);
                startActivity(relatedIntent);
                break;
        }
    }

}
