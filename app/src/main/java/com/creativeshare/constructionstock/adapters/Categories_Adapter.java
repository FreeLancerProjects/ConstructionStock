package com.creativeshare.constructionstock.adapters;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.home_activity.activities.Home_Activity;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_main;
import com.creativeshare.constructionstock.models.Categories_Model;
import com.creativeshare.constructionstock.preferences.Preferences;
import com.creativeshare.constructionstock.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Categories_Adapter extends RecyclerView.Adapter<Categories_Adapter.Cat_Holder> {
    List<Categories_Model.CData> CDatalist;
    Context context;
    Home_Activity activity;
    Preferences preferences;
    String current_lang;
    String data;
    Fragment_main fragment_main;
    private String current_language;


    public Categories_Adapter(List<Categories_Model.CData> CDatalist, Context context, Fragment_main fragment_main) {
        this.CDatalist = CDatalist;
        this.context = context;
        activity = (Home_Activity) context;
        preferences = Preferences.getInstance();
        this.fragment_main = fragment_main;
        Paper.init(context);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @Override
    public Cat_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_layout_row, viewGroup, false);
        Cat_Holder cat_holder = new Cat_Holder(v);
        return cat_holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final Cat_Holder viewHolder, final int i) {
        Categories_Model.CData model = CDatalist.get(i);

        viewHolder.BindData(model);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Categories_Model.CData model = CDatalist.get(i);
                fragment_main.setItemData(model);
            }
        });



    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class Cat_Holder extends RecyclerView.ViewHolder {
        TextView title;
        RoundedImageView image;

        public Cat_Holder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_title);
            image = itemView.findViewById(R.id.img_main);
        }
        public void BindData(Categories_Model.CData cData)
        {

            if (current_language.equals("ar"))
            {
                title.setText(cData.getAr_title());
            }else
            {
                title.setText(cData.getEn_title());
            }

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_CONTAINER_URL+cData.getImage())).fit().into(image);
        }

    }
    public void setSelectedPos(int pos)
    {

        sparseBooleanArray.clear();
        sparseBooleanArray.put(pos,true);
        notifyDataSetChanged();
    }
}
