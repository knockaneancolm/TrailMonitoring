package com.applications4d.trailmonitoring;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.SplittableRandom;
import java.io.File;

import static android.app.AlertDialog.*;

/**
 * Created by colmforde on 4/11/18.
 */
public class ListDataActivity extends AppCompatActivity{

    public static final String TAG = "ListDataActivity";
    DatabaseHelper mDatabaseHelper;
    ListView mListView;
    TextView mTextViewTitleListView;

    private static final String UPLOAD_URL = "http://irelandtreasuretrails.com/MonitorTrailUploads/monitortrailupload.php";
    private static final int IMAGE_REQUEST_CODE = 3;
    private static final int STORAGE_PERMISSION_CODE = 123;

    //my code to show id and county
    ArrayList<String> listDataID = new ArrayList<>();
    ArrayList<String> listDataCounty = new ArrayList<>();
    ArrayList<String> listDataTrailName = new ArrayList<>();
    ArrayList<String> listDataMonitorID = new ArrayList<>();
    ArrayList<String> listDataTrailProblem = new ArrayList<>();
    ArrayList<String> listDataTrailProblemDetail = new ArrayList<>();
    ArrayList<String> listDataLatLng = new ArrayList<>();
    ArrayList<String> listDataComment = new ArrayList<>();
    ArrayList<byte[]> listDataPhotos = new ArrayList<>();
    ArrayList<String> listDataDate = new ArrayList<>();


    String countyToUpload = "jimmy";
    String trailNameToUpload = "jimmy";
    String monitorIDToUpload = "jimmy";
    String problemToUpload = "jimmy";
    String problemDetailToUpload = "jimmy";
    String latLngToUpload = "jimmy";
    String commentToUpload = "jimmy";

    //String photoToUpload = "no photo";
    String actionReq = "none stated";
            String repairComplete = "1";
    String dateFixed = "";
    String dateToUpload = "jimmy";
    byte[] photoToUpload;
    String filePathToUpload;

    String encodedImage = "jimmy";
    Bitmap bitmap;
    Uri filePath;
    File f;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        mListView = (ListView) findViewById(R.id.listView);
//

        CustomAdapter customAdapter = new CustomAdapter();
        mListView.setAdapter(customAdapter);

        mDatabaseHelper = new DatabaseHelper(this);

        populateListView();
    }

    public void populateListView(){

        Log.d(TAG, "populateListView : display data in the listview");

        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();

        Log.d("Cursor Data",  data+"");


        while (data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the arraylist
            //col1 is actually 2nd column
            //listData.add(data.getString(0)+" "+data.getString(1));

            //mycode add to both arrays
            listDataID.add(data.getString(0));
            listDataCounty.add(data.getString(1));
            listDataTrailName.add(data.getString(2));
            listDataMonitorID.add(data.getString(3));
            listDataTrailProblem.add(data.getString(4));
            listDataTrailProblemDetail.add(data.getString(5));
            listDataLatLng.add(data.getString(6));
            listDataComment.add(data.getString(7));
            listDataPhotos.add(data.getBlob(8));
            Log.d("List data photos", data.getBlob(8) + "");
            listDataDate.add(data.getString(9));



        }
        //create list adapter and set adapter
//        final ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
//        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();

                Log.d(TAG, "onItemClick: You clicked on " + name);

                Cursor data = mDatabaseHelper.getItemID(name);

                int itemID = -1;
                while (data.moveToNext()) {
                    itemID = data.getInt(0);
                }
                if (itemID > -1) {
                    Log.d(TAG, "onItemClick: The ID is " + itemID);
                    Intent editScreenIntent = new Intent(ListDataActivity.this, EditDataActivity.class);
                    editScreenIntent.putExtra("id", itemID);
                    editScreenIntent.putExtra("name", name);

                    startActivity(editScreenIntent);
                } else {
                    toastMessage("No ID assosciated with that name");
                }
            }
        });
    }

    public void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listDataTrailName.size();
//            return Values.numberOfArticles;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.customlayout, null);
            RelativeLayout mRLCustom = (RelativeLayout) convertView.findViewById(R.id.RLCustom);
            TextView mTextViewID = (TextView) convertView.findViewById(R.id.customCellID);

            TextView mTextViewTrailName = (TextView) convertView.findViewById(R.id.CustomCellTrailName);
            TextView mTextViewDate = (TextView) convertView.findViewById(R.id.CustomCellDate);
            TextView mTextViewTrailProblem = (TextView) convertView.findViewById(R.id.CustomCellTrailProblem);
            TextView mTextViewTrailProblemDetail = (TextView) convertView.findViewById(R.id.CustomCellTrailProblemDetail);
            TextView mTextViewTrailComment = (TextView) convertView.findViewById(R.id.CustomCellComment);
            ImageView mImageview = (ImageView) convertView.findViewById(R.id.customCellImage);

            Button mButtonUpload = (Button) convertView.findViewById(R.id.CustomCellUploadButton);
            Button mButtonDelete = (Button) convertView.findViewById(R.id.CustomCellDeleteButton);

            mButtonUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int idToUpload =  Integer.parseInt(listDataID.get(position));
                     countyToUpload = listDataCounty.get(position);
                     trailNameToUpload = listDataTrailName.get(position);
                     monitorIDToUpload = listDataMonitorID.get(position);
                     problemToUpload = listDataTrailProblem.get(position);
                     problemDetailToUpload = listDataTrailProblemDetail.get(position);
                     latLngToUpload = listDataLatLng.get(position);
                     commentToUpload = listDataComment.get(position);
                    Log.d("here",  "11111");

                    photoToUpload = listDataPhotos.get(position);

                    encodedImage = Base64.encodeToString(photoToUpload, Base64.DEFAULT);

                    dateToUpload = listDataDate.get(position);


                    Log.d("DELETE", "Del");
                    AlertDialog al = AskOptionUpload(idToUpload);
                    al.show();

                }
            });

            mButtonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id =  Integer.parseInt(listDataID.get(position));

                    Log.d("DELETE", "Del");
                    AlertDialog al = AskOptionDelete(id);
                    al.show();

                }
            });

//            for (int i = 0; i < Values.ArticlesTitles.length; i++){
//                Log.d("loop in myads books", Values.ArticlesTitles[i]);
//                Log.d("len arr in myads books", Values.ArticlesTitles.length+"");
//            }

            //need to convert id yo images and book names array
            //then text title names array[position
            //then images array[posiytion
            byte[] data = listDataPhotos.get(position);
//            byte[] data = item1.getBytes();
           // Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
           // mImageview.setImageBitmap(bmp);
            Bitmap bmp = DbBitmapUtility.getImage(data);
            mImageview.setImageBitmap(bmp);
            Log.d("photo as bytes List", data + "");
            Log.d("photo as bitmap List", bmp + "");

            mTextViewID.setText(listDataID.get(position));
            mTextViewDate.setText(listDataDate.get(position));
            mTextViewTrailName.setText(listDataTrailName.get(position));
            mTextViewTrailProblem.setText(listDataTrailProblem.get(position));
            mTextViewTrailProblemDetail.setText(listDataTrailProblemDetail.get(position));
            mTextViewTrailComment.setText(listDataComment.get(position));
            mTextViewTrailProblem.setText(listDataTrailProblem.get(position));


//            new DownloadImageTask(mImageview).execute(Values.ArticlesImages[position]);
            //         mImageview.setImageBitmap(BitmapFactory.decodeFile("http://irelandtreasuretrails.com/imagesone.jpg"));

            //new DownloadImageTask(mImageview).execute("http://irelandtreasuretrails.com/imagestwo.jpg");
            //mImageview.setImageResource(Values.ArticlesImages[position]);

            return convertView;
        }
    }

    public void DeleteDataRow(int id){

        mDatabaseHelper.deleteRow(id);

    }

    private AlertDialog AskOptionDelete(final int id)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon

                .setMessage("This data has not been uploaded yet.\n\nAre you sure you want to delete it?")

                .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                            DeleteDataRow(id);
                            Intent intent = new Intent(getApplicationContext(), ListDataActivity.class);
                            finish();
                            startActivity(intent);
                    }
                })
                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();
                    }
                })

                .create();
        return myQuittingDialogBox;

    }

    private AlertDialog AskOptionUpload(final int id)
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon

                .setMessage("Upload entry "+id+".\n\nThis entry will then delete from the phone when upload completes.")

                .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        //check web connection first
                        if (isOnline()) {
                            //loadToDataBase(id);
                            uploadImage(id);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "No data connection. Try again later.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();
                    }
                })

                .create();
        return myQuittingDialogBox;

    }

    public void loadToDataBase(final int id){
        Log.d("In Upload to DB", "In Upload to DB");
        Response.Listener<String> responseListener = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d("xx Success ", jsonResponse+"");

                    boolean success = jsonResponse.getBoolean("success");
                    if (success){
                        Log.d("Success Db", "Success dB");
                        //if success then delete it then reload page
                        DeleteDataRow(id);
                        Intent intent = new Intent(getApplicationContext(), ListDataActivity.class);
                        finish();
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Entry "+id+" uploaded to Database and deleted from this phone", Toast.LENGTH_LONG).show();

                    }
                    else{
                        Log.d("Not Success ", "Not Success ");

                    }

                } catch (JSONException e){
                    Log.d("Error", "err%%%%%%%%");
                    e.printStackTrace();
                }
            }
        };
        LoadDataBase loadDataBase = new LoadDataBase(countyToUpload, trailNameToUpload, monitorIDToUpload, problemToUpload,
                problemDetailToUpload, latLngToUpload, commentToUpload, encodedImage, dateToUpload, actionReq, repairComplete, dateFixed, responseListener);
        Log.d("encoded image",  "" + encodedImage);
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        queue.add(loadDataBase);
    }

    ////////////////////
    private void uploadImage(final int id){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        //Toast.makeText(getApplicationContext(), s , Toast.LENGTH_LONG).show();
                        DeleteDataRow(id);
                        Intent intent = new Intent(getApplicationContext(), ListDataActivity.class);
                        finish();
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Entry "+id+" uploaded to Database and deleted from this phone", Toast.LENGTH_LONG).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getApplicationContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                //String image = getStringImage(bitmap);

                //Getting Image Name
                //String name = editTextName.getText().toString().trim();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("County", countyToUpload);
                params.put("TrailName", trailNameToUpload);
                params.put("MonitorID", monitorIDToUpload);
                params.put("ProblemType", problemToUpload);
                params.put("ProblemDetail", problemDetailToUpload);
                params.put("LatLng", latLngToUpload);
                params.put("DateFound", dateToUpload);
                params.put("Comment", commentToUpload);
                params.put("Photo", encodedImage);
                params.put("ActionReq", actionReq);
                params.put("RepairComplete", repairComplete);
                params.put("DateFixed", dateFixed);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            //Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}






