package net.cloudapp.chooser.chooser.presentation.presenters;

import net.cloudapp.chooser.chooser.presentation.ui.BaseView;

public interface LoginPresenter extends BasePresenter {
    interface View extends BaseView{

        void onLogin();
    }
    void isLoggedIn();
}
