package com.creativeshare.constructionstock.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home.Fragment_Notifications;
import com.creativeshare.constructionstock.models.NotificationDataModel;
import com.creativeshare.constructionstock.share.TimeAgo;
import com.creativeshare.constructionstock.tags.Tags;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;
    private final int ITEM_DATA_NOT_OTHER= 3;

    private List<NotificationDataModel.NotificationModel> notificationModelList;
    private Context context;
    private Fragment_Notifications fragment;

    public NotificationsAdapter(List<NotificationDataModel.NotificationModel> notificationModelList, Context context, Fragment_Notifications fragment) {

        this.notificationModelList = notificationModelList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            View view = LayoutInflater.from(context).inflate(R.layout.notifications_row, parent, false);
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
            NotificationDataModel.NotificationModel notificationModel = notificationModelList.get(myHolder.getAdapterPosition());
            myHolder.BindData(notificationModel);

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotificationDataModel.NotificationModel notificationModel = notificationModelList.get(myHolder.getAdapterPosition());
                    //fragment.setItemData(notificationModel,holder.getAdapterPosition());
                }
            });

        } else {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.progBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView tv_name, tv_order_num, tv_notification_date,tv_add_rate;

        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tv_notification_date = itemView.findViewById(R.id.tv_notification_date);
            tv_order_num = itemView.findViewById(R.id.tv_order_num);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_add_rate = itemView.findViewById(R.id.tv_add_rate);


        }

        public void BindData(NotificationDataModel.NotificationModel notificationModel) {
            tv_order_num.setText("#" + notificationModel.getOrder_id());

            tv_notification_date.setText(TimeAgo.getTimeAgo(Long.parseLong(notificationModel.getNot_date()) * 1000, context));
            tv_name.setText(notificationModel.getFrom_name());



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
        NotificationDataModel.NotificationModel notificationModel = notificationModelList.get(position);
        if (notificationModel == null) {
            return ITEM_LOAD;
        } else {
            if (notificationModel.getNotfication_type().equals(Tags.NOTIFICATION_TYPE_OTHER))
            {
                return ITEM_DATA_NOT_OTHER;
            }else
                {
                    return ITEM_DATA;

                }
        }



    }
}
