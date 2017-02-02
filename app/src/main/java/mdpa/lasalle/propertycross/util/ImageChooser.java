package mdpa.lasalle.propertycross.util;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImageChooser {
    private static final String fileFolder = "PropertyCross";
    private static final String fileName = fileFolder;

    private Uri fileUri;

    private static ImageChooser ourInstance = new ImageChooser();
    public static ImageChooser getInstance() {
        return ourInstance;
    }
    private ImageChooser() {}

    private static List<Intent> getCameraIntents(PackageManager packageManager, Uri outputFileUri) {
        List<Intent> cameraIntents = new ArrayList<>();

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(
                    res.activityInfo.packageName, res.activityInfo.name
            ));
            intent.setPackage(res.activityInfo.packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        return cameraIntents;
    }

    public static Intent getGalleryIntent() {
        return new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
    }

    private static Intent getImageChooserIntent(
            PackageManager packageManager, String message,
            Intent galleryIntent, List<Intent> cameraIntents) {

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent chooserIntent = Intent.createChooser(galleryIntent, message);
            chooserIntent.putExtra(
                    Intent.EXTRA_INITIAL_INTENTS,
                    cameraIntents.toArray(new Parcelable[cameraIntents.size()])
            );
            return chooserIntent;
        } else {
            return galleryIntent;
        }

    }

    private static String getFileName() {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return  timeStamp + "_" + fileName + ".jpg";
    }

    public Intent getIntent(PackageManager packageManager, String message) throws IOException {
        File fileRoot = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + File.separator
                        + fileFolder
                        + File.separator
        );
        if (fileRoot.mkdirs() || fileRoot.isDirectory()) {
            File fileImage = new File(fileRoot, getFileName());
            if (fileImage.createNewFile()) {
                fileUri = Uri.fromFile(fileImage);

                return getImageChooserIntent(
                        packageManager,
                        message,
                        getGalleryIntent(),
                        getCameraIntents(packageManager, fileUri)
                );
            } else {
                throw new IOException("Could not create image file!");
            }
        } else {
            throw new IOException("Could not create image folder!");
        }
    }

    public Uri getImage(Intent resultIntent) {
        boolean isCamera;

        if (resultIntent == null) {
            isCamera = true;
        } else {
            if (resultIntent.getAction() == null) {
                isCamera = false;
            } else {
                isCamera = resultIntent.getAction().equals(MediaStore.ACTION_IMAGE_CAPTURE);
            }
        }

        Uri imageUri;
        if (isCamera) {
            imageUri = fileUri;
        } else {
            imageUri = resultIntent.getData();
        }

        return imageUri;
    }
}