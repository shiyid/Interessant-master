package day.nice.happyvideo.binder.related;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import me.drakeet.multitype.ItemViewBinder;
import day.nice.happyvideo.R;
import day.nice.happyvideo.VideoListActivity;
import day.nice.happyvideo.interesting.InterestingActivity;
import day.nice.happyvideo.model.Header;
import day.nice.happyvideo.utils.IDUtils;

import static day.nice.happyvideo.MainActivity.CATEGORY_ID;

/**
 * @author zsj
 */

public class RelatedHeaderViewBinder extends
        ItemViewBinder<RelatedHeader, RelatedHeaderViewBinder.RelatedHeaderHolder> {


    @NonNull @Override
    protected RelatedHeaderHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_category_header, parent, false);
        return new RelatedHeaderHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull RelatedHeaderHolder holder,
                                    @NonNull RelatedHeader relatedHeader) {
        Header header = relatedHeader.header;

        holder.categoryTitle.setText(header.title);

        RxView.clicks(holder.content)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(aVoid ->
                        toInteresting(holder.content.getContext(), header.id, relatedHeader.related));
    }

    private void toInteresting(Context context, int id, boolean related) {
        if (IDUtils.isDetermined(id) && !related) {
            Intent videoIntent = new Intent(context, VideoListActivity.class);
            videoIntent.putExtra("id", id);
            videoIntent.putExtra("newest", true);
            context.startActivity(videoIntent);
        } else {
            Intent interestingIntent = new Intent(context, InterestingActivity.class);
            interestingIntent.putExtra(CATEGORY_ID, id);
            interestingIntent.putExtra(InterestingActivity.RELATED_VIDEO, true);
            context.startActivity(interestingIntent);
        }
    }

    static class RelatedHeaderHolder extends RecyclerView.ViewHolder {

        FrameLayout content;
        TextView categoryTitle;

        public RelatedHeaderHolder(View itemView) {
            super(itemView);
            content = (FrameLayout) itemView.findViewById(R.id.category_content);
            categoryTitle = (TextView) itemView.findViewById(R.id.category_title);
        }
    }
}
