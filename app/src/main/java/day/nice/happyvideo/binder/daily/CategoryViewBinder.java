package day.nice.happyvideo.binder.daily;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.drakeet.multitype.ItemViewBinder;
import day.nice.happyvideo.R;
import day.nice.happyvideo.model.Category;

import static day.nice.happyvideo.R.id.date;

/**
 * @author zsj
 */

public class CategoryViewBinder extends ItemViewBinder<Category, CategoryViewBinder.DateHolder> {

    @NonNull @Override
    protected DateHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
                                            @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_category_title, parent, false);
        return new DateHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull DateHolder holder, @NonNull Category category) {
        holder.category.setText(category.categoryTitle);
    }

    class DateHolder extends RecyclerView.ViewHolder {
        TextView category;

        public DateHolder(View itemView) {
            super(itemView);
            category = (TextView) itemView.findViewById(date);
        }
    }
}
