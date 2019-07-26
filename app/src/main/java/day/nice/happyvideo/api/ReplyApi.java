package day.nice.happyvideo.api;

import io.reactivex.Flowable;
import day.nice.happyvideo.model.Replies;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author zsj
 */

public interface ReplyApi {

    @GET("v1/replies/video")
    Flowable<Replies> fetchReplies(@Query("id") int id);

    @GET("v1/replies/video?num=10")
    Flowable<Replies> fetchReplies(@Query("id") int id, @Query("lastId") int lastId);

}
