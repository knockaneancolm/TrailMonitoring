package com.applications4d.trailmonitoring;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

/**
 * Created by colmforde on 4/15/18.
 */
public class SelectTrailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String[] trailsClare = { "Select Here", "East Clare Way", "Mid Clare Way", "West Clare Way", "Black Head", "Burren"};
    TextView mTextViewTrailSelected;
    Button mButtonBeginMonitoring;
    Spinner mSpinner;
    //public ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_trail);

        mTextViewTrailSelected = (TextView) findViewById(R.id.textViewTrailSelected);
        mTextViewTrailSelected.setVisibility(View.INVISIBLE);
        mButtonBeginMonitoring = (Button) findViewById(R.id.buttonBeginMonitoring);
        mButtonBeginMonitoring.setVisibility(View.INVISIBLE);

        mSpinner = (Spinner) findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,trailsClare);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        mSpinner.setAdapter(aa);


        mButtonBeginMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
               // finish();
                startActivity(intent);

            }
        });



    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView myText = (TextView) view;

        Values.trailSelected = myText.getText()+"";
        if (Values.trailSelected .equals("Select Here")){
            mTextViewTrailSelected.setVisibility(View.INVISIBLE);
            mButtonBeginMonitoring.setVisibility(View.INVISIBLE);
        }
        else {
            mTextViewTrailSelected.setText("You have selected to monitor\n\n" +Values.trailSelected);
            mTextViewTrailSelected.setVisibility(View.VISIBLE);
            mButtonBeginMonitoring.setVisibility(View.VISIBLE);
        }
//        Toast.makeText(getActivity(), "This is it "+myText.getText(),
//                Toast.LENGTH_LONG).show();
    }



    public void onNothingSelected(AdapterView<?> parent) {

    }
}