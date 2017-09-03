package mdpa.lasalle.propertycross.ui.fragments.login;

import android.app.AlertDialog;
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
import mdpa.lasalle.propertycross.http.Http;
import mdpa.lasalle.propertycross.http.project.Requests;
import mdpa.lasalle.propertycross.http.project.request.RequestLogin;
import mdpa.lasalle.propertycross.http.project.response.Response;
import mdpa.lasalle.propertycross.http.project.response.ResponseError;
import mdpa.lasalle.propertycross.http.project.response.ResponseLogin;

public class LoginFragment extends FragmentBase {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private String email, password;

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
        void onLogin(String userID, String authToken);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loginListener = onAttachGetListener(OnLoginListener.class, context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHttpManager().receiverRegister(getContext(), Requests.Values.POST_LOGIN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText = (EditText) root.findViewById(R.id.userEditText);
        passwordEditText = (EditText) root.findViewById(R.id.passwordEditText);
        loginButton = (Button) root.findViewById(R.id.loginButton);

        setListeners();

        return root;
    }

    private void setListeners(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpty(emailEditText) && !isEmpty(passwordEditText)){
                    email = emailEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    getHttpManager().callStart(
                            Http.RequestType.POST,
                            Requests.Values.POST_LOGIN,
                            null,
                            new RequestLogin(email, password),
                            null,
                            null,
                            null
                    );
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.error);
                    builder.setMessage(R.string.empty_fields);
                    builder.setPositiveButton(R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getHttpManager().receiverUnregister(getContext());
    }

    @Override
    public void onHttpBroadcastError(String requestId, ResponseError response) {
        super.onHttpBroadcastError(requestId, response);
        if (requestId.equals(Requests.Values.POST_LOGIN.id)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.error);
            builder.setMessage(R.string.dialog_title_error_login);
            builder.setPositiveButton(R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onHttpBroadcastSuccess(String requestId, Response response) {
        super.onHttpBroadcastSuccess(requestId, response);
        if (requestId.equals(Requests.Values.POST_LOGIN.id)) {
            String userID = ((ResponseLogin)response).getData().getUserId();
            String authToken = ((ResponseLogin)response).getData().getAuthToken();
            loginListener.onLogin(userID, authToken);
        }
    }
}
