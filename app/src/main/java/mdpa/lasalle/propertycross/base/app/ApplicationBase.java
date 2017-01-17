package mdpa.lasalle.propertycross.base.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.CallSuper;

import mdpa.lasalle.propertycross.util.Component;
import mdpa.lasalle.propertycross.util.LogUtils;

public abstract class ApplicationBase extends Application implements Component{
    protected final String TAG = getComponent().id();

    private int numCreatedActivities;
    private int numStartedActivities;
    private int numResumedActivities;

        @CallSuper
        @Override
        public void onCreate() {
            super.onCreate();
            numStartedActivities = 0;
            registerActivityLifecycleCallbacks(callbacks);
            LogUtils.v(TAG, "Application Created!");
        }

        @CallSuper
        protected void onStart() {
            LogUtils.v(TAG, "Application Started!");
        }

        protected void onPause() {
        /* override */
        }

        protected void onResume() {
        /* override */
        }

        @CallSuper
        protected void onStop() {
            LogUtils.v(TAG, "Application Stopped!");
        }

        @CallSuper
        protected void onDestroy() {
            LogUtils.v(TAG, "Application Destroyed!");
        }

        private final ActivityLifecycleCallbacks callbacks = new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                numCreatedActivities += 1;
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (numStartedActivities == 0) onStart();
                numStartedActivities += 1;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (numResumedActivities == 0) onResume();
                numResumedActivities += 1;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                numResumedActivities -= 1;
                if (numResumedActivities == 0) onPause();
            }

            @Override
            public void onActivityStopped(Activity activity) {
                numStartedActivities -= 1;
                if (numStartedActivities == 0) onStop();
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                numCreatedActivities -= 1;
                if (numCreatedActivities == 0) onDestroy();
            }
        };

        public boolean isCreated() {
            return numCreatedActivities > 0;
        }

        public boolean isStarted() {
            return numStartedActivities > 0;
        }

        public boolean isResumed() {
            return numResumedActivities > 0;
        }

        public boolean isPaused() {
            return !isResumed();
        }

        public boolean isStopped() {
            return !isStarted();
        }

        public boolean isDestroyed() {
            return  !isCreated();
        }
}
