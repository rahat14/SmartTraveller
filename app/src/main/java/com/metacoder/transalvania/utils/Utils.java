package com.metacoder.transalvania.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;

public class Utils {

    public static ProgressDialog createDialogue(Activity activity, String msg) {

        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        return dialog;
    }
}
