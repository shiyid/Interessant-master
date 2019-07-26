package day.nice.happyvideo.interesting;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import day.nice.happyvideo.IntentManager;
import day.nice.happyvideo.R;
import day.nice.happyvideo.common.Holder;
import day.nice.happyvideo.model.ItemList;

/**
 * @author zsj
 */

public class InterestingAdapter extends RecyclerView.Adapter<Holder> {

    private static final int TOUCH_TIME = 1000;
    public static final String VIDEO_TYPE = "video";

    private Activity context;
    private List<ItemList> itemList;

    public InterestingAdapter(Activity context, List<ItemList> itemLists) {
        this.context = context;
        this.itemList = itemLists;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final ItemList item = itemList.get(position);

        if (!item.type.equals(VIDEO_TYPE)) {
            holder.movieContent.setVisibility(View.GONE);
        } else {

            holder.movieAlbum.setOriginalSize(16, 9);
            Glide.with(holder.movieAlbum.getContext())
                    .load(item.data.cover.detail)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.movieAlbum);
            holder.movieDesc.setText(item.data.title);

//            if (item.data.author != null) {
//                holder.tag.setVisibility(View.VISIBLE);
//                holder.tag.setText(item.data.author.name);
//            } else {
                holder.tag.setVisibility(View.GONE);
//            }

            RxView.clicks(holder.movieContent).throttleFirst(TOUCH_TIME, TimeUnit.MILLISECONDS)
                    .subscribe(aVoid -> fly(holder.movieAlbum, item));
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private void fly(View view, ItemList item) {
        IntentManager.flyToMovieDetail(context, item, view);
    }

}
