package com.example.smartshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddItemActivity extends AppCompatActivity {

    ImageView itemImage;

    EditText itemName;
    EditText itemPrice;
    EditText itemQuantity;
    EditText itemBrand;

    Button itemAdd;

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
                finish();
            }
        });

    }
}