package net.dankito.richtexteditor.android.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import java.util.HashMap;


/**
 * Thanks to ravindu1024 for providing this class: https://github.com/ravindu1024/android-keyboardlistener/blob/master/keyboard-listener/src/main/java/com/rw/keyboardlistener/KeyboardUtils.java
 *
 * Based on the following Stackoverflow answer:
 * http://stackoverflow.com/questions/2150078/how-to-check-visibility-of-software-keyboard-in-android
 */
@SuppressWarnings("WeakerAccess")
public class KeyboardUtils implements ViewTreeObserver.OnGlobalLayoutListener
{
    private final static int MAGIC_NUMBER = 200;

    private SoftKeyboardToggleListener mCallback;
    private View mRootView;
    private Boolean prevValue = null;
    private float mScreenDensity = 1;
    private static HashMap<SoftKeyboardToggleListener, KeyboardUtils> sListenerMap = new HashMap<>();

    public interface SoftKeyboardToggleListener
    {
        void onToggleSoftKeyboard(boolean isVisible);
    }


    @Override
    public void onGlobalLayout()
    {
        Rect r = new Rect();
        mRootView.getWindowVisibleDisplayFrame(r);

        int heightDiff = mRootView.getRootView().getHeight() - (r.bottom - r.top);
        float dp = heightDiff/ mScreenDensity;
        boolean isVisible = dp > MAGIC_NUMBER;

        if (mCallback != null && (prevValue == null || isVisible != prevValue)) {
            prevValue = isVisible;
            mCallback.onToggleSoftKeyboard(isVisible);
        }
    }

    /**
     * Add a new keyboard listener
     * @param act calling activity
     * @param listener callback
     */
    public static void addKeyboardToggleListener(Activity act, SoftKeyboardToggleListener listener)
    {
        removeKeyboardToggleListener(listener);

        sListenerMap.put(listener, new KeyboardUtils(act, listener));
    }

    /**
     * Remove a registered listener
     * @param listener {@link SoftKeyboardToggleListener}
     */
    public static void removeKeyboardToggleListener(SoftKeyboardToggleListener listener)
    {
        if(sListenerMap.containsKey(listener))
        {
            KeyboardUtils k = sListenerMap.get(listener);
            k.removeListener();

            sListenerMap.remove(listener);
        }
    }

    /**
     * Remove all registered keyboard listeners
     */
    public static void removeAllKeyboardToggleListeners()
    {
        for(SoftKeyboardToggleListener l : sListenerMap.keySet())
            sListenerMap.get(l).removeListener();

        sListenerMap.clear();
    }

    /**
     * Manually toggle soft keyboard visibility
     * @param context calling context
     */
    public static void toggleKeyboardVisibility(Context context)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Force closes the soft keyboard
     * @param activeView the view with the keyboard focus
     */
    public static void forceCloseKeyboard(View activeView)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) activeView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activeView.getWindowToken(), 0);
    }

    private void removeListener()
    {
        mCallback = null;

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        else {
            mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }

    private KeyboardUtils(Activity act, SoftKeyboardToggleListener listener)
    {
        mCallback = listener;

        mRootView = ((ViewGroup) act.findViewById(android.R.id.content)).getChildAt(0);
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        mScreenDensity = act.getResources().getDisplayMetrics().density;
    }

}
