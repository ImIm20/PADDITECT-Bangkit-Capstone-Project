package com.example.Padditect

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
//import com.example.padditect.LoginGoogle
import android.provider.MediaStore
import android.graphics.Bitmap
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import android.app.Activity
import android.media.ThumbnailUtils
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.example.Padditect.ml.Model
import org.tensorflow.lite.DataType
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Model: AppCompatActivity() {
    var camera: Button = findViewById(R.id.ambil_foto)
    var gallery: Button = findViewById(R.id.unggahh_foto)
    //var logout: Button = findViewById(R.id.logout)
    var imageView: ImageView = findViewById(R.id.imageUploaded)
    var result: TextView = findViewById(R.id.teks_prediksi)
    //var name: TextView = findViewById(R.id.name)
    //var mail: TextView = findViewById(R.id.mail)
    //var status: TextView = findViewById(R.id.status_padi)
    var imageSize = 500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_model)
        camera = findViewById(R.id.ambil_foto)
        gallery = findViewById(R.id.unggahh_foto)
        result = findViewById(R.id.teks_prediksi)
        imageView = findViewById(R.id.imageUploaded)
        //status = findViewById(R.id.status_padi)

        //profile
        /*logout = findViewById(R.id.logout)
        name = findViewById(R.id.name)
        mail = findViewById(R.id.mail)
        val signInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (signInAccount != null) {
            name.setText(signInAccount.displayName)
            mail.setText(signInAccount.displayName)
        }
        logout.setOnClickListener(View.OnClickListener { view: View? ->
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, LoginGoogle::class.java)
            startActivity(intent)
        })*/
        camera.setOnClickListener(View.OnClickListener {
            val open_camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(open_camera, 100)
        })
        /*camera.setOnClickListener(view -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 3);
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        });*/gallery.setOnClickListener(View.OnClickListener { view: View? ->
            val cameraIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(cameraIntent, 1)
        })
    }

    fun classifyImage(image: Bitmap?) {
        try {
            val model = Model.newInstance(applicationContext)

            // Creates inputs for reference.
            val inputFeature0 =
                TensorBuffer.createFixedSize(intArrayOf(1, 500, 500, 3), DataType.FLOAT32)
            val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
            byteBuffer.order(ByteOrder.nativeOrder())
            val intValues = IntArray(imageSize * imageSize)
            image!!.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)
            var pixel = 0
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for (i in 0 until imageSize) {
                for (j in 0 until imageSize) {
                    val `val` = intValues[pixel++] // RGB
                    byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                    byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                    byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
                }
            }
            inputFeature0.loadBuffer(byteBuffer)

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            val confidences = outputFeature0.floatArray
            // find the index of the class with the biggest confidence.
            var maxPos = 0
            var maxConfidence = 0f
            for (i in confidences.indices) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }
            val classes = arrayOf(
                "tungro",
                "hispa",
                "downy mildew",
                "bacterial leaf streak",
                "bacterial leaf blight",
                "brown spot",
                "blast",
                "normal",
                "dead_heart",
                "bacterial_panicle_blight"
            )
            result!!.text = classes[maxPos]
            //status padi
            /*if(result = "normal"){
                status.setText("SEHAT");
                status.setTextColor(Color.GREEN);
            }else if(result = "hispa" || "downy mildew" || "dead_heart"){
                status.setText("SERANGGA");
                status.setTextColor(Color.RED);
            }else if(result = "brown spot" || "blast"){
                status.setText("JAMUR");
                status.setTextColor(Color.RED);
            }else{
                status.setText("BAKTERI");
                status.setTextColor(Color.RED);
            }*/

            //lepaskan model apabila sudah tidak digunakan
            model.close()
        } catch (e: IOException) {
            // TODO Handle the exception
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                var image = data!!.extras!!["data"] as Bitmap?
                val dimension = Math.min(image!!.width, image.height)
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
                imageView!!.setImageBitmap(image)
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false)
                classifyImage(image)
            } else {
                val dat = data!!.data
                var image: Bitmap? = null
                try {
                    image = MediaStore.Images.Media.getBitmap(this.contentResolver, dat)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                imageView!!.setImageBitmap(image)
                image = Bitmap.createScaledBitmap(image!!, imageSize, imageSize, false)
                classifyImage(image)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}