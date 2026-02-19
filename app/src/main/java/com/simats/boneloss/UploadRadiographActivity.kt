package com.simats.boneloss

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class UploadRadiographActivity : AppCompatActivity() {

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_radiograph)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnChooseFile = findViewById<MaterialButton>(R.id.btn_choose_file)
        val cardScanImage = findViewById<LinearLayout>(R.id.card_scan_image)
        val cardRecentFiles = findViewById<LinearLayout>(R.id.card_recent_files)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnChooseFile.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        cardScanImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        cardRecentFiles.setOnClickListener {
            val intent = Intent(this, PatientHistoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun uploadImage(uri: Uri) {
        val requestBody = createRequestBodyFromUri(uri)
        if (requestBody == null) {
            Toast.makeText(this, "Failed to create request body from Uri", Toast.LENGTH_SHORT).show()
            return
        }

        val fileName = getFileName(uri)
        val part = MultipartBody.Part.createFormData("file", fileName, requestBody)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.uploadImage(part)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val prediction = response.body()!!.prediction
                        if (prediction == "Invalid Image") {
                            Toast.makeText(this@UploadRadiographActivity, "Invalid Image", Toast.LENGTH_LONG).show()
                        } else {
                            val confidence = response.body()!!.confidence
                            val intent = Intent(this@UploadRadiographActivity, PredictionActivity::class.java)
                            intent.putExtra("PREDICTION", prediction)
                            intent.putExtra("CONFIDENCE", confidence)
                            startActivity(intent)
                        }
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "No error body"
                        Log.e("UploadError", "Code: ${response.code()}, Message: ${response.message()}, Body: $errorBody")
                        Toast.makeText(this@UploadRadiographActivity, "Invalid Image", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.printStackTrace()
                    val errorMessage = if (e is java.net.ConnectException || e is java.net.SocketTimeoutException) {
                        "Network Error: Could not connect to the server. Please check your network connection and ensure the server is running."
                    } else {
                        "Invalid Image"
                    }
                    Toast.makeText(this@UploadRadiographActivity, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createRequestBodyFromUri(uri: Uri): RequestBody? {
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                val fileBytes = inputStream.readBytes()
                fileBytes.toRequestBody(contentResolver.getType(uri)?.toMediaTypeOrNull())
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex)
                    }
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != null && cut != -1) {
                result = result!!.substring(cut + 1)
            }
        }
        return result ?: "temp_image.jpg"
    }
}
