package mdpa.lasalle.propertycross.base.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import mdpa.lasalle.propertycross.util.Component;
import mdpa.lasalle.propertycross.util.FragmentHelper;

public abstract class FragmentBase extends Fragment implements Component {
    protected final String TAG = getComponent().id();
    private FragmentHelper fragmentHelper;

    private List<AlertDialog> dismissibleDialogs;

    public FragmentBase() {
        dismissibleDialogs = new ArrayList<>();
    }

    private OnFragmentListener listener;
    public interface OnFragmentListener {
        AlertDialog onShowAlertDialog1bt(
                String title, String message,
                String positiveString, DialogInterface.OnClickListener positiveListener
        );
        AlertDialog onShowAlertDialog2bt(
                String title, String message,
                String positiveString, DialogInterface.OnClickListener positiveListener,
                String negativeString, DialogInterface.OnClickListener negativeListener
        );
        void onAddDismissibleDialog(AlertDialog aDialog, List<AlertDialog> dismissibleDialogs);
        void onDismissDialogs(List<AlertDialog> dismissibleDialogs);

        <F extends Fragment & Component> void onFinish(F fragment);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = onAttachGetListener(OnFragmentListener.class, context);
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentHelper = new FragmentHelper(getChildFragmentManager());
    }

    @CallSuper @Override
    public void onDestroy() {
        super.onDestroy();
    }


    protected FragmentHelper getFragmentHelper() {
        return fragmentHelper;
    }

    protected <T> T onAttachGetListener(Class<T> tClass, Context parent) {
        try { return tClass.cast(parent); }
        catch (ClassCastException e) {
            throw new ClassCastException(String.valueOf(parent)
                    + " must implement " + tClass.getSimpleName());
        }
    }

    //region Dialogs
    //----------------------------------------------------------------------------------------------

    protected AlertDialog showAlertDialog1bt(
            String title, String message,
            String positiveString, DialogInterface.OnClickListener positiveListener
    ) {
        return listener.onShowAlertDialog1bt(title, message, positiveString, positiveListener);
    }

    protected AlertDialog showAlertDialog2bt(
            String title, String message,
            String positiveString, DialogInterface.OnClickListener positiveListener,
            String negativeString, DialogInterface.OnClickListener negativeListener
    ) {
        return listener.onShowAlertDialog2bt(
                title, message,
                positiveString, positiveListener,
                negativeString, negativeListener
        );
    }

    protected void addDismissibleDialog(AlertDialog aDialog) {
        listener.onAddDismissibleDialog(aDialog, dismissibleDialogs);
    }

    protected void dismissDialogs() {
        listener.onDismissDialogs(dismissibleDialogs);
    }

    //----------------------------------------------------------------------------------------------
    //endregion Dialogs
}
