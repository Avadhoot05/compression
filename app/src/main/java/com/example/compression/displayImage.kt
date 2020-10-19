package com.example.compression

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import com.github.chrisbanes.photoview.PhotoView

class displayImage : Fragment() {

    var imageByteArray:ByteArray? = null
    private lateinit var image_view: PhotoView
    private lateinit var imageToDisplay:Bitmap


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view= inflater.inflate(R.layout.fragment_display_image, container, false)

        image_view = view.findViewById(R.id.imageView);


        imageByteArray  = arguments?.getByteArray("image")

        imageToDisplay = BitmapFactory.decodeByteArray(imageByteArray,0, imageByteArray?.size!!)

        permissionsForSave()

        image_view.setImageBitmap(imageToDisplay)
        return view
    }
    
    private fun permissionsForSave( ) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(getContext()!!,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                Toast.makeText(activity,"ask",Toast.LENGTH_LONG).show()
                ActivityCompat.requestPermissions(getActivity()!!, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),100)
            }
            else{
                saveImage()
            }
        }
        else{
            saveImage()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Toast.makeText(getActivity(), "Code${requestCode.toString()}", Toast.LENGTH_SHORT).show()
       if(requestCode==100) {
           if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
               saveImage()
           }
       }
        else{
           Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show()
       }
    }

    private fun saveImage(){
        val strgstate = Environment.getExternalStorageState()
        if (strgstate == Environment.MEDIA_MOUNTED){
            val storageDirectory = Environment.getExternalStorageDirectory().toString()+"/Engage/Media//Engage Images"
            val file = File(storageDirectory,"ENG_IMG_${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}"+".jpg")


            try {
                val stream: OutputStream = FileOutputStream(file)
                imageToDisplay.compress(Bitmap.CompressFormat.JPEG,100,stream)
                stream.flush()
                stream.close()
                Toast.makeText(getActivity(), "saved", Toast.LENGTH_SHORT).show()
            }
            catch (e:Exception){
                e.printStackTrace()
                Toast.makeText(getActivity(), "exception", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            Toast.makeText(getActivity(), "Unable To Access", Toast.LENGTH_SHORT).show()
        }
    }



}
