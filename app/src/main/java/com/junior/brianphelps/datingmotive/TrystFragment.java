package com.junior.brianphelps.datingmotive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.junior.brianphelps.datingmotive.TrystListFragment.Callbacks;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.widget.CompoundButton.*;

/**
 * Created by brianphelps on 12/1/17.
 */

public class TrystFragment extends Fragment {

    private static final String ARG_TRYST_ID = "tryst_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO= 2;
    private Tryst mTryst;
    private File mPhotoFile;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mTakenCheckBox;
    private Button mFriendButton;
    private Button mPropositionButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities
     */

    public interface Callbacks {
        void onTrystUpdated(Tryst tryst);
    }

    public static TrystFragment newInstance(UUID trystId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRYST_ID, trystId);

        TrystFragment fragment = new TrystFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID trystId = (UUID) getArguments().getSerializable(ARG_TRYST_ID);
        mTryst = TrystList.get(getActivity()).getTryst(trystId);
        mPhotoFile = TrystList.get(getActivity()).getPhotoFile(mTryst);
    }

    @Override
    public void onPause() {
        super.onPause();

        TrystList.get(getActivity())
                .updateTryst(mTryst);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tryst, container, false);

        mTitleField = (EditText) v.findViewById(R.id.tryst_title);
        mTitleField.setText(mTryst.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mTryst.setTitle(s.toString());
                updateTryst();
            }
            @Override
            public void afterTextChanged(Editable s) {
                // this one
            }

        });

        mDateButton = (Button) v.findViewById(R.id.tryst_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mTryst.getDate());
                dialog.setTargetFragment(TrystFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });


        mTakenCheckBox = (CheckBox)v.findViewById(R.id.tryst_taken);
        mTakenCheckBox.setChecked(mTryst.isTaken());
        mTakenCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                mTryst.setTaken(isChecked);
                updateTryst();
            }
        });

        mPropositionButton = (Button) v.findViewById(R.id.tryst_proposition);
        mPropositionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getTrystProposition());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.tryst_proposition_subject));
                i = Intent.createChooser(i, getString(R.string.send_proposition));
                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        mFriendButton = (Button) v.findViewById(R.id.tryst_friend);
        mFriendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if (mTryst.getFriend() != null) {
            mFriendButton.setText(mTryst.getFriend());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mFriendButton.setEnabled(false);
        }

        mPhotoButton = (ImageButton) v.findViewById(R.id.tryst_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.junior.brianphelps.datingmotive.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.tryst_photo);
        updatePhotoView();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mTryst.setDate(date);
            updateTryst();
            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            // specify which fields query to return
            // values for
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            //perform query - contactUri is like a 'where'
            // clause here
            Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);

            try {
                // Double-check that you actually got results
                if (c.getCount() == 0) {
                    return;
                }

                // Pull out the first column of the first row of data
                // that is your friend's name
                c.moveToFirst();
                String friend = c.getString(0);
                mTryst.setFriend(friend);
                updateTryst();
                mFriendButton.setText(friend);
            } finally {
                c.close();
            }
        } else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.junior.brianphelps.datingmotive.fileprovider",
                    mPhotoFile);

            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updateTryst();
            updatePhotoView();
        }
    }

    private void updateTryst() {
        TrystList.get(getActivity()).updateTryst(mTryst);
        mCallbacks.onTrystUpdated(mTryst);
    }

    private void updateDate() {
        mDateButton.setText(mTryst.getDate().toString());
    }

    private String getTrystProposition() {
        String takenString = null;
        if (mTryst.isTaken()) {
            takenString = getString(R.string.tryst_proposition_taken);
        } else {
            takenString = getString(R.string.tryst_proposition_not_taken);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat,
                mTryst.getDate()).toString();

        String friend = mTryst.getFriend();
        if (friend == null) {
            friend = getString(R.string.tryst_proposition_no_friend);
        } else {
            friend = getString(R.string.tryst_proposition_friend, friend);
        }

        String proposition = getString(R.string.tryst_proposition,
                mTryst.getTitle(), dateString, takenString, friend);

        return proposition;
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
            mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }
}