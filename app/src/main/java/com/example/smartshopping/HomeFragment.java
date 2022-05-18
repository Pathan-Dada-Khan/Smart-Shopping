package com.example.smartshopping;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class HomeFragment extends Fragment {

    ExtendedFloatingActionButton addItem;

    RecyclerView itemsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        addItem = view.findViewById(R.id.home_add_item);

        itemsRecyclerView = view.findViewById(R.id.items_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        itemsRecyclerView.setLayoutManager(linearLayoutManager);
        itemsRecyclerView.hasFixedSize();

        ItemAdapter itemAdapter = new ItemAdapter();

        itemsRecyclerView.setAdapter(itemAdapter);


        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddItemActivity.class));
            }
        });

        return view;
    }
}