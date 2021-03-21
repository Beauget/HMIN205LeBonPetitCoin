package com.example.lebonpetitcoin.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;


import com.example.lebonpetitcoin.AddAnnonceActivity;
import com.example.lebonpetitcoin.R;

public class AccountFragment extends Fragment {
    public static AccountFragment newInstance() {
        return (new AccountFragment());
    }
    Button addProduct;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        addProduct =view.findViewById(R.id.idAddProduct);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), AddAnnonceActivity.class);
                AccountFragment.this.startActivity(myIntent);
            }
        });
        super.onActivityCreated(savedInstanceState);
    }



}
