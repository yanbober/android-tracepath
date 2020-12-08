package cn.yan.android.tracepath;

import androidx.annotation.Keep;

/**
 * @author yanbo1
 */
@Keep
public final class AndroidTracePath {
    static {
        System.loadLibrary("tracepath-compat");
    }

    private StateListener mStateListener;

    public AndroidTracePath(StateListener stateListener) {
        this.mStateListener = stateListener;
        nativeInit();
    }

    public void startTrace(String hostName) {
        nativeStartTrace(hostName);
    }

    public native void nativeInit();

    public native void nativeStartTrace(String hostName);

    public void nativeOnStart() {
        if (null != mStateListener) {
            mStateListener.onStart();
        }
    }

    public void nativeOnUpdate(String update) {
        if (null != mStateListener) {
            mStateListener.onUpdate(update);
        }
    }

    public void nativeOnEnd() {
        if (null != mStateListener) {
            mStateListener.onEnd();
        }
    }

    public interface StateListener {
        void onStart();
        void onUpdate(String update);
        void onEnd();
    }
}
