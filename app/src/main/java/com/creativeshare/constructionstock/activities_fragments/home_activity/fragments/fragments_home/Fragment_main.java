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

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.home_activity.activities.Home_Activity;
import com.creativeshare.constructionstock.adapters.Categories_Adapter;
import com.creativeshare.constructionstock.models.Categories_Model;
import com.creativeshare.constructionstock.preferences.Preferences;
import com.creativeshare.constructionstock.remote.Api;
import com.creativeshare.constructionstock.tags.Tags;


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
    private RecyclerView categories;
    private ProgressBar progBar;
    private Categories_Adapter categories_adapter;
    private ArrayList<Categories_Model> categories_models;
    private Categories_Model categories_model;
    List<Categories_Model.CData> list;
    public static Fragment_main newInstance() {
        return new Fragment_main();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        intitview(view);
        getdata();
        return view;
    }

    private void intitview(View view) {
        categories_models = new ArrayList<>();
        activity = (Home_Activity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        curent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        arrow = view.findViewById(R.id.arrow);
        ll_back = view.findViewById(R.id.ll_back);

        categories = view.findViewById(R.id.recv_main);
        progBar = view.findViewById(R.id.progBarAds);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
      /*  if (curent_language.equals("ar")) {
//            arrow.setRotation(180.0f);
        }*/

        categories.setItemViewCacheSize(25);
        categories.setDrawingCacheEnabled(true);
        categories.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        categories_adapter = new Categories_Adapter(list, activity,this);
        categories.setAdapter(categories_adapter);
        categories.setLayoutManager(new GridLayoutManager(activity, 1));

       /* ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.Back();
            }
        });*/

    }


   private void getdata() {
        Api.getService(Tags.base_url).get_categories().enqueue(new Callback<List<Categories_Model>>() {
            @Override
            public void onResponse(Call<List<Categories_Model>> call, Response<List<Categories_Model>> response) {
                if (response.isSuccessful()) {
                    progBar.setVisibility(View.GONE);
                    if (response.body().size() > 0) {
                        categories_models.addAll(response.body());
                        categories_adapter.notifyDataSetChanged();

                    } else {
                        Log.e("message", "no data found");
                    }
                } else {
                    Log.e("error code", response.code() + "" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Categories_Model>> call, Throwable t) {
                progBar.setVisibility(View.GONE);

                Log.e("error message", t.getMessage());

            }
        });
    }


    public void setItemData(Categories_Model.CData model) {
        activity.DisplayFragmentMain();
    }
}
