package com.gamesexchange.gamesexchange.activities.legals

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gamesexchange.gamesexchange.R
import com.gamesexchange.gamesexchange.Utils.Constants
import com.gamesexchange.gamesexchange.model.Game
import kotlinx.android.synthetic.main.activity_eula.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class EulaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eula)
        readText()
    }

    fun readText()
    {


        val file_name = "eula.txt"
        val json_string = application.assets.open(file_name).bufferedReader().use{
            it.readText()
        }

        ID_eula_text.text = json_string


    }
}
