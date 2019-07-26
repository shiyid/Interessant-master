package day.nice.happyvideo.api;

import io.reactivex.Flowable;
import day.nice.happyvideo.model.Related;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author zsj
 */

public interface RelatedApi {

    @GET("v3/video/{id}/detail/related")
    Flowable<Related> related(@Path("id") int id);

}
