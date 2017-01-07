package com.example.thangbach.findhouse.VIEW;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.thangbach.findhouse.R;

/**
 * Created by THANG_BACH on 10/12/16.
 */

public class FragmentPost extends Fragment {
    public FragmentPost() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_post, container, false);
        // Inflate the layout for this fragment

        return view;
    }
}
