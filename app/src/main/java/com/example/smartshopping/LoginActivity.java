package com.example.smartshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class LoginActivity extends AppCompatActivity {

    TextView tabItemName;

    EditText phone;
    EditText otp;
    TextView message;
    CardView cardView;
    TextView cardText;
    ProgressBar cardProgress;

    PhoneAuthOptions options;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private String mVerificationId = "";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        tabItemName = findViewById(R.id.tab_item_name);

        phone = findViewById(R.id.phone_number);
        otp = findViewById(R.id.otp);
        message = findViewById(R.id.message);
        cardView = findViewById(R.id.card_view);
        cardText = findViewById(R.id.card_text);
        cardProgress = findViewById(R.id.card_progress);

        TabLayout loginTab = findViewById(R.id.login_tab);

        loginTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) {
                    tabItemName.setText("ShopKeeper");
                } else if(tab.getPosition() == 1) {
                    tabItemName.setText("Customer");
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cardText.setText(getString(R.string.generate_otp));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = phone.getText().toString();

                if(phoneNumber.length() != 10){
                    message.setText(" Enter a Valid Phone Number");
                    return;
                }

                if(cardText.getVisibility() == View.INVISIBLE) {
                    return;
                }

                if(getString(R.string.generate_otp).equals(cardText.getText().toString())){

                    options = PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber("+91" + phoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(LoginActivity.this)
                            .setCallbacks(mCallbacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);


                    message.setText(" OTP Sent Successfully");
                    cardText.setText(getString(R.string.submit));
                    cardText.setVisibility(View.INVISIBLE);
                    cardProgress.setVisibility(View.VISIBLE);

                    phone.clearFocus();
                    otp.requestFocus();

                } else {
                    if(otp.length() != 6){
                        message.setText(" Invalid OTP");
                        return;
                    }

                    if(phone.length() != 10){
                        message.setText(" Invalid Phone Number");
                        return;
                    }

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                cardProgress.setVisibility(View.INVISIBLE);
                cardText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                String code = credential.getSmsCode();
                if(code != null){
                    otp.setText(code);
                }
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    message.setText(" Invalid Phone Number");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    message.setText(" Sorry, OTP Limit Exceeded. Come Back Tomorrow");
                } else {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void submitPhoneNumber() {

        cardText.setVisibility(View.INVISIBLE);
        cardProgress.setVisibility(View.VISIBLE);

        SharedPreferences loginPreferences = getSharedPreferences(getString(R.string.login_preference), MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = loginPreferences.edit();
        editor.putString(getString(R.string.phone_preference), phone.getText().toString());
        editor.putString("user", tabItemName.getText().toString());
        editor.apply();
        Intent intent = new Intent(LoginActivity.this, ShopDetailsActivity.class);
        intent.putExtra("login", "login");
        startActivity(intent);
        finish();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            submitPhoneNumber();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message.setText(" Invalid Verification Code");
                            }
                        }
                    }
                });
    }

}