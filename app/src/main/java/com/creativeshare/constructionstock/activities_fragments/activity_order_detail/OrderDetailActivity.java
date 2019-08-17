package com.creativeshare.constructionstock.activities_fragments.activity_order_detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.adapters.OrderProductAdapter;
import com.creativeshare.constructionstock.language.Language_Helper;
import com.creativeshare.constructionstock.models.OrderDataModel;
import com.creativeshare.constructionstock.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class OrderDetailActivity extends AppCompatActivity {

    private ImageView arrow;
    private LinearLayout llBack;
    private CircleImageView image;
    private TextView tvName,tvOffer;
    private String lang;
    private OrderDataModel.OrderModel orderModel;
    private RecyclerView recView;
    private LinearLayoutManager manager;
    private OrderProductAdapter adapter;


    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base,Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            orderModel = (OrderDataModel.OrderModel) intent.getSerializableExtra("data");
        }
    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang",Locale.getDefault().getLanguage());
        arrow = findViewById(R.id.arrow);
        if (lang.equals("ar"))
        {
            arrow.setRotation(180.0f);
        }
        llBack = findViewById(R.id.llBack);
        image = findViewById(R.id.image);
        tvName = findViewById(R.id.tvName);
        tvOffer = findViewById(R.id.tvOffer);
        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this);
        recView.setLayoutManager(manager);


        updateUI();


        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void updateUI()
    {
        if (orderModel.getCompany_image()!=null&&!orderModel.getCompany_image().isEmpty()&&!orderModel.getCompany_image().equals("0"))
        {
            Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+orderModel.getCompany_image())).fit().into(image);
        }
        tvName.setText(orderModel.getCompany_name());
        tvOffer.setText(String.format("%s %s",String.valueOf(orderModel.getOffer_value()),getString(R.string.sar)));

        adapter = new OrderProductAdapter(orderModel.getOrder_details(),this);
        recView.setAdapter(adapter);

    }
}
