package com.hormaz.studentplanner.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hormaz.studentplanner.App;
import com.hormaz.studentplanner.R;
import com.hormaz.studentplanner.ui.common.MainActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private ProgressBar progressBar;
    private TextView tvSignup;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initPresenter();
        setupListeners();
    }

    private void initViews() {
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        tvSignup = findViewById(R.id.tvSignup);
    }

    private void initPresenter() {
        presenter = new LoginPresenter(App.getInstance().getAuthRepository());
        presenter.attachView(this);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> {
            tilEmail.setError(null);
            tilPassword.setError(null);
            String email = etEmail.getText() != null ? etEmail.getText().toString() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString() : "";
            presenter.login(email, password);
        });

        tvSignup.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void showEmailError(String error) {
        tilEmail.setError(error);
    }

    @Override
    public void showPasswordError(String error) {
        tilPassword.setError(error);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        btnLogin.setEnabled(true);
    }

    @Override
    public void showError(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
