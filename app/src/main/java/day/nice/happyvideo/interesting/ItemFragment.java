package day.nice.happyvideo.interesting;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import day.nice.happyvideo.R;
import day.nice.happyvideo.RetrofitFactory;
import day.nice.happyvideo.api.InterestingApi;
import day.nice.happyvideo.model.Interesting;
import day.nice.happyvideo.model.ItemList;

/**
 * @author zsj
 */

public class ItemFragment extends RxFragment {

    RecyclerView list;
    LinearLayoutManager layoutManager;

    InterestingApi interestingApi;
    int start = 0;
    Activity context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (Activity) context;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.interesting_list_fragment, container, false);
        list = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layoutManager = new LinearLayoutManager(context);
        interestingApi = RetrofitFactory.getRetrofit().createApi(InterestingApi.class);
    }

    FlowableTransformer<Interesting, List<ItemList>>
            interestingTransformer = interestingObservable -> interestingObservable
                    .filter(interesting -> interesting != null)
                    .map(interesting -> interesting.itemList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

}
