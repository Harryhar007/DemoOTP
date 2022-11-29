package com.simmi.demo;

import static android.widget.Toast.LENGTH_SHORT;
import static androidx.constraintlayout.widget.Constraints.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class register extends AppCompatActivity {
    EditText mobile,name;
    Button btnsend;
    FirebaseAuth Auth = FirebaseAuth.getInstance();
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mobile = findViewById(R.id.mobnum);
        name = findViewById(R.id.name);
        btnsend = findViewById(R.id.sendotp);
        ProgressBar progressBar = findViewById(R.id.otpsent);
        PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                progressBar.setVisibility(View.GONE);
                btnsend.setVisibility(View.VISIBLE);
                Toast.makeText(register.this,"Verification Success",LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG,  e.toString());
                progressBar.setVisibility(View.GONE);
                btnsend.setVisibility(View.VISIBLE);
                Toast.makeText(register.this,"Verification Failed!", LENGTH_SHORT).show();


                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                progressBar.setVisibility(View.GONE);
                btnsend.setVisibility(View.VISIBLE);
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                Intent intent = new Intent(register.this, verify.class);
                intent.putExtra("mobile", mobile.getText().toString());
                intent.putExtra("verificationId",verificationId);
                intent.putExtra("name",name.getText().toString());
                startActivity(intent);
            }
        };
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mobile.getText().toString().trim().isEmpty()) {
                    if ((mobile.getText().toString().trim()).length() == 10) {

                        progressBar.setVisibility(View.VISIBLE);
                        btnsend.setVisibility(View.INVISIBLE);

                        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(Auth).setPhoneNumber("+91"+mobile.getText().toString())       // Phone number to verify
                                        .setTimeout(60L, TimeUnit.SECONDS)// Timeout and unit
                                        .setActivity(register.this)
                                        .setCallbacks(callbacks)
                                        .build();
                        PhoneAuthProvider.verifyPhoneNumber(options);


                    }
                    else {
                        Toast.makeText(register.this,"Please enter correct number", LENGTH_SHORT).show() ;
                    }
                }
                else {
                    Toast.makeText(register.this,"Enter Mobile number", LENGTH_SHORT).show();
                }


            }

        });
    }
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        Auth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//
//                            FirebaseUser user = task.getResult().getUser();
//                            // Update UI
//                        } else {
//                            // Sign in failed, display a message and update the UI
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                // The verification code entered was invalid
//                            }
//                        }
//                    }
//                });
//    }
}