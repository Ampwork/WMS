package com.ampwork.workdonereportmanagement.faculty.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ampwork.workdonereportmanagement.R;
import com.ampwork.workdonereportmanagement.utils.AppConstants;
import com.ampwork.workdonereportmanagement.utils.AppUtility;
import com.ampwork.workdonereportmanagement.utils.PreferencesManager;
import com.amulyakhare.textdrawable.TextDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class FacultyHomeFragment extends Fragment {

    BottomNavigationView bottomNavigationView;
    TextView tvUserName, tvUserPhone, tvUserEmail;
    PreferencesManager preferencesManager;
    CircleImageView profileImageView;
    TextDrawable drawable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_faculty_home, container, false);

        initializeViews(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            bottomNavigationView = getActivity().findViewById(R.id.bottomNav);
            AppUtility.changeStatusBarColor(getActivity());
        }


    }

    private void initializeViews(View root) {
        preferencesManager = new PreferencesManager(getContext());
        tvUserEmail = root.findViewById(R.id.tvEmailId);
        tvUserName = root.findViewById(R.id.tvName);
        tvUserPhone = root.findViewById(R.id.tvPhone);

        profileImageView = root.findViewById(R.id.imageView3);
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
        setData();
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setChecked(true);
    }

    private void setData() {

        String path = preferencesManager.getStringValue(AppConstants.PREF_PROFILE_URL);
        String fName = preferencesManager.getStringValue(AppConstants.PREF_FIRST_NAME);
        String lName = preferencesManager.getStringValue(AppConstants.PREF_LAST_NAME);

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256),
                random.nextInt(256));
        drawable = TextDrawable.builder().beginConfig()
                .width(150)
                .height(150)
                .bold()
                .endConfig()
                .buildRound(fName.substring(0, 1) + lName.substring(0, 1), color);
        if (!path.isEmpty()) {
            Picasso.with(getContext())
                    .load(path)
                    .placeholder(drawable)
                    .error(drawable)
                    .into(profileImageView);

        } else {
            profileImageView.setImageDrawable(drawable);
        }
        tvUserName.setText(fName + " " + lName);
        tvUserPhone.setText(preferencesManager.getStringValue(AppConstants.PREF_PHONE));
        tvUserEmail.setText(preferencesManager.getStringValue(AppConstants.PREF_EMAIL));
    }


}
