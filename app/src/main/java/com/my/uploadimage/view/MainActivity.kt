package com.my.uploadimage.view

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.my.uploadimage.R
import com.my.uploadimage.model.ImageApiResponse
import com.my.uploadimage.network.RetrofitService
import com.my.uploadimage.utility.InternetUtility
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var btn_click_photo: Button
    private lateinit var imageView: ImageView
    private lateinit var sub_layout: ConstraintLayout
    private lateinit var imagePath: File

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_click_photo = findViewById<Button>(R.id.btn_click_photo)
        imageView = findViewById<ImageView>(R.id.photo)
        sub_layout = findViewById<ConstraintLayout>(R.id.sub_layout)
        val btn_upload = findViewById<Button>(R.id.btn_upload)
        btn_click_photo.setOnClickListener(View.OnClickListener { openCamera() })
        btn_upload.setOnClickListener {
            if (InternetUtility.hasNetworkConnection(this@MainActivity)) {
                if (imagePath != null) {
                    uploadImageToServer()
                } else Toast.makeText(
                    this@MainActivity,
                    "Please click image or select",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                InternetUtility.internetCheckPopup(this@MainActivity)
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            val photo = data!!.extras!!["data"] as Bitmap?
            imageView!!.setImageBitmap(photo)
            sub_layout!!.visibility = View.VISIBLE
            btn_click_photo!!.visibility = View.GONE


            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            val tempUri = getImageUri(applicationContext, photo)

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            imagePath = File(getRealPathFromURI(tempUri))
        }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap?): Uri {
        val bytes = ByteArrayOutputStream()
        inImage!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun getRealPathFromURI(uri: Uri): String {
        var path = ""
        if (contentResolver != null) {
            val cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    private fun uploadImageToServer() {
        val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("type", "media")
            .addFormDataPart(
                "image", "/C:/Users/c5141506/Pictures/Picture1.jpg",
                RequestBody.create(
                    MediaType.parse("application/octet-stream"),
                    imagePath
                )
            )
            .build()
        val dialog = ProgressDialog(this@MainActivity)
        try {
            val retrofitClient: RetrofitService? = RetrofitService.instance
            val postService = retrofitClient?.myApi
            val categoryResponseCall = postService!!.uploadImage(body)
            dialog.setCancelable(true)
            dialog.show()
            categoryResponseCall!!.enqueue(object : Callback<ImageApiResponse?> {
                override fun onResponse(
                    call: Call<ImageApiResponse?>,
                    response: Response<ImageApiResponse?>
                ) {
                    dialog.dismiss()
                    if (response.code() == 200) {
                        val imageApiResponse = response.body()
                        if (imageApiResponse != null) Toast.makeText(
                            this@MainActivity,
                            imageApiResponse.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ImageApiResponse?>, error: Throwable) {
                    dialog.dismiss()
                }
            })
        } catch (ex: Exception) {
            dialog.dismiss()
        }
    }

    companion object {
        private const val CAMERA_PIC_REQUEST = 100
    }
}