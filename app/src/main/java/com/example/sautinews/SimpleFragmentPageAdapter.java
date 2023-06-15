package com.example.sautinews;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.sautinews.WelcomeFragment1;
import com.example.sautinews.WelcomeFragment2;
import com.example.sautinews.WelcomeFragment3;

public class SimpleFragmentPageAdapter extends FragmentPagerAdapter {
public SimpleFragmentPageAdapter(FragmentManager fragmentManager){

    super(fragmentManager);
}

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new WelcomeFragment1();
        } else if (position==1){
            return new WelcomeFragment2();
        }

       else {
            return new WelcomeFragment3();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
