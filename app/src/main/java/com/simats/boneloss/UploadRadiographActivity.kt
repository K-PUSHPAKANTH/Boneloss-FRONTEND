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
        uri?.let { uploadImage(it) }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            // Handle bitmap upload (omitted for brevity, or convert to Uri)
            Toast.makeText(this, "Scanning feature coming soon in full version", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_radiograph)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnChooseFile = findViewById<MaterialButton>(R.id.btn_choose_file)
        val cardScanImage = findViewById<LinearLayout>(R.id.card_scan_image)
        val cardRecentFiles = findViewById<LinearLayout>(R.id.card_recent_files)

        btnBack.setOnClickListener { finish() }

        val uploadMode = intent.getStringExtra("UPLOAD_MODE") ?: "UPLOAD"
        if (uploadMode == "SCAN") {
            // If started in SCAN mode, we could auto-launch camera or picker
            // takePictureLauncher.launch(null) 
        }

        btnChooseFile.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        cardScanImage.setOnClickListener {
            pickImageLauncher.launch("image/*") // In student dashboard, often both open picker for convenience
        }

        cardRecentFiles.setOnClickListener {
            val sharedPrefs = getSharedPreferences("user_profile", MODE_PRIVATE)
            val userId = sharedPrefs.getInt("user_id", -1)
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putExtra("user_id", userId)
            startActivity(intent)
        }
    }

    private fun uploadImage(uri: Uri) {
        val fileName = getFileName(uri)
        val requestFile = createRequestBodyFromUri(uri)
        if (requestFile == null) {
            Toast.makeText(this, "Failed to create request body from Uri", Toast.LENGTH_SHORT).show()
            return
        }

        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            fileName,
            requestFile
        )

        val sharedPrefs = getSharedPreferences("user_profile", MODE_PRIVATE)
        val userId = sharedPrefs.getInt("user_id", -1)
        val patientId = "PATIENT_${System.currentTimeMillis()}" // Placeholder for patient ID

        val patientIdBody = patientId
            .toRequestBody("text/plain".toMediaTypeOrNull())

        val userIdBody = userId.toString()
            .toRequestBody("text/plain".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiClient.apiService.uploadXray(
                    patientIdBody,
                    userIdBody,
                    multipartBody
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val analysisResponse = response.body()!!
                        val prediction = analysisResponse.prediction
                        
                        if (prediction == "Invalid Image") {
                            Toast.makeText(this@UploadRadiographActivity, "Invalid Image", Toast.LENGTH_LONG).show()
                        } else {
                            val confidence = analysisResponse.confidence ?: 0f
                            val analysisId = analysisResponse.analysis_id ?: -1
                            val averageBoneLoss = analysisResponse.average_bone_loss ?: 0f
                            val heatmapJson = com.google.gson.Gson().toJson(analysisResponse.heatmap ?: emptyList<HeatmapData>())
                            
                            // Save latest prediction for Dashboard
                            saveLatestPrediction(prediction ?: "Unknown", confidence, averageBoneLoss)

                            val intent = Intent(this@UploadRadiographActivity, SeverityClassificationActivity::class.java).apply {
                                putExtra("prediction", prediction)
                                putExtra("confidence", confidence.toDouble())
                                putExtra("average_bone_loss", averageBoneLoss.toDouble())
                                putExtra("heatmap_json", heatmapJson)
                                putExtra("ANALYSIS_ID", analysisId)
                            }
                            startActivity(intent)
                            finish()
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
                val mediaType = (contentResolver.getType(uri) ?: "image/*").toMediaTypeOrNull()
                fileBytes.toRequestBody(mediaType)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun saveLatestPrediction(prediction: String, confidence: Float, averageBoneLoss: Float) {
        val sharedPref = getSharedPreferences("BoneLossPrefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("LATEST_PREDICTION", prediction)
            putFloat("LATEST_CONFIDENCE", confidence)
            putFloat("LATEST_AVERAGE_BONE_LOSS", averageBoneLoss)
            apply()
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
