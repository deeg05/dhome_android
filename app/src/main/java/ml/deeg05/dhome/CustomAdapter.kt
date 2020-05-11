package ml.deeg05.dhome

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Switch
import com.google.android.material.snackbar.Snackbar
import okhttp3.*
import java.io.IOException

val TAG = "Response"

// Our custom ListView adapter
class DevicesAdapter(private val context: Activity, private val title: ArrayList<String>) : ArrayAdapter<String>(context, R.layout.list_item, title) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater // Inflate listview with items
        val rowView = inflater.inflate(R.layout.list_item, null, true) // Init rowView

        val titleText = rowView.findViewById(R.id.devswitch) as Switch // Text of item
        val toggleSwitch = rowView.findViewById(R.id.devswitch) as Switch //Switch of item

        titleText.text = title[position] // Set text

        val url : String = "http://" + title[position] // Set URL

        toggleSwitch.setOnCheckedChangeListener { buttonView, isChecked -> // Listen to switch changes
            if (isChecked) {

                val urlOn = "$url/1"
                getHttpResponse(urlOn) // Change state

                val snackbar = Snackbar // Show Snackbar
                    .make(rowView, "Turned ON", Snackbar.LENGTH_LONG)
                snackbar.show()
            } else {

                val urlOff = "$url/0"
                getHttpResponse(urlOff) // Change state

                val snackbar = Snackbar
                    .make(rowView, "Turned OFF", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }

        return rowView
    }

    // Http Get request function
    @Throws(IOException::class)
    fun getHttpResponse(url: String) {

        val client = OkHttpClient() // Request builder stuff
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback { // Make call
            override fun onFailure(call: Call, e: IOException) {
                val mMessage = e.message.toString()
                Log.w("failure Response", mMessage)
                //call.cancel();
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val mMessage = response.body!!.string()
                Log.w("status is", mMessage)
                Log.w(mMessage, mMessage)
            }
        })
    }
}