package com.creativeshare.constructionstock.activities_fragments.sub_category_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.adapters.SubCategories_Adapter;
import com.creativeshare.constructionstock.language.Language_Helper;
import com.creativeshare.constructionstock.models.CategoriesDataModel;
import com.creativeshare.constructionstock.models.ItemCartModel;
import com.creativeshare.constructionstock.models.UserModel;
import com.creativeshare.constructionstock.preferences.Preferences;
import com.creativeshare.constructionstock.share.Common;
import com.creativeshare.constructionstock.singleton.CartSingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class SubCategoryActivity extends AppCompatActivity {
    private RecyclerView recView;
    private GridLayoutManager manager;
    private CategoriesDataModel.CategoryModel categoryModel;
    private List<CategoriesDataModel.SubCategory> subCategoryList;
    private SubCategories_Adapter adapter;
    private TextView tvNoDetails;
    private LinearLayout llBack;
    private ImageView arrow;
    private String lang;
    private UserModel userModel;
    private Preferences preferences;
    private CartSingleton cartSingleton;
    private boolean hasItem = false;

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base,Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        initView();
        getDataFromIntent();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {

            categoryModel = (CategoriesDataModel.CategoryModel) intent.getSerializableExtra("data");
            updateUI();
        }
    }



    private void initView() {
        cartSingleton = CartSingleton.newInstance();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        subCategoryList = new ArrayList<>();
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        arrow = findViewById(R.id.arrow);
        if (lang.equals("ar"))
        {
            arrow.setRotation(180.0f);
        }
        llBack = findViewById(R.id.llBack);
        tvNoDetails = findViewById(R.id.tvNoDetails);

        recView = findViewById(R.id.recView);
        manager = new GridLayoutManager(this,2);
        recView.setLayoutManager(manager);
        adapter = new SubCategories_Adapter(subCategoryList,this);
        recView.setAdapter(adapter);



        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back();
            }
        });
    }



    private void updateUI() {
        subCategoryList.addAll(categoryModel.getSub_categories());
        adapter.notifyDataSetChanged();
        if (subCategoryList.size()==0)
        {
            tvNoDetails.setVisibility(View.VISIBLE);
        }
    }

    public void setItemData(CategoriesDataModel.SubCategory model) {
        if (userModel!=null)
        {
           CreateAmountDialog(model);
        }else
            {
                Common.CreateSignAlertDialog(this,getString(R.string.si_su));
            }
    }

    private  void CreateAmountDialog(final CategoriesDataModel.SubCategory subCategory)
    {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_amount,null);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        final EditText edtAmount = view.findViewById(R.id.edtAmount);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = edtAmount.getText().toString().trim();
                if (!TextUtils.isEmpty(amount))
                {
                    dialog.dismiss();
                    edtAmount.setError(null);
                    Common.CloseKeyBoard(SubCategoryActivity.this,edtAmount);
                    addToCart(subCategory,amount);
                }else
                    {
                        edtAmount.setError(getString(R.string.field_req));

                    }
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }

    private void addToCart(CategoriesDataModel.SubCategory model, String amount) {
        ItemCartModel itemCartModel = new ItemCartModel(model.getId(),model.getAr_title(),model.getEn_title(),model.getImage(),model.getCat_id(),model.getId(),Double.parseDouble(amount));
        cartSingleton.addItem(itemCartModel);
        Toast.makeText(this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
        hasItem = true;
    }

    @Override
    public void onBackPressed() {
        Back();
    }

    private void Back() {
        if (hasItem)
        {
            Intent intent = getIntent();
            intent.putExtra("hasItems",true);
            setResult(RESULT_OK,intent);
            finish();
        }else
            {
                finish();
            }


    }
}
