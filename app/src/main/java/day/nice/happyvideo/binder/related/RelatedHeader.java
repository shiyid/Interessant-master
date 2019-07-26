package day.nice.happyvideo.binder.related;

import android.support.annotation.NonNull;

import day.nice.happyvideo.model.Header;

/**
 * @author zsj
 */

public class RelatedHeader {

    public Header header;
    public boolean related;

    public RelatedHeader(@NonNull Header header, boolean related) {
        this.header = header;
        this.related = related;
    }

}
