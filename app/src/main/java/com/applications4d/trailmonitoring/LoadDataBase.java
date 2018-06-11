package com.applications4d.trailmonitoring;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by colmforde on 4/17/18.
 */

/**
 * Created by colmforde on 5/9/17.
 */
public class LoadDataBase extends StringRequest{

    private static final String REGISTER_REQUEST_URL = "http://irelandtreasuretrails.com/monitortraildata.php";
    private Map<String, String> params;

    public LoadDataBase(String countyToUpload, String trailNameToUpload, String monitorIDToUpload, String problemToUpload,
                        String problemDetailToUpload, String latLngToUpload, String commentToUpload, String photoToUpload, String dateToUpload, String actionReq, String repairComplete, String dateFixed, com.android.volley.Response.Listener<String> listener){

        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("County", countyToUpload);
        params.put("TrailName", trailNameToUpload);
        params.put("MonitorID", monitorIDToUpload);
        params.put("ProblemType", problemToUpload);
        params.put("ProblemDetail", problemDetailToUpload);
        params.put("LatLng", latLngToUpload);
        params.put("DateFound", dateToUpload);
        params.put("Comment", commentToUpload);
        params.put("Photo", photoToUpload);
        params.put("ActionReq", actionReq);
        params.put("RepairComplete", repairComplete);
        params.put("DateFixed", dateFixed);


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}



/////////////////////////
//
//
//public class MainActivity extends Activity implements View.OnClickListener {
//
//    private Button buttonUpload;
//    private Button buttonChoose;
//    private Button buttonChoose1;
//
//    private EditText edithouse;
//    private EditText editbuild;
//    private ImageView imageView;
//    private ImageView imageView1;
//
//    public static final String KEY_IMAGE = "image";
//    public static final String KEY_IMAGE1 = "image1";
//    public static final String KEY_TEXT = "house";
//    public static final String KEY_TEXT1 = "build";
//    public static final String UPLOAD_URL = "http://oursite/PhotoUploadWithText/upload.php";
//
//    private int PICK_IMAGE_REQUEST = 1;
//    private int PICK_IMAGE_REQUEST1 = 2;
//
//    private Bitmap bitmap;
//    private Bitmap bitmap1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        buttonUpload = (Button) findViewById(R.id.save);
//        buttonChoose = (Button) findViewById(R.id.choose);
//        buttonChoose1 = (Button) findViewById(R.id.choose1);
//
//        edithouse = (EditText) findViewById(R.id.houseno);
//        editbuild = (EditText) findViewById(R.id.buildno);
//        imageView = (ImageView) findViewById(R.id.imageViews);
//        imageView1 = (ImageView) findViewById(R.id.imageViews1);
//
//        buttonChoose.setOnClickListener(this);
//        buttonChoose1.setOnClickListener(this);
//        buttonUpload.setOnClickListener(this);
//    }
//
//    private void showFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//    }
//    private void showFileChooser1() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST1);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri filePath = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                //bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                imageView.setImageBitmap(bitmap);
//                // imageView1.setImageBitmap(bitmap1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri filePath = data.getData();
//            try {
//                //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                //imageView.setImageBitmap(bitmap);
//                imageView1.setImageBitmap(bitmap1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public String getStringImage(Bitmap bmp){
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] imageBytes = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        return encodedImage;
//    }
//
//    public String getStringImage1(Bitmap bmp){
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] imageBytes = baos.toByteArray();
//        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//        return encodedImage;
//    }
//
//
//    public void uploadImage(){
//        final String house = edithouse.getText().toString().trim();
//        final String build = editbuild.getText().toString().trim();
//        final String image = getStringImage(bitmap);
//        final String image1 = getStringImage1(bitmap1);
//        class UploadImage extends AsyncTask<Void,Void,String> {
//            ProgressDialog loading;
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(MainActivity.this,"Please wait...","uploading",false,false);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            protected String doInBackground(Void... params) {
//                RequestHandler rh = new RequestHandler();
//                HashMap<String,String> param = new HashMap<String,String>();
//                param.put(KEY_TEXT,house);
//                param.put(KEY_TEXT1,build);
//                param.put(KEY_IMAGE,image);
//                param.put(KEY_IMAGE1,image1);
//                String result = rh.sendPostRequest(UPLOAD_URL, param);
//                return result;
//            }
//        }
//        UploadImage u = new UploadImage();
//        u.execute();
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        if(v == buttonChoose){
//            showFileChooser();
//        }
//        if(v == buttonUpload){
//            uploadImage();
//        }
//        if(v == buttonChoose1){
//            showFileChooser1();
//        }
//    }
//}