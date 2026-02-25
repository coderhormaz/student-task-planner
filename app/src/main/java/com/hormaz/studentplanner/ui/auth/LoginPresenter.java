package com.hormaz.studentplanner.ui.auth;

import android.util.Patterns;

import com.hormaz.studentplanner.data.remote.dto.LoginResponse;
import com.hormaz.studentplanner.data.repository.AuthRepository;
import com.hormaz.studentplanner.ui.common.BasePresenter;

public class LoginPresenter extends BasePresenter<LoginContract.View>
        implements LoginContract.Presenter {

    private final AuthRepository authRepository;

    public LoginPresenter(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public void login(String email, String password) {
        if (!isViewAttached()) return;

        // Validate
        if (email == null || email.trim().isEmpty()) {
            getView().showEmailError("Please enter your email");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            getView().showEmailError("Please enter a valid email");
            return;
        }
        if (password == null || password.isEmpty()) {
            getView().showPasswordError("Please enter your password");
            return;
        }
        if (password.length() < 6) {
            getView().showPasswordError("Password must be at least 6 characters");
            return;
        }

        getView().showLoading();

        authRepository.login(email.trim(), password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(LoginResponse response) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().onLoginSuccess();
                }
            }

            @Override
            public void onError(String message) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().showError(message);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        detachView();
    }
}
