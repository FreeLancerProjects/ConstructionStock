package com.creativeshare.constructionstock.activities_fragments.home_activity.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_Contact_Us;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_Home;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_More;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_Notifications;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_Terms_Condition;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_main;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.fragment_orders.Fragment_Orders;
import com.creativeshare.constructionstock.activities_fragments.sign_in_sign_up_activity.activity.Login_Activity;
import com.creativeshare.constructionstock.language.Language_Helper;
import com.creativeshare.constructionstock.models.NotificationStatusModel;
import com.creativeshare.constructionstock.models.UserModel;
import com.creativeshare.constructionstock.preferences.Preferences;
import com.creativeshare.constructionstock.remote.Api;
import com.creativeshare.constructionstock.singleton.CartSingleton;
import com.creativeshare.constructionstock.tags.Tags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home_Activity extends AppCompatActivity {

    private int fragment_count = 0;
    private FragmentManager fragmentManager;
    private Fragment_Home fragment_home;
    private Fragment_main fragment_main;
    private Fragment_More fragment_more;
    private Fragment_Terms_Condition fragment_terms_condition;
    private Fragment_Contact_Us fragment_contact_us;
    private Fragment_Notifications fragment_notifications;
    private Fragment_Orders fragment_orders;
    private UserModel userModel;
    private Preferences preferences;
    private CartSingleton singleton;


    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base,Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        fragmentManager = this.getSupportFragmentManager();
        if (savedInstanceState == null) {
            DisplayFragmentHome();
            DisplayFragmentMain();
            singleton = CartSingleton.newInstance();
            try {
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (fragment_home!=null&&fragment_home.isAdded())
                                {
                                    fragment_home.updateCartCount(singleton.getItemCount());
                                }
                            }
                        },1000);

                getDataFromIntent();



            }catch (Exception e){}





        }
        try {
            String lastVisit = preferences.getLastVisit(this);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            String now = dateFormat.format(new Date(Calendar.getInstance().getTimeInMillis()));

            if (!lastVisit.equals(now))
            {
                updateVisit(now,(Calendar.getInstance().getTimeInMillis()/1000));

            }
            updateUserToken();
        }catch (Exception e){}



    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenToNotification(NotificationStatusModel statusModel)
    {
        try {
            refreshFragmentOrder();
            refreshFragmentNotification();
        }catch (Exception e){}

    }
    private void getDataFromIntent()
    {
        Intent intent = getIntent();

        if (intent!=null&&intent.hasExtra("status"))
        {
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try
                            {

                                DisplayFragmentOrders();
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                if (manager!=null)
                                {
                                    manager.cancel(1250);
                                }
                            }catch (Exception e){}
                        }
                    },1000);

        }else if (intent!=null&&intent.hasExtra("hasDataNotification"))
        {
            if (intent.getBooleanExtra("hasDataNotification",false))
            {
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try
                                {

                                    DisplayFragmentOrders();
                                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    if (manager!=null)
                                    {
                                        manager.cancel(1250);
                                    }
                                }catch (Exception e){}
                            }
                        },1000);
            }

        }
    }
    public void updateUserData(UserModel userModel)
    {
        try {
            this.userModel = userModel;

        }catch (Exception e){}

    }
    private void updateVisit(final String now, long time) {
        Api.getService(Tags.base_url)
                .updateVisit(now,1)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            preferences.setLastVisit(Home_Activity.this,now);
                        }else
                        {
                            try {
                                Log.e("errorVisitCode",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            Log.e("Error",t.getMessage()+"_");
                        }catch (Exception e){}
                    }
                });
    }

    private void updateUserToken() {
        if (userModel!=null)
        {
            EventBus.getDefault().register(this);

            FirebaseInstanceId.getInstance()
                    .getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {

                            if (task.isSuccessful())
                            {
                                String token = task.getResult().getToken();
                                Api.getService(Tags.base_url)
                                        .updateToken(userModel.getId(),token)
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful())
                                                {
                                                    Log.e("token","updated successfully");
                                                }else
                                                    {
                                                        Log.e("token","updated failed");

                                                    }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                try {
                                                    Log.e("token",t.getMessage());

                                                }catch (Exception e){}

                                            }
                                        });
                            }
                        }
                    });
        }
    }

    public void DisplayFragmentHome()
    {
        fragment_count += 1;

        if (fragment_home == null) {
            fragment_home = Fragment_Home.newInstance();
        }

        if (fragment_home.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_home).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home, "fragment_home").addToBackStack("fragment_home").commit();
        }

    }
    public void DisplayFragmentMain()
    {


        if (fragment_notifications != null && fragment_notifications.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_notifications).commit();
        }

        if (fragment_orders != null && fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_orders).commit();
        }

        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_main == null) {
            fragment_main = Fragment_main.newInstance();
        }

        if (fragment_main.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_main).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();
        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.UpdateAHBottomNavigationPosition(0);
        }
    }

    public void DisplayFragmentMore()
    {
        if (fragment_notifications != null && fragment_notifications.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_notifications).commit();
        }

        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }

        if (fragment_orders != null && fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_orders).commit();
        }
        if (fragment_more == null) {
            fragment_more = Fragment_More.newInstance();
        }

        if (fragment_more.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_more).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_more, "fragment_more").addToBackStack("fragment_more").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.UpdateAHBottomNavigationPosition(3);
        }

    }
    public void DisplayFragmentOrders()
    {

        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_notifications != null && fragment_notifications.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_notifications).commit();
        }

        if (fragment_orders == null) {
            fragment_orders = Fragment_Orders.newInstance();
        }

        if (fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_orders).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_orders, "fragment_orders").addToBackStack("fragment_orders").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.UpdateAHBottomNavigationPosition(1);
        }






    }

    public void DisplayFragmentNotification()
    {



        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }
       /* if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }*/
        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }

        if (fragment_orders != null && fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_orders).commit();
        }
        if (fragment_notifications == null) {
            fragment_notifications = Fragment_Notifications.newInstance();
        }

        if (fragment_notifications.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_notifications).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_notifications, "fragment_notifications").addToBackStack("fragment_notifications").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.UpdateAHBottomNavigationPosition(2);
        }

    }
    public void DisplayFragmentContactUS()
    {

        fragment_count +=1;

        fragment_contact_us = Fragment_Contact_Us.newInstance();

        if (fragment_contact_us.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_contact_us).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_contact_us, "fragment_contact_us").addToBackStack("fragment_contact_us").commit();

        }


    }

    public void DisplayFragmentTerms_AboutUs(int type)
    {

        fragment_count +=1;

        fragment_terms_condition = Fragment_Terms_Condition.newInstance(type);

        if (fragment_terms_condition.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_terms_condition).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_terms_condition, "fragment_terms_condition").addToBackStack("fragment_terms_condition").commit();

        }


    }

    public void updateCartCount(int count)
    {
        try {
            if (fragment_home!=null&&fragment_home.isAdded())
            {
                fragment_home.updateCartCount(count);
            }
        }catch (Exception e){}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void RefreshActivity(String lang)
    {
        Paper.book().write("lang",lang);
        Language_Helper.setNewLocale(this,lang);
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent =  getIntent();
                        finish();
                        startActivity(intent);
                    }
                },1050);



    }
    @Override
    public void onBackPressed() {
        Back();
    }
    public void NavigateToSignInActivity(boolean isSignIn) {

        Intent intent = new Intent(this, Login_Activity.class);
        intent.putExtra("sign_up",isSignIn);
        startActivity(intent);
        finish();

    }
    public void Back() {
        if (fragment_count > 1) {
            super.onBackPressed();
            fragment_count -= 1;
        } else {
            if (fragment_main!=null&&fragment_main.isAdded()&&!fragment_main.isVisible())
            {
                DisplayFragmentMain();
            }else
            {
                if (singleton.getItemCount()>0)
                {
                    CreateCartDialog(singleton.getItemCount());

                }else
                    {
                        if (userModel!=null)
                        {
                            finish();
                        }else
                        {
                            NavigateToSignInActivity(true);
                        }
                    }

            }
        }

    }

    private void CreateCartDialog(int itemCount) {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_remove_cart_item,null);
        TextView tvCartCount = view.findViewById(R.id.tvCartCount);
        tvCartCount.setText(String.valueOf(itemCount));

        TextView tvDelete = view.findViewById(R.id.tvDelete);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    singleton.clear();
                    if (fragment_home!=null&&fragment_home.isAdded())
                    {
                        fragment_home.updateCartCount(0);
                    }
                }catch (Exception e){}

                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();

    }

    public void Logout() {

        if (userModel!=null)
        {
            preferences.create_update_userdata(this,null);
            preferences.create_update_session(this, Tags.session_logout);
            NavigateToSignInActivity(true);
            CartSingleton singleton = CartSingleton.newInstance();
            singleton.clear();
            this.userModel = null;

        }else
        {

            Intent intent = new Intent(Home_Activity.this,Login_Activity.class);
            startActivity(intent);
            finish();

        }

    }

    public void refreshFragmentOrder() {

        try {
            if (fragment_orders!=null&&fragment_orders.isAdded())
            {
                fragment_orders.refreshFragment();
            }
        }catch (Exception e)
        {

        }

    }

    public void refreshFragmentNotification() {

        try {
            if (fragment_notifications!=null&&fragment_notifications.isAdded())
            {
                fragment_notifications.getNotification();
            }
        }catch (Exception e)
        {

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }
    }
}