package mdpa.lasalle.propertycross.ui.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;

public class LoginFragment extends FragmentBase {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;

    @NonNull
    @Override
    public ID getComponent() {
        return ID.LoginFragment;
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private OnLoginListener loginListener;
    public interface OnLoginListener{
        void onLogin(String username, String password);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loginListener = onAttachGetListener(OnLoginListener.class, context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        usernameEditText = (EditText) root.findViewById(R.id.userEditText);
        passwordEditText = (EditText) root.findViewById(R.id.passwordEditText);
        loginButton = (Button) root.findViewById(R.id.loginButton);

        setListeners();

        return root;
    }

    private void setListeners(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpty(usernameEditText) && !isEmpty(passwordEditText)){
                    loginListener.onLogin(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                }else{

                }
            }
        });
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }
}
