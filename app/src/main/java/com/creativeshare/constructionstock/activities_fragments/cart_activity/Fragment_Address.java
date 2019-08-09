package com.creativeshare.constructionstock.activities_fragments.cart_activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.creativeshare.constructionstock.R;
import com.creativeshare.constructionstock.models.ItemCartUploadModel;
import com.creativeshare.constructionstock.models.PlaceGeocodeData;
import com.creativeshare.constructionstock.models.PlaceMapDetailsData;
import com.creativeshare.constructionstock.models.UserModel;
import com.creativeshare.constructionstock.preferences.Preferences;
import com.creativeshare.constructionstock.remote.Api;
import com.creativeshare.constructionstock.share.Common;
import com.creativeshare.constructionstock.singleton.CartSingleton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Address extends Fragment implements OnCountryPickerListener , DatePickerDialog.OnDateSetListener, OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks, LocationListener {
    private LinearLayout llDate,llSend,llBack;
    private TextView tvDate,tv_code;
    private EditText edt_phone,edtAddress;
    private ImageView image_phone_code,imageSearch,arrow1,arrow2;
    private String lang;
    private CartActivity activity;
    private CountryPicker picker;
    private DatePickerDialog datePickerDialog;
    private long date=0;
    private String address="",code="";
    private Marker marker;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private final String fineLocPerm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int loc_req = 1225;
    private double lat=0.0,lng=0.0;
    private float zoom = 15.6f;
    private CartSingleton singleton;
    private ItemCartUploadModel itemCartUploadModel;
    private UserModel userModel;
    private Preferences preferences;


    public static Fragment_Address newInstance()
    {
        return new Fragment_Address();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        singleton =CartSingleton.newInstance();
        activity = (CartActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        image_phone_code = view.findViewById(R.id.image_phone_code);
        if (lang.equals("ar"))
        {
            arrow1.setRotation(180.0f);
            arrow2.setRotation(0.0f);
            image_phone_code.setRotation(180.0f);

        }

        llDate = view.findViewById(R.id.llDate);
        llSend = view.findViewById(R.id.llSend);
        llBack = view.findViewById(R.id.llBack);
        tvDate = view.findViewById(R.id.tvDate);
        tv_code = view.findViewById(R.id.tv_code);
        edt_phone = view.findViewById(R.id.edt_phone);
        edtAddress = view.findViewById(R.id.edtAddress);
        imageSearch = view.findViewById(R.id.imageSearch);

        edtAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    String query = edtAddress.getText().toString().trim();
                    if (!TextUtils.isEmpty(query))
                    {
                        Search(query);
                    }
                }
                return false;
            }
        });
        imageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = edtAddress.getText().toString().trim();
                if (!TextUtils.isEmpty(query))
                {
                    Search(query);
                }
            }
        });
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.Back();
            }
        });
        image_phone_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.showDialog(activity);
            }
        });
        llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show(activity.getFragmentManager(),"");
            }
        });
        llSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel!=null)
                {
                    checkData();

                }else
                    {
                        Common.CreateSignAlertDialog(activity,getString(R.string.si_su));

                    }
            }
        });
        CreateCountryDialog();
        createDatePickerDialog();
        CheckPermission();
        updateUI();


    }

    private void checkData()
    {
        String phone  = edt_phone.getText().toString().trim();
        if (!TextUtils.isEmpty(code)&&
                !TextUtils.isEmpty(phone)&&
                !TextUtils.isEmpty(address)&&
                date!=0
        )
        {
            tv_code.setError(null);
            edtAddress.setError(null);
            edt_phone.setError(null);
            Common.CloseKeyBoard(activity,edt_phone);
            String mPhone = code.replace("+","00")+phone;
            itemCartUploadModel = new ItemCartUploadModel(userModel.getUser().getId(),address,lat,lng,String.valueOf(date),mPhone,singleton.getItemCartModelList());
            send(itemCartUploadModel);


        }else
            {
                if (TextUtils.isEmpty(code))
                {
                    tv_code.setError(getString(R.string.field_req));
                }else
                    {
                        tv_code.setError(null);

                    }

                if (TextUtils.isEmpty(phone))
                {
                    edt_phone.setError(getString(R.string.field_req));
                }else
                {
                    edt_phone.setError(null);

                }

                if (TextUtils.isEmpty(address))
                {
                    edtAddress.setError(getString(R.string.field_req));
                }else
                {
                    edtAddress.setError(null);

                }

            }
    }

    private void send(ItemCartUploadModel itemCartUploadModel) {

    }

    private void CreateCountryDialog() {
        CountryPicker.Builder builder = new CountryPicker.Builder()
                .canSearch(true)
                .with(activity)
                .listener(this);
        picker = builder.build();

        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);


        if (picker.getCountryFromSIM() != null) {
            updateUi(picker.getCountryFromSIM());

        } else if (telephonyManager != null && picker.getCountryByISO(telephonyManager.getNetworkCountryIso()) != null) {
            updateUi(picker.getCountryByISO(telephonyManager.getNetworkCountryIso()));


        } else if (picker.getCountryByLocale(Locale.getDefault()) != null) {
            updateUi(picker.getCountryByLocale(Locale.getDefault()));

        } else {
            tv_code.setText("+966");
            code = "+966";
        }


    }
    private void updateUi(Country country) {

        tv_code.setText(country.getDialCode());
        code = country.getDialCode();

    }
    @Override
    public void onSelectCountry(Country country) {
        updateUi(country);

    }

    private void createDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.setAccentColor(ActivityCompat.getColor(activity,R.color.colorPrimary));
        datePickerDialog.setCancelColor(ActivityCompat.getColor(activity,R.color.gray4));
        datePickerDialog.setOkColor(ActivityCompat.getColor(activity,R.color.colorPrimary));
        datePickerDialog.setOkText(getString(R.string.select));
        datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setLocale(new Locale(lang));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(calendar);


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);


        tvDate.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
        date = calendar.getTimeInMillis();


    }


    private void CheckPermission()
    {
        if (ActivityCompat.checkSelfPermission(activity,fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{fineLocPerm}, loc_req);
        } else {

            initGoogleApi();
        }
    }
    private void initGoogleApi() {
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }


    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(activity, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    marker.setPosition(latLng);
                    lat = latLng.latitude;
                    lng = latLng.longitude;
                    getGeoData(lat,lng);
                }
            });

        }
    }

    private void AddMarker(double lat, double lng) {

        try {
            this.lat = lat;
            this.lng = lng;

            if (marker == null) {
                IconGenerator iconGenerator = new IconGenerator(activity);
                iconGenerator.setBackground(null);
                View view = LayoutInflater.from(activity).inflate(R.layout.search_map_icon, null);
                iconGenerator.setContentView(view);
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())).anchor(iconGenerator.getAnchorU(), iconGenerator.getAnchorV()).draggable(true));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));

            } else {
                marker.setPosition(new LatLng(lat, lng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));


            }

        }catch (Exception e){}


    }

    private void getGeoData(final double lat, double lng) {

        try {
            String location = lat + "," + lng;
            Api.getService("https://maps.googleapis.com/maps/api/")
                    .getGeoData(location, lang, getString(R.string.map_api_key))
                    .enqueue(new Callback<PlaceGeocodeData>() {
                        @Override
                        public void onResponse(Call<PlaceGeocodeData> call, Response<PlaceGeocodeData> response) {
                            if (response.isSuccessful() && response.body() != null) {


                                if (response.body().getResults().size() > 0) {
                                    address = response.body().getResults().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                    edtAddress.setText(address);
                                    //place_id = response.body().getCandidates().get(0).getPlace_id();
                                }
                            } else {

                                try {
                                    Log.e("error_code", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }


                        }

                        @Override
                        public void onFailure(Call<PlaceGeocodeData> call, Throwable t) {
                            try {


                                // Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {

                            }
                        }
                    });
        }catch (Exception e){}

    }

    private void Search(String query) {

        String fields = "id,place_id,name,geometry,formatted_address";
        Api.getService("https://maps.googleapis.com/maps/api/")
                .searchOnMap("textquery", query, fields, lang, getString(R.string.map_api_key))
                .enqueue(new Callback<PlaceMapDetailsData>() {
                    @Override
                    public void onResponse(Call<PlaceMapDetailsData> call, Response<PlaceMapDetailsData> response) {
                        if (response.isSuccessful() && response.body() != null) {


                            if (response.body().getCandidates().size() > 0) {
                                address = response.body().getCandidates().get(0).getFormatted_address().replace("Unnamed Road,", "");
                                //place_id = response.body().getCandidates().get(0).getPlace_id();
                                lat = response.body().getCandidates().get(0).getGeometry().getLocation().getLat();
                                lng = response.body().getCandidates().get(0).getGeometry().getLocation().getLng();
                                AddMarker(response.body().getCandidates().get(0).getGeometry().getLocation().getLat(), response.body().getCandidates().get(0).getGeometry().getLocation().getLng());
                            }
                        } else {


                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(Call<PlaceMapDetailsData> call, Throwable t) {
                        try {


                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        initLocationRequest();
    }

    private void initLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setFastestInterval(1000);
        locationRequest.setInterval(60000);
        LocationSettingsRequest.Builder request = new LocationSettingsRequest.Builder();
        request.addLocationRequest(locationRequest);
        request.setAlwaysShow(false);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, request.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdate();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(activity,100);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;

                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient!=null)
        {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @SuppressLint("MissingPermission")
    private void startLocationUpdate()
    {
        locationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(activity)
                .requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

        AddMarker(lat,lng);
        getGeoData(lat,lng);
        LocationServices.getFusedLocationProviderClient(activity)
                .removeLocationUpdates(locationCallback);
        googleApiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleApiClient!=null)
        {
            if (locationCallback!=null)
            {
                LocationServices.getFusedLocationProviderClient(activity).removeLocationUpdates(locationCallback);
                googleApiClient.disconnect();
                googleApiClient = null;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == loc_req)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                initGoogleApi();
            }else
            {
                Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100&&resultCode== Activity.RESULT_OK)
        {

            startLocationUpdate();
        }
    }

}
