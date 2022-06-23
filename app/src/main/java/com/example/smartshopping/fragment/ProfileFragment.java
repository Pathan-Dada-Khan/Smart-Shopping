package com.example.smartshopping.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.smartshopping.LoginActivity;
import com.example.smartshopping.R;
import com.example.smartshopping.ShopDetailsActivity;
import com.example.smartshopping.model.ShopDetailsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_MULTI_PROCESS;

public class ProfileFragment extends Fragment {

    private static final int SHOP_IMAGE_PICKER = 200;
    private static final int OWNER_IMAGE_PICKER = 100;

    private Uri ownerImageUri = null;
    private Uri shopImageUri = null;

    String phone = null;

    ImageView shopImage;
    ImageView ownerImage;

    TextView shopName;
    TextView ownerName;
    TextView phoneNumber;

    TextView rating;
    RatingBar ratingBar;
    TextView ratingCount;

    TextView weekDaysOpens;
    TextView weekDaysClosed;
    TextView weekEndsOpens;
    TextView weekEndsClosed;

    TextView category;
    TextView address;

    CardView locationCard;
    TextView location;
    ImageView locationChange;
    TextView locationAddress;

    Button editProfile;
    Button logout;

    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        shopImage = view.findViewById(R.id.shop_image);
        ownerImage = view.findViewById(R.id.owner_image);

        shopName = view.findViewById(R.id.profile_shop_name);
        ownerName = view.findViewById(R.id.profile_owner_name);
        phoneNumber = view.findViewById(R.id.profile_phone_number);

        rating = view.findViewById(R.id.profile_rating);
        ratingBar = view.findViewById(R.id.profile_rating_bar);
        ratingCount = view.findViewById(R.id.profile_rating_count);

        weekDaysOpens = view.findViewById(R.id.profile_week_days_opens);
        weekDaysClosed = view.findViewById(R.id.profile_week_days_closed);
        weekEndsOpens = view.findViewById(R.id.profile_week_ends_opens);
        weekEndsClosed = view.findViewById(R.id.profile_week_ends_closed);

        category = view.findViewById(R.id.profile_category);
        address = view.findViewById(R.id.profile_address);

        locationCard = view.findViewById(R.id.profile_location_card);
        location = view.findViewById(R.id.profile_location);
        locationChange = view.findViewById(R.id.profile_location_change);
        locationAddress = view.findViewById(R.id.profile_location_address);

        editProfile = view.findViewById(R.id.edit_profile);
        logout = view.findViewById(R.id.logout);

        shopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getShopImage();
            }
        });

        ownerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOwnerImage();
            }
        });

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

        SharedPreferences loginPreferences = getContext().getSharedPreferences(getString(R.string.login_preference), MODE_MULTI_PROCESS);
        phone = loginPreferences.getString(getString(R.string.phone_preference), null);

        String PATH = "shops/" + phone + "/shopDetails/";

        databaseReference = FirebaseDatabase.getInstance().getReference(PATH);

        getFirebaseData();

        return view;
    }

    private void getOwnerImage() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, OWNER_IMAGE_PICKER);
    }

    private void getShopImage() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SHOP_IMAGE_PICKER);
    }

    private void getFirebaseData() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ShopDetailsModel model = snapshot.getValue(ShopDetailsModel.class);

                if(model != null) {

                    shopName.setText(model.getShopName());
                    ownerName.setText(model.getOwnerName());

                    phoneNumber.setText(phone);

                    weekDaysOpens.setText(model.getWeekDaysOpens());
                    weekDaysClosed.setText(model.getWeekDaysClosed());
                    weekEndsOpens.setText(model.getWeekEndsOpens());
                    weekEndsClosed.setText(model.getWeekEndsClosed());

                    category.setText(model.getCategory());
                    address.setText(model.getAddress());

                    location.setText(model.getLocation());
                    locationAddress.setText(model.getAddress());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

                        SharedPreferences loginPreferences = getContext().getSharedPreferences(getString(R.string.login_preference), MODE_MULTI_PROCESS);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == OWNER_IMAGE_PICKER && resultCode == RESULT_OK) {
            ownerImageUri =  data.getData();
            ownerImage.setImageURI(ownerImageUri);
        } else if(requestCode == SHOP_IMAGE_PICKER && resultCode == RESULT_OK) {
            shopImageUri = data.getData();
            shopImage.setImageURI(shopImageUri);
        }

    }
}