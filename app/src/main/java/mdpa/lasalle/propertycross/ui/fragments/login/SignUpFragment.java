package mdpa.lasalle.propertycross.ui.fragments.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;
import mdpa.lasalle.propertycross.http.project.Requests;
import mdpa.lasalle.propertycross.http.project.response.Response;
import mdpa.lasalle.propertycross.http.project.response.ResponseError;
import mdpa.lasalle.propertycross.http.project.response.ResponseLogin;
import mdpa.lasalle.propertycross.util.CircleTransform;
import mdpa.lasalle.propertycross.util.ImageChooser;


public class SignUpFragment extends FragmentBase implements ViewPager.OnPageChangeListener{

    private static final int SELECT_PICTURE = 1;

    private ViewPager signupPager;
    private ImageView signupProfileImage;

    private String username, password;

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
        void onSignUp(String username, String userID, String authToken);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        signUpListener = onAttachGetListener(OnSignUpListener.class, context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        //signupPager.onInterceptTouchEvent(new MotionEvent());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PICTURE:
                if(resultCode == -1) {
                    Uri imageUri = ImageChooser.getInstance().getImage(data);

                    CropImage.activity(imageUri)
                            .start(getContext(), this);
                }
                break;
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                if(resultCode == -1) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri croppedImageUri = result.getUri();
                    Glide.with(getContext())
                            .load(croppedImageUri)
                            .placeholder(R.drawable.default_user)
                            .transform(new CircleTransform(getContext()))
                            .crossFade()
                            .into(signupProfileImage);
                }
                break;
        }
    }

    public class AdapterPagerSignUp extends PagerAdapter {

        private Context context;

        public AdapterPagerSignUp(Context context) {
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
                    signupProfileImage = (ImageView) layout.findViewById(R.id.signUpImage);
                    ImageView addImage = (ImageView) layout.findViewById(R.id.addSignUpImage);
                    final EditText emailEditText = (EditText) layout.findViewById(R.id.emailSignUpEditText);
                    final EditText nameEditText = (EditText) layout.findViewById(R.id.nameSignUpEditText);
                    final EditText surnameEditText = (EditText) layout.findViewById(R.id.surnameSignUpEditText);
                    final Switch notificationsSwitch = (Switch) layout.findViewById(R.id.notificationsSignUpSwitch);
                    Button signUpButton = (Button) layout.findViewById(R.id.signUpButton);

                    addImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
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

                    signUpButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(!isEmpty(emailEditText) && isEmpty(nameEditText) && !isEmpty(surnameEditText)){
                                String email = emailEditText.getText().toString();
                                if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                    String name = nameEditText.getText().toString();
                                    String surname = surnameEditText.getText().toString();
                                    boolean notificationsBool = notificationsSwitch.isChecked();
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

        }
    }

    @Override
    public void onHttpBroadcastSuccess(String requestId, Response response) {
        super.onHttpBroadcastSuccess(requestId, response);
        if (requestId.equals(Requests.Values.POST_SIGN_UP.id)) {
            String userID = ((ResponseLogin)response).getUserId();
            String authToken = ((ResponseLogin)response).getAuthToken();
            signUpListener.onSignUp(username, userID, authToken);
        }
    }
}
