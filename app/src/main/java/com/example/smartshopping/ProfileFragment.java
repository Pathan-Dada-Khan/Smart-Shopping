package com.example.smartshopping;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ProfileFragment extends Fragment {

    Button editProfile;
    Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        editProfile = view.findViewById(R.id.edit_profile);
        logout = view.findViewById(R.id.logout);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ShopDetailsActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog();
            }
        });

        return view;
    }

    private void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.logout)
                .setMessage("Are you sure to Logout ?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences loginPreferences = getContext().getSharedPreferences(getString(R.string.login_preference), Context.MODE_MULTI_PROCESS);
                        SharedPreferences.Editor editor = loginPreferences.edit();
                        editor.putString(getString(R.string.phone_preference), null);
                        editor.apply();

                        startActivity(new Intent(getContext(), LoginActivity.class));
                        getActivity().finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}