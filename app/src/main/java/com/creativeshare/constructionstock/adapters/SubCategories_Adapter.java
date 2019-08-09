package com.creativeshare.constructionstock.adapters;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.sub_category_activity.SubCategoryActivity;
import com.creativeshare.constructionstock.models.CategoriesDataModel;
import com.creativeshare.constructionstock.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class SubCategories_Adapter extends RecyclerView.Adapter<SubCategories_Adapter.Category_Holder> {
    private List<CategoriesDataModel.SubCategory> subCategoryList;
    private Context context;
    private String current_lang;
    private SubCategoryActivity activity;


    public SubCategories_Adapter(List<CategoriesDataModel.SubCategory> subCategoryList, Context context) {
        this.subCategoryList = subCategoryList;
        this.context = context;
        activity = (SubCategoryActivity) context;
        Paper.init(context);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @Override
    public Category_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sub_category_row, viewGroup, false);
        return  new Category_Holder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final Category_Holder viewHolder, final int i) {
        CategoriesDataModel.SubCategory model = subCategoryList.get(i);

        viewHolder.BindData(model);

        viewHolder.llAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CategoriesDataModel.SubCategory model = subCategoryList.get(i);
                activity.setItemData(model);
            }
        });



    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }

    public class Category_Holder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private RoundedImageView image;
        private LinearLayout llAddToCart;

        public Category_Holder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            image = itemView.findViewById(R.id.image);
            llAddToCart = itemView.findViewById(R.id.llAddToCart);

        }
        public void BindData(CategoriesDataModel.SubCategory subCategory)
        {

            if (current_lang.equals("ar"))
            {
                tvTitle.setText(subCategory.getAr_title());
            }else
            {
                tvTitle.setText(subCategory.getEn_title());
            }

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL + subCategory.getImage())).fit().into(image);
        }

    }

}
