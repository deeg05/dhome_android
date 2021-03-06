package ml.deeg05.dhome

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Switch
import com.google.android.material.snackbar.Snackbar
import devicesModel
import okhttp3.*
import java.io.IOException
import java.util.concurrent.CountDownLatch


class DevicesAdapter(private val activity: Activity, devices: List<devicesModel>) : BaseAdapter() {

    private var devices = ArrayList<devicesModel>()
    val mHandler = Handler(Looper.getMainLooper())

    var switch: Switch? = null

    init {
        this.devices = devices as ArrayList
    }

    override fun getCount(): Int {
        return devices.size
    }

    override fun getItem(i: Int): Any {
        return i
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    @SuppressLint("InflateParams", "ViewHolder")
    override fun getView(i: Int, View: View?, viewGroup: ViewGroup): View {
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val vi = inflater.inflate(R.layout.list_item, null) // Inflate view
        switch = vi.findViewById(R.id.devswitch) as Switch
        switch?.text = devices[i].name // Set switch name

        val url: String = "http://" + devices[i].ip // Set URL
        getHttpResponse(url)

        switch?.setOnCheckedChangeListener { _, isChecked -> // Listen to switch changes
            if (isChecked) {

                val urlOn = "$url/1"
                getHttpResponse(urlOn) // Change state
            } else {

                val urlOff = "$url/0"
                getHttpResponse(urlOff) // Change state
            }
        }

        return vi
    }

    @Throws(IOException::class)
    fun getHttpResponse(url: String): String {
        var returnResponse: String? = null

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
                Log.w("response", mMessage)

                //Log.w(mMessage, mMessage)
                returnResponse = mMessage
                mHandler.post {
                    switch?.isChecked = mMessage == "ON"
                }
            }
        })

        if (returnResponse != null) return returnResponse as String
        else return "error"
    }
}