package ml.deeg05.dhome

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import devicesModel
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Type


open class MainActivity : AppCompatActivity() {

    private var devices : ArrayList<devicesModel> = ArrayList<devicesModel>()
    private var finalArray : ArrayList<String> = ArrayList<String>()
    lateinit var devicesAdapter : DevicesAdapter
    private var isFirstRun : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Get element as ListView
        val devicesView = findViewById<View>(R.id.devices) as ListView

        getList() // Load devices from SharedPreferences

        // Initialise devicesAdapter
        devicesAdapter = DevicesAdapter(this, devices)

        //Set adapter of listView to adapter
        devicesView.adapter = devicesAdapter

        // Set fab onClickListener
        fab.setOnClickListener { view ->
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }

        isFirstRun = false
    }
    // Inflate menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    // Handle clicks on menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_clear -> {
                devices.clear() // Clear deviceModel ArrayList

                saveList() // Save
                updateListView() // Update listView

                return true
            }
            R.id.action_settings -> { // Handle click on settings item
                Toast.makeText(this, "Here be settings...", Toast.LENGTH_LONG).show()
                return true
            }
            R.id.action_about -> {  // Handle click on about item
                showAboutDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAboutDialog() { // Show About AlertDialog
        // Create Dialog builder
        val builder = AlertDialog.Builder(this)
        // Set Dialog title
        builder.setTitle("About")
        // Set Dialog Message
        builder.setMessage("DHome is ecosystem that allows you to manage your own home with convenience")
        // Add button with caption "OK" and listener what will just close dialog
        builder.setPositiveButton("Ok", { dialogInterface: DialogInterface, i: Int -> })
        builder.show()
    }

    private fun saveList() {
        val sharedPreference =  getSharedPreferences("devices",Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        
        val gson = Gson()
        val json = gson.toJson(devices)
        editor.putString("devices", json)
        
        editor.apply()
    }

    private fun getList() {
        val sharedPreference =  getSharedPreferences("devices",Context.MODE_PRIVATE)
        val gson = Gson()

        if (sharedPreference.getString("devices", "") != "") {
            val json = sharedPreference.getString("devices", "")
            val type: Type = object : TypeToken<List<devicesModel?>?>() {}.type
            devices.clear()
            devices.addAll(gson.fromJson<List<devicesModel>>(json, type))
        }
    }

    private fun updateListView() {
        getList()
        devicesAdapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        //saveList()
    }

    override fun onResume() {
        super.onResume()
        updateListView()
    }
}