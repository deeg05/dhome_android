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
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import devicesModel
import kotlinx.android.synthetic.main.activity_add.*
import java.lang.reflect.Type
import java.util.ArrayList


class AddActivity : AppCompatActivity() {

    private var devices : ArrayList<devicesModel> = ArrayList<devicesModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        // TextBoxes
        val editName : EditText = findViewById<View>(R.id.nameEdit) as EditText
        val editAddress : EditText = findViewById<View>(R.id.addressEdit) as EditText

        // Listen to keys
        editAddress.setOnKeyListener(View.OnKeyListener {_, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN) if (keyCode == KeyEvent.KEYCODE_ENTER) {

                getList() // Get List

                val nameString : String = editName.text.toString() // Get text from EditTexts
                val addressString : String = editAddress.text.toString()

                devices.add(devicesModel(nameString, addressString)) // Add fresh device to list

                saveList() // Save list

                finish()
                return@OnKeyListener true
            }
            false
        })
    }

    private fun saveList() {
        val sharedPreference =  getSharedPreferences("devices",Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()

        val gson = Gson()
        val json = gson.toJson(devices)
        Log.d("json is", json)
        editor.putString("devices", json)

        editor.apply()
    }

    private fun getList() {
        val sharedPreference =  getSharedPreferences("devices",Context.MODE_PRIVATE)
        val gson = Gson()

        if (sharedPreference.getString("devices", "") != "") {
            val json = sharedPreference.getString("devices", "")
            val type: Type = object : TypeToken<List<devicesModel?>?>() {}.type
            devices.addAll(gson.fromJson<List<devicesModel>>(json, type))
        }
    }
}
