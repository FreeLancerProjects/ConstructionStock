package com.creativeshare.constructionstock.activities_fragments.cart_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.language.Language_Helper;
import com.creativeshare.constructionstock.singleton.CartSingleton;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity {

    private LinearLayout llBack;
    private ImageView arrow;
    private String lang;
    private CartSingleton singleton;
    private FragmentManager fragmentManager;
    private Fragment_Cart fragment_cart;
    private Fragment_Address fragment_address;
    private int fragment_count = 0;

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base,Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        if (savedInstanceState==null)
        {
            fragmentManager = getSupportFragmentManager();
            displayFragmentCart();
        }
        initView();
    }

    private void initView() {
        singleton = CartSingleton.newInstance();
        Paper.init(this);
        lang = Paper.book().read("lang",Locale.getDefault().getLanguage());
        arrow = findViewById(R.id.arrow);
        if (lang.equals("ar"))
        {
            arrow.setRotation(180.0f);
        }
        llBack = findViewById(R.id.llBack);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back();
            }
        });


    }

    public void displayFragmentCart()
    {
        try
        {  fragment_count+=1;
            fragment_cart = Fragment_Cart.newInstance();
            if (fragment_cart.isAdded())
            {
                fragmentManager.beginTransaction().show(fragment_cart).commit();
            }else
            {
                fragmentManager.beginTransaction().add(R.id.fragmentContainer,fragment_cart,"fragment_cart").addToBackStack("fragment_cart").commit();
            }

        }catch (Exception e){}


    }


    public void displayFragmentAddress()
    {
        try
        {
            fragment_count+=1;
            fragment_address = Fragment_Address.newInstance();
            if (fragment_address.isAdded())
            {
                fragmentManager.beginTransaction().show(fragment_address).commit();
            }else
            {
                fragmentManager.beginTransaction().add(R.id.fragmentContainer,fragment_address,"fragment_address").addToBackStack("fragment_address").commit();
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
    public void Back() {

        Log.e("count",fragment_count+"_");
        if (fragment_count>1)
        {
            try {
                fragment_count=-1;
                super.onBackPressed();
            }catch (Exception e){}

        }else
            {
                Log.e("ssss","ggggg");
                try {
                    if (singleton.getItemCount()>0)
                    {
                        Intent intent = getIntent();
                        if (intent!=null)
                        {
                            intent.putExtra("hasItems",true);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    }else
                    {
                        Intent intent = getIntent();
                        if (intent!=null)
                        {
                            intent.putExtra("hasItems",false);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    }
                }catch (Exception e){}

            }


    }

    @Override
    public void onBackPressed() {
        Back();
    }




}
