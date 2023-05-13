package com.example.newsocialgift;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.newsocialgift.fragments.CreateFragment;
import com.example.newsocialgift.fragments.HomeFragment;
import com.example.newsocialgift.fragments.MessagesFragment;
import com.example.newsocialgift.fragments.NotificationsFragment;
import com.example.newsocialgift.fragments.ProfileFragment;
import com.example.newsocialgift.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Main  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_nav_bar);
        setupBottomMenu();
    }

    private BottomNavigationView bottomNavigationView;

    private void setupBottomMenu() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.page_message:
                    showFragment(MessagesFragment.newInstance(R.drawable.baseline_mail_outline_24));
                    break;
                case R.id.page_notification:
                    showFragment(NotificationsFragment.newInstance(R.drawable.baseline_notifications_24));
                    break;
                case R.id.page_home:
                    showFragment(HomeFragment.newInstance(R.drawable.baseline_home_24));
                    break;
                case R.id.page_search:
                    showFragment(SearchFragment.newInstance(R.drawable.baseline_search_24));
                    break;
                /*case R.id.page_create:
                    showFragment(CreateFragment.newInstance(R.drawable.baseline_create_24));
                    break;*/
                case R.id.page_profile:
                    showFragment(ProfileFragment.newInstance(R.drawable.ic_profile));
                    break;
                default:
                    throw new IllegalArgumentException("item not implemented : " + item.getItemId());
            }
            return true;
        });
        bottomNavigationView.setSelectedItemId(R.id.page_home);
    }

    private void showFragment(Fragment frg) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, frg)
                .commit();

    }
}
