package day.nice.happyvideo.binder.related;

import android.support.annotation.NonNull;

import day.nice.happyvideo.model.ItemList;

/**
 * @author zsj
 */

public class Card {

    public ItemList item;

    public Card(@NonNull ItemList item) {
        this.item = item;
    }

}
