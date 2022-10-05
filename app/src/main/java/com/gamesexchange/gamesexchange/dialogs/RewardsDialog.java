package com.gamesexchange.gamesexchange.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.TextView;

import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.views.CustomButtonView;


public class RewardsDialog {


    public Dialog dialog;
    public CustomButtonView dialogGreatButton;

    public void showDialog(Activity activity, String msg) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_reward);

        TextView text = (TextView) dialog.findViewById(R.id.ID_dialog_message);
        text.setText(msg);

        dialogGreatButton = (CustomButtonView) dialog.findViewById(R.id.ID_dialog_great_btn);
      /*  dialogConfirmReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Log.d("Rewards Dialog: ", "Dialog is dismissed");
            }
        });*/

        dialog.show();

    }
}
