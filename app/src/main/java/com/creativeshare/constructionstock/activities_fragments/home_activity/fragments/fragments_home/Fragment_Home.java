package com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.cart_activity.CartActivity;
import com.creativeshare.constructionstock.activities_fragments.home_activity.activities.Home_Activity;
import com.creativeshare.constructionstock.models.UserModel;
import com.creativeshare.constructionstock.preferences.Preferences;
import com.creativeshare.constructionstock.share.Common;
import com.creativeshare.constructionstock.singleton.CartSingleton;


import java.util.Locale;

import io.paperdb.Paper;


public class Fragment_Home extends Fragment {
    private AHBottomNavigation bottomNavigationView;
    private Home_Activity activity;
    private TextView tv_title;
    private String current_lang;
    private UserModel userModel;
    private Preferences preferences;
    private FrameLayout flCart;
    private TextView tvCartCount;
    private CartSingleton singleton;

    public static Fragment_Home newInstance() {
        return new Fragment_Home();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);

        setUpBottomNav();

        return view;
    }

    private void initView(View view) {
        singleton = CartSingleton.newInstance();
        preferences = Preferences.getInstance();
        activity = (Home_Activity) getActivity();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        tv_title = view.findViewById(R.id.tv_title);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        flCart = view.findViewById(R.id.flCart);
        tvCartCount = view.findViewById(R.id.tvCartCount);

        flCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CartActivity.class);
                startActivityForResult(intent,1598);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1598 && resultCode== Activity.RESULT_OK&&data!=null)
        {
            if (data.hasExtra("hasItems"))
            {
                boolean hasItems = data.getBooleanExtra("hasItems",false);
                if (hasItems)
                {
                    updateCartCount(singleton.getItemCount());
                }else
                    {
                        updateCartCount(0);
                    }
            }else if (data.hasExtra("order_sent"))
            {

            }
        }
    }

    private void setUpBottomNav() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.home, R.drawable.ic_home, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.orders, R.drawable.ic_gift, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.not), R.drawable.ic_notification, R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.more, R.drawable.ic_more, R.color.colorPrimary);

        bottomNavigationView.addItem(item1);
        bottomNavigationView.addItem(item2);
        bottomNavigationView.addItem(item3);
        bottomNavigationView.addItem(item4);

        bottomNavigationView.setAccentColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        bottomNavigationView.setDefaultBackgroundColor(ContextCompat.getColor(activity, R.color.white));
        bottomNavigationView.setInactiveColor(ContextCompat.getColor(activity, R.color.black));
        bottomNavigationView.setForceTint(true);
        bottomNavigationView.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigationView.setColored(false);
        bottomNavigationView.setTitleTextSizeInSp(16,13);
        bottomNavigationView.setCurrentItem(0);


        bottomNavigationView.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        activity.DisplayFragmentMain();
                        break;
                    case 1:
                        if (userModel!=null)
                        {
                            activity.DisplayFragmentOrders();

                        }else
                            {
                                Common.CreateUserNotSignInAlertDialog(activity);

                            }
                        break;
                    case 2:

                        if (userModel!=null)
                        {
                            activity.DisplayFragmentNotification();
                        }else
                        {
                            Common.CreateUserNotSignInAlertDialog(activity);

                        }
                        break;


                    case 3:
                       // if (userModel!=null)
                      //  {
                            activity.DisplayFragmentMore();

                       // }else
                     //   {
                          //  Common.CreateUserNotSignInAlertDialog(activity);

                       // }
                        break;


                }
                return false;
            }
        });
    }

    public void UpdateAHBottomNavigationPosition(int pos) {

        if (pos == 0) {
            tv_title.setText(getString(R.string.home));
        } else if (pos == 1) {
            tv_title.setText(getString(R.string.orders));

        } else if (pos == 2) {

            tv_title.setText(getString(R.string.not));

        }
        else if (pos == 3) {
            tv_title.setText(getString(R.string.more));

        }
        bottomNavigationView.setCurrentItem(pos, false);
    }

    public void updateCartCount(int count)
    {
        if (count ==0)
        {
            tvCartCount.setVisibility(View.GONE);
        }else
            {
                tvCartCount.setText(String.valueOf(count));
                tvCartCount.setVisibility(View.VISIBLE);
            }
    }
}