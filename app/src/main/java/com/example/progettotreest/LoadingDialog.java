package com.example.progettotreest;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;


public class LoadingDialog {

    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity currentActivity){
        activity=currentActivity;
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog,null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

    }

    void dismissLoadingDialog(){
        dialog.dismiss();
    }

    boolean isShowing(){
        return dialog.isShowing();
    }
}
