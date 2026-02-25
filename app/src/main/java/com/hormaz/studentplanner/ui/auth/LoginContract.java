package com.hormaz.studentplanner.ui.auth;

import com.hormaz.studentplanner.ui.common.BaseView;

public interface LoginContract {

    interface View extends BaseView {
        void onLoginSuccess();
        void showEmailError(String error);
        void showPasswordError(String error);
    }

    interface Presenter {
        void login(String email, String password);
        void onDestroy();
    }
}
