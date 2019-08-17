package com.creativeshare.constructionstock.activities_fragments.activity_profile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.language.Language_Helper;
import com.creativeshare.constructionstock.models.UserModel;
import com.creativeshare.constructionstock.preferences.Preferences;
import com.creativeshare.constructionstock.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class ProfileActivity extends AppCompatActivity {

    private LinearLayout llBack;
    private ImageView arrow;
    private CircleImageView image;
    private TextView tvName,tv_email,tv_phone;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base,Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();

    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
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
        tv_email = findViewById(R.id.tv_email);
        tv_phone = findViewById(R.id.tv_phone);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updateUI();

    }

    private void updateUI() {

        if (userModel.getImage()!=null&&!userModel.getImage().isEmpty()&&!userModel.getImage().equals("0"))
        {
            Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+userModel.getImage())).fit().into(image);
        }
        tvName.setText(userModel.getUsername());
        tv_email.setText(userModel.getEmail());
        tv_phone.setText(String.format("%s %s",userModel.getPhone_code().replace("00","+"),userModel.getPhone()));
    }
}
