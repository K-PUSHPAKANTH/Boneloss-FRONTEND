package com.simats.boneloss

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClinicalInterpretationActivity : AppCompatActivity() {

    private lateinit var tvClinicalSummary: TextView
    private lateinit var tvPrimaryDiagnosis: TextView
    private lateinit var tvIcd10: TextView
    private lateinit var tvStage: TextView
    private lateinit var tvGrade: TextView
    private lateinit var tvExtent: TextView
    private lateinit var tvFindingsDescription: TextView
    private lateinit var pbLoading: ProgressBar
    private lateinit var scrollView: NestedScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinical_interpretation)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val btnTreatment = findViewById<Button>(R.id.btn_treatment_recommendations)
        
        tvClinicalSummary = findViewById(R.id.tv_clinical_summary)
        tvPrimaryDiagnosis = findViewById(R.id.tv_primary_diagnosis)
        tvIcd10 = findViewById(R.id.tv_icd_10)
        tvStage = findViewById(R.id.tv_stage)
        tvGrade = findViewById(R.id.tv_grade)
        tvExtent = findViewById(R.id.tv_extent)
        tvFindingsDescription = findViewById(R.id.tv_findings_description)
        pbLoading = findViewById(R.id.pb_loading)
        scrollView = findViewById(R.id.scroll_view)

        val analysisId = intent.getIntExtra("ANALYSIS_ID", -1)

        if (analysisId != -1) {
            fetchClinicalInterpretation(analysisId)
        } else {
            Toast.makeText(this, "Error: No Analysis ID found", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnTreatment.setOnClickListener {
            val intent = android.content.Intent(this, TreatmentRecommendationsActivity::class.java)
            intent.putExtra("ANALYSIS_ID", analysisId)
            startActivity(intent)
        }
    }

    private fun fetchClinicalInterpretation(analysisId: Int) {
        pbLoading.visibility = View.VISIBLE
        scrollView.alpha = 0.3f
        scrollView.isEnabled = false

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = ApiClient.apiService.getClinicalInterpretation(analysisId)
                withContext(Dispatchers.Main) {
                    pbLoading.visibility = View.GONE
                    scrollView.alpha = 1.0f
                    scrollView.isEnabled = true

                    if (response.isSuccessful && response.body() != null) {
                        bindData(response.body()!!)
                    } else {
                        Toast.makeText(this@ClinicalInterpretationActivity, "Failed to load clinical data", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    pbLoading.visibility = View.GONE
                    scrollView.alpha = 1.0f
                    scrollView.isEnabled = true
                    Toast.makeText(this@ClinicalInterpretationActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bindData(data: ClinicalInterpretationResponse) {
        tvClinicalSummary.text = data.clinical_summary ?: "No clinical summary available."
        tvPrimaryDiagnosis.text = data.primary_diagnosis ?: "Inconclusive"
        tvIcd10.text = "ICD-10: ${data.icd_10 ?: "N/A"}"
        tvStage.text = data.stage ?: "N/A"
        tvGrade.text = data.grade ?: "N/A"
        tvExtent.text = data.extent ?: "N/A"
        tvFindingsDescription.text = data.clinical_findings ?: "No detailed findings available."
    }
}
