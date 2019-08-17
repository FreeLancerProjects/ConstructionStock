package com.creativeshare.constructionstock.activities_fragments.offer_details_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.language.Language_Helper;
import com.creativeshare.constructionstock.models.NotificationDataModel;
import com.creativeshare.constructionstock.remote.Api;
import com.creativeshare.constructionstock.share.Common;
import com.creativeshare.constructionstock.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferDetailsActivity extends AppCompatActivity {
    private ImageView arrow;
    private LinearLayout llBack;
    private CircleImageView image;
    private TextView tvName,tvOffer;
    private Button btnAccept,btnRefuse;
    private String lang;
    private NotificationDataModel.NotificationModel notificationModel;

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base,Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            notificationModel = (NotificationDataModel.NotificationModel) intent.getSerializableExtra("data");
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

        image = findViewById(R.id.image);
        tvName = findViewById(R.id.tvName);
        tvOffer = findViewById(R.id.tvOffer);
        btnAccept = findViewById(R.id.btnAccept);
        btnRefuse = findViewById(R.id.btnRefuse);
        llBack = findViewById(R.id.llBack);

        updateUI();

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accept();
            }
        });

        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refuse();
            }
        });

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void updateUI()
    {
        if (notificationModel.getCompany_image()!=null&&!notificationModel.getCompany_image().isEmpty()&&!notificationModel.getCompany_image().equals("0"))
        {
            Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+notificationModel.getCompany_image())).fit().into(image);
        }
        tvName.setText(notificationModel.getCompany_name());
        tvOffer.setText(String.format("%s %s",String.valueOf(notificationModel.getOffer_value()),getString(R.string.sar)));
    }
    private void accept() {
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .acceptOffer(notificationModel.getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful())
                        {
                            back(1);
                            Toast.makeText(OfferDetailsActivity.this,getString(R.string.accept), Toast.LENGTH_SHORT).show();
                        }else
                            {
                                try {
                                    Log.e("code_error",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(OfferDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(OfferDetailsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }

    private void refuse() {

        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .refuseOffer(notificationModel.getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful())
                        {
                            back(2);
                            Toast.makeText(OfferDetailsActivity.this,getString(R.string.refuse), Toast.LENGTH_SHORT).show();
                        }else
                        {
                            try {
                                Log.e("code_error",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(OfferDetailsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(OfferDetailsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void back(int action)
    {
        Intent intent = getIntent();
        intent.putExtra("action",action);
        setResult(RESULT_OK,intent);
        finish();
    }


}
