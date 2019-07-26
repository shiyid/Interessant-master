package day.nice.happyvideo;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Pair;
import android.view.View;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import day.nice.happyvideo.R;
import day.nice.happyvideo.model.ItemList;

/**
 * @author zsj
 */

public class IntentManager {

    public static void flyToMovieDetail(final Activity context,
                                        final ItemList item, final View view) {
        Picasso.with(context).load(item.data.cover.detail)
                .fetch(new Callback() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(MainActivity.PROVIDER_ITEM, item);
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                                context,
                                Pair.create(view, context.getString(R.string.transition_shot)),
                                Pair.create(view, context.getString(R.string.transition_shot_background))
                        );
                        context.startActivity(intent, options.toBundle());
                    }

                    @Override
                    public void onError() {
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(MainActivity.PROVIDER_ITEM, item);
                        context.startActivity(intent);
                    }
                });
    }
}
