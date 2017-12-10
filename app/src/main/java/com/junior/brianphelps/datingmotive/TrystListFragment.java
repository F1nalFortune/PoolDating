package com.junior.brianphelps.datingmotive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by brianphelps on 12/1/17.
 */

public class TrystListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private RecyclerView mTrystRecyclerView;
    private TrystAdapter mAdapter;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onTrystSelected(Tryst tryst);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tryst_list, container, false);

        mTrystRecyclerView = (RecyclerView) view
                .findViewById(R.id.tryst_recycler_view);
        mTrystRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_tryst_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_tryst:
                Tryst tryst = new Tryst();
                TrystList.get(getActivity()).addTryst(tryst);
                updateUI();
                mCallbacks.onTrystSelected(tryst);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        TrystList trystList = TrystList.get(getActivity());
        int trystCount = trystList.getTrysts().size();
        String subtitle = getString(R.string.subtitle_format, trystCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }


    public void updateUI() {
        TrystList trystList = TrystList.get(getActivity());
        List<Tryst> trysts = trystList.getTrysts();

        if (mAdapter == null) {
            mAdapter = new TrystAdapter(trysts);
            mTrystRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setTrysts(trysts);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class TrystHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Tryst mTryst;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mTakenImageView;

        public TrystHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_tryst, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.tryst_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.tryst_date);
            mTakenImageView = (ImageView) itemView.findViewById(R.id.tryst_taken);
        }


        public void bind(Tryst tryst) {
            mTryst = tryst;
            mTitleTextView.setText(mTryst.getTitle());
            mDateTextView.setText(mTryst.getDate().toString());
            mTakenImageView.setVisibility(tryst.isTaken() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view) {
            mCallbacks.onTrystSelected(mTryst);
        }
    }

    private class TrystAdapter extends RecyclerView.Adapter<TrystHolder> {

        private List<Tryst> mTrysts;

        public TrystAdapter(List<Tryst> trysts) {
            mTrysts = trysts;
        }

        @Override
        public TrystHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new TrystHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(TrystHolder holder, int position) {
            Tryst tryst = mTrysts.get(position);
            holder.bind(tryst);
        }

        @Override
        public int getItemCount() {
            return mTrysts.size();
        }

        public void setTrysts(List<Tryst> trysts) {
            mTrysts = trysts;
        }
    }
}
