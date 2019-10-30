package com.deyvitineo.instagram.Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.deyvitineo.instagram.R;
import com.deyvitineo.instagram.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";
    private ImageView profileImage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        profileImage = view.findViewById(R.id.profilePhoto);
        initImageLoader();
        setProfileImage();

        return view;
    }

    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getActivity());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setProfileImage(){
        Log.d(TAG, "setProfileImage: Setting profile image");
        String imgURL = "thenypost.files.wordpress.com/2017/05/oscars_blacks.jpg?quality=90&strip=all&w=618&h=410&crop=1";
        String append = "https://";

        UniversalImageLoader.setImage(imgURL, profileImage, null, append);


    }
}
