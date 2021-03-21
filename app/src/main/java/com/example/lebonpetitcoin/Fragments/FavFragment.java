package com.example.lebonpetitcoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.lebonpetitcoin.R;

public class FavFragment extends Fragment {
    public static FavFragment newInstance() {
        return (new FavFragment());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fav, container, false);
    }
}
