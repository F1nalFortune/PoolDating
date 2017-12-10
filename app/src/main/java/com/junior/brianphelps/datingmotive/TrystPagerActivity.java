package com.junior.brianphelps.datingmotive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by brianphelps on 12/2/17.
 */

public class TrystPagerActivity extends AppCompatActivity implements TrystFragment.Callbacks  {
    private static final String EXTRA_TRYST_ID=
            "com.junior.brianphelps.datingmotive.tryst_id";

    private ViewPager mViewPager;
    private List<Tryst> mTrysts;

    public static Intent newIntent(Context packageContext, UUID trystId) {
        Intent intent = new Intent(packageContext, TrystPagerActivity.class);
        intent.putExtra(EXTRA_TRYST_ID, trystId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tryst_pager);

        UUID trystId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_TRYST_ID);

        mViewPager = (ViewPager) findViewById(R.id.tryst_view_pager);

        mTrysts = TrystList.get(this).getTrysts();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Tryst tryst = mTrysts.get(position);
                return TrystFragment.newInstance(tryst.getId());
            }

            @Override
            public int getCount() {
                return mTrysts.size();
            }
        });

        for (int i = 0; i < mTrysts.size(); i++) {
            if (mTrysts.get(i).getId().equals(trystId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onTrystUpdated(Tryst tryst) {

    }
}
