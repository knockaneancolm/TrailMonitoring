package com.applications4d.trailmonitoring;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by colmforde on 10/8/16.
 */
public class Blank extends Fragment {

    public Blank(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.blank_screen, container, false);



        return view;

    }
}
