package com.hormaz.studentplanner.ui.auth;

import com.hormaz.studentplanner.ui.common.BaseView;

public interface SignupContract {

    interface View extends BaseView {
        void onSignupSuccess();
        void showNameError(String error);
        void showEmailError(String error);
        void showPasswordError(String error);
    }

    interface Presenter {
        void signup(String name, String email, String password, String confirmPassword);
        void onDestroy();
    }
}
