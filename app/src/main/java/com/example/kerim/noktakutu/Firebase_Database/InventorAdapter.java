package com.example.kerim.noktakutu.Firebase_Database;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kerim.noktakutu.R;

import java.util.List;

/**
 * Created by Abdullah on 2.10.2016.
 */

public class InventorAdapter extends RecyclerView.Adapter<InventorAdapter.InventorViewHolder> {

    private Context mContext;
    private List<InventorModel> inventorModels;

    public InventorAdapter(Context mContext, List<InventorModel> inventorModels) {
        this.mContext = mContext;
        this.inventorModels = inventorModels;
    }

    @Override
    public InventorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventor_card_layout, parent, false);

        return new InventorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final InventorViewHolder holder, final int position) {
        InventorModel inventor = inventorModels.get(position);
        holder.kullaniciadi.setText(inventor.getKullanici_adi());
        holder.puan.setText((inventor.getPuan()+""));
        // loading cover using Glide library

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow, position);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();


    }


    /**
     * Click listener for popup menu items
     */



    @Override
    public int getItemCount() {
        return inventorModels.size();
    }

    public class InventorViewHolder extends RecyclerView.ViewHolder {
        public TextView kullaniciadi, puan;
        public ImageView overflow;

        public InventorViewHolder(View view) {
            super(view);
            kullaniciadi = (TextView) view.findViewById(R.id.kullaniciAdi);
            puan = (TextView) view.findViewById(R.id.kullanicipuani);

            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }
}
