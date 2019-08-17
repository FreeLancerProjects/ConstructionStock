package com.creativeshare.constructionstock.activities_fragments.activity_order_details;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.language.Language_Helper;

import java.util.Locale;

import io.paperdb.Paper;

public class OrderDetailsActivity extends AppCompatActivity {

    private LinearLayout llBack;

    private ImageView imageStep1,imageStep2,imageStep3,imageStep4,arrow;
    private TextView tvStep1,tvStep2,tvStep3,tvStep4;
    private boolean isStatusChanged = false;
    private String lang;

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base,Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        initView();
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

        imageStep1 = findViewById(R.id.imageStep1);
        imageStep2 = findViewById(R.id.imageStep2);
        imageStep3 = findViewById(R.id.imageStep3);
        imageStep4 = findViewById(R.id.imageStep4);

        tvStep1 = findViewById(R.id.tvStep1);
        tvStep2 = findViewById(R.id.tvStep2);
        tvStep3 = findViewById(R.id.tvStep3);
        tvStep4 = findViewById(R.id.tvStep4);

        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStatusChanged)
                {
                    Intent intent = getIntent();
                    if (intent!=null)
                    {
                        intent.putExtra("status",true);
                        setResult(RESULT_OK,intent);
                        finish();
                    }else
                        {
                            finish();
                        }
                }
            }
        });
    }


    private void updateSteps(int status)
    {
        switch (status)
        {
            case 0:
                clearSteps();
                break;
            case 1:
                step1();
                break;
            case 2:
                step2();
                break;
            case 3:
                step3();
                break;
            case 4:
                step4();
                break;
        }
    }

    private void clearSteps()
    {
        imageStep1.setImageResource(R.drawable.circle_empty);
        imageStep2.setImageResource(R.drawable.circle_empty);
        imageStep3.setImageResource(R.drawable.circle_empty);
        imageStep4.setImageResource(R.drawable.circle_empty);

        tvStep1.setTextColor(ContextCompat.getColor(this,R.color.gray4));
        tvStep2.setTextColor(ContextCompat.getColor(this,R.color.gray4));
        tvStep3.setTextColor(ContextCompat.getColor(this,R.color.gray4));
        tvStep4.setTextColor(ContextCompat.getColor(this,R.color.gray4));

    }

    private void step1()
    {
        imageStep1.setImageResource(R.drawable.circle_colored);
        imageStep2.setImageResource(R.drawable.circle_empty);
        imageStep3.setImageResource(R.drawable.circle_empty);
        imageStep4.setImageResource(R.drawable.circle_empty);

        tvStep1.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        tvStep2.setTextColor(ContextCompat.getColor(this,R.color.gray4));
        tvStep3.setTextColor(ContextCompat.getColor(this,R.color.gray4));
        tvStep4.setTextColor(ContextCompat.getColor(this,R.color.gray4));

    }

    private void step2()
    {

        imageStep1.setImageResource(R.drawable.circle_colored);
        imageStep2.setImageResource(R.drawable.circle_colored);
        imageStep3.setImageResource(R.drawable.circle_empty);
        imageStep4.setImageResource(R.drawable.circle_empty);

        tvStep1.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        tvStep2.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        tvStep3.setTextColor(ContextCompat.getColor(this,R.color.gray4));
        tvStep4.setTextColor(ContextCompat.getColor(this,R.color.gray4));

    }
    private void step3()
    {
        imageStep1.setImageResource(R.drawable.circle_colored);
        imageStep2.setImageResource(R.drawable.circle_colored);
        imageStep3.setImageResource(R.drawable.circle_colored);
        imageStep4.setImageResource(R.drawable.circle_empty);

        tvStep1.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        tvStep2.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        tvStep3.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        tvStep4.setTextColor(ContextCompat.getColor(this,R.color.gray4));

    }

    private void step4()
    {
        imageStep1.setImageResource(R.drawable.circle_colored);
        imageStep2.setImageResource(R.drawable.circle_colored);
        imageStep3.setImageResource(R.drawable.circle_colored);
        imageStep4.setImageResource(R.drawable.circle_colored);

        tvStep1.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        tvStep2.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        tvStep3.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        tvStep4.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));

    }

}
