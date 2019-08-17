package com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.home_activity.activities.Home_Activity;
import com.creativeshare.constructionstock.activities_fragments.sub_category_activity.SubCategoryActivity;
import com.creativeshare.constructionstock.adapters.Categories_Adapter;
import com.creativeshare.constructionstock.models.CategoriesDataModel;
import com.creativeshare.constructionstock.preferences.Preferences;
import com.creativeshare.constructionstock.remote.Api;
import com.creativeshare.constructionstock.singleton.CartSingleton;
import com.creativeshare.constructionstock.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_main extends Fragment {
    private Home_Activity activity;
    private Preferences preferences;
    private String curent_language;
    private RecyclerView recView;
    private LinearLayoutManager manager;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progBar;
    private Categories_Adapter categories_adapter;
    private List<CategoriesDataModel.CategoryModel> categoryModelList;
    public static Fragment_main newInstance() {
        return new Fragment_main();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        getData();
        return view;
    }

    private void initView(View view) {
        categoryModelList = new ArrayList<>();
        activity = (Home_Activity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        curent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        recView = view.findViewById(R.id.recv_main);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        swipeRefresh.setColorSchemeColors(ContextCompat.getColor(activity,R.color.colorPrimary),ContextCompat.getColor(activity,R.color.cart),ContextCompat.getColor(activity,R.color.delete_color),ContextCompat.getColor(activity,R.color.black));

        progBar = view.findViewById(R.id.progBarAds);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        recView.setItemViewCacheSize(25);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        categories_adapter = new Categories_Adapter(categoryModelList, activity,this);
        recView.setAdapter(categories_adapter);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });



    }


   private void getData() {
        Api.getService(Tags.base_url).get_categories().enqueue(new Callback<CategoriesDataModel>() {
            @Override
            public void onResponse(Call<CategoriesDataModel> call, Response<CategoriesDataModel> response) {
                progBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                if (response.isSuccessful()&&response.body()!=null) {
                    if (response.body().getData().size() > 0) {
                        categoryModelList.clear();
                        categoryModelList.addAll(response.body().getData());
                        categories_adapter.notifyDataSetChanged();

                    }
                } else {
                    try {
                        Log.e("code_error",response.code()+"_"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CategoriesDataModel> call, Throwable t) {
                try {
                    swipeRefresh.setRefreshing(false);

                    progBar.setVisibility(View.GONE);
                    Log.e("error message", t.getMessage());
                }catch (Exception e){}


            }
        });
    }


    public void setItemData(CategoriesDataModel.CategoryModel model) {
        Intent intent = new Intent(activity, SubCategoryActivity.class);
        intent.putExtra("data",model);
        startActivityForResult(intent,1133);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1133&&resultCode== Activity.RESULT_OK&&data!=null)
        {
            if (data.hasExtra("hasItems"))
            {
                CartSingleton singleton = CartSingleton.newInstance();
                activity.updateCartCount(singleton.getItemCount());
            }
        }
    }
}
