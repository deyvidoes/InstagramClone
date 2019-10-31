package com.deyvitineo.instagram.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.deyvitineo.instagram.R;
import com.deyvitineo.instagram.Utils.BottomNavigationViewHelper;
import com.deyvitineo.instagram.Utils.GridImageAdapter;
import com.deyvitineo.instagram.Utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    private Context mContext = ProfileActivity.this;
    private static final int ACTIVITY_NUM = 4;
    private ImageView profilePicture;
    private static final int NUM_GRID_COLUMNS = 3;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: Started");
        setupActivityWidgets();
        setProfileImage();

        setUpBottomNavigationView();
        setupToolBar();

        tempGridSetup();
    }

    private void tempGridSetup(){
        ArrayList<String> imgURLs = new ArrayList<>();
        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQMpb_UOvv-EOZGXlOdrqPDkwci2JjlDQ66O9PqsJrD4rwBnC8C&s");
        imgURLs.add("https://sportshub.cbsistatic.com/i/r/2019/10/24/c74bb5c3-0e0a-47ed-a7fe-f92710d8f9e2/thumbnail/1200x675/6c3b32465605c348e298c4cd46529d4d/steph-curry.jpg");
        imgURLs.add("https://cdn.britannica.com/16/77316-050-827BA4E2/Denzel-Washington-Training-Day.jpg");
        imgURLs.add("https://pmcdeadline2.files.wordpress.com/2019/06/mkx_2808_20190606102742488-copy-2-1024x683-e1559955128476.jpg?crop=0px%2C32px%2C1024px%2C574px&resize=681%2C383");
        imgURLs.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR1YTcu93AQj4ZJvSnOUruzWlTWxqvyUwDVdsW2EOjcfdf1uu6I0Q&s");
        imgURLs.add("https://media1.fdncms.com/sevendaysvt/imager/u/original/10836759/movie-reveiw1.jpg");
        imgURLs.add("https://images-na.ssl-images-amazon.com/images/I/51kcqbcnGzL.jpg");
        imgURLs.add("https://images-na.ssl-images-amazon.com/images/I/51mSo7oefcL.jpg");
        imgURLs.add("https://m.media-amazon.com/images/M/MV5BMTQ2MzE2NTk0NF5BMl5BanBnXkFtZTgwOTM3NTk1MjE@._V1_.jpg");
        imgURLs.add("https://static.squarespace.com/static/51b3dc8ee4b051b96ceb10de/51ce6099e4b0d911b4489b79/51ce61a3e4b0d911b449b0cd/1320274343084/1000w/safe-house-denzel-first-1.jpg");
        imgURLs.add("https://fsmedia.imgix.net/0b/c5/83/e8/3b2f/4960/90a7/1a45124f47b9/the-expanse-season-3-finale.jpeg?auto=compress&h=1200&w=1200&crop=edges&fit=crop");
        imgURLs.add("https://cdn2us.denofgeek.com/sites/denofgeekus/files/styles/main_wide/public/2017/09/the-expanse-origins-graphic-novel-alcon-boom-studios.jpg?itok=yZiP04lT");
        imgURLs.add("https://icdn2.digitaltrends.com/image/digitaltrends/the-expanse-sdcc-trailer2.jpg");
        imgURLs.add("https://i.imgur.com/DroAGZ0.jpg");
        imgURLs.add("https://i.pinimg.com/originals/cb/06/98/cb0698c28b5daf700509105f07b71466.jpg");

        setUpImageGrid(imgURLs);
    }


    private void setUpImageGrid(ArrayList<String> imgURLs){
        GridView gridView = findViewById(R.id.gridView);

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        GridImageAdapter adapter = new GridImageAdapter(mContext, R.layout.layout_grid_image_view,  "", imgURLs);
        gridView.setAdapter(adapter);
    }

    private void setProfileImage(){
        Log.d(TAG, "setProfileImage: Setting profile photo");
        String imgURL = "https://thenypost.files.wordpress.com/2017/05/oscars_blacks.jpg?quality=90&strip=all&w=618&h=410&crop=1";
        UniversalImageLoader.setImage(imgURL, profilePicture, mProgressBar, "");
    }

    private void setupActivityWidgets(){
        mProgressBar = findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);
        profilePicture = findViewById(R.id.profileImage);
    }

    private void setupToolBar(){
        Toolbar toolbar = findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);

        ImageView profileMenu = findViewById(R.id.backArrow);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to account settings");
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);

            }
        });
    }

    /**
     * BottomNavigationView setup
     */
    private void setUpBottomNavigationView(){
        Log.d(TAG, "setUpBottomNavigationView: Setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
