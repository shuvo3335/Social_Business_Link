package com.example.redoy.lynk.util;

import android.content.Context;
import android.graphics.Color;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by redoy.ahmed on 19-Feb-2018.
 */

public class     CustomSweetAlertDialog {

    public static SweetAlertDialog getProgressDialog(Context context, String contextText) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(contextText);
        pDialog.setCancelable(false);
        pDialog.show();

        return pDialog;
    }

    public static SweetAlertDialog getAlertDialog(Context context, int dialogType, String titleText, String contextText) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, dialogType);
        alertDialog.setTitleText(titleText);
        alertDialog.setContentText(contextText);
        alertDialog.show();
        return alertDialog;
    }
}
