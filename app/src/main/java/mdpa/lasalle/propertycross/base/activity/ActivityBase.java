package mdpa.lasalle.propertycross.base.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;
import mdpa.lasalle.propertycross.http.HttpBroadcastManager;
import mdpa.lasalle.propertycross.http.project.Requests;
import mdpa.lasalle.propertycross.http.project.response.Response;
import mdpa.lasalle.propertycross.http.project.response.ResponseError;
import mdpa.lasalle.propertycross.util.Component;
import mdpa.lasalle.propertycross.util.FragmentHelper;
import mdpa.lasalle.propertycross.util.FragmentManagerUtils;

public abstract class ActivityBase extends AppCompatActivity  implements Component, HttpBroadcastManager.HttpBroadcastListener, FragmentBase.OnFragmentListener {
    private FragmentHelper fragmentHelper;
    private List<AlertDialog> dismissibleDialogs;
    private HttpBroadcastManager httpManager;
    private ArrayList<String> httpProgressDialogs;
    private ProgressDialog progressDialog;
    private boolean isResumedd = false;

    public ActivityBase() {
        httpManager = new HttpBroadcastManager(this);

        httpProgressDialogs = new ArrayList<>();
        dismissibleDialogs = new ArrayList<>();
    }

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentHelper = new FragmentHelper(getSupportFragmentManager());
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumedd = true;
    }


    @Override
    protected void onPause() {
        isResumedd = false;
        dismissDialogs();
        super.onPause();
    }

    @CallSuper @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public @NonNull
    View findViewById(@IdRes int id) {
        View view = super.findViewById(id);
        if (view != null) {
            return view;
        } else {
            throw new Resources.NotFoundException("Resource id '" + String.valueOf(id) + "' cannot be found.");
        }
    }

    public boolean isResumedd() {
        return isResumedd;
    }

    protected FragmentHelper getFragmentHelper() {
        return fragmentHelper;
    }

    protected void startActivityAndFinish(Intent intent) {
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();
        Log.e("Count",count+"");

        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStackImmediate();
        }

    }

    //region Fragments
    //----------------------------------------------------------------------------------------------

    protected  <F extends Fragment> F findFragment(Class <F> clazz, Component.ID id) {
        return FragmentManagerUtils.findFragment(getSupportFragmentManager(), clazz, id);
    }

    protected <F extends Fragment> boolean fragmentRemove(Class <F> clazz, ID id) {
        return FragmentManagerUtils.fragmentRemove(getSupportFragmentManager(), clazz, id);

    }

    protected boolean fragmentRemoveBackStack(ID id, boolean inclusive) {
        return FragmentManagerUtils.fragmentRemoveBackStack(getSupportFragmentManager(),id, inclusive);
    }

    protected <F extends Fragment & Component> void fragmentCall(F fragment) {
        FragmentManagerUtils.fragmentCall(getSupportFragmentManager(), fragment);
    }

    protected <F extends Fragment & Component> void fragmentReplace(
            @IdRes int containerResourceId,
            F fragment, boolean animate, boolean save
    ) {
        FragmentManagerUtils.fragmentReplace(getSupportFragmentManager(), containerResourceId,
                fragment, animate, save);
    }

    @Override
    public <F extends Fragment & Component> void onFinish(F fragment) {
        if (fragmentRemoveBackStack(fragment.getComponent(), true)) {
            Log.i(getComponent().toString(), "Finished Fragment from BackStack!");
        } else if (fragmentRemove(fragment.getClass(), fragment.getComponent())) {
            Log.i(getComponent().toString(), "Finished Fragment!");
        } else {
            Log.w(getComponent().toString(), "Could not finish Fragment!");
        }
    }

    //----------------------------------------------------------------------------------------------
    //endregion Fragments

    public HttpBroadcastManager getHttpManager() {
        return httpManager;
    }

    @Override
    public void onHttpCallStart(String requestId) {
        showHttpProgressDialog(requestId);
    }

    private AlertDialog errorDialog;
    @Override
    @CallSuper
    public void onHttpBroadcastError(String requestId, ResponseError response) {
        if (response != null) {
            switch (response.getHttpStatus()){
                case 400:
                case 401:
                case 403:
                case 404:
                case 409:
                case 412:
                    break;
                case 500:
                    addDismissibleDialog(showAlertDialog1bt(
                            getString(R.string.error), getString(R.string.message_error_500),
                            getString(android.R.string.ok), null
                    ));
                    break;
            }
        } else if (errorDialog == null || !errorDialog.isShowing()) {
            errorDialog = showAlertDialog1bt(
                    null, getString(R.string.dialog_connection_error),
                    getString(android.R.string.ok), null
            ); addDismissibleDialog(errorDialog);
        }
    }

    @Override
    public void onHttpBroadcastSuccess(String requestId, Response response) {

    }

    @Override
    public void onHttpCallEnd(String requestId) {
        cancelHttpProgressDialog(requestId);
    }

    @Override
    public boolean onCanExecuteBroadcastResponse(String requestId) {
        return this.isResumedd;
    }

    //----------------------------------------------------------------------------------------------
    //endregion HttpCall

    //region Dialogs
    //----------------------------------------------------------------------------------------------

    protected void showHttpProgressDialog(String val) {
        httpProgressDialogs.add(val);
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(getString(R.string.dialog_loading));
            progressDialog.show();
        }
    }

    protected void cancelHttpProgressDialog(String val) {
        httpProgressDialogs.remove(val);
        if (httpProgressDialogs.isEmpty()) {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    @Override
    public void onHttpShowProgressDialog(Requests.Values val) {
        showHttpProgressDialog(val.id);
    }

    @Override
    public void onHttpCancelProgressDialog(Requests.Values val) {
        cancelHttpProgressDialog(val.id);
    }

    protected AlertDialog showAlertDialog1bt(
            String title, String message,
            String positiveString, DialogInterface.OnClickListener positiveListener
    ) {
        AlertDialog aDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveString, positiveListener)
                .setCancelable(false)
                .create();
        aDialog.show();
        return aDialog;
    }

    protected AlertDialog showAlertDialog2bt(
            String title, String message,
            String positiveString, DialogInterface.OnClickListener positiveListener,
            String negativeString, DialogInterface.OnClickListener negativeListener
    ) {
        AlertDialog aDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveString, positiveListener)
                .setNegativeButton(negativeString, negativeListener)
                .setCancelable(false)
                .create();
        aDialog.show();
        return aDialog;
    }

    @Override
    public AlertDialog onShowAlertDialog1bt(
            String title, String message,
            String positiveString, DialogInterface.OnClickListener positiveListener
    ) {
        return showAlertDialog1bt(title, message, positiveString, positiveListener);
    }

    @Override
    public AlertDialog onShowAlertDialog2bt(
            String title, String message,
            String positiveString, DialogInterface.OnClickListener positiveListener,
            String negativeString, DialogInterface.OnClickListener negativeListener
    ) {
        return showAlertDialog2bt(
                title, message,
                positiveString, positiveListener,
                negativeString, negativeListener
        );
    }

    protected void addDismissibleDialog(AlertDialog aDialog) {
        dismissibleDialogs.add(aDialog);
    }

    protected void dismissDialogs() {
        for (AlertDialog aDialog : dismissibleDialogs) {
            aDialog.dismiss();
        }
        dismissibleDialogs.clear();
    }

    @Override
    public void onAddDismissibleDialog(AlertDialog aDialog, List<AlertDialog> dismissibleDialogs) {
        dismissibleDialogs.add(aDialog);
    }

    @Override
    public void onDismissDialogs(List<AlertDialog> dismissibleDialogs) {
        for (AlertDialog aDialog : dismissibleDialogs) {
            aDialog.dismiss();
        }
        dismissibleDialogs.clear();
    }

    //----------------------------------------------------------------------------------------------
    //endregion Dialogs
}
