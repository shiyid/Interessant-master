package day.nice.happyvideo;

import android.os.Bundle;
import android.support.annotation.Nullable;

import day.nice.happyvideo.R;
import day.nice.happyvideo.base.ToolbarActivity;

/**
 * @author zsj
 */

public class SettingsActivity extends ToolbarActivity {

    @Override
    public int providerLayoutId() {
        return R.layout.settings_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar.setNavigationOnClickListener(v -> finish());

        getFragmentManager().beginTransaction()
                .replace(R.id.container, new SettingsFragment())
                .commit();

    }

}
