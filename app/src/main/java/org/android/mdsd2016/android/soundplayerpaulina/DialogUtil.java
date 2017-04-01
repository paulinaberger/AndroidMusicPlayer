package org.android.mdsd2016.android.soundplayerpaulina;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by paulinaberger on 2017-04-01.
 */

public class DialogUtil {

    public static void showAlertDialog(Context context, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(content);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

}