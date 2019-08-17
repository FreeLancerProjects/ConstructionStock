package com.creativeshare.constructionstock.adapters;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.models.ItemCartModel;
import com.creativeshare.constructionstock.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Cart_Holder> {
    private List<ItemCartModel> itemCartModelList;
    private Context context;
    private String current_lang;


    public CartAdapter(List<ItemCartModel> itemCartModelList, Context context) {
        this.itemCartModelList = itemCartModelList;
        this.context = context;
        Paper.init(context);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @Override
    public Cart_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_row, viewGroup, false);
        return  new Cart_Holder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final Cart_Holder viewHolder, final int i) {
        ItemCartModel model = itemCartModelList.get(i);

        viewHolder.BindData(model);





    }

    @Override
    public int getItemCount() {
        return itemCartModelList.size();
    }

    public class Cart_Holder extends RecyclerView.ViewHolder {
        private TextView tvTitle,tvAmount;
        private RoundedImageView image;
        public ConstraintLayout consBackground,consForeground;
        public LinearLayout llLeft,llRight;


        public Cart_Holder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAmount = itemView.findViewById(R.id.tvAmount);

            consBackground = itemView.findViewById(R.id.consBackground);
            consForeground = itemView.findViewById(R.id.consForeground);
            llLeft = itemView.findViewById(R.id.llLeft);
            llRight = itemView.findViewById(R.id.llRight);


        }
        public void BindData(ItemCartModel itemCartModel)
        {

            if (current_lang.equals("ar"))
            {
                tvTitle.setText(itemCartModel.getAr_name());
            }else
            {
                tvTitle.setText(itemCartModel.getEn_name());
            }

            tvAmount.setText(String.valueOf(itemCartModel.getAmount()));
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL + itemCartModel.getImage())).fit().into(image);
        }

    }

}
