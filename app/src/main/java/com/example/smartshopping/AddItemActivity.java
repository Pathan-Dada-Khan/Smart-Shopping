package com.example.smartshopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity {

    ImageView itemImage;

    EditText itemName;
    EditText itemPrice;
    EditText itemQuantity;
    EditText itemBrand;

    Button itemAdd;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        itemImage = findViewById(R.id.add_item_image);

        itemName = findViewById(R.id.add_item_name);
        itemPrice = findViewById(R.id.add_item_price);
        itemQuantity = findViewById(R.id.add_item_quantity);
        itemBrand = findViewById(R.id.add_item_brand);

        itemAdd = findViewById(R.id.add_item_add);

        itemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
                finish();
            }
        });

    }

    private void addItem() {

        databaseReference = FirebaseDatabase.getInstance().getReference("shops");

        SharedPreferences loginPreferences = getSharedPreferences(getString(R.string.login_preference), MODE_MULTI_PROCESS);
        String phoneNumber = loginPreferences.getString(getString(R.string.phone_preference), null);

        ArrayList<ItemModel> items = new ArrayList<>();

        items.add(new ItemModel(null
        , itemName.getText().toString().trim()
        , Double.parseDouble(itemPrice.getText().toString().trim())
        , Double.parseDouble(itemQuantity.getText().toString().trim())
        , itemBrand.getText().toString().trim()));

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                databaseReference.child(phoneNumber).child("items").setValue(items);
                Toast.makeText(AddItemActivity.this, "Items Added Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddItemActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}