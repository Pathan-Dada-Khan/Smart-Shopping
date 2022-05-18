package com.example.smartshopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class ShopDetailsActivity extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 1;

    EditText shopName;
    EditText ownerName;

    EditText weekDaysOpens;
    EditText weekDaysClosed;
    EditText weekEndsOpens;
    EditText weekEndsClosed;

    EditText category;
    EditText location;
    EditText address;

    Button save;

    int hour;
    int minute;

    private int checkedID = -1;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        Intent intent = getIntent();

        shopName = findViewById(R.id.shop_name);
        ownerName = findViewById(R.id.owner_name);

        weekDaysOpens = findViewById(R.id.week_days_opens);
        weekDaysClosed = findViewById(R.id.week_days_closed);
        weekEndsOpens = findViewById(R.id.week_ends_opens);
        weekEndsClosed = findViewById(R.id.week_ends_closed);

        category = findViewById(R.id.category);
        location = findViewById(R.id.location);
        address = findViewById(R.id.address);

        save = findViewById(R.id.save);

        weekDaysOpens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog(weekDaysOpens);
            }
        });

        weekDaysClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog(weekDaysClosed);
            }
        });

        weekEndsOpens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog(weekEndsOpens);
            }
        });

        weekEndsClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog(weekEndsClosed);
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryDialog();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(ShopDetailsActivity.this, MapsActivity.class), PLACE_PICKER_REQUEST);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveShopDetails();
                
                if (intent.hasExtra("login")) {
                    startActivity(new Intent(ShopDetailsActivity.this, MainActivity.class));
                }
                finish();
            }
        });

    }

    private void saveShopDetails() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("shops");

        ShopDetailsModel item = new ShopDetailsModel(shopName.getText().toString().trim()
                                        , ownerName.getText().toString().trim()
                                        , weekDaysOpens.getText().toString().trim()
                                        , weekDaysClosed.getText().toString().trim()
                                        , weekEndsOpens.getText().toString().trim()
                                        , weekEndsClosed.getText().toString().trim()
                                        , category.getText().toString().trim()
                                        , location.getText().toString().trim()
                                        , address.getText().toString().trim()
        );

        SharedPreferences loginPreferences = getSharedPreferences(getString(R.string.login_preference), MODE_MULTI_PROCESS);
        String phoneNumber = loginPreferences.getString(getString(R.string.phone_preference), null);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(phoneNumber).child("shopDetails").setValue(item);
                Toast.makeText(ShopDetailsActivity.this, "Shop Details Added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShopDetailsActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PLACE_PICKER_REQUEST) {
            if(data != null) {
                location.setText(data.getStringExtra("LOCALITY"));
                address.setText(data.getStringExtra("ADDRESS_LINE"));
            }
        }
    }

    private void categoryDialog() {

        String[] items = {"Grocery Store", "Super Market", "Medical Store", "Fashion Mall", "Others"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.category)
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setSingleChoiceItems(items, checkedID, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        category.setText(items[i]);
                        checkedID = i;
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void timePickerDialog(EditText editText) {

        String time = editText.getText().toString();

        if(!time.equals("") && !time.isEmpty()) {

            String[] timeDays = time.split(" ");

            String[] timeMinute = timeDays[0].split(":");

            hour = Integer.parseInt(timeMinute[0]);
            minute = Integer.parseInt(timeMinute[1]);

            if(Integer.parseInt(timeMinute[0]) == 12) {
                hour = 0;
            }

            if(timeDays[1].equals("PM")) hour += 12;

        }

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                editText.setText(String.format(Locale.getDefault(), "%02d:%02d %s", (hour%12 == 0)?12:hour%12, minute, hour<12?"AM":"PM"));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);
        timePickerDialog.show();
    }
}