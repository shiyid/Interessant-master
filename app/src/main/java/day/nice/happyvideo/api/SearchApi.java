package day.nice.happyvideo.api;

import java.util.List;

import day.nice.happyvideo.model.SearchResult;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author zsj
 */

public interface SearchApi {

    @GET("v3/queries/hot")
    Flowable<List<String>> getTrendingTag();

    @GET("v1/search?num=10")
    Flowable<SearchResult> query(@Query("query") String key, @Query("start") int start);

}
