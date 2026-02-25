package com.hormaz.studentplanner.ui.auth;

import android.util.Patterns;

import com.hormaz.studentplanner.data.remote.dto.LoginResponse;
import com.hormaz.studentplanner.data.repository.AuthRepository;
import com.hormaz.studentplanner.ui.common.BasePresenter;

public class SignupPresenter extends BasePresenter<SignupContract.View>
        implements SignupContract.Presenter {

    private final AuthRepository authRepository;

    public SignupPresenter(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public void signup(String name, String email, String password, String confirmPassword) {
        if (!isViewAttached()) return;

        // Validate
        if (name == null || name.trim().isEmpty()) {
            getView().showNameError("Please enter your name");
            return;
        }
        if (email == null || email.trim().isEmpty()) {
            getView().showEmailError("Please enter your email");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            getView().showEmailError("Please enter a valid email");
            return;
        }
        if (password == null || password.isEmpty()) {
            getView().showPasswordError("Please enter a password");
            return;
        }
        if (password.length() < 6) {
            getView().showPasswordError("Password must be at least 6 characters");
            return;
        }
        if (!password.equals(confirmPassword)) {
            getView().showPasswordError("Passwords don't match");
            return;
        }

        getView().showLoading();

        authRepository.signup(email.trim(), password, name.trim(),
                new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(LoginResponse response) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().onSignupSuccess();
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
