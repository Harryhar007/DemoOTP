package com.simmi.demo;
import static android.widget.Toast.LENGTH_SHORT;
import static androidx.constraintlayout.widget.Constraints.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class verify extends AppCompatActivity {
    EditText ip1,ip2,ip3,ip4,ip5,ip6;
    String verificationId,name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        final String mobile = getIntent().getStringExtra("mobile");
        verificationId = getIntent().getStringExtra("verificationId");
        name  = getIntent().getStringExtra("name");
        final Button submit = findViewById(R.id.submitotp);
        TextView textView = findViewById(R.id.textmobilenumber);
        textView.setText(String.format("+91-%s",mobile));
        final ProgressBar progressBar = findViewById(R.id.otpverify);
        ip1 = findViewById(R.id.inputotp1);
        ip2 = findViewById(R.id.inputotp2);
        ip3 = findViewById(R.id.inputotp3);
        ip4 = findViewById(R.id.inputotp4);
        ip5 = findViewById(R.id.inputotp5);
        ip6 = findViewById(R.id.inputotp6);



        submit.setOnClickListener(v -> {
            if(!ip1.getText().toString().trim().isEmpty() && !ip2.getText().toString().trim().isEmpty()&& !ip3.getText().toString().trim().isEmpty()&& !ip4.getText().toString().trim().isEmpty()&& !ip5.getText().toString().trim().isEmpty()&& !ip6.getText().toString().trim().isEmpty()){
                String otp = ip1.getText().toString()+ ip2.getText().toString()+ ip3.getText().toString()+ ip4.getText().toString()+ ip5.getText().toString()+ ip6.getText().toString();
                if(verificationId!=null && otp!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.INVISIBLE);

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId,otp);
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    submit.setVisibility(View.VISIBLE);
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(verify.this, homepage.class);
                                        intent.putExtra("cred",phoneAuthCredential);
                                        intent.putExtra("name",name);
                                        intent.putExtra("phone",mobile);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        Toast.makeText(verify.this,"OTP verifed",Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(verify.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(verify.this,"Check Internet Connection",Toast.LENGTH_SHORT).show();
                }



               // Toast.makeText(verify.this,"OTP verify",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(verify.this,"Please enter all digits",Toast.LENGTH_SHORT).show();
            }
        });


        numberotpmove();
        findViewById(R.id.resendotp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth Auth = FirebaseAuth.getInstance();
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
                        Toast.makeText(verify.this,"Verification Success",LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        // This callback is invoked in an invalid request for verification is made,
                        // for instance if the the phone number format is not valid.
                        Log.w(TAG,  e.toString());
                        Toast.makeText(verify.this,"Verification Failed!", LENGTH_SHORT).show();


                        // Show a message and update the UI
                    }

                    @Override
                    public void onCodeSent(@NonNull String newverificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        // The SMS verification code has been sent to the provided phone number, we
                        // now need to ask the user to enter the code and then construct a credential
                        // by combining the code with a verification ID.
//                        Log.d(TAG, "onCodeSent:" + verificationId);
                        verificationId = newverificationId;
                        Toast.makeText(verify.this,"OTP sent successfully", LENGTH_SHORT).show();

                    }
                };

                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(Auth).setPhoneNumber("+91"+mobile)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)// Timeout and unit
                        .setActivity(verify.this)
                        .setCallbacks(callbacks)
                        .build();
                PhoneAuthProvider.verifyPhoneNumber(options);

            }
        });
    }
    private void numberotpmove(){
        ip1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    ip2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ip2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    ip3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ip3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    ip4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ip4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    ip5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ip5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    ip6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}