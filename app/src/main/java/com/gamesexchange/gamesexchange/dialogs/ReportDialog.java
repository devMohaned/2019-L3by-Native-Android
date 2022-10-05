package com.gamesexchange.gamesexchange.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.views.CustomButtonView;


public class ReportDialog {


    public Dialog dialog;
    public Button dialogConfirmReportButton;

    public void showDialog(Activity activity) {
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_report);

        TextView text = (TextView) dialog.findViewById(R.id.ID_dialog_report_text);
        text.setText(activity.getString(R.string.confirm_report));

        dialogConfirmReportButton = (Button) dialog.findViewById(R.id.ID_dialog_report_button);
        Button dialogCancelButton = dialog.findViewById(R.id.ID_dialog_report_cancel);
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
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
