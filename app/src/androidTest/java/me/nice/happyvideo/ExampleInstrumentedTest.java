package me.nice.happyvideo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import day.nice.happyvideo.RetrofitFactory;
import day.nice.happyvideo.api.DailyApi;
import day.nice.happyvideo.api.ReplyApi;
import day.nice.happyvideo.api.SearchApi;
import day.nice.happyvideo.model.ItemList;
import day.nice.happyvideo.model.Replies;
import day.nice.happyvideo.model.ReplyList;
import day.nice.happyvideo.model.SearchResult;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;


/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("me.zsj.interessant", appContext.getPackageName());
    }

    @Test public void dailyApi() throws Exception {

        DailyApi dailyApi = RetrofitFactory.getRetrofit().createApi(DailyApi.class);

        //assertNotNull(dailyApi);
    }

    @Test public void testResult() throws Exception {
        SearchApi searchApi = RetrofitFactory.getRetrofit().createApi(SearchApi.class);

        searchApi.query("清新", 0)
                .filter(new Func1<SearchResult, Boolean>() {
                    @Override
                    public Boolean call(SearchResult searchResult) {
                        return searchResult != null;
                    }
                })
                .map(new Func1<SearchResult, List<ItemList>>() {
                    @Override
                    public List<ItemList> call(SearchResult searchResult) {
                        //assertNotNull(searchResult.itemList);
                        return searchResult.itemList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ItemList>>() {
                    @Override
                    public void call(List<ItemList> itemLists) {}
                });
    }

    @Test public void testReplyList() throws Exception {
        final ReplyApi replyApi = RetrofitFactory.getRetrofit().createApi(ReplyApi.class);
        replyApi.fetchReplies(9962)
                .filter(new Func1<Replies, Boolean>() {
                    @Override
                    public Boolean call(Replies replies) {
                        return replies != null;
                    }
                })
                .map(new Func1<Replies, List<ReplyList>>() {
                    @Override
                    public List<ReplyList> call(Replies replies) {
                        Log.d("ExampleInstrumentedTest", replies.replyList.toString());
                        //assertNotNull(replies.replyList);
                        return replies.replyList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ReplyList>>() {
                    @Override
                    public void call(List<ReplyList> replyLists) {
                    }
                });
    }
}
