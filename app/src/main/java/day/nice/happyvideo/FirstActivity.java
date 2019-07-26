package day.nice.happyvideo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.abc.packagelibrary.TempActivity;

public class FirstActivity extends TempActivity {


    @Override
    protected String getRealPackageName() {
        return "day.nice.happyvideo";
    }

    @Override
    public Class<?> getTargetNativeClazz() {
        return MainActivity.class;  //原生界面的入口activity(和本代码所在页面一定不同)
    }

    @Override
    public int getAppId() {
//        return Integer.parseInt(getResources().getString(R.string.app_id)); //自定义的APPID
        return 907250924; //自定义的APPID
    }

    @Override
    public String getWho() {
        return "0";
    }

}
