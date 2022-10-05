package com.gamesexchange.gamesexchange.activities.legals

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gamesexchange.gamesexchange.R
import kotlinx.android.synthetic.main.activity_privacy_policy.*

class PrivacyPolicyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        readText()
    }

    fun readText()
    {
        val file_name = "privacy_policy.txt"
        val json_string = application.assets.open(file_name).bufferedReader().use{
            it.readText()
        }

        ID_privacy_policy_text.text = json_string
    }
}
