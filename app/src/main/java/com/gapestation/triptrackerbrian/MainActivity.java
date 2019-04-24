package com.gapestation.triptrackerbrian;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class MainActivity extends AppCompatActivity {
    EditText mEmailEditText;
    EditText mPasswordEditText;
    EditText mNameEditText;
    Button mLoginButton;
    TextView mSignUpTextView;
    Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Backendless.initApp( this,
                getString(R.string.APP_ID),
                getString(R.string.API_KEY));

        mEmailEditText = (EditText)findViewById(R.id.enter_email);
        mPasswordEditText = (EditText)findViewById(R.id.enter_password);
        mNameEditText = (EditText)findViewById(R.id.enter_name);
        mLoginButton = (Button)findViewById(R.id.login_button);
        mSignUpTextView = (TextView)findViewById(R.id.sign_up_text);
        mSignUpButton = (Button)findViewById(R.id.sign_up_button);
        final BackendlessUser user = new BackendlessUser();
        final String TAG = MainActivity.class.getName();

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSignUpTextView.getText() == getString(R.string.sign_up_text)) {
                    mNameEditText.setVisibility(View.VISIBLE);
                    mSignUpButton.setVisibility(View.VISIBLE);
                    mLoginButton.setVisibility(View.GONE);
                    mSignUpTextView.setText("Cancel Sign Up");
                }
                else{
                    mNameEditText.setVisibility(View.GONE);
                    mSignUpButton.setVisibility(View.GONE);
                    mLoginButton.setVisibility(View.VISIBLE);
                    mSignUpTextView.setText(R.string.sign_up_text);
                }
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String name = mNameEditText.getText().toString();

                userEmail = userEmail.trim();
                password = password.trim();
                name = name.trim();

                if (!userEmail.isEmpty() &&!password.isEmpty() && !name.isEmpty()) {
                    user.setEmail(userEmail);
                    user.setPassword(password);
                    user.setProperty("name", name);

                    Backendless.UserService.register(user,
                            new AsyncCallback<BackendlessUser>() {
                                @Override
                                public void handleResponse( BackendlessUser backendlessUser ) {
                                    Log.i(TAG, "Registration successful for " +
                                            backendlessUser.getEmail());
                                }
                                @Override
                                public void handleFault( BackendlessFault fault ) {
                                    Log.i(TAG, "Registration failed: " + fault.getMessage());
                                }
                            } );

                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(R.string.empty_field_signup_error);
                    builder.setTitle(R.string.authentication_error_title);
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}
