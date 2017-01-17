package mdpa.lasalle.propertycross.util;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentHelper {
    public static class Target<F extends Fragment & Component> {
        private @IdRes
        int containerResourceId;
        private F fragment;

        public Target(int containerResourceId, @NonNull F fragment) {
            this.containerResourceId = containerResourceId;
            this.fragment = fragment;
        }
    }

    private final FragmentManager manager;
    public FragmentHelper(@NonNull FragmentManager manager) {
        this.manager = manager;
    }

    public @Nullable
    <F extends Fragment> F find(
            @NonNull Class <F> clazz, @IdRes int containerResourceId
    ) {
        Fragment fragment = manager.findFragmentById(containerResourceId);
        if (fragment != null && fragment.getClass().equals(clazz)) {
            return clazz.cast(fragment);
        } else {
            return null;
        }
    }

    public @Nullable <F extends Fragment> F find(
            @NonNull Class <F> clazz, @NonNull Component.ID tag
    ) {
        Fragment fragment = manager.findFragmentByTag(tag.id());
        if (fragment != null && fragment.getClass().equals(clazz)) {
            return clazz.cast(fragment);
        } else {
            return null;
        }
    }

    public FragmentTransaction replace (
            @Nullable String backStackName,
            @NonNull Target... targets
    ){
        if (targets.length == 0) { throw new IllegalArgumentException(); }
        FragmentTransaction transaction = manager.beginTransaction();

        for (Target target: targets) {
            transaction.replace(
                    target.containerResourceId,
                    target.fragment,
                    ((Component)target.fragment).getComponent().id()
            );
        }

        if (backStackName != null) {
            transaction.addToBackStack(backStackName);
        }
        return transaction;
    }

    public void commit(FragmentTransaction transaction) {
        transaction.commit();
        manager.executePendingTransactions();
    }
}
