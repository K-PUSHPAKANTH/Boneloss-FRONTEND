package com.simats.boneloss

import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class AnalysisLoadingActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private lateinit var tvPercent: TextView
    private lateinit var itemPreprocessing: LinearLayout
    private lateinit var itemDetection: LinearLayout
    private lateinit var itemIdentification: LinearLayout
    private lateinit var itemCalculation: LinearLayout

    private lateinit var iconPreprocessing: ImageView
    private lateinit var iconDetection: ImageView
    private lateinit var iconIdentification: ImageView
    private lateinit var iconCalculation: ImageView

    private lateinit var btnGoToDashboard: View
    private lateinit var tvFooterNote: View
    private lateinit var errorLayout: View
    private lateinit var btnTryAgain: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis_loading)

        progressBar = findViewById(R.id.progress_bar)
        tvPercent = findViewById(R.id.tv_progress_percent)
        
        itemPreprocessing = findViewById(R.id.item_preprocessing)
        itemDetection = findViewById(R.id.item_detection)
        itemIdentification = findViewById(R.id.item_identification)
        itemCalculation = findViewById(R.id.item_calculation)

        iconPreprocessing = findViewById(R.id.icon_preprocessing)
        iconDetection = findViewById(R.id.icon_detection)
        iconIdentification = findViewById(R.id.icon_identification)
        iconCalculation = findViewById(R.id.icon_calculation)

        btnGoToDashboard = findViewById(R.id.btn_go_to_dashboard)
        tvFooterNote = findViewById(R.id.tv_footer_note)
        errorLayout = findViewById(R.id.error_layout)
        btnTryAgain = findViewById(R.id.btn_try_again)

        val imageUriString = intent.getStringExtra("IMAGE_URI")

        btnTryAgain.setOnClickListener {
            finish() // Go back to re-upload
        }

        btnGoToDashboard.setOnClickListener {
            val intent = Intent(this, ToothDetectionActivity::class.java)
            intent.putExtra("IMAGE_URI", imageUriString)
            startActivity(intent)
            finish()
        }

        startLoadingAnimation()
    }

    private fun startLoadingAnimation() {
        val successColor = ContextCompat.getColor(this, R.color.success_green)
        val successColorStateList = ColorStateList.valueOf(successColor)
        
        val animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 4000 
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Int
            progressBar.progress = progress
            tvPercent.text = "$progress%"

            // Sequentially update states
            when {
                progress >= 25 -> {
                    if (!isImageValid(intent.getStringExtra("IMAGE_URI"))) {
                        animator.cancel()
                        showErrorState()
                        return@addUpdateListener
                    }
                    itemPreprocessing.alpha = 1.0f
                    iconPreprocessing.setImageResource(R.drawable.ic_check_circle)
                    iconPreprocessing.imageTintList = successColorStateList
                }
            }
            when {
                progress >= 50 -> {
                    itemDetection.alpha = 1.0f
                    iconDetection.setImageResource(R.drawable.ic_check_circle)
                    iconDetection.imageTintList = successColorStateList
                }
            }
            when {
                progress >= 75 -> {
                    itemIdentification.alpha = 1.0f
                    iconIdentification.setImageResource(R.drawable.ic_check_circle)
                    iconIdentification.imageTintList = successColorStateList
                }
            }
            when {
                progress >= 95 -> {
                    itemCalculation.alpha = 1.0f
                    iconCalculation.setImageResource(R.drawable.ic_check_circle)
                    iconCalculation.imageTintList = successColorStateList
                }
            }
        }

        animator.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                tvFooterNote.visibility = View.GONE
                btnGoToDashboard.visibility = View.VISIBLE
            }
        })

        animator.start()
    }

    private fun isImageValid(uriString: String?): Boolean {
        if (uriString == null) return false
        // Mock validation: In a real app, this would be an API call or localized ML check.
        // For this mock, we'll check if the filename contains "teeth" or "tooth" to consider it valid.
        val uri = Uri.parse(uriString)
        val path = uri.path?.lowercase() ?: ""

        // The image is considered valid only if the path contains "teeth" or "tooth"
        return path.contains("teeth") || path.contains("tooth")
    }

    private fun showErrorState() {
        errorLayout.visibility = View.VISIBLE
    }
}
