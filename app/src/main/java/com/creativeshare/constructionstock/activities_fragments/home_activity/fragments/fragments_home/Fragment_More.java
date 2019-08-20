package com.creativeshare.constructionstock.activities_fragments.home_activity.fragments.fragments_home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.activities_fragments.activity_edit_profile.EditProfileActivity;
import com.creativeshare.constructionstock.activities_fragments.activity_profile.ProfileActivity;
import com.creativeshare.constructionstock.activities_fragments.home_activity.activities.Home_Activity;
import com.creativeshare.constructionstock.models.UserModel;
import com.creativeshare.constructionstock.preferences.Preferences;
import com.creativeshare.constructionstock.share.Common;

import java.util.Locale;

import io.paperdb.Paper;


public class Fragment_More extends Fragment {
    private Home_Activity activity;
    private ConstraintLayout cons_profile,cons_edit_profile,cons_language,cons_terms,cons_rate,cons_about,cons_contact,cons_logout;
    private ImageView arrow1,arrow2,arrow3,arrow4,arrow5,arrow6,arrow7,arrow8;
    private String current_language;
    private String[] language_array;
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
        arrow7 = view.findViewById(R.id.arrow7);
        arrow8 = view.findViewById(R.id.arrow8);


        language_array = new String[]{"English","العربية"};

        if (current_language.equals("ar"))
        {

            arrow1.setRotation(180.0f);
            arrow2.setRotation(180.0f);
            arrow3.setRotation(180.0f);
            arrow4.setRotation(180.0f);
            arrow5.setRotation(180.0f);
            arrow6.setRotation(180.0f);
            arrow7.setRotation(180.0f);
            arrow8.setRotation(180.0f);

        }

        cons_profile = view.findViewById(R.id.cons_profile);

        cons_edit_profile = view.findViewById(R.id.cons_edit_profile);
        cons_language = view.findViewById(R.id.cons_language);
        cons_terms = view.findViewById(R.id.cons_terms);
        cons_rate = view.findViewById(R.id.cons_rate);
        cons_about = view.findViewById(R.id.cons_about);
        cons_contact = view.findViewById(R.id.cons_contact);
        cons_logout = view.findViewById(R.id.cons_logout);

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

        cons_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userModel!=null)
                {
                    Intent intent = new Intent(activity, ProfileActivity.class);
                    startActivity(intent);
                }else
                    {
                        Common.CreateUserNotSignInAlertDialog(activity);

                    }

            }
        });
        cons_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userModel!=null)
                {
                    Intent intent = new Intent(activity, EditProfileActivity.class);
                    startActivityForResult(intent,2536);
                }else
                {
                    Common.CreateUserNotSignInAlertDialog(activity);

                }



            }
        });

        cons_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Logout();
            }
        });


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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2536&&resultCode== Activity.RESULT_OK&&data!=null)
        {
            if (data.hasExtra("data"))
            {
                userModel = (UserModel) data.getSerializableExtra("data");
                activity.updateUserData(userModel);
            }
        }
    }
}
