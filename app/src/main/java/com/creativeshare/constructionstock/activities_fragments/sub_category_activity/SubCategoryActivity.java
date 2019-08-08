package com.creativeshare.constructionstock.activities_fragments.sub_category_activity;

import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.models.CategoriesDataModel;

import java.util.List;

public class SubCategoryActivity extends AppCompatActivity {
    private RecyclerView recView;
    private GridLayoutManager manager;
    private List<CategoriesDataModel.SubCategory> subCategoryList;
    private ProgressBar progBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
    }
}
