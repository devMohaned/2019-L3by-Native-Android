package com.gamesexchange.gamesexchange.activities.reports

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.gamesexchange.gamesexchange.R
import com.gamesexchange.gamesexchange.Utils.Constants
import com.gamesexchange.gamesexchange.activities.homepage.MVPHomePageActivity.currentUser
import com.gamesexchange.gamesexchange.dialogs.ReportDialog
import com.gamesexchange.gamesexchange.model.Report
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_report.*

class ReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)


        ID_report_button.setOnClickListener {
           buildDialog()
        }

        ID_report_cancel.setOnClickListener {
           finish()
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.ID_report_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_left_arrow)


    }

    fun makeReport(issueText : String, issueTitle: String)
    {
        val report = Report()
        report.reportId = FirebaseDatabase.getInstance().reference.push().key
        report.reportedText = issueText
        report.reporterName = currentUser.first_name + " " +currentUser.last_name
        report.reportedTitle = issueTitle
        report.reporterId = currentUser.firebase_uid
        FirebaseDatabase.getInstance().reference.child(Constants.FIREBASE_DB_REPORTS)
                .child(report.reportId)
                .setValue(report)

    }

    fun buildDialog()
    {
        if (ID_report_issue_text.text!!.length > 10 && ID_report_issue_text.length() > 6) {

        val reportDialog = ReportDialog()
        reportDialog.showDialog(this)
        reportDialog.dialogConfirmReportButton.setOnClickListener {


          /*  makeReport(ID_report_issue_title.text.toString()
                    , ID_report_issue_title.text.toString())*/

            sendEmail(this,ID_report_issue_title.text.toString(),
                    ID_report_issue_text.text.toString())


            reportDialog.dialog.dismiss()
//            Toast.makeText(this,getString(R.string.solved),Toast.LENGTH_LONG).show()
            finish()
        }
        }else{
            Toast.makeText(this,getString(R.string.issue_is_so_small),Toast.LENGTH_LONG).show()
        }
    }

    fun sendEmail(context: Context, subject: String, body: String) {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf("talentsinstudio@gmail.com"))
        i.putExtra(Intent.EXTRA_SUBJECT, subject)
        i.putExtra(Intent.EXTRA_TEXT, body)
        try {
            context.startActivity(Intent.createChooser(i, context.getString(R.string.report_an_issue)))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, context.getString(R.string.dont_have_email), Toast.LENGTH_SHORT).show()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            if (item.itemId == android.R.id.home) {
                supportFinishAfterTransition();
            }
        }

        return super.onOptionsItemSelected(item)

    }
}
