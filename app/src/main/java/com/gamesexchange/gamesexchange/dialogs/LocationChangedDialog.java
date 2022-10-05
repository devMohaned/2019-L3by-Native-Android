package com.gamesexchange.gamesexchange.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.views.CustomButtonView;


public class LocationChangedDialog {


    public Dialog dialog;
    public CustomButtonView dialogGreatButton;

    public void showDialog(Activity activity) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_location_changed);


        dialogGreatButton = (CustomButtonView) dialog.findViewById(R.id.ID_dialog_great_btn);


        dialog.show();

    }
}
