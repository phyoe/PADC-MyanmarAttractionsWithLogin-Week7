package xyz.aungpyaephyo.padc.myanmarattractions.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import xyz.aungpyaephyo.padc.myanmarattractions.MyanmarAttractionsApp;
import xyz.aungpyaephyo.padc.myanmarattractions.R;
import xyz.aungpyaephyo.padc.myanmarattractions.adapters.CountryListAdapter;
import xyz.aungpyaephyo.padc.myanmarattractions.controllers.ControllerAccountControl;
import xyz.aungpyaephyo.padc.myanmarattractions.events.DataEvent;
import xyz.aungpyaephyo.padc.myanmarattractions.utils.MyanmarAttractionsConstants;
import xyz.aungpyaephyo.padc.myanmarattractions.views.PasswordVisibilityListener;

/**
 * Created by Phyoe Khant on 7/19/2016.
 */
public class LoginFragment extends Fragment {

    @BindView(R.id.lbl_login_title)
    TextView lblLoginTitle;

    @BindView(R.id.et_login_email)
    EditText etLoginEmail;

    @BindView(R.id.et_login_password)
    EditText etLoginPassword;

    @BindView(R.id.link_forget_password)
    TextView tvLinkForgetPassword;

    @BindView(R.id.link_register)
    TextView tvLinkRegister;

    private ControllerAccountControl mControllerAccountControl;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mControllerAccountControl = (ControllerAccountControl) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Start Here " , "login");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);

        lblLoginTitle.setText(Html.fromHtml(getString(R.string.lbl_login_title)));
        etLoginPassword.setOnTouchListener(new PasswordVisibilityListener());
        Log.d("Start Here " , "Login");

        SpannableString spanString = new SpannableString(getString(R.string.link_forget_password));
        spanString.setSpan(new UnderlineSpan(), 0, tvLinkForgetPassword.length(), 0);
        tvLinkForgetPassword.setText(spanString);

        spanString = new SpannableString(getString(R.string.link_register));
        spanString.setSpan(new UnderlineSpan(), 0, tvLinkRegister.length(), 0);
        tvLinkRegister.setText(spanString);

        return rootView;
    }

    @OnClick(R.id.btn_login)
    public void onTapLogin(Button btnLogin) {
        String email = etLoginEmail.getText().toString();
        String password = etLoginPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            //One of the required data is empty
            if (TextUtils.isEmpty(email)) {
                etLoginEmail.setError(getString(R.string.error_missing_email));
            }

            if (TextUtils.isEmpty(password)) {
                etLoginPassword.setError(getString(R.string.error_missing_password));
            }
        } else if (!isEmailValid(email)) {
            //Email address is not in the right format.
            etLoginEmail.setError(getString(R.string.error_email_is_not_valid));
        } else {
            //Checking on client side is done here.
            mControllerAccountControl.onLogin(email, password);
        }
    }

    public boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile(MyanmarAttractionsConstants.EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    @OnClick(R.id.link_forget_password)
    public void onTapForgetPassword(){
        Toast.makeText(getContext(), "Forget password", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.link_register)
    public void onTapRegister(){
        Toast.makeText(getContext(), "Register", Toast.LENGTH_SHORT).show();
    }

    //Success login
    public void onEventMainThread(DataEvent.RefreshUserLoginStatusEvent event) {

    }

    /**
     * register event into acceptable fragment
     */
    @Override
    public void onStart() {
        super.onStart();
        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    /**
     * unregister event
     * when user inactive, no need to display event
     */
    @Override
    public void onStop() {
        super.onStop();
        EventBus eventBus = EventBus.getDefault();
        eventBus.unregister(this);
    }
}
