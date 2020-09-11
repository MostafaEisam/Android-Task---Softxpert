package com.example.softxpert.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.softxpert.R;
import com.example.softxpert.Services.RequestListener;
import com.example.softxpert.Services.ServiceFactory;
import com.example.softxpert.adapters.ItemsAdapter;
import com.example.softxpert.classes.Data;
import com.example.softxpert.responses.GetCarsListResponse;
import com.example.softxpert.utilities.InternetConnection;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RequestListener {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mPullToRefresh;
    @BindView(R.id.rv_Cars)
    RecyclerView mRvCars;
    @BindView(R.id.pb)
    ContentLoadingProgressBar mProgressBar;

    private ArrayList<Data> mCarsList;
    private ItemsAdapter mRvCarsAdapter;
    private int pageNumber = 1;
    private boolean mLoading = true;
    private int mPastVisibleItems, mVisibleItemCount, mTotalItemCount;
    private LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mCarsList = new ArrayList<>();
        mRvCarsAdapter = new ItemsAdapter(this, mCarsList);
        mRvCars.setAdapter(mRvCarsAdapter);
        mRvCars.setLayoutManager(mLayoutManager);
        mRvCars.setHasFixedSize(true);

        mRvCars.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    mVisibleItemCount = mLayoutManager.getChildCount();
                    mTotalItemCount = mLayoutManager.getItemCount();
                    mPastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (mLoading) {
                        if ((mVisibleItemCount + mPastVisibleItems) >= mTotalItemCount) {
                            mLoading = false;
                            pageNumber++;
                            mProgressBar.setVisibility(View.VISIBLE);
                            getCarsData();
                        }
                    }
                }
            }
        });
        pullToRefresh();

        getCarsData();
    }

    public void getCarsData() {
        ServiceFactory.getData(this, "http://demo1585915.mockable.io/api/v1/cars?page=" + pageNumber, this);
    }

    private void pullToRefresh() {
        mPullToRefresh.setColorSchemeColors(Color.parseColor("#EA5454"), Color.parseColor("#5db25d"));
        mPullToRefresh.setOnRefreshListener(() -> {
            if (InternetConnection.checkConnection(MainActivity.this)) {
                mCarsList.clear();
                mRvCarsAdapter.notifyDataSetChanged();
                pageNumber = 1;
                getCarsData();
            } else {
                Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                if (mPullToRefresh.isRefreshing()) {
                    mPullToRefresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onSuccess(Object object) {
        GetCarsListResponse mAllCarsListResponse = new Gson().fromJson((String) object, GetCarsListResponse.class);
        if (mAllCarsListResponse != null && mAllCarsListResponse.getData().size() > 0 && mAllCarsListResponse.getStatus() == 1) {
            mCarsList.addAll(mAllCarsListResponse.getData());

            runOnUiThread(() -> {
                mRvCarsAdapter.notifyDataSetChanged();
                mLoading = true;
            });
        }

        runOnUiThread(() -> {
            if (mPullToRefresh.isRefreshing()) {
                mPullToRefresh.setRefreshing(false);
            }
            if (mProgressBar.getVisibility()==View.VISIBLE){
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onFailure(int errorCode) {
        runOnUiThread(() -> Toast.makeText(this, "onFailure", Toast.LENGTH_SHORT).show());
    }
}