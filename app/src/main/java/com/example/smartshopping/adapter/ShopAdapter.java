package com.example.smartshopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartshopping.R;
import com.example.smartshopping.model.ShopModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ShopAdapter extends FirebaseRecyclerAdapter<ShopModel, ShopAdapter.ViewHolder> {

    public ShopAdapter(@NonNull FirebaseRecyclerOptions<ShopModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ShopModel model) {
        holder.bind(model);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.shops, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView shopImage;
        ImageView ownerImage;

        TextView shopName;
        TextView address;
        TextView rating;
        TextView numberOfRating;

        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shopImage = itemView.findViewById(R.id.shop_list_shop_image);
            ownerImage = itemView.findViewById(R.id.shop_list_owner_image);

            shopName = itemView.findViewById(R.id.shop_list_shop_name);
            address = itemView.findViewById(R.id.shop_list_address);
            rating = itemView.findViewById(R.id.shop_list_rating_bar);
            numberOfRating = itemView.findViewById(R.id.shop_list_number_of_ratings);

            ratingBar = itemView.findViewById(R.id.shop_list_rating_bar);

        }

        public void bind(ShopModel model) {

            shopName.setText(model.getShopName());
            address.setText(model.getAddress());

        }
    }

}
