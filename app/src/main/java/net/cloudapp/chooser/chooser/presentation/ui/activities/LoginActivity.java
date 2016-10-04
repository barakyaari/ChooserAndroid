package net.cloudapp.chooser.chooser.presentation.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.facebook.login.widget.LoginButton;

import net.cloudapp.chooser.chooser.R;
import net.cloudapp.chooser.chooser.presentation.presenters.LoginPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends Activity implements LoginPresenter.View {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        LoginButton fbLoginButton;
    }

    @Override
    public void onLogin() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError(String message) {

    }
}
