package com.example.smartshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.smartshopping.adapter.ShopAdapter;
import com.example.smartshopping.model.ShopModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerActivity extends AppCompatActivity {

    RecyclerView shops;
    FloatingActionButton cart;

    DatabaseReference databaseReference;
    ShopAdapter shopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        shops = findViewById(R.id.shops);
        cart = findViewById(R.id.cart);

        databaseReference = FirebaseDatabase.getInstance().getReference("shops/");

        shops.setLayoutManager(new LinearLayoutManager(this));
        shops.hasFixedSize();

        FirebaseRecyclerOptions<ShopModel> items = new FirebaseRecyclerOptions.Builder<ShopModel>()
                .setQuery(databaseReference, ShopModel.class)
                .build();

        shopAdapter = new ShopAdapter(items);
        shops.setAdapter(shopAdapter);
        shopAdapter.startListening();

    }
}