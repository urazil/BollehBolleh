package com.example.kotlinnodejsauth.util

import android.os.Handler
import android.os.Looper
import com.example.kotlinnodejsauth.EditVideoPage
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class ProgressRequestBody3(private val mFile: File, private val mListener3: EditVideoPage) :
    RequestBody() {



    interface UploadCallbacks {
        fun onProgressUpdate(percentage: Int)
    }

    override fun contentType(): MediaType? {
        return MediaType.parse("image/*")
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mFile.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink?) {
        val fileLength = mFile.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val `in` = FileInputStream(mFile)
        var uploaded: Long = 0
        try {
            var read: Int = 0
            val handler = Handler(Looper.getMainLooper())
            while (`in`.read(buffer).let { read = it; it != -1 }) {
                handler.post(ProgressUpdater(uploaded, fileLength))
                uploaded += read.toLong()
                sink!!.write(buffer, 0, read)

            }
        } finally {
            `in`.close()
        }
    }

    private inner class ProgressUpdater(private val mUploaded: Long, private val mTotal: Long) :
        Runnable {
        override fun run() {
            mListener3.onProgressUpdate((100 * mUploaded / mTotal).toInt())
        }
    }
}