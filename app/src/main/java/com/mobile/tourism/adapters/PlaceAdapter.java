package com.mobile.tourism.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobile.tourism.PlaceDetailActivity;
import com.mobile.tourism.R;
import com.mobile.tourism.models.Place;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private final List<Place> places;

    public PlaceAdapter(List<Place> places) {
        this.places = places;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = places.get(position);

        holder.nameTextView.setText(place.getName());
        holder.descriptionTextView.setText(place.getDescription());
        holder.locationTextView.setText(place.getLocation());

        String imageUrl = place.getImage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.imageView);
        }
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, PlaceDetailActivity.class);
            intent.putExtra("placeId", place.getId());
            System.out.println("intent placeId"+place.getId());
            intent.putExtra("placeName", place.getName());
            intent.putExtra("placeDescription", place.getDescription());
            intent.putExtra("placeLocation", place.getLocation());
            intent.putExtra("placeImageUrl", place.getImage());
            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return places.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        TextView nameTextView;
        TextView descriptionTextView;
        TextView locationTextView;

        ViewHolder(View view) {
            super(view);
            imageView=view.findViewById(R.id.imageView);
            nameTextView = view.findViewById(R.id.nameTextView);
            descriptionTextView = view.findViewById(R.id.descriptionTextView);
            locationTextView = view.findViewById(R.id.locationTextView);
        }
    }
}
