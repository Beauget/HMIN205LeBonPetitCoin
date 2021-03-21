package com.example.lebonpetitcoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.lebonpetitcoin.R;

public class AccueilFragment extends Fragment {
    public static AccueilFragment newInstance() {
        return (new AccueilFragment());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accueil, container, false);
    }
}
