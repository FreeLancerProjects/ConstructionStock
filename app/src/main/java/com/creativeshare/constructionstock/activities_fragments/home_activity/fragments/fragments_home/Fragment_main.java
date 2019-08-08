package com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.home_activity.activities.Home_Activity;
import com.creativeshare.constructionstock.adapters.Categories_Adapter;
import com.creativeshare.constructionstock.models.CategoriesDataModel;
import com.creativeshare.constructionstock.preferences.Preferences;
import com.creativeshare.constructionstock.remote.Api;
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
    private ImageView arrow;
    private LinearLayout ll_back;
    private RecyclerView recView;
    private LinearLayoutManager manager;
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
        arrow = view.findViewById(R.id.arrow);
        ll_back = view.findViewById(R.id.ll_back);
        recView = view.findViewById(R.id.recv_main);
        progBar = view.findViewById(R.id.progBarAds);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        recView.setItemViewCacheSize(25);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        categories_adapter = new Categories_Adapter(categoryModelList, activity,this);
        recView.setAdapter(categories_adapter);



    }


   private void getData() {
        Api.getService(Tags.base_url).get_categories().enqueue(new Callback<CategoriesDataModel>() {
            @Override
            public void onResponse(Call<CategoriesDataModel> call, Response<CategoriesDataModel> response) {
                progBar.setVisibility(View.GONE);

                if (response.isSuccessful()&&response.body()!=null) {
                    if (response.body().getData().size() > 0) {
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
                    progBar.setVisibility(View.GONE);
                    Log.e("error message", t.getMessage());
                }catch (Exception e){}


            }
        });
    }


    public void setItemData(CategoriesDataModel.CategoryModel model) {
        activity.DisplayFragmentMain();
    }
}
