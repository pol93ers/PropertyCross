package mdpa.lasalle.propertycross.ui.fragments.login;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;
import mdpa.lasalle.propertycross.util.FacebookUserData;


public class SessionFragment extends FragmentBase implements
        FacebookCallback<LoginResult> {

    private TextView loginSessionText, signupSessionText;
    private CallbackManager fbCallbackManager;
    private LoginButton fbLoginButton;

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

        fbLoginButton = (LoginButton) root.findViewById(R.id.facebookLoginButton);
        loginSessionText = (TextView) root.findViewById(R.id.loginSessionText);
        signupSessionText = (TextView) root.findViewById(R.id.signupSessionText);

        fbLoginButton.setFragment(this);

        fbLoginButton.setReadPermissions(
                "public_profile", "email", "user_birthday", "user_friends"
        );
        fbLoginButton.registerCallback(fbCallbackManager, this);

        setListeners();

        return root;
    }

    private void setListeners(){
        fbLoginButton.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onSuccess(LoginResult loginResult) {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }

    private GraphRequest.GraphJSONObjectCallback graphRegisterCallback = new GraphRequest.GraphJSONObjectCallback() {
        @Override
        public void onCompleted(JSONObject object, GraphResponse response) {
            new LoadFacebookPictureBitmap2Base64(){
                @Override
                protected void onPostExecute(String base64image) {

                }
            }.execute(Profile.getCurrentProfile().getProfilePictureUri(500, 500));
        }
    };

    private GraphRequest.GraphJSONObjectCallback graphLoginCallback = new GraphRequest.GraphJSONObjectCallback() {
        @Override
        public void onCompleted(JSONObject object, GraphResponse response) {
            FacebookUserData fbData = new Gson().fromJson(
                    response.getRawResponse(), FacebookUserData.class);
        }
    };

    private static class LoadFacebookPictureBitmap2Base64 extends AsyncTask<Uri, Void, String> {
        @Override
        protected String doInBackground(Uri... params) {
            URL imageURL;
            try {
                imageURL = new URL(params[0].toString());
            } catch (MalformedURLException e) {
                Log.wtf("LoadBitmap2Base64", "Uri error! ", e);
                return null;
            }
            Log.i("LoadBitmap2Base64", "Loading " + imageURL);
            Bitmap bitmap = null;
            do {
                try {
                    bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                } catch (IOException e) {
                    Log.e("LoadBitmap2Base64", "", e);
                }
            } while (bitmap == null);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bb = bos.toByteArray();

            return Base64.encodeToString(bb, 0);
        }
    }



}
