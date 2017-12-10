package com.junior.brianphelps.datingmotive;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by brianphelps on 12/1/17.
 */

public class TrystListActivity extends SingleFragmentActivity implements TrystListFragment.Callbacks, TrystFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new TrystListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onTrystSelected(Tryst tryst) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = TrystPagerActivity.newIntent(this, tryst.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = TrystFragment.newInstance(tryst.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    public void onTrystUpdated(Tryst tryst) {
        TrystListFragment listFragment = (TrystListFragment)
                getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
