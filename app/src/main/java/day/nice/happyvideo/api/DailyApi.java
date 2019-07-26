package day.nice.happyvideo.api;

import io.reactivex.Flowable;
import day.nice.happyvideo.model.Daily;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author zsj
 */

public interface DailyApi {

    @GET("v2/feed?num=2")
    Flowable<Daily> getDaily(@Query("date") long date);

    @GET("v2/feed?num=2")
    Flowable<Daily> getDaily();

}
