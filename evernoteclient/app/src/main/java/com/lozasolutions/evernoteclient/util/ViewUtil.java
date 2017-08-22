package com.lozasolutions.evernoteclient.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.lozasolutions.evernoteclient.R;

public final class ViewUtil {

    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }

    public static int dpToPx(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void showSnackbar(View view, @StringRes int stringRes) {
        showSnackbar(view, view.getContext().getString(stringRes));
    }

    public static void showSnackbar(View view, CharSequence text) {
        showSnackbar(view, text, Snackbar.LENGTH_LONG);
    }

    public static void showSnackbar(View view, CharSequence text, int length) {
        CoordinatorLayout coordinatorLayout = findFabCoordinator(view, R.id.cordinatorLayout);
        if (coordinatorLayout != null) {
            view = coordinatorLayout;
        }

        Snackbar.make(view, text, length).show();
    }

    public static CoordinatorLayout findFabCoordinator(View view, @IdRes int coordinatorId) {
        View coordinatorView = view.findViewById(coordinatorId);
        if (coordinatorView != null) {
            return (CoordinatorLayout) coordinatorView;
        }

        return findFabCoordinatorInParent(view, coordinatorId);
    }

    private static CoordinatorLayout findFabCoordinatorInParent(View view, @IdRes int coordinatorId) {
        if (view.getId() == android.R.id.content) {
            return null;
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child.equals(view)) {
                continue;
            }

            View coordinatorView = child.findViewById(coordinatorId);
            if (coordinatorView != null) {
                return (CoordinatorLayout) coordinatorView;
            }
        }

        return findFabCoordinator(parent, coordinatorId);
    }
}
