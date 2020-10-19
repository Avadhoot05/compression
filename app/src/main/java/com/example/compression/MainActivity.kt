package com.example.compression

import android.R.attr.bitmap
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.ByteArrayOutputStream
import java.io.File


class MainActivity : AppCompatActivity(),Communicator {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionsForSave()
        makeDirectories()

        val fragment_displayButton = displayButtons()

        supportFragmentManager.beginTransaction().replace(R.id.FragmentContainer,fragment_displayButton).commit()
    }

    override fun passDataCom(img: Bitmap,flag:Boolean){


        val bytes = ByteArrayOutputStream()
        if(flag)
            img.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        else
            img.compress(Bitmap.CompressFormat.JPEG, 40, bytes)

        val byteArray = bytes.toByteArray()

        val bundle = Bundle()
        bundle.putByteArray("image",byteArray)

        bytes.flush()
        bytes.close()
        val transaction = this.supportFragmentManager.beginTransaction()
        val fragmentDisplayImage = displayImage()

        fragmentDisplayImage.arguments = bundle

        transaction.replace(R.id.FragmentContainer,fragmentDisplayImage).commit()
    }

    private fun makeDirectories(){
        val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
        var dir = File(extStorageDirectory)
        if (!dir.exists())
            dir.mkdirs()

         dir = File(extStorageDirectory, "/Engage")

        if (!dir.exists())
            dir.mkdirs()

        dir = File("$extStorageDirectory/Engage", "/Media")
        if (!dir.exists())
            dir.mkdirs()

        dir = File("$extStorageDirectory/Engage/Media", "/Engage Images")
        if (!dir.exists())
            dir.mkdirs()
    }

    private fun permissionsForSave( ) {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),150)
            }
            else{
                makeDirectories()
            }
        }
        else{
            makeDirectories()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode==150 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            makeDirectories()
        }
        else{
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }


}
