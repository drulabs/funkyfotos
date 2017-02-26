package org.drulabs.vividvidhi.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.drulabs.vividvidhi.PresenterCreator;
import org.drulabs.vividvidhi.R;
import org.drulabs.vividvidhi.ui.NotificationToast;
import org.drulabs.vividvidhi.utils.Store;

public class LoginActivity extends AppCompatActivity implements LoginContract.View, View
        .OnClickListener {

    // UI references.
    ImageView imgLogin;
    Button signInButton;
    View requestAccess;
    View progressBar;
    View loginViewHolder;
    EditText etUserName, etPassword;

    //Presenter reference
    LoginContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeUIElements();

        PresenterCreator.createLoginPresenter(this, this);

        // User has already signed in
        if (Store.getInstance(this).getMyName() != null) {
            presenter.navigateToHome();
            return;
        }

        presenter.start();
        presenter.fetchWelcomeImageIn(imgLogin);
        presenter.requestSDCardPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.cancelLogin();
        presenter.destroy();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
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
                presenter.handleLogin(username, pwd);
                break;
            case R.id.request_access:
//                Builder.getInstance().setButton(signInButton);
                presenter.handleRequestAccess();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onUserNameError(String message) {
        //showSnackbar(message);
        etUserName.setError(message);
    }

    @Override
    public void onPasswordError(String message) {
        //showSnackbar(message);
        etPassword.setError(message);
    }

    @Override
    public void onLoginFailure() {
        showSnackbar(getString(R.string.invalid_credentials_msg));
    }

    @Override
    public void onLoginSuccess() {
        showSnackbar(getString(R.string.txt_login_successful));
        presenter.navigateToHome();
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