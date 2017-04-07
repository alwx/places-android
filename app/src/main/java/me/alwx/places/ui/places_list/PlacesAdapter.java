package me.alwx.places.ui.places_list;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.alwx.places.R;
import me.alwx.places.databinding.ItemPlaceBinding;
import me.alwx.places.data.models.Place;

/**
 * @author alwx
 * @version 1.0
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    private PlacesListFragment fragment;
    private List<Place> placeList = new ArrayList<>();
    private OnClickListener onClickListener;

    public PlacesAdapter(PlacesListFragment fragment) {
        this.fragment = fragment;
    }
    
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);

        return new ViewHolder(v);
    }

    public void setPlaceList(List<Place> placeList) {
        this.placeList = placeList;
        notifyDataSetChanged();
    }
    
    @Override
    public int getItemCount() {
        return placeList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Place place = placeList.get(position);
        /*holder.binding.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(holder.getAdapterPosition());
            }
        });*/
        holder.binding.name.setText(place.title());
        //holder.binding.address.setText(place.getAddress().toString());

        //setDistance(place.getDistance(), holder.binding.distance);
    }

    private void setDistance(double distance, final TextView distanceView) {
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
        void onClick(int position);
    }
}
