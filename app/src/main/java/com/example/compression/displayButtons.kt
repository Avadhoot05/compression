package com.example.compression

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_display_buttons.view.*



class displayButtons : Fragment() {

    val REQUEST_IMAGE_CAPTURE = 1
    val IMAGE_PICK_CODE = 1000
    private lateinit var communicator: Communicator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val  view = inflater.inflate(R.layout.fragment_display_buttons, container, false)
        communicator = activity as Communicator

        view.CamButton.setOnClickListener(){

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } catch (e: ActivityNotFoundException) {
                // display error state to the user
            }
        }

        view.SelButton.setOnClickListener() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            val imageBitmap = data?.extras?.get("data") as Bitmap
            communicator.passDataCom(imageBitmap,true)
        }

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null){
            val selectedPhotoUri = data.data
            try {
                selectedPhotoUri?.let {
                    if(Build.VERSION.SDK_INT < 28) {
                        val imageBitmap = MediaStore.Images.Media.getBitmap(
                            getActivity()?.getContentResolver()!!,
                            selectedPhotoUri
                        )
                        communicator.passDataCom(imageBitmap,false)
                    } else {
                        val source = ImageDecoder.createSource(getActivity()?.getContentResolver()!!, selectedPhotoUri)
                        val imageBitmap = ImageDecoder.decodeBitmap(source)
                        communicator.passDataCom(imageBitmap,false)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

           //val imageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data?.data);


            }

    }

}
