package com.example.miniprojetand;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class HotelAdapterAdmin extends RecyclerView.Adapter<HotelAdapterAdmin.HotelViewHolder> {
    private List<Hotel> hotelList;
    private List<Hotel> filteredHotelList;

    public HotelAdapterAdmin(List<Hotel> hotelList) {
        this.hotelList = hotelList;
        this.filteredHotelList = hotelList; // Initialize both lists as identical
    }

    @Override
    public HotelAdapterAdmin.HotelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ligne_admin, parent, false);
        return new HotelAdapterAdmin.HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HotelAdapterAdmin.HotelViewHolder holder, int position) {
        Hotel hotel = filteredHotelList.get(position);

        // Displaying hotel information
        holder.nameTextView.setText(hotel.getName());
        holder.locationTextView.setText(hotel.getLocation());
        holder.priceTextView.setText(hotel.getPrice() + " TND");

        // Loading the hotel's main image
        Glide.with(holder.itemView.getContext())
                .load(hotel.getMainImageUrl())
                .into(holder.hotelImageView);

        // Click on the "View" button to show the hotel details
        holder.viewButton.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, HotelModifierActivity.class);
            intent.putExtra("hotelId", hotel.getId());
            intent.putExtra("hotelName", hotel.getName());
            intent.putExtra("hotelLocation", hotel.getLocation());
            intent.putExtra("hotelPrice", hotel.getPrice());
            intent.putExtra("hotelImage", hotel.getMainImageUrl());
            intent.putExtra("hotelDescription", hotel.getDescription());
            intent.putExtra("latitude", hotel.getLatitude());
            intent.putExtra("longitude", hotel.getLongitude());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredHotelList.size();
    }

    // Filtering the search results
    public void filter(String query) {
        if (query.isEmpty()) {
            // Si la barre de recherche est vide, réinitialiser la liste filtrée avec tous les hôtels
            filteredHotelList = new ArrayList<>(hotelList);
        } else {
            // Créer une nouvelle liste contenant uniquement les hôtels correspondant à la recherche
            List<Hotel> tempList = new ArrayList<>();
            for (Hotel hotel : hotelList) {
                if (hotel.getName().toLowerCase().contains(query.toLowerCase())) {
                    tempList.add(hotel);
                }
            }
            filteredHotelList = tempList; // Mettre à jour la liste filtrée
        }
        notifyDataSetChanged(); // Notifier RecyclerView que les données ont changé
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {
        ImageView hotelImageView;
        TextView nameTextView, locationTextView, priceTextView;
        Button viewButton;

        public HotelViewHolder(View itemView) {
            super(itemView);
            hotelImageView = itemView.findViewById(R.id.hotelImageView);
            nameTextView = itemView.findViewById(R.id.hotelNameTextView);
            locationTextView = itemView.findViewById(R.id.hotelLocationTextView);
            priceTextView = itemView.findViewById(R.id.hotelPriceTextView);
            viewButton = itemView.findViewById(R.id.viewButton);  // "View" button
        }
    }
}