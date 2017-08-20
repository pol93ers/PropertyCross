package mdpa.lasalle.propertycross.ui.fragments.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;

import mdpa.lasalle.propertycross.ApplicationPropertyCross;
import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;
import mdpa.lasalle.propertycross.http.Http;
import mdpa.lasalle.propertycross.http.project.Requests;
import mdpa.lasalle.propertycross.http.project.response.Response;
import mdpa.lasalle.propertycross.http.project.response.ResponseError;
import mdpa.lasalle.propertycross.ui.activities.MainActivity;
import mdpa.lasalle.propertycross.util.CircleTransform;
import mdpa.lasalle.propertycross.util.ImageChooser;

public class ProfileFragment extends FragmentBase{

    private EditText nameProfileEditText, surnameEditText, emailEditText, passwordEditText;
    private TextView usernameTextView;
    private ImageView profileImageView, addProfileImageView;
    private RelativeLayout noSessionProfileLayout;
    private ScrollView profileLayout;
    private Switch receiveNotificationsSwitch;
    private Button logoutButton, removeUserButton, sessionProfileButton;

    private static final int SELECT_PICTURE = 1;

    private OnLoginActivityListener loginActivityListener;
    public interface OnLoginActivityListener {
        void onLoginActivity();
    }

    @NonNull
    @Override
    public ID getComponent() {
        return ID.ProfileFragment;
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loginActivityListener = onAttachGetListener(OnLoginActivityListener.class, context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHttpManager().receiverRegister(getContext(), Requests.Values.GET_USER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.profile);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_main_profile, container, false);

        usernameTextView = (TextView) root.findViewById(R.id.usernameProfileText);
        nameProfileEditText = (EditText) root.findViewById(R.id.nameProfileEditText);
        surnameEditText = (EditText) root.findViewById(R.id.surnameProfileEditText);
        emailEditText = (EditText) root.findViewById(R.id.emailProfileEditText);
        passwordEditText = (EditText) root.findViewById(R.id.passwordProfileEditText);
        receiveNotificationsSwitch = (Switch) root.findViewById(R.id.notificationsSwitch);
        logoutButton = (Button) root.findViewById(R.id.logoutButton);
        removeUserButton = (Button) root.findViewById(R.id.removeUserButton);
        profileImageView = (ImageView) root.findViewById(R.id.profileImageView);
        addProfileImageView = (ImageView) root.findViewById(R.id.addProfileImageView);
        noSessionProfileLayout = (RelativeLayout) root.findViewById(R.id.noSessionProfileLayout);
        profileLayout = (ScrollView) root.findViewById(R.id.profileLayout);
        sessionProfileButton = (Button) root.findViewById(R.id.sessionProfileButton);

        setListeners();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(ApplicationPropertyCross.getInstance().preferences().getLoginApiKey() != null){
            noSessionProfileLayout.setVisibility(View.GONE);
            profileLayout.setVisibility(View.VISIBLE);

            getHttpManager().callStart(
                    Http.RequestType.GET,
                    Requests.Values.GET_USER,
                    String.valueOf(ApplicationPropertyCross.getInstance().preferences().getUserId()),
                    null,
                    ApplicationPropertyCross.getInstance().preferences().getLoginApiKey(),
                    null
            );
        }else{
            noSessionProfileLayout.setVisibility(View.VISIBLE);
            profileLayout.setVisibility(View.GONE);
        }
    }

    private void setListeners(){

        addProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getImage;
                try {
                    getImage = ImageChooser.getInstance().getIntent(
                            getContext().getPackageManager(),
                            getString(R.string.select_photo)
                    );
                } catch (IOException e) {
                    getImage = ImageChooser.getGalleryIntent();
                }

                startActivityForResult(getImage, SELECT_PICTURE);
            }
        });

        sessionProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginActivityListener.onLoginActivity();
            }
        });

        receiveNotificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicationPropertyCross.getInstance().preferences().removeLogin();
                ApplicationPropertyCross.getInstance().preferences().removeUser();
            }
        });

        removeUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PICTURE:
                if(resultCode == -1) {
                    Uri imageUri = ImageChooser.getInstance().getImage(data);
                    CropImage.activity(imageUri).start(getContext(), this);
                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if(resultCode == -1) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri croppedImageUri = result.getUri();
                    Glide.with(getContext())
                            .load(croppedImageUri)
                            .transform(new CircleTransform(getContext()))
                            .crossFade()
                            .placeholder(R.drawable.default_user)
                            .into(profileImageView);
                }
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getHttpManager().receiverUnregister(getContext());
    }

    @Override
    public void onHttpBroadcastError(String requestId, ResponseError response) {
        super.onHttpBroadcastError(requestId, response);
        if (requestId.equals(Requests.Values.GET_USER.id)) {

        }
    }

    @Override
    public void onHttpBroadcastSuccess(String requestId, Response response) {
        super.onHttpBroadcastSuccess(requestId, response);
        if (requestId.equals(Requests.Values.GET_USER.id)) {

        }
    }
}
