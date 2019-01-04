package com.ownmetrro.manjuagecy.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.ownmetrro.manjuagecy.R;
import com.ownmetrro.manjuagecy.app.MyAppPrefsManager;
import com.ownmetrro.manjuagecy.constant.ConstantValues;
import com.ownmetrro.manjuagecy.customs.DialogLoader;
import com.ownmetrro.manjuagecy.databases.User_Info_DB;
import com.ownmetrro.manjuagecy.models.user_model.UserData;
import com.ownmetrro.manjuagecy.models.user_model.UserDetails;
import com.ownmetrro.manjuagecy.network.APIClient;
import com.ownmetrro.manjuagecy.network.StartAppRequests;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTP extends AppCompatActivity {

    private String mVerificationId;

    //The edittext to input the code
    private android.support.design.widget.TextInputEditText otp_one;

    //firebase auth object
    private FirebaseAuth mAuth;

    User_Info_DB userInfoDB;
    DialogLoader dialogLoader;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    String mobile;
    CountDownTimer cTimer = null;
    View parentView;


    private CallbackManager callbackManager;
    TextView mob_num,resend,mTextField;
    private final int SPLASH_DISPLAY_LENGTH = 60000;

Button signin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotp);
        mAuth = FirebaseAuth.getInstance();



        otp_one = findViewById(R.id.ed_one);
        mob_num = findViewById(R.id.mob_num);
        resend = findViewById(R.id.resend);
        resend.setVisibility(View.GONE);
        mTextField= findViewById(R.id.ctimer);
        signin = findViewById(R.id.buttonSignIn);



        dialogLoader = new DialogLoader(OTP.this);
        startTimer();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                resend.setVisibility(View.VISIBLE);
            }
        }, SPLASH_DISPLAY_LENGTH);


        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startTimer();
                resend.setVisibility(View.GONE);
                sendVerificationCode(mobile);
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        resend.setVisibility(View.VISIBLE);
                    }
                }, SPLASH_DISPLAY_LENGTH);
            }
        });



        userInfoDB = new User_Info_DB();
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);



        mobile = ConstantValues.Mobile;
        mob_num.setText("+91\t"+ConstantValues.Mobile);

        sendVerificationCode(mobile);


        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentView = v;
                String code_one = otp_one.getText().toString().trim();


                String code = code_one ;
                if (code.isEmpty() || code.length() < 6) {
                    Toast.makeText(getApplicationContext(),"Please Enter the 6 digit Code",Toast.LENGTH_LONG).show();

                    return;
                }else {
                  //  Toast.makeText(getApplicationContext(),code,Toast.LENGTH_LONG).show();
                    //verifying the code entered manually
                    verifyVerificationCode(code);
                  //  dialogLoader.showProgressDialog();
                }


            }
        });


    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otp_one.setText(code);


                //verifying the code
               // verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Log.e("OTP error",e.getMessage());

          //  Toast.makeText(OTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTP.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            dialogLoader.showProgressDialog();
                            processRegistration();                          /*  //verification successful we will start the profile activity
                            Intent intent = new Intent(OTP.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);*/

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

                           /*// Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();*/
                        }
                    }
                });
    }

    //start timer function
    void startTimer() {
        mTextField.setVisibility(View.VISIBLE);
        cTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                mTextField.setVisibility(View.GONE);
            }
        };
        cTimer.start();
    }


    //cancel timer
    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }
    private void processLogin() {

        Call<UserData> call = APIClient.getInstance()
                .processOtp
                        (
                                mobile
                        );

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {

                if (response.isSuccessful()) {

                    if (response.body().getSuccess().equalsIgnoreCase("1") || response.body().getSuccess().equalsIgnoreCase("2"))
                    {
                        dialogLoader.hideProgressDialog();
                        // Get the User Details from Response
                        UserDetails userDetails = response.body().getData().get(0);

                        // Save User Data to Local Databases
                        if (userInfoDB.getUserData(userDetails.getCustomersId()) != null) {
                            // User already exists
                            userInfoDB.updateUserData(userDetails);
                        } else {
                            // Insert Details of New User
                            userInfoDB.insertUserData(userDetails);
                        }

                        // Save necessary details in SharedPrefs
                        editor = sharedPreferences.edit();
                        editor.putString("userID", userDetails.getCustomersId());
                        editor.putString("userPhone", userDetails.getCustomersTelephone());

                        editor.putString("userEmail", userDetails.getCustomersEmailAddress());
                        editor.putString("userName", userDetails.getCustomersFirstname()+" "+userDetails.getCustomersLastname());
                        editor.putString("userDefaultAddressID", userDetails.getCustomersDefaultAddressId());
                        editor.putBoolean("isLogged_in", true);
                        editor.apply();

                        // Set UserLoggedIn in MyAppPrefsManager
                        MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(OTP.this);
                        myAppPrefsManager.setUserLoggedIn(true);

                        // Set isLogged_in of ConstantValues
                        ConstantValues.IS_USER_LOGGED_IN = myAppPrefsManager.isUserLoggedIn();


                        StartAppRequests.RegisterDeviceForFCM(OTP.this);


                        // Navigate back to MainActivity
                        Intent i = new Intent(OTP.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);

                    }
                    else if (response.body().getSuccess().equalsIgnoreCase("0"))
                    {
                        // Get the Error Message from Response
                        String message = response.body().getMessage();
                     Toast.makeText(OTP.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(OTP.this, getString(R.string.unexpected_response), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // Show the Error Message
                    Toast.makeText(OTP.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                dialogLoader.hideProgressDialog();
                Toast.makeText(OTP.this, "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void processRegistration() {

        dialogLoader.showProgressDialog();


        Call<UserData> call = APIClient.getInstance()
                .processRegistration
                        (
                                ConstantValues.First_Name,
                               ConstantValues.Last_Name,
                                ConstantValues.Email,
                                ConstantValues.Password,
                                ConstantValues.Mobile,
                                ConstantValues.p_photo
                        );

        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, retrofit2.Response<UserData> response) {

                dialogLoader.hideProgressDialog();

                // Check if the Response is successful
                if (response.isSuccessful()) {
                    if (response.body().getSuccess().equalsIgnoreCase("1")) {
                        editor = sharedPreferences.edit();
                        editor.putString("userPhone",ConstantValues.Mobile);
                        editor.putString("userPassword", ConstantValues.Password);
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
                      //  Snackbar.make(parentView, message, Snackbar.LENGTH_SHORT).show();

                    }
                    else {
                        // Unable to get Success status
                        Toast.makeText(OTP.this, getString(R.string.unexpected_response), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // Show the Error Message
                    Toast.makeText(OTP.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                dialogLoader.hideProgressDialog();
                Toast.makeText(OTP.this, "NetworkCallFailure : "+t, Toast.LENGTH_LONG).show();
            }
        });
    }


}
