package com.junior.brianphelps.datingmotive;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class DateActivity extends SingleFragmentActivity {

    private static final String EXTRA_TRYST_ID =
            "com.junior.brianphelps.datingmotive.tryst_id";

    public static Intent newIntent(Context packageContext, UUID trystId) {
        Intent intent = new Intent(packageContext, DateActivity.class);
        intent.putExtra(EXTRA_TRYST_ID, trystId);
        return intent;
    }


    @Override
    protected Fragment createFragment() {
        UUID trystId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_TRYST_ID);
        return TrystFragment.newInstance(trystId);
    }
}