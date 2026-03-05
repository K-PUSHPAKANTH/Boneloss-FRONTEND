package com.simats.boneloss

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object FileHelper {

    /**
     * Saves a PDF file to the public Downloads directory.
     * Uses MediaStore for Android 10+ and traditional file API for older versions.
     */
    fun savePdfToDownloads(context: Context, body: ResponseBody, fileName: String): Uri? {
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ (Scoped Storage)
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    inputStream = body.byteStream()
                    outputStream = resolver.openOutputStream(uri)
                    if (outputStream != null) {
                        copyStream(inputStream, outputStream)
                        return uri
                    }
                }
            } else {
                // Legacy Android
                val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file = File(downloadsDir, fileName)
                inputStream = body.byteStream()
                outputStream = FileOutputStream(file)
                copyStream(inputStream, outputStream)
                return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
        return null
    }

    private fun copyStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(4096)
        var read: Int
        while (input.read(buffer).also { read = it } != -1) {
            output.write(buffer, 0, read)
        }
        output.flush()
    }

    /**
     * Opens a PDF file using an external PDF viewer app.
     */
    fun openPdf(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        
        try {
            context.startActivity(Intent.createChooser(intent, "Open PDF with"))
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle cases where no PDF viewer is installed
        }
    }
}
