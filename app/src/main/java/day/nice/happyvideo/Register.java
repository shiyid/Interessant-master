package day.nice.happyvideo;

import android.app.Activity;

import day.nice.happyvideo.binder.daily.CategoryViewBinder;
import day.nice.happyvideo.binder.daily.DailyViewBinder;
import day.nice.happyvideo.binder.related.Card;
import day.nice.happyvideo.binder.related.CardViewBinder;
import day.nice.happyvideo.binder.related.HeaderItem;
import day.nice.happyvideo.binder.related.HeaderViewBinder;
import day.nice.happyvideo.binder.related.RelatedHeader;
import day.nice.happyvideo.binder.related.RelatedHeaderViewBinder;
import day.nice.happyvideo.binder.video.FooterForward;
import day.nice.happyvideo.binder.video.FooterForwardViewBinder;
import day.nice.happyvideo.binder.video.VideoViewBinder;
import me.drakeet.multitype.MultiTypeAdapter;
import day.nice.happyvideo.model.Category;
import day.nice.happyvideo.model.ItemList;

/**
 * @author zsj.
 */

public class Register {

    public static void registerItem(MultiTypeAdapter adapter, Activity context) {
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(ItemList.class, new DailyViewBinder(context));
    }

    public static void registerRelatedItem(MultiTypeAdapter adapter, Activity context) {
        registerCommonItem(adapter, context);
    }

    public static void registerFindItem(MultiTypeAdapter adapter, Activity context) {
        adapter.register(FooterForward.class, new FooterForwardViewBinder());
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(ItemList.class, new VideoViewBinder(context));
        registerCommonItem(adapter, context);
    }

    public static void registerAuthorItem(MultiTypeAdapter adapter, Activity context) {
        adapter.register(Category.class, new CategoryViewBinder());
        adapter.register(ItemList.class, new VideoViewBinder(context));
        registerCommonItem(adapter, context);
    }

    private static void registerCommonItem(MultiTypeAdapter adapter, Activity context) {
        adapter.register(HeaderItem.class, new HeaderViewBinder());
        adapter.register(Card.class, new CardViewBinder(context));
        adapter.register(RelatedHeader.class, new RelatedHeaderViewBinder());
    }

}
