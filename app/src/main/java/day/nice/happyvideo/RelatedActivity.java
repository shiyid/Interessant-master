package day.nice.happyvideo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import day.nice.happyvideo.binder.related.Card;
import day.nice.happyvideo.binder.related.HeaderItem;
import day.nice.happyvideo.binder.related.RelatedHeader;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import day.nice.happyvideo.R;
import day.nice.happyvideo.api.RelatedApi;
import day.nice.happyvideo.model.Header;
import day.nice.happyvideo.model.ItemList;
import day.nice.happyvideo.rx.ErrorAction;

/**
 * @author zsj
 */

public class RelatedActivity extends RxAppCompatActivity {

    public static final String ID = "id";

    private MultiTypeAdapter adapter;
    private Items items = new Items();
    private RelatedApi relatedApi;

    private int id;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.related_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> finishAfterTransition());

        RecyclerView list = (RecyclerView) findViewById(R.id.related_list);

        adapter = new MultiTypeAdapter(items);
        list.setAdapter(adapter);

        Register.registerRelatedItem(adapter, this);

        id = getIntent().getIntExtra(ID, id);
        relatedApi = RetrofitFactory.getRetrofit().createApi(RelatedApi.class);

        loadRelated();
    }

    private void loadRelated() {
        relatedApi.related(id)
                .compose(bindToLifecycle())
                .filter(related -> related != null)
                .filter(related -> related.itemList != null)
                .flatMap(related -> Flowable.fromIterable(related.itemList))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> adapter.notifyDataSetChanged())
                .subscribe(this::addData, ErrorAction.error(this));
    }

    private void addData(ItemList item) {
        Header header = item.data.header;
        if (header != null) {
            if (header.description != null) {
                items.add(new HeaderItem(item.data.header, true));
            } else {
                items.add(new RelatedHeader(item.data.header, true));
            }
            items.add(new Card(item));
        }
    }

}
