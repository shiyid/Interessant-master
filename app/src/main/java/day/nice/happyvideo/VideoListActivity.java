package day.nice.happyvideo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;

import java.util.ArrayList;
import java.util.List;

import day.nice.happyvideo.interesting.InterestingAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import day.nice.happyvideo.R;
import day.nice.happyvideo.api.InterestingApi;
import day.nice.happyvideo.base.ToolbarActivity;
import day.nice.happyvideo.model.ItemList;
import day.nice.happyvideo.rx.ErrorAction;
import day.nice.happyvideo.rx.RxScroller;

/**
 * @author zsj
 */

public class VideoListActivity extends ToolbarActivity {

    private RecyclerView list;

    private InterestingAdapter adapter;
    private InterestingApi api;

    private List<ItemList> items = new ArrayList<>();

    private int start;
    private int id;
    private boolean trending;
    private boolean newest;
    private boolean firstIn = true;

    @Override
    public int providerLayoutId() {
        return R.layout.video_list_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = (RecyclerView) findViewById(R.id.list);

        api = RetrofitFactory.getRetrofit().createApi(InterestingApi.class);

        id = getIntent().getExtras().getInt("id");
        trending = getIntent().getBooleanExtra("trending", false);
        newest = getIntent().getBooleanExtra("newest", false);

        if (trending) ab.setTitle("Trending");
        else if (newest) ab.setTitle("Newest");

        adapter = new InterestingAdapter(this, items);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);

        list.setAdapter(adapter);

        RxRecyclerView.scrollStateChanges(list)
                .compose(bindToLifecycle())
                .compose(RxScroller.scrollTransformer(layoutManager,
                        adapter, items))
                .subscribe(newState -> {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        start += 10;
                        loadVideos();
                    }
                });

        loadVideos(true);
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        if (firstIn) {
            list.scheduleLayoutAnimation();
            firstIn = false;
        }
    }

    private void loadVideos() {
        loadVideos(false);
    }

    private void loadVideos(boolean clean) {
        String strategy = null;
        if (trending) strategy = "mostPopular";
        else if (newest) strategy = "date";

        api.videoList(id, start, strategy)
                .compose(bindToLifecycle())
                .filter(interesting -> interesting != null)
                .filter(interesting -> interesting.itemList != null)
                .map(interesting -> interesting.itemList)
                .doOnNext(itemLists -> { if (clean) items.clear(); })
                .doOnNext(itemLists -> items.addAll(itemLists))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemLists -> {
                    adapter.notifyDataSetChanged();
                }, ErrorAction.error(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
