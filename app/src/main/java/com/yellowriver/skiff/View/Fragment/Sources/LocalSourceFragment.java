package com.yellowriver.skiff.View.Fragment.Sources;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yellowriver.skiff.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalSourceFragment extends Fragment {


    public LocalSourceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local_source, container, false);
    }

}
