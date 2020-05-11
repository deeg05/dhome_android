package ml.deeg05.dhome

// DHome 0.1 - Public Beta
// "Lightswitch only" - only remote ON/OFF capability

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*
import kotlin.collections.HashSet


open class MainActivity : AppCompatActivity() {

    var devicesArray : ArrayList<String> = ArrayList<String>()
    lateinit var devicesAdapter : DevicesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Get element as ListView
        val devicesView = findViewById<View>(R.id.devices) as ListView

        getList() // Load deviceArray from SharedPreferences

        // Initialise devicesAdapter
        devicesAdapter = DevicesAdapter(this, devicesArray)

        //Set adapter of listView to adapter
        devicesView.adapter = devicesAdapter

        // Set fab onClickListener
        fab.setOnClickListener { view ->

            // Make snackbar saying that we are working on it
            Snackbar.make(view, "Work in progress", Snackbar.LENGTH_LONG).show()

            //TODO Activity for IP address input
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
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
                val sharedPreference =  getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                editor.remove("devices") // Remove from SharedPreferences
                editor.commit()

                devicesArray.clear() // Clear arrayList
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
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
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

    private fun updateListView() {
        devicesAdapter.clear()

        getList()
        devicesAdapter.addAll(devicesArray)
        devicesAdapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        saveList()
    }

    override fun onResume() {
        super.onResume()

        updateListView()
    }
}