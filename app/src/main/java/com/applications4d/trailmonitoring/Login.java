package com.applications4d.trailmonitoring;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.api.Response;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by colmforde on 4/6/18.
 */
public class Login extends Fragment{


    Button mButtonLogin, mButtonLogout, mButtonMonitor, mButtonViewEntries;
    ProgressBar mProgressBarLogin;
    EditText mEditTextUsername, mEditTextPassword;
    TextView mTextViewHi, Spacer;
    ImageView mImageViewWelcome, mImageViewTrailMonAtLogin;
    String username, password;
    SharedPreferences prefsTrailMonitoringIreland;
    String uname = "";

    View view;

    public Login(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);

        mTextViewHi = (TextView) view.findViewById(R.id.textViewHi);
        Spacer = (TextView) view.findViewById(R.id.SpacerBelowUsername);
        mImageViewWelcome = (ImageView) view.findViewById(R.id.welcomeImage);
        mButtonLogin = (Button) view.findViewById(R.id.buttonLoginSubmit);
        mButtonMonitor = (Button) view.findViewById(R.id.buttonMonitor);
        mButtonLogout = (Button) view.findViewById(R.id.buttonLogout);
        mEditTextUsername = (EditText) view.findViewById(R.id.EdittextUsernameLogin);
        mEditTextPassword = (EditText) view.findViewById(R.id.EdittextPasswordLogin);
        mButtonViewEntries = (Button) view.findViewById(R.id.buttonViewEntries);
        mImageViewTrailMonAtLogin = (ImageView) view.findViewById(R.id.imageViewTrailMonAtLogin);

        prefsTrailMonitoringIreland = getActivity().getSharedPreferences("userInfoTrailMonitoringIreland", Context.MODE_PRIVATE);
        uname = prefsTrailMonitoringIreland.getString("username", null);
        if (uname != null) {

            mTextViewHi.setText(uname+"\nCo.Clare");

            mButtonMonitor.setVisibility(View.VISIBLE);
            mButtonLogin.setVisibility(View.INVISIBLE);
            mImageViewTrailMonAtLogin.setVisibility(View.INVISIBLE);
            mButtonViewEntries.setVisibility(View.VISIBLE);

            mButtonLogout.setVisibility(View.VISIBLE);
            mEditTextUsername.setVisibility(View.INVISIBLE);
            mEditTextPassword.setVisibility(View.INVISIBLE);
            Spacer.setVisibility(View.INVISIBLE);
            mImageViewWelcome.setVisibility(View.VISIBLE);
            Values.username = uname;

        }
        else {
            mTextViewHi.setVisibility(View.INVISIBLE);
            mImageViewWelcome.setVisibility(View.INVISIBLE);
            mImageViewTrailMonAtLogin.setVisibility(View.VISIBLE);

            mButtonLogin.setVisibility(View.VISIBLE);
            mButtonMonitor.setVisibility(View.INVISIBLE);
            mButtonViewEntries.setVisibility(View.INVISIBLE);

            mButtonLogout.setVisibility(View.INVISIBLE);
            mEditTextUsername.setVisibility(View.VISIBLE);
            mEditTextPassword.setVisibility(View.VISIBLE);
            Spacer.setVisibility(View.VISIBLE);

        }

        mButtonViewEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                        Toast.makeText(getActivity(), "Data base will be displayed in a table for this location ",
//                                Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), ListDataActivity.class);
                //getActivity().finish();

                startActivity(intent);



            }
        });


        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /////////

                username = mEditTextUsername.getText().toString();
                password = mEditTextPassword.getText().toString();

                if (username.length() == 0 || password.length() == 0) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("You must enter details in all fields.")
                            .setNegativeButton("Ok", null)
                            .create()
                            .show();
                } else {
                    loginUser();
                }
            }
        });

        mButtonMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to new activity to allow back button use.


                Intent intent = new Intent(getActivity(), SelectTrailActivity.class);
                //getActivity().finish();
                startActivity(intent);

            }
        });

        mButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uname = prefsTrailMonitoringIreland.getString("username", null);
                if (uname != null) {
                    Log.d("In ASK OPTION", "Ask");
                    AlertDialog al = AskOption();
                    al.show();
                }
            }
        });

        return view;
    }
    public void loginUser(){
                    if (username.equals("CForde") && password.equals("pw")||username.equals("EHogan") && password.equals("pw")){
                        Values.username = username;
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Login Success")
                                .setNegativeButton("Ok", null)
                                .create()
                                .show();
                        SharedPreferences sharedPref = getActivity().getSharedPreferences("userInfoTrailMonitoringIreland", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("username", username);
                        editor.apply();

                        getFragmentManager().beginTransaction().replace(R.id.FullScreenContainerInIntro, new Login()).commit();
                    }
                    else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Login Failed")
                        .setNegativeButton("Retry", null)
                        .create()
                        .show();
            }

    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getActivity())
                //set message, title, and icon

                .setMessage("Are you sure you want to Logout?")

                .setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        prefsTrailMonitoringIreland.edit().remove("username").commit();
                        getFragmentManager().beginTransaction().replace(R.id.FullScreenContainerInIntro, new Login()).commit();

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



}
