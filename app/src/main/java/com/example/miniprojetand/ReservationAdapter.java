package com.example.miniprojetand;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {
    private List<Reservation> reservationList;

    public ReservationAdapter(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @Override
    public ReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.hotelNameTextView.setText(reservation.hotelName);
        holder.hotelPriceTextView.setText(reservation.hotelPrice);
        holder.checkInDateTextView.setText(reservation.checkInDate);
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView hotelNameTextView, hotelPriceTextView, checkInDateTextView;

        public ReservationViewHolder(View itemView) {
            super(itemView);
            hotelNameTextView = itemView.findViewById(R.id.hotelNameTextView);
            hotelPriceTextView = itemView.findViewById(R.id.hotelPriceTextView);
            checkInDateTextView = itemView.findViewById(R.id.checkInDateTextView);
        }
    }
}