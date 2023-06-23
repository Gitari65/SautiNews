package com.example.sautinews;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class NotificationPageAdapter extends FragmentPagerAdapter {
    public NotificationPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0){

            return new GeneralNotificationFragment();
        } else  {
            return new SystemNotificationFragment();
        }

    }

    @Override
    public int getCount() {
        return 0;
    }
}
