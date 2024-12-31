package com.mobile.tourism.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.mobile.tourism.R;
import com.mobile.tourism.models.Place;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private List<Place> places;
    private OnItemClickListener onItemClickListener;

    // Define an interface for item click events
    public interface OnItemClickListener {
        void onItemClick(Place place);
    }

    // Constructor to initialize the places list and set the item click listener
    public PlaceAdapter(List<Place> places, OnItemClickListener listener) {
        this.places = places;
        this.onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Place place = places.get(position);
        holder.nameTextView.setText(place.getName());
        holder.descriptionTextView.setText(place.getDescription());
        holder.locationTextView.setText(place.getLocation());

        // Set click listener for each item
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(place); // Notify the listener when an item is clicked
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size(); // Return the size of the places list
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        TextView locationTextView;

        // Initialize the ViewHolder with the views from the layout
        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            descriptionTextView = view.findViewById(R.id.descriptionTextView);
            locationTextView = view.findViewById(R.id.locationTextView);
        }
    }
}
