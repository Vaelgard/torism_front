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

    public PlaceAdapter(List<Place> places) {
        this.places = places;
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
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        TextView locationTextView;

        public ViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            descriptionTextView = view.findViewById(R.id.descriptionTextView);
            locationTextView = view.findViewById(R.id.locationTextView);
        }
    }
} 