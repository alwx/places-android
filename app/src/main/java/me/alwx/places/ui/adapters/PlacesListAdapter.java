package me.alwx.places.ui.adapters;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.alwx.places.R;
import me.alwx.places.data.models.Geodata;
import me.alwx.places.databinding.ItemPlaceBinding;
import me.alwx.places.data.models.Place;
import me.alwx.places.ui.fragments.PlacesListFragment;

/**
 * Adapter for {@link PlacesListFragment}
 *
 * @author alwx (https://alwx.me)
 * @version 1.0
 */
public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.ViewHolder> {
    private PlacesListFragment fragment;
    private OnClickListener onClickListener;

    private List<Place> placeList = new ArrayList<>();
    private LongSparseArray<Geodata> geodataArray = new LongSparseArray<>();
    private Location location;

    public PlacesListAdapter(PlacesListFragment fragment) {
        this.fragment = fragment;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);

        return new ViewHolder(v);
    }

    /**
     * Updates the places list.
     *
     * @param placeList list of {@link Place} objects
     */
    public void setPlaceList(List<Place> placeList) {
        this.placeList = placeList;
        notifyDataSetChanged();
    }

    /**
     * Updates the geodata list.
     *
     * @param geodataList list of {@link Geodata} objects
     */
    public void setGeodataList(List<Geodata> geodataList) {
        geodataArray = new LongSparseArray<>();
        for (Geodata geodata : geodataList) {
            geodataArray.append(geodata.id(), geodata);
        }
        notifyDataSetChanged();
    }

    /**
     * Updates location. We need location to calculate the distance between a place and a user.
     *
     * @param location {@link Location} object
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Sets OnClickListener for an adapter item.
     *
     * @param onClickListener listener
     */
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    
    @Override
    public int getItemCount() {
        return placeList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.binding.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(placeList.get(holder.getAdapterPosition()));
                }
            }
        });
        holder.binding.name.setText(place.title());
        holder.binding.address.setText(place.address().asString());

        bindDistance(
                holder.binding.distance,
                Geodata.calculateDistance(
                        geodataArray.get(place.id()),
                        location
                )
        );
    }

    /**
     * Displays the distance.
     *
     * @param distanceView TextView for distance information
     * @param distance distance in meters
     */
    private void bindDistance(@NonNull TextView distanceView, double distance) {
        if (distance == -1d) {
            distanceView.setText("");
        } else if (distance >= 1000) {
            distanceView.setText(fragment.getString(R.string.kilometers, distance / 1000d));
        } else {
            distanceView.setText(fragment.getString(R.string.meters, (int) distance));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemPlaceBinding binding;

        ViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }
    }

    public interface OnClickListener {
        void onClick(Place place);
    }
}
