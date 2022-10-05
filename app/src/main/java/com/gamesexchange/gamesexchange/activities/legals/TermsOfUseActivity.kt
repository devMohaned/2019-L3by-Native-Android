package com.gamesexchange.gamesexchange.activities.legals

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gamesexchange.gamesexchange.R
import kotlinx.android.synthetic.main.activity_terms_of_use.*

class TermsOfUseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_of_use)
        readText()
    }

    fun readText()
    {
        val file_name = "terms_of_use.txt"
        val json_string = application.assets.open(file_name).bufferedReader().use{
            it.readText()
        }

        ID_terms_of_use_text.text = json_string
    }
}
