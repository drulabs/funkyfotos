package com.appfest.funkyfotos.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.appfest.funkyfotos.PresenterCreator;
import com.appfest.funkyfotos.signin.SignInActivity;
import com.appfest.funkyfotos.utils.Store;
import com.google.firebase.auth.FirebaseAuth;

import com.appfest.funkyfotos.R;

import com.appfest.funkyfotos.ui.NotificationToast;

public class LoginActivity extends AppCompatActivity implements LoginContract.View, View
        .OnClickListener {

    // UI references.
    ImageView imgLogin;
    Button signInButton;
    View requestAccess;
    View progressBar;
    View loginViewHolder;
    EditText etUserName, etPassword;

    private ProgressDialog dialog = null;

    //Presenter reference
    LoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Store.getInstance(this).getUserEmail() == null || FirebaseAuth.getInstance()
                .getCurrentUser() == null) {
            Intent signInIntent = new Intent(this, SignInActivity.class);
            startActivity(signInIntent);
            finish();
            return;
        }



        initializeUIElements();

        PresenterCreator.createLoginPresenter(this, this);

        // User has already signed in
        if (Store.getInstance(this).getMyName() != null && Store.getInstance(this).getPassword()
                != null) {

//            String savedUsername = Store.getInstance(this).getUsername();
//            String savedPassword = Store.getInstance(this).getPassword();
//
//            dialog = new ProgressDialog(this);
//            dialog.setMessage(getString(R.string.logging_in));
//            dialog.show();
//
//            mPresenter.handleLogin(savedUsername, savedPassword);
            mPresenter.navigateToHome();
            return;
        }

        mPresenter.start();
        mPresenter.fetchWelcomeImageIn(imgLogin);
        mPresenter.requestSDCardPermission();

        // Login shortcut
        String username = getIntent().getStringExtra("userName");
        String pwd = getIntent().getStringExtra("password");
        //mPresenter.handleLogin(username, pwd);
        etUserName.setText(username);
        etPassword.setText(pwd);
        signInButton.setVisibility(View.GONE);
        signInButton.performClick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.cancelLogin();
            mPresenter.destroy();
        }
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    private void initializeUIElements() {
        loginViewHolder = findViewById(R.id.login_view_holder);
        imgLogin = (ImageView) findViewById(R.id.login_image);
        signInButton = (Button) findViewById(R.id.email_sign_in_button);
        requestAccess = findViewById(R.id.request_access);
        progressBar = findViewById(R.id.login_progress);
        etUserName = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        signInButton.setOnClickListener(this);
        requestAccess.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.email_sign_in_button:
                String username = etUserName.getText().toString();
                String pwd = etPassword.getText().toString();
                mPresenter.handleLogin(username, pwd);
                break;
            case R.id.request_access:
//                Builder.getInstance().setButton(signInButton);
                mPresenter.handleRequestAccess();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void destroyWithMessage(String message) {
        if (message != null && !message.isEmpty()) {
            NotificationToast.showToast(LoginActivity.this, message);
        }
        LoginActivity.this.finish();
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onUserNameError(String message) {
        //showSnackbar(message);
        etUserName.setError(message);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onPasswordError(String message) {
        //showSnackbar(message);
        etPassword.setError(message);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onLoginFailure() {
        showSnackbar(getString(R.string.invalid_credentials_msg));
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onLoginSuccess() {
        showSnackbar(getString(R.string.txt_login_successful));
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        mPresenter.navigateToHome();
    }

    @Override
    public void onRequestTimeout() {
        showSnackbar(getString(R.string.request_timedout_msg));
    }

    @Override
    public void clearUsername() {
        etUserName.setText("");
    }

    @Override
    public void clearPassword() {
        etPassword.setText("");
    }

    private void showSnackbar(String msg) {
        Snackbar.make(loginViewHolder, msg, Snackbar.LENGTH_SHORT).show();
    }
}