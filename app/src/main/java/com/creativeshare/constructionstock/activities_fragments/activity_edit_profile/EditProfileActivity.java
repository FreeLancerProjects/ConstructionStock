package com.creativeshare.constructionstock.activities_fragments.activity_edit_profile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.language.Language_Helper;
import com.creativeshare.constructionstock.models.UserModel;
import com.creativeshare.constructionstock.preferences.Preferences;
import com.creativeshare.constructionstock.remote.Api;
import com.creativeshare.constructionstock.share.Common;
import com.creativeshare.constructionstock.tags.Tags;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements OnCountryPickerListener {
    private String lang;
    private LinearLayout ll_back;
    private ImageView arrow, imageUpdate, imagePhoneCode;
    private CircleImageView image;
    private EditText edtName, edtPhone;
    private TextView tvPhoneCode;
    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 1, IMG_REQ2 = 2;
    private Uri imgUri1 = null;
    private CountryPicker picker;
    private Preferences preferences;
    private UserModel userModel;
    private String phone_code="";
    private boolean isDataUpdated = false;

    @Override
    protected void attachBaseContext(Context base) {
        Paper.init(base);
        super.attachBaseContext(Language_Helper.updateResources(base, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initView();
    }

    private void initView() {
        Paper.init(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        arrow = findViewById(R.id.arrow);
        imagePhoneCode = findViewById(R.id.imagePhoneCode);

        if (lang.equals("ar")) {
            arrow.setRotation(180.0f);
            imagePhoneCode.setRotation(180.0f);

        }
        ll_back = findViewById(R.id.ll_back);
        imageUpdate = findViewById(R.id.imageUpdate);
        image = findViewById(R.id.image);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        tvPhoneCode = findViewById(R.id.tvPhoneCode);

        updateUIData(userModel);


        CreateCountryDialog();

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back();
            }
        });
        imagePhoneCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.showDialog(EditProfileActivity.this);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateImageAlertDialog();
            }
        });

        imageUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
            }
        });


    }

    private void checkData()
    {
        String m_name = edtName.getText().toString().trim();
        String m_phone = edtPhone.getText().toString().trim();

        if (!TextUtils.isEmpty(m_name)&&
                !TextUtils.isEmpty(m_phone)&&
                !TextUtils.isEmpty(phone_code)
        )
        {
            edtPhone.setError(null);
            edtName.setError(null);
            tvPhoneCode.setError(null);
            Common.CloseKeyBoard(this,edtName);
            updateUserData(m_name,phone_code,m_phone);
        }else
            {
                if (TextUtils.isEmpty(m_name))
                {
                    edtName.setError(getString(R.string.field_req));

                }else
                    {
                        edtName.setError(null);

                    }

                if (TextUtils.isEmpty(m_phone))
                {
                    edtPhone.setError(getString(R.string.field_req));

                }else
                {
                    edtPhone.setError(null);

                }

                if (TextUtils.isEmpty(phone_code))
                {
                    tvPhoneCode.setError(getString(R.string.field_req));

                }else
                {
                    tvPhoneCode.setError(null);

                }
            }
    }
    private void updateUIData(UserModel userModel)
    {

        if (userModel.getImage()!=null&&!userModel.getImage().isEmpty()&&!userModel.getImage().equals("0"))
        {
            Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+userModel.getImage())).fit().into(image);
        }
        phone_code = userModel.getPhone_code();
        edtName.setText(userModel.getUsername());
        tvPhoneCode.setText(userModel.getPhone_code().replaceFirst("00", "+"));
        edtPhone.setText(userModel.getPhone());

    }

    private void CreateCountryDialog() {
        CountryPicker.Builder builder = new CountryPicker.Builder()
                .canSearch(true)
                .with(this)
                .listener(this);
        picker = builder.build();


    }

    @Override
    public void onSelectCountry(Country country) {
        updateUi(country);
    }

    private void updateUi(Country country) {
        phone_code = country.getDialCode().replace("+","00");
        tvPhoneCode.setText(country.getDialCode());
    }

    private void CreateImageAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_image,null);
        Button btn_camera = view.findViewById(R.id.btn_camera);
        Button btn_gallery = view.findViewById(R.id.btn_gallery);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Check_CameraPermission();
            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                CheckReadPermission();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
    }

    private void CheckReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM}, IMG_REQ1);
        } else {
            SelectImage(IMG_REQ1);
        }
    }

    private void Check_CameraPermission() {
        if (ContextCompat.checkSelfPermission(this, camera_permission) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, write_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{camera_permission, write_permission}, IMG_REQ2);
        } else {
            SelectImage(IMG_REQ2);

        }

    }

    private void SelectImage(int img_req) {

        Intent intent = new Intent();

        if (img_req == IMG_REQ1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            } else {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent, img_req);

        } else if (img_req == IMG_REQ2) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, img_req);
            } catch (SecurityException e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();

            }


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG_REQ1) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ1);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == IMG_REQ2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SelectImage(IMG_REQ2);
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            imgUri1 = getUriFromBitmap(bitmap);

            updateImage(imgUri1);


        } else if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {

            imgUri1 = data.getData();
            updateImage(imgUri1);



        }

    }



    private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }


    private void updateImage(Uri imgUri1)
    {
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        RequestBody user_id_part = Common.getRequestBodyText(String.valueOf(userModel.getId()));
        RequestBody name_part = Common.getRequestBodyText(userModel.getUsername());
        RequestBody phone_code_part = Common.getRequestBodyText(userModel.getPhone_code());
        RequestBody phone_part = Common.getRequestBodyText(userModel.getPhone());
        MultipartBody.Part image_part = Common.getMultiPart(this,imgUri1,"image");

        Api.getService(Tags.base_url)
                .updateImage(user_id_part,name_part,phone_part,phone_code_part,image_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            EditProfileActivity.this.userModel = response.body();
                            preferences.create_update_userdata(EditProfileActivity.this,response.body());

                            Toast.makeText(EditProfileActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            isDataUpdated = true;
                            Back();
                        }else
                            {
                                try {
                                    Log.e("error_code",response.code()+"__"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(EditProfileActivity.this,getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(EditProfileActivity.this,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });


    }

    private void updateUserData(String name,String phone_code,String phone)
    {

        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .updateUserData(userModel.getId(),name,phone,phone_code)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            EditProfileActivity.this.userModel = response.body();
                            preferences.create_update_userdata(EditProfileActivity.this,response.body());

                            Toast.makeText(EditProfileActivity.this, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            isDataUpdated = true;
                            Back();
                        }else
                        {
                            try {
                                Log.e("error_code",response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(EditProfileActivity.this,getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(EditProfileActivity.this,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Back();
    }

    private void Back() {
        if (isDataUpdated)
        {
            Intent intent = getIntent();
            intent.putExtra("data",userModel);
            setResult(RESULT_OK,intent);
            finish();

        }else
            {
                finish();
            }
    }
}
