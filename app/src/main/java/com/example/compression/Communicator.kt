package com.example.compression

import android.graphics.Bitmap

interface Communicator {

    fun passDataCom(img:Bitmap,flag:Boolean)
}