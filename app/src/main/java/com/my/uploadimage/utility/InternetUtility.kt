package com.my.uploadimage.utility

import com.my.uploadimage.R
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button

object InternetUtility {
    fun hasNetworkConnection(context: Context): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals(
                    "WIFI",
                    ignoreCase = true
                )
            ) if (ni.isConnected) haveConnectedWifi = true
            if (ni.typeName.equals(
                    "MOBILE",
                    ignoreCase = true
                )
            ) if (ni.isConnected) haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }

    fun internetCheckPopup(context: Context?) {
        val view = LayoutInflater.from(context).inflate(R.layout.internet_check_dialog, null)

        //initialize alert builder.
        val alert = AlertDialog.Builder(context)

        //set our custom alert dialog to tha alertdialog builder
        alert.setView(view)
        val dialog = alert.create()

        //this line removed app bar from dialog and make it transperent and you see the image is like floating outside dialog box.
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //finally show the dialog box in android all
        dialog.show()
        val ok_btn = view.findViewById<View>(R.id.ok_btn) as Button
        ok_btn.setOnClickListener { dialog.dismiss() }
    }
}