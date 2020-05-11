package ml.deeg05.dhome

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_main.*
import android.content.SharedPreferences
import android.widget.Toast
import java.util.ArrayList


class AddActivity : AppCompatActivity() {

    var devicesArray : ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // TextBoxes
        val editName = findViewById<View>(R.id.name) as EditText
        val editAddress = findViewById<View>(R.id.ip) as EditText

        // Listen to keys
        editAddress.setOnKeyListener(View.OnKeyListener {_, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) if (keyCode == KeyEvent.KEYCODE_ENTER) {

                getList()
                devicesArray.add(editAddress.text.toString())

                editName.setText("")
                editAddress.setText("")

                saveList()

                finish()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun saveList() {
        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()

        editor.putStringSet("devices", HashSet(devicesArray)) // Put StringSet into SharedPreferences
        editor.commit()
    }

    private fun getList() {
        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)

        if (sharedPreference.getStringSet("devices",null) != null) { // If there is anything in SharedPreferences
            val set = sharedPreference.getStringSet("devices", null)!!.toMutableList() // Convert StringSet to MutableList
            devicesArray = ArrayList(set)
        }
    }
}
