package mdpa.lasalle.propertycross.ui.fragments.login;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;
import mdpa.lasalle.propertycross.http.Http;
import mdpa.lasalle.propertycross.http.project.Requests;
import mdpa.lasalle.propertycross.http.project.request.RequestLogin;
import mdpa.lasalle.propertycross.http.project.request.RequestSignUp;
import mdpa.lasalle.propertycross.http.project.response.Response;
import mdpa.lasalle.propertycross.http.project.response.ResponseError;
import mdpa.lasalle.propertycross.http.project.response.ResponseGeneric;
import mdpa.lasalle.propertycross.http.project.response.ResponseLogin;

public class SignUpFragment extends FragmentBase implements ViewPager.OnPageChangeListener{

    private ViewPager signupPager;

    private String username, password, email;

    @NonNull
    @Override
    public ID getComponent() {
        return ID.SignUpFragment;
    }

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    private OnSignUpListener signUpListener;
    public interface OnSignUpListener {
        void onSignUp(String userID, String authToken);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        signUpListener = onAttachGetListener(OnSignUpListener.class, context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHttpManager().receiverRegister(getContext(), Requests.Values.POST_SIGN_UP);
        getHttpManager().receiverRegister(getContext(), Requests.Values.POST_LOGIN);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getHttpManager().receiverUnregister(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_signup, container, false);

        signupPager = (ViewPager) root.findViewById(R.id.signupViewPager);
        signupPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        signupPager.addOnPageChangeListener(this);
        AdapterPagerSignUp pagerRegisterAdapter = new AdapterPagerSignUp(getContext());
        signupPager.setAdapter(pagerRegisterAdapter);

        return root;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class AdapterPagerSignUp extends PagerAdapter {

        private Context context;

        private AdapterPagerSignUp(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(context);
            ViewGroup layout = null;
            switch (position){
                case 0:
                    layout = (ViewGroup) inflater.inflate(R.layout.fragment_signup_pager1, container, false);
                    final EditText usernameEditText = (EditText) layout.findViewById(R.id.usernameSignUpEditText);
                    final EditText passwordEditText = (EditText) layout.findViewById(R.id.passwordSignUpEditText);
                    final EditText repeatPasswordEditText = (EditText) layout.findViewById(R.id.repeatPasswordSignUpEditText);
                    Button nextButton = (Button) layout.findViewById(R.id.nextSignUpButton);
                    nextButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!isEmpty(usernameEditText) && !isEmpty(passwordEditText) && !isEmpty(repeatPasswordEditText)){
                                username = usernameEditText.getText().toString();
                                password = passwordEditText.getText().toString();
                                String repeatPassword = repeatPasswordEditText.getText().toString();
                                if(password.equals(repeatPassword)){
                                    signupPager.setCurrentItem(1);
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle(R.string.error);
                                    builder.setMessage(R.string.no_same_passwords);
                                    builder.setPositiveButton(R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle(R.string.error);
                                builder.setMessage(R.string.empty_fields);
                                builder.setPositiveButton(R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                    break;
                case 1:
                    layout = (ViewGroup) inflater.inflate(R.layout.fragment_signup_pager2, container, false);
                    final EditText emailEditText = (EditText) layout.findViewById(R.id.emailSignUpEditText);
                    final EditText nameEditText = (EditText) layout.findViewById(R.id.nameSignUpEditText);
                    final EditText surnameEditText = (EditText) layout.findViewById(R.id.surnameSignUpEditText);
                    final Switch notificationsSwitch = (Switch) layout.findViewById(R.id.notificationsSignUpSwitch);
                    Button signUpButton = (Button) layout.findViewById(R.id.signUpButton);

                    signUpButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!isEmpty(emailEditText) && !isEmpty(nameEditText) && !isEmpty(surnameEditText)){
                                email = emailEditText.getText().toString();
                                if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                    String name = nameEditText.getText().toString();
                                    String surname = surnameEditText.getText().toString();
                                    boolean notificationsBool = notificationsSwitch.isChecked();
                                    getHttpManager().callStart(
                                            Http.RequestType.POST,
                                            Requests.Values.POST_SIGN_UP,
                                            null,
                                            new RequestSignUp(username, password, email, name, surname, notificationsBool),
                                            null,
                                            null,
                                            null
                                    );
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle(R.string.error);
                                    builder.setMessage(R.string.email_no_valid);
                                    builder.setPositiveButton(R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle(R.string.error);
                                builder.setMessage(R.string.empty_fields);
                                builder.setPositiveButton(R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });

                    break;
            }
            container.addView(layout);
            return layout;
        }

        @Override
        public void	destroyItem(ViewGroup container, int position, Object view){
            container.removeView((View) view);
        }

        private boolean isEmpty(EditText editText) {
            return editText.getText().toString().trim().length() == 0;
        }
    }

    @Override
    public void onHttpBroadcastError(String requestId, ResponseError response) {
        super.onHttpBroadcastError(requestId, response);
        if (requestId.equals(Requests.Values.POST_SIGN_UP.id)) {

        } else if (requestId.equals(Requests.Values.POST_LOGIN.id)) {

        }
    }

    @Override
    public void onHttpBroadcastSuccess(String requestId, Response response) {
        super.onHttpBroadcastSuccess(requestId, response);
        if (requestId.equals(Requests.Values.POST_SIGN_UP.id)) {
            getHttpManager().callStart(
                    Http.RequestType.POST,
                    Requests.Values.POST_LOGIN,
                    null,
                    new RequestLogin(email, password),
                    null,
                    null,
                    null
            );
        } else if (requestId.equals(Requests.Values.POST_LOGIN.id)) {
            String userID = ((ResponseLogin)response).getData().getUserId();
            String authToken = ((ResponseLogin)response).getData().getAuthToken();
            signUpListener.onSignUp(userID, authToken);
        }
    }
}
