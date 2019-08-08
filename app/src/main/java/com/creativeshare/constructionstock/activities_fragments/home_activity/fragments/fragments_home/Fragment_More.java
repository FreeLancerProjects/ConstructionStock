package com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.home_activity.activities.Home_Activity;
import com.creativeshare.constructionstock.models.UserModel;
import com.creativeshare.constructionstock.preferences.Preferences;
import com.zcw.togglebutton.ToggleButton;
import java.util.Locale;

import io.paperdb.Paper;


public class Fragment_More extends Fragment {
    private Home_Activity activity;
    private LinearLayout ll_allow_receive_order;
    private ConstraintLayout cons_edit_profile,cons_language,cons_terms,cons_rate,cons_about,cons_contact;
    private ImageView arrow1,arrow2,arrow3,arrow4,arrow5,arrow6;
    private String current_language;
    private String[] language_array;
    private ToggleButton toggle_btn;
    private UserModel userModel;
    private Preferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more,container,false);
        initView(view);
        return view;
    }

    public static Fragment_More newInstance()
    {
        return new Fragment_More();
    }
    private void initView(View view) {
        activity = (Home_Activity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        arrow3 = view.findViewById(R.id.arrow3);
        arrow4 = view.findViewById(R.id.arrow4);
        arrow5 = view.findViewById(R.id.arrow5);
        arrow6 = view.findViewById(R.id.arrow6);


        language_array = new String[]{"English","العربية"};

        if (current_language.equals("ar"))
        {

            arrow1.setRotation(180.0f);
            arrow2.setRotation(180.0f);
            arrow3.setRotation(180.0f);
            arrow4.setRotation(180.0f);
            arrow5.setRotation(180.0f);
            arrow6.setRotation(180.0f);

        }

        ll_allow_receive_order = view.findViewById(R.id.ll_allow_receive_order);

        cons_edit_profile = view.findViewById(R.id.cons_edit_profile);
        cons_language = view.findViewById(R.id.cons_language);
        cons_terms = view.findViewById(R.id.cons_terms);
        cons_rate = view.findViewById(R.id.cons_rate);
        cons_about = view.findViewById(R.id.cons_about);
        cons_contact = view.findViewById(R.id.cons_contact);

      //  toggle_btn = view.findViewById(R.id.toggle_btn);

      /*  if (userModel.getUser().getCompany_information()!=null)
        {
            if (userModel.getUser().getCompany_information().getIs_avaliable()==0)
            {
                toggle_btn.setToggleOff();
            }else
                {
                    toggle_btn.setToggleOn();
                }
        }*/
        cons_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName())));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName())));
                }
            }
        });

        cons_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentTerms_AboutUs(1);
            }

        });

        cons_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentContactUS();
            }
        });

        cons_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentTerms_AboutUs(2);

            }
        });

        cons_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateLanguageDialog();
            }
        });

        cons_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //activity.DisplayFragmentEditProfile();
            }
        });








        /*if (userModel!=null&&userModel.getUser().getCompany_information()!=null)
        {
            ll_allow_receive_order.setVisibility(View.VISIBLE);
        }else
        {
            ll_allow_receive_order.setVisibility(View.GONE);


        }
*/
    }




    private void CreateLanguageDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view  = LayoutInflater.from(activity).inflate(R.layout.dialog_language,null);
        Button btn_select = view.findViewById(R.id.btn_select);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        final NumberPicker numberPicker = view.findViewById(R.id.numberPicker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(language_array.length-1);
        numberPicker.setDisplayedValues(language_array);
        numberPicker.setWrapSelectorWheel(false);
        if (current_language.equals("en"))
        {
            numberPicker.setValue(0);

        }else
            {
                numberPicker.setValue(1);
            }
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                int pos = numberPicker.getValue();
                if (pos == 0) {
                    activity.RefreshActivity("en");
                }
                else
                {
                    activity.RefreshActivity("ar");

                }

            }
        });




        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setView(view);
        dialog.show();
    }


}
