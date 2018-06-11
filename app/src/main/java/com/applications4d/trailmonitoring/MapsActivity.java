package com.applications4d.trailmonitoring;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback ,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    DatabaseHelper mDatabaseHelper;

    private static final int IMAGE_REQUEST_CODE = 3;
    private static final int STORAGE_PERMISSION_CODE = 123;

    TextView mTextViewCoordinates, mTextViewDate;
    ImageButton mImageButtonCamera;
    ImageView mImageViewPhoto;
    Button mButtonChooseFlowers, mButtonChooseSigns, mButtonChooseTrails, mButtonSaveTrailProblem;
    Spinner mSpinnerProblemDetailsTrails, mSpinnerProblemDetailsFlowers, mSpinnerProblemDetailsSigns;
    EditText mEditTextComment;
    SharedPreferences prefs;
    byte[] photoArray;
    private File imageFile;
    private Uri filePath;
    String filePathString;
    Bitmap bitmap;
    String part1, part2, part3, part4 = "";
    int newNum = 0;
    String trailToSetUserDefaults = "";

    MediaPlayer mediaPlayerQ;
    LatLng ce = new LatLng(40.692213, -73.985189);
    double LATce = 40.692213;
    double LONGce = -73.985189;

    LatLng CurrentLOC = new LatLng(40.716995, -74.075657);


    private LocationManager locationManager;
    private LocationListener listener;


    float zoomLevel = 17;


    double LatitudeOnSearch = 8.131;
    double LongtitudeOnSearch = 8.131;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        mDatabaseHelper = new DatabaseHelper(this);

        Date c = Calendar.getInstance().getTime();
        Log.d("UnDatexx", c + "");


        //SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        final String formattedDate = df.format(c);
        Log.d("Datexx", formattedDate);

        Values.currentCoordinates = "";
        mTextViewCoordinates = (TextView) findViewById(R.id.textViewCoordinates);
        mTextViewDate = (TextView) findViewById(R.id.textViewDate);
        mTextViewDate.setText(formattedDate);

        mImageButtonCamera = (ImageButton) findViewById(R.id.imageButtonPhoto);
        mButtonChooseFlowers = (Button) findViewById(R.id.buttonChooseFlowers);
        mButtonChooseFlowers = (Button) findViewById(R.id.buttonChooseFlowers);
        mButtonChooseSigns = (Button) findViewById(R.id.buttonChooseSigns);
        mButtonChooseTrails = (Button) findViewById(R.id.buttonChooseTrails);
        mSpinnerProblemDetailsTrails = (Spinner) findViewById(R.id.spinnerProblemDetailsTrails);
        mSpinnerProblemDetailsTrails.setVisibility(View.INVISIBLE);
        mSpinnerProblemDetailsSigns = (Spinner) findViewById(R.id.spinnerProblemDetailsSigns);
        mSpinnerProblemDetailsSigns.setVisibility(View.INVISIBLE);
        mSpinnerProblemDetailsFlowers = (Spinner) findViewById(R.id.spinnerProblemDetailsFlowers);
        mSpinnerProblemDetailsFlowers.setVisibility(View.INVISIBLE);
        mImageButtonCamera.setVisibility(View.INVISIBLE);
        mEditTextComment = (EditText) findViewById(R.id.editTextComment);
        mEditTextComment.setVisibility(View.INVISIBLE);

        mImageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);

        mImageViewPhoto.setVisibility(View.INVISIBLE);
        mImageButtonCamera.setVisibility(View.VISIBLE);

        mButtonSaveTrailProblem = (Button) findViewById(R.id.buttonSaveTrailProblem);
        mButtonSaveTrailProblem.setVisibility(View.INVISIBLE);


        mButtonSaveTrailProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = mEditTextComment.getText().toString();
                //Log.d("photoarraylength", photoArray == null + "");

                if (Values.currentCoordinates.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please wait for GPS", Toast.LENGTH_LONG).show();
                } else if (comment.equals("") || photoArray == null) {
                    Toast.makeText(getApplicationContext(), "Please include a photo and comment", Toast.LENGTH_LONG).show();
                } else {
                    AddData("Clare", Values.trailSelected, Values.username, Values.problemSelected, Values.problemDetails, Values.currentCoordinates, comment, photoArray, formattedDate);

                    //reset all
                    //set all bg grey
                    mButtonChooseTrails.setBackgroundResource(R.drawable.maroon_button_background);
                    mButtonChooseSigns.setBackgroundResource(R.drawable.maroon_button_background);
                    mButtonChooseFlowers.setBackgroundResource(R.drawable.maroon_button_background);
                    //set all texts black
                    mButtonChooseFlowers.setTextColor(Color.BLACK);
                    mButtonChooseTrails.setTextColor(Color.BLACK);
                    mButtonChooseSigns.setTextColor(Color.BLACK);

                    mSpinnerProblemDetailsTrails.setVisibility(View.INVISIBLE);
                    mSpinnerProblemDetailsFlowers.setVisibility(View.INVISIBLE);
                    mSpinnerProblemDetailsSigns.setVisibility(View.INVISIBLE);

                    //set camera, comment and save to invisible
                    mImageButtonCamera.setVisibility(View.INVISIBLE);
                    mImageViewPhoto.setVisibility(View.INVISIBLE);
                    mImageButtonCamera.setImageResource(R.drawable.camtrailstwo);
                    mEditTextComment.setVisibility(View.INVISIBLE);
                    mEditTextComment.setText("");
                    photoArray = null;

                    mButtonSaveTrailProblem.setVisibility(View.INVISIBLE);

                }

            }
        });

        mImageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                               Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "trails.jpg");
//                Uri tempUri = Uri.fromFile(imageFile);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
//                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, 0);



            }
        });


        mSpinnerProblemDetailsFlowers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);

                TextView myText = (TextView) view;
                Values.problemDetails = myText.getText() + "";
                Log.d("Probelme Details", Values.problemDetails + "");
                if (Values.problemDetails.equals("Choose Details")) {
                    mImageButtonCamera.setVisibility(View.INVISIBLE);
                } else {
                    mImageButtonCamera.setVisibility(View.VISIBLE);
                    mEditTextComment.setVisibility(View.VISIBLE);
                    mButtonSaveTrailProblem.setVisibility(View.VISIBLE);
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mSpinnerProblemDetailsTrails.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);

                TextView myText = (TextView) view;
                Values.problemDetails = myText.getText() + "";
                Log.d("Probelme Details", Values.problemDetails + "");
                if (Values.problemDetails.equals("Choose Details")) {
                    mImageButtonCamera.setVisibility(View.INVISIBLE);
                } else {
                    mImageButtonCamera.setVisibility(View.VISIBLE);
                    mEditTextComment.setVisibility(View.VISIBLE);
                    mButtonSaveTrailProblem.setVisibility(View.VISIBLE);

                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mSpinnerProblemDetailsSigns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);

                TextView myText = (TextView) view;
                Values.problemDetails = myText.getText() + "";
                Log.d("Probelme Details", Values.problemDetails + "");
                if (Values.problemDetails.equals("Choose Details")) {
                    mImageButtonCamera.setVisibility(View.INVISIBLE);
                } else {
                    mImageButtonCamera.setVisibility(View.VISIBLE);
                    mEditTextComment.setVisibility(View.VISIBLE);
                    mButtonSaveTrailProblem.setVisibility(View.VISIBLE);
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        mButtonChooseTrails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonChooseTrails.setBackgroundResource(R.drawable.orange_button_black_rim);
                mButtonChooseTrails.setTextColor(Color.WHITE);
                mButtonChooseSigns.setTextColor(Color.BLACK);
                mButtonChooseFlowers.setTextColor(Color.BLACK);
                mButtonChooseSigns.setBackgroundResource(R.drawable.maroon_button_background);
                mButtonChooseFlowers.setBackgroundResource(R.drawable.maroon_button_background);
                Values.problemSelected = "Trail";
                mSpinnerProblemDetailsTrails.setVisibility(View.VISIBLE);
                mSpinnerProblemDetailsTrails.setSelection(0);
                mSpinnerProblemDetailsFlowers.setVisibility(View.INVISIBLE);
                mSpinnerProblemDetailsSigns.setVisibility(View.INVISIBLE);

                //set camera, comment and save to invisible
                mImageButtonCamera.setVisibility(View.INVISIBLE);
                mImageButtonCamera.setImageResource(R.drawable.camtrailstwo);
                mImageButtonCamera.setScaleType(ImageView.ScaleType.FIT_CENTER);

                mEditTextComment.setVisibility(View.INVISIBLE);
                mEditTextComment.setText("");
                mButtonSaveTrailProblem.setVisibility(View.INVISIBLE);


            }
        });

        mButtonChooseSigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonChooseSigns.setBackgroundResource(R.drawable.orange_button_black_rim);
                mButtonChooseTrails.setTextColor(Color.BLACK);
                mButtonChooseSigns.setTextColor(Color.WHITE);
                mButtonChooseFlowers.setTextColor(Color.BLACK);
                mButtonChooseTrails.setBackgroundResource(R.drawable.maroon_button_background);
                mButtonChooseFlowers.setBackgroundResource(R.drawable.maroon_button_background);
                Values.problemSelected = "Sign";
                mSpinnerProblemDetailsTrails.setVisibility(View.INVISIBLE);
                mSpinnerProblemDetailsFlowers.setVisibility(View.INVISIBLE);
                mSpinnerProblemDetailsSigns.setVisibility(View.VISIBLE);
                mSpinnerProblemDetailsSigns.setSelection(0);


                //set camera, comment and save to invisible
                mImageButtonCamera.setVisibility(View.INVISIBLE);
                mImageButtonCamera.setImageResource(R.drawable.camtrailstwo);
                mImageButtonCamera.setScaleType(ImageView.ScaleType.FIT_CENTER);

                mEditTextComment.setVisibility(View.INVISIBLE);
                mEditTextComment.setText("");
                mButtonSaveTrailProblem.setVisibility(View.INVISIBLE);
            }
        });

        mButtonChooseFlowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonChooseTrails.setBackgroundResource(R.drawable.maroon_button_background);
                mButtonChooseTrails.setTextColor(Color.BLACK);
                mButtonChooseSigns.setTextColor(Color.BLACK);
                mButtonChooseFlowers.setTextColor(Color.WHITE);
                mButtonChooseSigns.setBackgroundResource(R.drawable.maroon_button_background);
                mButtonChooseFlowers.setBackgroundResource(R.drawable.orange_button_black_rim);
                Values.problemSelected = "Flowers";
                mSpinnerProblemDetailsTrails.setVisibility(View.INVISIBLE);
                mSpinnerProblemDetailsFlowers.setVisibility(View.VISIBLE);
                mSpinnerProblemDetailsFlowers.setSelection(0);
                mSpinnerProblemDetailsSigns.setVisibility(View.INVISIBLE);

                //set camera, comment and save to invisible
                mImageButtonCamera.setVisibility(View.INVISIBLE);
                mImageButtonCamera.setImageResource(R.drawable.camtrailstwo);
                mImageButtonCamera.setScaleType(ImageView.ScaleType.FIT_CENTER);

                mEditTextComment.setVisibility(View.INVISIBLE);
                mEditTextComment.setText("");
                mButtonSaveTrailProblem.setVisibility(View.INVISIBLE);
            }
        });


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("In Location Changed", "Changed");
                CurrentLOC = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d("Current Location", CurrentLOC + "");
                Log.d("Current Latit", location.getLatitude() + "");
                Log.d("Current Long", location.getLongitude() + "");
                //mTextViewCoordinates.setText(location.getLatitude() + ", "+location.getLongitude());
                DecimalFormat precision = new DecimalFormat("0.00000");
                // dblVariable is a number variable and not a String in this case
                mTextViewCoordinates.setText(precision.format(location.getLatitude()) + ", " + precision.format(location.getLongitude()));
                Values.currentCoordinates = precision.format(location.getLatitude()) + ", " + precision.format(location.getLongitude());


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(52.870606, -9.007369)).zoom(16).bearing(0).tilt(53).build();

        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);

            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);

        }
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

//    @Override
//    public void onBackPressed() {
//        Toast.makeText(getApplicationContext(), "Back button is disabled.",
//                Toast.LENGTH_SHORT).show();
//    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }

        locationManager.requestLocationUpdates("gps", 1000, 0, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("Request Code", requestCode + "");
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }





    @Override
    protected void onResume() {
        super.onResume();
        configure_button();
    }
//    private void drawMarker(Location location) {
//        if (mMap != null) {
//            mMap.clear();
//            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
//            mMap.addMarker(new MarkerOptions()
//                    .position(gps)
//                    .title("Current Position")
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bottle)));
//        }
//
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        mImageViewPhoto.setVisibility(View.VISIBLE);
        mImageButtonCamera.setVisibility(View.INVISIBLE);
        mImageViewPhoto.setImageBitmap(bitmap);
        photoArray = DbBitmapUtility.getBytes(bitmap);
        Values.imageInValues = bitmap;

        Log.d("Noting in Onres", "Nothin");

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void AddData(String county, String trailName, String monitorID, String problemType, String problemDetail, String latLng, String comment, byte[] photoAsBytes, String formattedDate) {

        boolean insertData = mDatabaseHelper.addData(county, trailName, monitorID, problemType, problemDetail, latLng, comment, photoAsBytes, formattedDate);
        if (insertData) {
            toastMessage("Data successfully saved in this phone.");
            //Log.d("photo Add data  save", photoAsBytes + "");

        } else {
            toastMessage("Something went wrong");

        }
    }

    public void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
}


