package com.example.lebonpetitcoin.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.lebonpetitcoin.R;

public class SignUpChoiceFragment extends Fragment {
    private static final String TAG = "SignUpChoiceFragment";
    CardView pro;
    CardView particulier;
    public static Fragment newInstance() {
        return (new SignUpChoiceFragment());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_choice, container, false);
        particulier = view.findViewById(R.id.particulier);
        pro = view.findViewById(R.id.pro);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pro.setOnClickListener(new View.OnClickListener() {
            private Fragment fragment;
            @Override
            public void onClick(View view) {
                if (this.fragment == null)
                    this.fragment = SignUpFragment.newInstance();
                Bundle arguments = new Bundle();
                arguments.putString("pro", "pro");
                fragment.setArguments(arguments);
                ((AppCompatActivity) getActivity())
                        .getSupportFragmentManager()
                        .beginTransaction().addToBackStack(TAG)
                        .replace(R.id.activity_main_frame_layout, fragment).commit();
            }
        });

        particulier.setOnClickListener(new View.OnClickListener() {
            private Fragment fragment;
            @Override
            public void onClick(View view) {
                if (this.fragment == null)
                    this.fragment = SignUpFragment.newInstance();
                ((AppCompatActivity) getActivity()).getSupportFragmentManager()
                        .beginTransaction().replace(R.id.activity_main_frame_layout, fragment).commit();

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
