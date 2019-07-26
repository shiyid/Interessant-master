package day.nice.happyvideo.api;

import day.nice.happyvideo.model.VideoAuthor;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author zsj
 */

public interface AuthorApi {

    @GET("v4/pgcs/all?num=10")
    Flowable<VideoAuthor> authors(@Query("start") int start);

}
