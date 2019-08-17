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
import com.creativeshare.constructionstock.models.OrderDataModel;
import com.creativeshare.constructionstock.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.Category_Holder> {
    private List<OrderDataModel.OrderDetails> orderDetailsList;
    private Context context;
    private String current_lang;


    public OrderProductAdapter(List<OrderDataModel.OrderDetails> orderDetailsList, Context context) {
        this.orderDetailsList = orderDetailsList;
        this.context = context;
        Paper.init(context);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @Override
    public Category_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_products_row, viewGroup, false);
        return  new Category_Holder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final Category_Holder viewHolder, final int i) {
        OrderDataModel.OrderDetails model = orderDetailsList.get(i);

        viewHolder.BindData(model);





    }

    @Override
    public int getItemCount() {
        return orderDetailsList.size();
    }

    public class Category_Holder extends RecyclerView.ViewHolder {
        private TextView tvTitle,tvAmount;
        private RoundedImageView image;

        public Category_Holder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            image = itemView.findViewById(R.id.image);

        }
        public void BindData(OrderDataModel.OrderDetails orderDetails)
        {

            if (current_lang.equals("ar"))
            {
                tvTitle.setText(orderDetails.getSub_ar_title());
            }else
            {
                tvTitle.setText(orderDetails.getSub_en_title());
            }

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL + orderDetails.getSub_image())).fit().into(image);
            tvAmount.setText(String.valueOf(orderDetails.getAmount()));

        }

    }

}
