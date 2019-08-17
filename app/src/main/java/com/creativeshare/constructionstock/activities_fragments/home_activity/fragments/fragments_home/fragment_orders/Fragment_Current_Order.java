package com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.fragment_orders;


import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.activity_order_detail.OrderDetailActivity;
import com.creativeshare.constructionstock.activities_fragments.home_activity.activities.Home_Activity;
import com.creativeshare.constructionstock.adapters.OrdersAdapter;
import com.creativeshare.constructionstock.models.OrderDataModel;
import com.creativeshare.constructionstock.models.UserModel;
import com.creativeshare.constructionstock.preferences.Preferences;
import com.creativeshare.constructionstock.remote.Api;
import com.creativeshare.constructionstock.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Current_Order extends Fragment {
    private RecyclerView recView;
    private LinearLayoutManager manager;
    private ProgressBar progBar;
    private LinearLayout ll_no_order;
    private OrdersAdapter adapter;
    private Preferences preferences;
    private UserModel userModel;
    private List<OrderDataModel.OrderModel> orderModelList;
    private Home_Activity activity;
    private boolean isLoading = false;
    private int current_page=1;
    private int selected_pos = -1;

    public static Fragment_Current_Order newInstance() {

        Fragment_Current_Order fragment_current_order = new Fragment_Current_Order();
        return fragment_current_order;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_previous_order, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        orderModelList = new ArrayList<>();
        activity = (Home_Activity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        ll_no_order = view.findViewById(R.id.ll_no_order);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        adapter = new OrdersAdapter(orderModelList,activity,this);
        recView.setAdapter(adapter);
        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                {
                    int total_item = adapter.getItemCount();
                    int last_item_pos = manager.findLastCompletelyVisibleItemPosition();

                    if (total_item>5&&last_item_pos==(total_item-5)&&!isLoading)
                    {
                        isLoading = true;
                        orderModelList.add(null);
                        adapter.notifyItemInserted(orderModelList.size()-1);
                        int page = current_page+1;
                        loadMore(page);
                    }
                }
            }
        });

        getOrders();


    }



    public void getOrders()
    {

        Api.getService(Tags.base_url)
                .getCurrentOrders(userModel.getId(),1)
                .enqueue(new Callback<OrderDataModel>() {
                    @Override
                    public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                        {
                            orderModelList.clear();
                            orderModelList.addAll(response.body().getData());
                            if (orderModelList.size()>0)
                            {
                                Log.e("2","2");

                                ll_no_order.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();

                            }else
                                {
                                    Log.e("3","3");

                                    ll_no_order.setVisibility(View.VISIBLE);
                                }
                        }else
                            {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    }

                    @Override
                    public void onFailure(Call<OrderDataModel> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void loadMore(int page) {


        Api.getService(Tags.base_url)
                .getCurrentOrders(userModel.getId(),page)
                .enqueue(new Callback<OrderDataModel>() {
                    @Override
                    public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {
                        orderModelList.remove(orderModelList.size()-1);
                        adapter.notifyItemRemoved(orderModelList.size()-1);
                        isLoading = false;
                        if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                        {

                            orderModelList.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();

                        }else
                        {
                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderDataModel> call, Throwable t) {
                        try {
                            isLoading = false;
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    public void setItemData(OrderDataModel.OrderModel orderModel) {

        Intent intent = new Intent(activity, OrderDetailActivity.class);
        intent.putExtra("data",orderModel);
        startActivity(intent);

    }


}
