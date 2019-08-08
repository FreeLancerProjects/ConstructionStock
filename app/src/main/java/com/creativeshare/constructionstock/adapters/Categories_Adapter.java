package com.creativeshare.constructionstock.adapters;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_main;
import com.creativeshare.constructionstock.models.CategoriesDataModel;
import com.creativeshare.constructionstock.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Categories_Adapter extends RecyclerView.Adapter<Categories_Adapter.Category_Holder> {
    private List<CategoriesDataModel.CategoryModel> categoryModelList;
    private Context context;
    private String current_lang;
    private Fragment_main fragment_main;


    public Categories_Adapter(List<CategoriesDataModel.CategoryModel> categoryModelList, Context context, Fragment_main fragment_main) {
        this.categoryModelList = categoryModelList;
        this.context = context;
        this.fragment_main = fragment_main;
        Paper.init(context);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @Override
    public Category_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_layout_row, viewGroup, false);
        return  new Category_Holder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final Category_Holder viewHolder, final int i) {
        CategoriesDataModel.CategoryModel model = categoryModelList.get(i);

        viewHolder.BindData(model);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CategoriesDataModel.CategoryModel model = categoryModelList.get(i);
                fragment_main.setItemData(model);
            }
        });



    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class Category_Holder extends RecyclerView.ViewHolder {
        private TextView title;
        private RoundedImageView image;

        public Category_Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            image = itemView.findViewById(R.id.img_main);
        }
        public void BindData(CategoriesDataModel.CategoryModel categoryModel)
        {

            if (current_lang.equals("ar"))
            {
                title.setText(categoryModel.getAr_title());
            }else
            {
                title.setText(categoryModel.getEn_title());
            }

            Picasso.with(context).load(Uri.parse(Tags.categories_url+ categoryModel.getImage())).fit().into(image);
        }

    }

}
