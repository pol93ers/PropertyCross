package mdpa.lasalle.propertycross.ui.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;


public class SessionFragment extends FragmentBase {

    private TextView fbSessionText, loginSessionText, signupSessionText;

    @NonNull
    @Override
    public ID getComponent() {
        return ID.SessionFragment;
    }

    public static SessionFragment newInstance() {
        return new SessionFragment();
    }

    private OnLoginFragmentListener loginFragmentListener;
    public interface OnLoginFragmentListener{
        void onLoginFragment();
    }

    private OnSignUpFragmentListener signUpFragmentListener;
    public interface OnSignUpFragmentListener{
        void onSignUpFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loginFragmentListener = onAttachGetListener(OnLoginFragmentListener.class, context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_session, container, false);

        fbSessionText = (TextView) root.findViewById(R.id.fbSessionText);
        loginSessionText = (TextView) root.findViewById(R.id.loginSessionText);
        signupSessionText = (TextView) root.findViewById(R.id.signupSessionText);

        setListeners();

        return root;
    }

    private void setListeners(){
        fbSessionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        loginSessionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFragmentListener.onLoginFragment();
            }
        });

        signupSessionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpFragmentListener.onSignUpFragment();
            }
        });
    }


}
