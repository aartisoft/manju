package com.ownmetrro.manjuagecy.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.ownmetrro.manjuagecy.customs.CircularImageView;

import com.ownmetrro.manjuagecy.R;

import com.ownmetrro.manjuagecy.constant.ConstantValues;
import com.ownmetrro.manjuagecy.customs.DialogLoader;
import com.ownmetrro.manjuagecy.utils.LocaleHelper;
import com.ownmetrro.manjuagecy.models.user_model.UserData;
import com.ownmetrro.manjuagecy.network.APIClient;
import com.ownmetrro.manjuagecy.utils.CheckPermissions;
import com.ownmetrro.manjuagecy.utils.Utilities;
import com.ownmetrro.manjuagecy.utils.ImagePicker;
import com.ownmetrro.manjuagecy.utils.ValidateInputs;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * SignUp activity handles User's Registration
 **/

public class Signup extends AppCompatActivity {

    View parentView;
    String profileImage;
    private static final int PICK_IMAGE_ID = 360;           // the number doesn't matter

    Toolbar toolbar;
    ActionBar actionBar;
    DialogLoader dialogLoader;
    
    AdView mAdView;
    Button signupBtn;
    FrameLayout banner_adView;
    TextView signup_loginText;
    TextView service_terms, privacy_policy, refund_policy, and_text;
    CircularImageView user_photo;
    FloatingActionButton user_photo_edit_fab;
    EditText user_firstname, user_lastname, user_email, user_password, user_mobile,user_cpassword,user_ccode;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    //It is the verification id that will be sent to the user
    private String mVerificationId;

    //The edittext to input the code
    private EditText editTextCode;

    //firebase auth object
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
    
        MobileAds.initialize(this, ConstantValues.ADMOBE_ID);
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);


        // setting Toolbar
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.signup));
        actionBar.setDisplayHomeAsUpEnabled(false);


        // Binding Layout Views
        user_photo = (CircularImageView) findViewById(R.id.user_photo);
        user_firstname = (EditText) findViewById(R.id.user_firstname);
        user_lastname = (EditText) findViewById(R.id.user_lastname);
        user_email = (EditText) findViewById(R.id.user_email);
        user_password = (EditText) findViewById(R.id.user_password);
        user_cpassword = (EditText) findViewById(R.id.user_cpassword);
        user_mobile = (EditText) findViewById(R.id.user_mobile);
        signupBtn = (Button) findViewById(R.id.signupBtn);
        and_text = (TextView) findViewById(R.id.and);
        service_terms = (TextView) findViewById(R.id.service_terms);
        privacy_policy = (TextView) findViewById(R.id.privacy_policy);
        refund_policy = (TextView) findViewById(R.id.refund_policy);
        signup_loginText = (TextView) findViewById(R.id.signup_loginText);
        banner_adView = (FrameLayout) findViewById(R.id.banner_adView);
        user_photo_edit_fab = (FloatingActionButton) findViewById(R.id.user_photo_edit_fab);
        user_ccode = findViewById(R.id.user_ccode);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();



        

        dialogLoader = new DialogLoader(Signup.this);
    
        and_text.setText(" "+getString(R.string.and)+" ");
    
        
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Signup.this, android.R.style.Theme_NoTitleBar);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_webview_fullscreen, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
    
                final ImageButton dialog_button = (ImageButton) dialogView.findViewById(R.id.dialog_button);
                final TextView dialog_title = (TextView) dialogView.findViewById(R.id.dialog_title);
                final WebView dialog_webView = (WebView) dialogView.findViewById(R.id.dialog_webView);
            
                dialog_title.setText(getString(R.string.privacy_policy));
            
            
                String description = ConstantValues.PRIVACY_POLICY;
                String styleSheet = "<style> " +
                                        "body{background:#eeeeee; margin:10; padding:10} " +
                                        "p{color:#757575;} " +
                                        "img{display:inline; height:auto; max-width:100%;}" +
                                    "</style>";
    
                dialog_webView.setVerticalScrollBarEnabled(true);
                dialog_webView.setHorizontalScrollBarEnabled(false);
                dialog_webView.setBackgroundColor(Color.TRANSPARENT);
                dialog_webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                dialog_webView.loadDataWithBaseURL(null, styleSheet+description, "text/html", "utf-8", null);
            
            
                final AlertDialog alertDialog = dialog.create();
    
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    alertDialog.getWindow().setStatusBarColor(ContextCompat.getColor(Signup.this, R.color.colorPrimaryDark));
                }
            
                dialog_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            
                alertDialog.show();
            
            }
        });
    
        refund_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Signup.this, android.R.style.Theme_NoTitleBar);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_webview_fullscreen, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
    
                final ImageButton dialog_button = (ImageButton) dialogView.findViewById(R.id.dialog_button);
                final TextView dialog_title = (TextView) dialogView.findViewById(R.id.dialog_title);
                final WebView dialog_webView = (WebView) dialogView.findViewById(R.id.dialog_webView);
            
                dialog_title.setText(getString(R.string.refund_policy));
            
            
                String description = ConstantValues.REFUND_POLICY;
                String styleSheet = "<style> " +
                                        "body{background:#eeeeee; margin:10; padding:10} " +
                                        "p{color:#757575;} " +
                                        "img{display:inline; height:auto; max-width:100%;}" +
                                    "</style>";
    
                dialog_webView.setVerticalScrollBarEnabled(true);
                dialog_webView.setHorizontalScrollBarEnabled(false);
                dialog_webView.setBackgroundColor(Color.TRANSPARENT);
                dialog_webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                dialog_webView.loadDataWithBaseURL(null, styleSheet+description, "text/html", "utf-8", null);
            
            
                final AlertDialog alertDialog = dialog.create();
    
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    alertDialog.getWindow().setStatusBarColor(ContextCompat.getColor(Signup.this, R.color.colorPrimaryDark));
                }
            
                dialog_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            
                alertDialog.show();
            }
        });
    
        service_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Signup.this, android.R.style.Theme_NoTitleBar);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_webview_fullscreen, null);
                dialog.setView(dialogView);
                dialog.setCancelable(false);
    
                final ImageButton dialog_button = (ImageButton) dialogView.findViewById(R.id.dialog_button);
                final TextView dialog_title = (TextView) dialogView.findViewById(R.id.dialog_title);
                final WebView dialog_webView = (WebView) dialogView.findViewById(R.id.dialog_webView);
            
                dialog_title.setText(getString(R.string.service_terms));
            
            
                String description = ConstantValues.TERMS_SERVICES;
                String styleSheet = "<style> " +
                                        "body{background:#eeeeee; margin:10; padding:10} " +
                                        "p{color:#757575;} " +
                                        "img{display:inline; height:auto; max-width:100%;}" +
                                    "</style>";
    
                dialog_webView.setVerticalScrollBarEnabled(true);
                dialog_webView.setHorizontalScrollBarEnabled(false);
                dialog_webView.setBackgroundColor(Color.TRANSPARENT);
                dialog_webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                dialog_webView.loadDataWithBaseURL(null, styleSheet+description, "text/html", "utf-8", null);
            
            
                final AlertDialog alertDialog = dialog.create();
    
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    alertDialog.getWindow().setStatusBarColor(ContextCompat.getColor(Signup.this, R.color.colorPrimaryDark));
                }
            
                dialog_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            
                alertDialog.show();
            }
        });


        // Handle Click event of user_photo_edit_fab FAB
        user_photo_edit_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckPermissions.is_CAMERA_PermissionGranted()  &&  CheckPermissions.is_STORAGE_PermissionGranted()) {
                    pickImage();
                }
                else {
                    ActivityCompat.requestPermissions
                            (
                                    Signup.this,
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    CheckPermissions.PERMISSIONS_REQUEST_CAMERA
                            );
                }
            }
        });


        // Handle Click event of signup_loginText TextView
        signup_loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish SignUpActivity to goto the LoginActivity
                finish();
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
            }
        });


        // Handle Click event of signupBtn Button
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate Login Form Inputs
                boolean isValidData = validateForm();

                if (isValidData) {
                    parentView = v;


                    ConstantValues.First_Name =  user_firstname.getText().toString().trim();
                     ConstantValues.Last_Name =  user_lastname.getText().toString().trim();
                           ConstantValues.Email =  user_email.getText().toString().trim();
                           ConstantValues.Password =  user_password.getText().toString().trim();
                           ConstantValues.Mobile =  user_mobile.getText().toString().trim();
                           ConstantValues.p_photo =  profileImage;

                 processRegistration();
                    // Proceed User Registration

                    /*Intent intent = new Intent(getApplicationContext(),OTP.class);
                    startActivity(intent);
                    finish();
                    */
                }
            }
        });
    }





    //*********** Picks User Profile Image from Gallery or Camera ********//

    private void pickImage() {
        // Intent with Image Picker Apps from the static method of ImagePicker class
        Intent chooseImageIntent = ImagePicker.getImagePickerIntent(Signup.this);
        chooseImageIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        chooseImageIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    
        // Start Activity with Image Picker Intent
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
        
    }
    
    
    
    //*********** Receives the result from a previous call of startActivityForResult(Intent, int) ********//
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Handle Activity Result
            if (requestCode == PICK_IMAGE_ID) {

                // Get the User Selected Image as Bitmap from the static method of ImagePicker class
                Bitmap bitmap = ImagePicker.getImageFromResult(Signup.this, resultCode, data);
    
                // Upload the Bitmap to ImageView
                user_photo.setImageBitmap(bitmap);
                
                // Get the converted Bitmap as Base64ImageString from the static method of Helper class
                profileImage = Utilities.getBase64ImageStringFromBitmap(bitmap);
                
            }
        }
    }
    
    
    
    //*********** This method is invoked for every call on requestPermissions(Activity, String[], int) ********//
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    
        if (requestCode == CheckPermissions.PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // The Camera and Storage Permission is granted
                pickImage();
            }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(Signup.this, Manifest.permission.CAMERA)) {
                    // Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                    builder.setTitle(getString(R.string.permission_camera_storage));
                    builder.setMessage(getString(R.string.permission_camera_storage_needed));
                    builder.setPositiveButton(getString(R.string.grant), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions
                                    (
                                            Signup.this,
                                            new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            CheckPermissions.PERMISSIONS_REQUEST_CAMERA
                                    );
                        }
                    });
                    builder.setNegativeButton(getString(R.string.not_now), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                else {
                    Toast.makeText(Signup.this,getString(R.string.permission_rejected), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    
    
    
    //*********** Proceed User Registration Request ********//

    private void processRegistration() {

        String mob_num = user_ccode.getText().toString()+user_mobile.getText().toString();

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Verify Number")
                .setMessage("Please confirm your contact number is \n"+ mob_num)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        Call<UserData> call = APIClient.getInstance()
                                .processRegistration
                                        (
                                                user_firstname.getText().toString().trim(),
                                                user_lastname.getText().toString().trim(),
                                                user_email.getText().toString().trim(),
                                                user_password.getText().toString().trim(),
                                                mob_num,
                                                profileImage
                                        );

                        call.enqueue(new Callback<UserData>() {
                            @Override
                            public void onResponse(Call<UserData> call, retrofit2.Response<UserData> response) {

                                dialogLoader.hideProgressDialog();

                                // Check if the Response is successful
                                if (response.isSuccessful()) {
                                    if (response.body().getSuccess().equalsIgnoreCase("1")) {
                                        editor = sharedPreferences.edit();
                                        editor.putString("userPhone", user_mobile.getText().toString());
                                        editor.putString("userPassword", user_password.getText().toString());
                                        editor.apply();


                                        Intent intent = new Intent(getApplicationContext(),Login.class);
                                        startActivity(intent);

                                        // Finish SignUpActivity to goto the LoginActivity
                                        finish();
                                        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);

                                    }
                                    else if (response.body().getSuccess().equalsIgnoreCase("0")) {
                                        // Get the Error Message from Response
                                        String message = response.body().getMessage();
                                        Snackbar.make(parentView, message, Snackbar.LENGTH_SHORT).show();

                                    }
                                    else {
                                        // Unable to get Success status
                                        Toast.makeText(Signup.this, getString(R.string.unexpected_response), Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    // Show the Error Message
                                    Toast.makeText(Signup.this, response.message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserData> call, Throwable t) {
                                dialogLoader.hideProgressDialog();
                                Toast.makeText(Signup.this, "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
                            }
                        });
                        dialogLoader.showProgressDialog();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    

    }



    //*********** Validate SignUp Form Inputs ********//

    private boolean validateForm() {
        if (!ValidateInputs.isValidName(user_firstname.getText().toString().trim())) {
            user_firstname.setError(getString(R.string.invalid_first_name));
            return false;
        } else if (!ValidateInputs.isValidName(user_lastname.getText().toString().trim())) {
            user_lastname.setError(getString(R.string.invalid_last_name));
            return false;
        } else if (!ValidateInputs.isValidEmail(user_email.getText().toString().trim())) {
            user_email.setError(getString(R.string.invalid_email));
            return false;
        } else if (!ValidateInputs.isValidPassword(user_password.getText().toString().trim())) {
            user_password.setError(getString(R.string.invalid_password));
            return false;
        } else if (!ValidateInputs.isValidNumber(user_mobile.getText().toString().trim())) {
            user_mobile.setError(getString(R.string.invalid_contact));
            return false;
        } else if (user_mobile.getText().toString().trim().length() != 10){
            user_mobile.setError(getString(R.string.invalid_contact));
            return false;
        }
        else if (!user_password.getText().toString().equals(user_cpassword.getText().toString())){
            user_password.setError("Password Mismatch");
            user_cpassword.setError("password Mismatch");
            return false;
        }
        else {
            return true;
        }
    }
    
    
    
    //*********** Set the Base Context for the ContextWrapper ********//
    
    @Override
    protected void attachBaseContext(Context newBase) {
    
        String languageCode = ConstantValues.LANGUAGE_CODE;
        if ("".equalsIgnoreCase(languageCode))
            languageCode = ConstantValues.LANGUAGE_CODE = "en";
    
        super.attachBaseContext(LocaleHelper.wrapLocale(newBase, languageCode));
    }
    
    
    
    //*********** Called when the Activity has detected the User pressed the Back key ********//
    
    @Override
    public void onBackPressed() {
        // Finish SignUpActivity to goto the LoginActivity
        finish();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
    }


}

