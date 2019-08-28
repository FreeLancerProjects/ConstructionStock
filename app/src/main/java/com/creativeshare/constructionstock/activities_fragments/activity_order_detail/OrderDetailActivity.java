package com.creativeshare.constructionstock.activities_fragments.activity_order_detail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.adapters.OrderProductAdapter;
import com.creativeshare.constructionstock.language.Language_Helper;
import com.creativeshare.constructionstock.models.OrderDataModel;
import com.creativeshare.constructionstock.remote.Api;
import com.creativeshare.constructionstock.share.Common;
import com.creativeshare.constructionstock.tags.Tags;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private Button btnEnd;
    private float rate;


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
        btnEnd = findViewById(R.id.btnEnd);

        manager = new LinearLayoutManager(this);
        recView.setLayoutManager(manager);


        updateUI();


        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateRateDialog();
            }
        });

    }

    private  void CreateRateDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_rate,null);
        Button btnRate = view.findViewById(R.id.btnRate);
        SimpleRatingBar rateBar = view.findViewById(R.id.rateBar);
        final TextView tvRate = view.findViewById(R.id.tvRate);
        rateBar.setOnRatingBarChangeListener(new SimpleRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
                tvRate.setText(String.valueOf(rating));
                rate = rating;

            }
        });
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EndOrder(rate);

                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }


    private void EndOrder(float rate) {

        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .endOrder(orderModel.getId(),(int)rate)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            Intent intent = getIntent();
                            if (intent!=null)
                            {
                                intent.putExtra("end",true);
                                setResult(RESULT_OK,intent);
                                finish();
                            }
                        }else
                        {
                            Toast.makeText(OrderDetailActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
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
