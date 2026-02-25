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

public class SignupActivity extends AppCompatActivity implements SignupContract.View {

    private TextInputLayout tilName, tilEmail, tilPassword, tilConfirmPassword;
    private TextInputEditText etName, etEmail, etPassword, etConfirmPassword;
    private MaterialButton btnSignup;
    private ProgressBar progressBar;
    private TextView tvLogin;
    private SignupPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();
        initPresenter();
        setupListeners();
    }

    private void initViews() {
        tilName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignup = findViewById(R.id.btnSignup);
        progressBar = findViewById(R.id.progressBar);
        tvLogin = findViewById(R.id.tvLogin);
    }

    private void initPresenter() {
        presenter = new SignupPresenter(App.getInstance().getAuthRepository());
        presenter.attachView(this);
    }

    private void setupListeners() {
        btnSignup.setOnClickListener(v -> {
            tilName.setError(null);
            tilEmail.setError(null);
            tilPassword.setError(null);
            tilConfirmPassword.setError(null);

            String name = etName.getText() != null ? etName.getText().toString() : "";
            String email = etEmail.getText() != null ? etEmail.getText().toString() : "";
            String password = etPassword.getText() != null ? etPassword.getText().toString() : "";
            String confirm = etConfirmPassword.getText() != null ?
                    etConfirmPassword.getText().toString() : "";

            presenter.signup(name, email, password, confirm);
        });

        tvLogin.setOnClickListener(v -> finish());
    }

    @Override
    public void onSignupSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void showNameError(String error) {
        tilName.setError(error);
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
        btnSignup.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        btnSignup.setEnabled(true);
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
