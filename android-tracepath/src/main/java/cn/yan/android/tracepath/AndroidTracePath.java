package cn.yan.android.tracepath;

/**
 * @author yanbo1
 */
public final class AndroidTracePath {
    static {
        System.loadLibrary("tracepath-compat");
    }

    private StateListener mStateListener;

    public AndroidTracePath(StateListener stateListener) {
        this.mStateListener = stateListener;
    }

    public void startTrace(String hostName) {
        nativeInit();
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
