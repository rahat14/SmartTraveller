package com.metacoder.transalvania.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.metacoder.transalvania.ui.Events.EventDetails;

public class Utils {

    public static ProgressDialog createDialogue(Activity activity, String msg) {

        ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage(msg);
        dialog.setCancelable(false);
        return dialog;
    }


    public static void showLOginError(Context context) {
        Toast.makeText(context, "Please Login...", Toast.LENGTH_SHORT).show();
    }
}
