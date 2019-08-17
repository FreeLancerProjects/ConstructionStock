package com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home;

import android.app.Activity;
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
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.home_activity.activities.Home_Activity;
import com.creativeshare.constructionstock.activities_fragments.offer_details_activity.OfferDetailsActivity;
import com.creativeshare.constructionstock.adapters.NotificationsAdapter;
import com.creativeshare.constructionstock.models.NotificationDataModel;
import com.creativeshare.constructionstock.models.UserModel;
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

public class Fragment_Notifications extends Fragment {

    private ProgressBar progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private Home_Activity activity;
    private LinearLayout ll_not;
    private UserModel userModel;
    private Preferences preferences;
    private NotificationsAdapter adapter;
    private List<NotificationDataModel.NotificationModel> notificationModelList;
    private boolean isLoading = false;
    private int current_page = 1;
    private boolean isFirstTime = true;
    private String current_language;
    private int selected_pos = -1;


    @Override
    public void onStart() {
        super.onStart();
        if (!isFirstTime && adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        initView(view);
        return view;
    }


    public static Fragment_Notifications newInstance() {
        return new Fragment_Notifications();
    }

    private void initView(View view) {

        notificationModelList = new ArrayList<>();
        activity = (Home_Activity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        ll_not = view.findViewById(R.id.ll_not);

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);


        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);

        adapter = new NotificationsAdapter(notificationModelList,activity,this);
        recView.setAdapter(adapter);
        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int lastVisibleItem = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
                    int totalItems = manager.getItemCount();

                    if (lastVisibleItem >= (totalItems - 5) && !isLoading) {
                        isLoading = true;
                        notificationModelList.add(null);
                        adapter.notifyItemInserted(notificationModelList.size() - 1);
                        int next_page = current_page + 1;
                        loadMore(next_page);
                    }
                }
            }
        });
        getNotification();

    }

    public void getNotification() {

        if (userModel == null) {
            preferences = Preferences.getInstance();
            userModel = preferences.getUserData(activity);
        }






        Api.getService(Tags.base_url)
                .getNotification(userModel.getId(),1)
                .enqueue(new Callback<NotificationDataModel>() {
            @Override
            public void onResponse(Call<NotificationDataModel> call, Response<NotificationDataModel> response) {
                progBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    notificationModelList.clear();

                    if (response.body() != null && response.body().getData().size() > 0) {
                        ll_not.setVisibility(View.GONE);
                        notificationModelList.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                        isFirstTime = false;
                    } else {
                        ll_not.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();


                    }
                } else {

                    Toast.makeText(activity, R.string.failed, Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationDataModel> call, Throwable t) {
                try {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {
                }
            }
        });

    }
    private void loadMore(int page_index)
    {
        if (userModel == null) {
            preferences = Preferences.getInstance();
            userModel = preferences.getUserData(activity);
        }



        Api.getService(Tags.base_url)
                .getNotification(userModel.getId(),page_index)
                .enqueue(new Callback<NotificationDataModel>() {
                    @Override
                    public void onResponse(Call<NotificationDataModel> call, Response<NotificationDataModel> response) {
                        progBar.setVisibility(View.GONE);
                        isLoading = false;
                        notificationModelList.remove(notificationModelList.size()-1);
                        adapter.notifyItemRemoved(notificationModelList.size()-1);

                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData().size() > 0) {
                                notificationModelList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                current_page = response.body().getCurrent_page();
                            }
                        } else {

                            Toast.makeText(activity, R.string.failed, Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationDataModel> call, Throwable t) {
                        try {
                            notificationModelList.remove(notificationModelList.size()-1);
                            adapter.notifyItemRemoved(notificationModelList.size()-1);
                            isLoading = false;
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }

    public void setItemData(NotificationDataModel.NotificationModel notificationModel, int adapterPosition)
    {
        this.selected_pos =adapterPosition;
        if (notificationModel.getAction_type()==1)
        {
            Intent intent = new Intent(activity, OfferDetailsActivity.class);
            intent.putExtra("data",notificationModel);
            startActivityForResult(intent,1747);
        }
    }

    public void removeItem()
    {
        if (selected_pos!=-1)
        {
            notificationModelList.remove(selected_pos);
            adapter.notifyItemRemoved(selected_pos);
            selected_pos=-1;

            if (notificationModelList.size()==0)
            {
                ll_not.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1747&&resultCode== Activity.RESULT_OK&&data!=null)
        {
            if (data.hasExtra("action"))
            {
                int action = data.getIntExtra("action",0);
                if (action==1)
                {
                    activity.refreshFragmentOrder();
                    getNotification();
                    //accept
                }else if (action ==2)
                {
                    activity.refreshFragmentOrder();
                    getNotification();
                    //refuse
                }
            }
        }
    }
}
