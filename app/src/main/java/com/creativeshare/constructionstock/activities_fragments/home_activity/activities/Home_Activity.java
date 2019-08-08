package com.creativeshare.constructionstock.activities_fragments.home_activity.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_Contact_Us;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_Home;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_More;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_Notifications;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_Orders;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_Terms_Condition;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_main;
import com.creativeshare.constructionstock.activities_fragments.sign_in_sign_up_activity.activity.Login_Activity;
import com.creativeshare.constructionstock.language.Language_Helper;
import com.creativeshare.constructionstock.models.UserModel;
import com.creativeshare.constructionstock.preferences.Preferences;
import com.creativeshare.constructionstock.tags.Tags;

import java.util.Locale;

import io.paperdb.Paper;


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
       /* if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }*/
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
            fragment_home.UpdateAHBottomNavigationPosition(4);
        }

    }
    public void DisplayFragmentOrders()
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

   /* public void DisplayFragmentMain() {

        if (userModel!=null)
        {
            fragment_count += 1;

            fragment_main = Fragment_main.newInstance();


            if (fragment_main.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_main).commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

            }
        }else
        {
            Common.CreateUserNotSignInAlertDialog(this);
        }


    }
*/
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

    public void Logout() {
        this.userModel = null;
        preferences.create_update_userdata(this,null);
        preferences.create_update_session(this, Tags.session_logout);
        NavigateToSignInActivity(true);
    }


}