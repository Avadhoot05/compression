package com.example.compression

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_display_buttons.view.*
import java.io.File


class displayButtons : Fragment() {

    val REQUEST_IMAGE_CAPTURE = 1
    val IMAGE_PICK_CODE = 1000
    private lateinit var communicator: Communicator
    private lateinit var imageURI:Uri

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val  view = inflater.inflate(R.layout.fragment_display_buttons, container, false)
        communicator = activity as Communicator

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        view.CamButton.setOnClickListener()
        {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //val s:String = Environment.DIRECTORY_PICTURES
            val mediaStorageDir = File(Environment.getExternalStorageDirectory().toString()+"//Engage", "Profile_pictures")

            if (!mediaStorageDir.exists())
            {
                mediaStorageDir.mkdirs()
            }

            imageURI = Uri.fromFile(File(mediaStorageDir.getPath() + File.separator + "profile_img.jpg"));
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);

            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }

        view.SelButton.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            if (imageURI != null) {
                communicator.passDataCom(imageURI)
            }
        }

        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null){
            //val photoUri = data.data
            imageURI = data.data!!

            if (imageURI != null) {
                communicator.passDataCom(imageURI)
            }

           //val imageBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data?.data);
            }

    }

}
