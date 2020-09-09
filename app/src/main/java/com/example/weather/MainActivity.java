package com.example.weather;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.weather.apiClient.ApiClient;
import com.example.weather.apiClient.ApiInterface;
import com.example.weather.currentModel.MinutelyItem;
import com.example.weather.currentModel.WeatherRes;
import com.example.weather.tabfragment.TodayFragment;
import com.example.weather.tabfragment.TomorrowFragment;
import com.example.weather.weatherList.ListItem;
import com.example.weather.weatherList.WeatherListResp;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    LocationManager locationManager;

    @BindView(R.id.btnretry)
    ImageView btnretry;
    @BindView(R.id.imgIcon)
    ImageView imgIcon;

    @BindView(R.id.txtCity)
    TextView txtCity;
    @BindView(R.id.txtWeather)
    TextView txtWeather;
    @BindView(R.id.txtWeatherDis)
    TextView txtWeatherDis;
    @BindView(R.id.toolbarName)
    TextView toolbarName;
    @BindView(R.id.txtWind)
    TextView txtWind;
    @BindView(R.id.txtTemp)
    TextView txtTemp;
    @BindView(R.id.txtPressure)
    TextView txtPressure;
    @BindView(R.id.txtHumidity)
    TextView txtHumidity;
    @BindView(R.id.txtCurrentDate)
    TextView txtCurrentDate;

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    public static String strCity = "";
    public static String city;
    public static String country;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static List<MinutelyItem> minutely = new ArrayList<MinutelyItem>();


    List<ListItem> listResData = new ArrayList<>();
    List<ListItem> listTodayData = new ArrayList<>();
    List<ListItem> listTomorrowData = new ArrayList<>();
    public static String fulldate;
    int  REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return ;
            }
        }


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        txtCurrentDate.setText(formattedDate);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); //

    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TodayFragment(listTodayData), "Today");
        adapter.addFragment(new TomorrowFragment(listTomorrowData), "Tomorrow");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<ListItem> listTomorrowData = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public ListItem getlistdat(int position) {
            return listTomorrowData.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        String longitude = "Longitude: " + location.getLongitude();
        String latitude = "Latitude: " + location.getLatitude();

    }


    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

            if (addresses != null && addresses.size() > 0) {


                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                Log.d("MainActivity ", "getAddress:  address" + address);
                Log.d("MainActivity ", "getAddress:  country" + country);
                Log.d("MainActivity ", "getAddress:  city" + city);
                Log.d("MainActivity ", "getAddress:  state" + state);
                Log.d("MainActivity ", "getAddress:  postalCode" + postalCode);
                Log.d("MainActivity ", "getAddress:  knownName" + knownName);

                toolbarName.setText(country);
                strCity = city;
                txtCity.setText(city);

                ////////////////////////////////
                /////Calling api every 15min
                ////////////////////////////////

                Timer timer = new Timer();

                timer.scheduleAtFixedRate(new TimerTask() {
                    private Handler updateUI = new Handler() {
                        @Override
                        public void dispatchMessage(Message msg) {
                            super.dispatchMessage(msg);

                            callWatherApi();
                        }
                    };

                    public void run() {
                        try {
                            updateUI.sendEmptyMessage(0);
                            Toast.makeText(MainActivity.this, "Weather is Updating.", Toast.LENGTH_LONG).show();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 0, 15 * 60 * 1000);

                ////////////////////////////////
                /////Calling api for List of Weather Today and Tomorrow
                ////////////////////////////////
                callWatherListApi(city, country);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

        } else {

            DecimalFormat form = new DecimalFormat("0.0000");

            //If everything went fine lets get latitude and longitude
            currentLatitude = Double.parseDouble(form.format(location.getLatitude()));
            currentLongitude = Double.parseDouble(form.format(location.getLongitude()));

            getAddress(MainActivity.this, currentLatitude, currentLongitude);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }




    @OnClick(R.id.btnretry)
    public void clickRetry(ImageView iv) {
        Toast.makeText(MainActivity.this, "Weather is Updateding.", Toast.LENGTH_LONG).show();
        callWatherApi();
    }


    private void callWatherApi() {

        minutely.clear();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<WeatherRes> call = apiService.getWeatherCall(currentLatitude, currentLongitude, "hourly,daily", "ff368f2a1a04a92b7d6e86ea1b68418e");

        Log.v("Url ", "call " + call.request());

        call.enqueue(new retrofit2.Callback<WeatherRes>() {
            @Override
            public void onResponse(Call<WeatherRes> call, Response<WeatherRes> response) {

                Log.v("Url ", call.request() + "if==+==");


                if (response.body() != null) {
                    //   Log.v("Url ", response.raw() + "if===");
                    // Log.v("Url ", "resp " + response.body().toString());

                    txtHumidity.setText(response.body().getCurrent().getHumidity() + "%");
                    txtPressure.setText(response.body().getCurrent().getPressure() + " mbar");
                    txtWind.setText(response.body().getCurrent().getWindSpeed() + " km/h");
                    txtWeatherDis.setText(response.body().getCurrent().getWeather().get(0).getDescription() + "");
                    txtWeather.setText(response.body().getCurrent().getWeather().get(0).getMain() + "");


                    Double dobTem = (response.body().getCurrent().getTemp() - 273.15);

                    DecimalFormat form = new DecimalFormat("0");
                    String Formatted = form.format(dobTem);
                    txtTemp.setText(Formatted + " \u2103");

                    String icon = response.body().getCurrent().getWeather().get(0).getIcon();
                    String iconUrl = " http://openweathermap.org/img/wn/" + icon + "@2x.png";


                    //Picasso.with(MainActivity.this).load(iconUrl).into(imgIcon);
                    imgIcon.setImageURI(Uri.parse(iconUrl));
                    minutely.addAll(response.body().getMinutely());

                } else {
                    Log.v("Url ", response.raw() + "else");
                }

            }

            @Override
            public void onFailure(Call<WeatherRes> call, Throwable t) {
                Log.v("Url ", t.getMessage() + "  fail");

            }
        });
    }

    private void callWatherListApi(String strCity, String country) {


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<WeatherListResp> call = apiService.getWeatherList(/*strCity+","+country*/"bangalore,India", "ff368f2a1a04a92b7d6e86ea1b68418e", "metric");

        call.enqueue(new retrofit2.Callback<WeatherListResp>() {
            @Override
            public void onResponse(Call<WeatherListResp> call, Response<WeatherListResp> response) {

                listResData.clear();
                Log.v("Url ", response.message() + "==res succ=");

                if (response.body() != null) {

                    listResData = response.body().getList();

                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    Calendar calendar = Calendar.getInstance();
                    Date today = calendar.getTime();

                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    Date tomorrow = calendar.getTime();

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    String todayAsString = dateFormat.format(today);
                    String tomorrowAsString = dateFormat.format(tomorrow);

                    for (int i = 0; i < listResData.size(); i++) {
                        fulldate = listResData.get(i).getDtTxt();
                        final String[] time_chat_s1 = fulldate.split(" ");

                        if (todayAsString.equals(time_chat_s1[0])) {

                            listTodayData.add(listResData.get(i));

                        } else if (tomorrowAsString.equals(time_chat_s1[0])) {

                            listTomorrowData.add(listResData.get(i));

                        } else {

                        }

                    }
                    Log.v("Url", "listResData - " + listResData.size());
                    Log.v("Url", "listTodayData - " + listTodayData.size());
                    Log.v("Url", "listTomorrowData - " + listTomorrowData.size());

//                    mAdapter = new WeatherAdapter(getActivity(), listTodayData);
//                    recyclerView.setAdapter(mAdapter);
//
//                    mListener.onTommorowList(listTomorrowData);


                    setupViewPager(viewPager);

                    tabLayout.setupWithViewPager(viewPager);


                } else {
                    Log.v("Url ", response.raw() + "");
                }

            }

            @Override
            public void onFailure(Call<WeatherListResp> call, Throwable t) {

                Log.v("Url ", t.getMessage() + "==fail=");
                Log.v("Url ", t.getStackTrace() + "==fail=");

            }
        });
    }

    String provider = "";
    @Override
    protected void onResume() {
        super.onResume();

        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }


}
