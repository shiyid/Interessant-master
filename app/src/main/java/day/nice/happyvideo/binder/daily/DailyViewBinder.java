package day.nice.happyvideo.binder.daily;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import me.drakeet.multitype.ItemViewBinder;
import day.nice.happyvideo.IntentManager;
import day.nice.happyvideo.R;
import day.nice.happyvideo.common.Holder;
import day.nice.happyvideo.model.ItemList;

/**
 * @author zsj
 */

public class DailyViewBinder extends ItemViewBinder<ItemList, Holder> {

    private static final String VIDEO_TAG = "video";

    private Activity context;

    public DailyViewBinder(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected Holder onCreateViewHolder(@NonNull LayoutInflater inflater,
                                        @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_movie, parent, false);
        return new Holder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final Holder holder,
                                    @NonNull final ItemList item) {

        if (item.type.contains(VIDEO_TAG)) {
            holder.movieContent.setVisibility(View.VISIBLE);
            holder.movieAlbum.setOriginalSize(16, 9);
            Glide.with(holder.movieAlbum.getContext())
                    .load(item.data.cover.detail)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.movieAlbum);
            holder.movieDesc.setText(item.data.title);
            holder.tag.setVisibility(View.GONE);
        } else {
            holder.movieContent.setVisibility(View.GONE);
        }

        RxView.clicks(holder.movieContent)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(aVoid -> fly(holder.movieAlbum, item));
    }

    private void fly(View view, ItemList item) {
        IntentManager.flyToMovieDetail(context, item, view);
    }

}
