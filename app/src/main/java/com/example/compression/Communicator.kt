package com.example.compression

import android.graphics.Bitmap
import android.net.Uri

interface Communicator {

    fun passDataCom(img: Uri)
}