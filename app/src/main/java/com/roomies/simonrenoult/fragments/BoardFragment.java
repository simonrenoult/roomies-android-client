package com.roomies.simonrenoult.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roomies.simonrenoult.roomies.R;

public class BoardFragment extends Fragment {
    
    View _boardView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _boardView = inflater.inflate(R.layout.fragment_board, container, false);
        return _boardView;
    }
}