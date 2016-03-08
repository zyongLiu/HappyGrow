package rapid.decoder;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;

public class NextLayoutInspector {
    private static final boolean TEST_COMPAT = false;

    public interface OnNextLayoutListener {
        void onNextLayout(View v);
    }

    @SuppressLint("NewApi")
    public static void inspectNextLayout(View v, final OnNextLayoutListener listener) {
        //noinspection ConstantConditions,PointlessBooleanExpression
        if (TEST_COMPAT || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            new LayoutChangeInspector(v, listener).startInspect();
        } else {
            v.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                           int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    listener.onNextLayout(v);
                }
            });
        }
    }

    private static class LayoutChangeInspector implements Runnable,
            ViewTreeObserver.OnGlobalLayoutListener {
        private WeakReference<View> mViewRef;
        private int mLeft;
        private int mTop;
        private int mRight;
        private int mBottom;
        private OnNextLayoutListener mListener;

        public LayoutChangeInspector(View v, OnNextLayoutListener listener) {
            mViewRef = new WeakReference<View>(v);
            mLeft = v.getLeft();
            mTop = v.getTop();
            mRight = v.getRight();
            mBottom = v.getBottom();
            mListener = listener;
        }

        public void startInspect() {
            View v = mViewRef.get();
            if (v == null) return;

            if (!v.isLayoutRequested()) {
                mListener.onNextLayout(v);
            } else {
                v.post(this);
                v.getViewTreeObserver().addOnGlobalLayoutListener(this);
            }
        }

        @Override
        public void run() {
            View v = mViewRef.get();
            if (v == null) return;

            if (!v.isLayoutRequested() ||
                    v.getLeft() != mLeft ||
                    v.getTop() != mTop ||
                    v.getRight() != mRight ||
                    v.getBottom() != mBottom) {

                removeOnGlobalLayoutListener(v);
                mListener.onNextLayout(v);
            } else {
                v.post(this);
            }
        }

        @Override
        public void onGlobalLayout() {
            View v = mViewRef.get();
            if (v == null) return;

            removeOnGlobalLayoutListener(v);
            v.removeCallbacks(this);
            mListener.onNextLayout(v);
        }

        @SuppressWarnings("deprecation")
        private void removeOnGlobalLayoutListener(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                v.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        }
    }
}
