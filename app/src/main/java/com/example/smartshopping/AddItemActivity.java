package com.example.smartshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smartshopping.model.ItemModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        databaseReference = FirebaseDatabase.getInstance().getReference("smart-shops");

        SharedPreferences loginPreferences = getSharedPreferences(getString(R.string.login_preference), MODE_MULTI_PROCESS);
        String phoneNumber = loginPreferences.getString(getString(R.string.phone_preference), null);

        ItemModel items = new ItemModel(null
                                        , phoneNumber
                                        , itemName.getText().toString().trim()
                                        , Double.parseDouble(itemPrice.getText().toString().trim())
                                        , Double.parseDouble(itemQuantity.getText().toString().trim())
                                        , itemBrand.getText().toString().trim());

        DatabaseReference childReference = databaseReference.child("items").push();
        childReference.setValue(items);

        Toast.makeText(AddItemActivity.this, "Items Added Successfully", Toast.LENGTH_SHORT).show();

    }
}