package com.example.softxpert.adapters;


import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.softxpert.classes.Data;
import com.example.softxpert.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Data> mItemsList;
    private Context mContext;

    public ItemsAdapter(Context context, List<Data> itemsList) {
        this.mItemsList = itemsList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup view = (ViewGroup) mInflater.inflate(R.layout.row_items, viewGroup, false);
        return new CarHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        final CarHolder carHolder = (CarHolder) holder;

        final Data car = mItemsList.get(position);

        Glide.with(mContext)
                .load(car.getImageUrl())
                .fitCenter()
                .placeholder(R.drawable.placeholdercar)
                .into(carHolder.mIvCar);

        if (car.getBrand() != null) {
            carHolder.mTvBrand.setText(car.getBrand());
        } else {
            carHolder.mTvBrand.setText("-");
        }

        if (car.getConstractionYear() != null) {
            carHolder.mTvConstructionYear.setText(car.getConstractionYear());
        } else {
            carHolder.mTvConstructionYear.setText("-");
        }

        if (car.isUsed()) {
            carHolder.mTvCondition.setText("Used");
        } else {
            carHolder.mTvCondition.setText("New");
        }


    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    public static class CarHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_carImage)
        ImageView mIvCar;
        @BindView(R.id.tv_brand)
        TextView mTvBrand;
        @BindView(R.id.tv_ConstructionYear)
        TextView mTvConstructionYear;
        @BindView(R.id.tv_Condition)
        TextView mTvCondition;

        public CarHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
