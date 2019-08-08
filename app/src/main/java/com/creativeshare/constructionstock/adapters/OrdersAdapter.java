package com.creativeshare.constructionstock.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_Orders;
import com.creativeshare.constructionstock.models.OrderDataModel;
import com.creativeshare.constructionstock.tags.Tags;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<OrderDataModel.OrderModel> orderModelList;
    private Context context;
    private Fragment fragment;
    private String user_type;

    public OrdersAdapter(List<OrderDataModel.OrderModel> orderModelList, Context context, Fragment fragment, String user_type) {

        this.orderModelList = orderModelList;
        this.context = context;
        this.fragment = fragment;
        this.user_type = user_type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            View view = LayoutInflater.from(context).inflate(R.layout.order_row, parent, false);
            return new MyHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.load_more_row, parent, false);
            return new LoadMoreHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {

            final MyHolder myHolder = (MyHolder) holder;
            OrderDataModel.OrderModel orderModel = orderModelList.get(myHolder.getAdapterPosition());
            myHolder.BindData(orderModel);

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment instanceof Fragment_Orders)
                    {
                        OrderDataModel.OrderModel orderModel = orderModelList.get(myHolder.getAdapterPosition());
                        Fragment_Orders fragment_current_order = (Fragment_Orders) fragment;
                   ///     fragment_current_order.setItemData(orderModel,holder.getAdapterPosition());

                    }
                }
            });

        } else {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.progBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private ImageView image_state;
        private TextView tv_name, tv_order_num,tv_order_type;

        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            image_state = itemView.findViewById(R.id.image_state);

            tv_order_num = itemView.findViewById(R.id.tv_order_num);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_order_type = itemView.findViewById(R.id.tv_order_type);


        }

        public void BindData(OrderDataModel.OrderModel orderModel) {
            tv_order_num.setText("#" + orderModel.getId());

            if (user_type.equals(Tags.TYPE_USER))
            {
                tv_name.setText(orderModel.getTo_name());

            }else
                {
                    tv_name.setText(orderModel.getFrom_name());

                }



            if (fragment instanceof Fragment_Orders)
            {
                image_state.setImageResource(R.drawable.ic_clock);
                image_state.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary));
            }else
                {
                    image_state.setImageResource(R.drawable.ic_done);
                    image_state.setColorFilter(ContextCompat.getColor(context,R.color.done));

                }




        }
    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder {
        private ProgressBar progBar;

        public LoadMoreHolder(View itemView) {
            super(itemView);
            progBar = itemView.findViewById(R.id.progBar);
            progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemViewType(int position) {
        OrderDataModel.OrderModel orderModel = orderModelList.get(position);
        if (orderModel == null) {
            return ITEM_LOAD;
        } else {
            return ITEM_DATA;

        }



    }
}
