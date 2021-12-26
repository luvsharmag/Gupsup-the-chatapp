package com.example.gupsup.signin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.gupsup.databinding.ActivityOtpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;

import java.util.concurrent.TimeUnit;

public class otpActivity extends AppCompatActivity {
ActivityOtpBinding binding;
FirebaseAuth auth;
String VerifyingID;
ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pd = new ProgressDialog(this);
        pd.setMessage("Sending OTP...");
        pd.setCancelable(false);
        pd.show();
        String Num = getIntent().getStringExtra("Number");
        binding.phonelabel.setText("Verify "+ Num);
        auth = FirebaseAuth.getInstance();
        PhoneAuthOptions phoneAuthOptions = new PhoneAuthOptions.Builder(auth)
                .setActivity(otpActivity.this)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setPhoneNumber(Num)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull  PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(@NonNull String verifyId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verifyId, forceResendingToken);
                        pd.dismiss();
                        VerifyingID = verifyId;
                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
        binding.otpview.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                Log.d("OnOTPcompleted",otp);
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerifyingID,otp);
                auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                         if(task.isSuccessful()){
                             startActivity(new Intent(otpActivity.this, setupprofileActivity.class));
                             finishAffinity();
                             Toast.makeText(otpActivity.this, "login Success", Toast.LENGTH_SHORT).show();
                         }else{
                             Toast.makeText(otpActivity.this, ""+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                         }
                    }
                });
            }
        });
    }
}