package com.example.smartshopping.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smartshopping.AddItemActivity;
import com.example.smartshopping.adapter.ItemAdapter;
import com.example.smartshopping.R;
import com.example.smartshopping.model.ItemModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_MULTI_PROCESS;

public class HomeFragment extends Fragment {

    TextView itemCount;
    TextView shopBudget;

    ExtendedFloatingActionButton addItem;

    RecyclerView itemsRecyclerView;

    DatabaseReference databaseReference;

    ItemAdapter itemAdapter;

    private String ITEMS_LIST_PATH;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        itemCount = view.findViewById(R.id.shop_item_count);
        shopBudget = view.findViewById(R.id.shop_budget);
        addItem = view.findViewById(R.id.home_add_item);

        SharedPreferences loginPreferences = getContext().getSharedPreferences(getString(R.string.login_preference), MODE_MULTI_PROCESS);
        String phoneNumber = loginPreferences.getString(getString(R.string.phone_preference), null);

        ITEMS_LIST_PATH = "shops" + "/" + phoneNumber + "/" + "items";

        databaseReference = FirebaseDatabase.getInstance().getReference(ITEMS_LIST_PATH);

        itemsRecyclerView = view.findViewById(R.id.items_recycler_view);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemsRecyclerView.hasFixedSize();

        FirebaseRecyclerOptions<ItemModel> items = new FirebaseRecyclerOptions.Builder<ItemModel>()
                .setQuery(databaseReference, ItemModel.class)
                .build();

        itemAdapter = new ItemAdapter(items);

        itemsRecyclerView.setAdapter(itemAdapter);

        itemAdapter.startListening();

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddItemActivity.class));
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemCount.setText(String.valueOf(snapshot.getChildrenCount()));
                shopBudget.setText("\u20B9298.0");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}