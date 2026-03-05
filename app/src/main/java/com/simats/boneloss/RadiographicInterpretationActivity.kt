package com.simats.boneloss

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.simats.boneloss.R

data class Question(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

class RadiographicInterpretationActivity : AppCompatActivity() {

    private lateinit var questions: List<Question>
    private var currentQuestionIndex = 0
    private var score = 0
    private var isAnswered = false

    // UI elements
    private lateinit var tvQuestionText: TextView
    private lateinit var rgOptions: RadioGroup
    private lateinit var rbOptions: List<RadioButton>
    private lateinit var tvQuizProgress: TextView
    private lateinit var tvScoreTrack: TextView
    private lateinit var quizProgressBar: ProgressBar
    private lateinit var layoutExplanation: LinearLayout
    private lateinit var tvExplanationText: TextView
    private lateinit var btnNext: Button
    
    private lateinit var layoutQuiz: LinearLayout
    private lateinit var layoutResults: LinearLayout
    private lateinit var tvResultTitle: TextView
    private lateinit var tvResultScore: TextView
    private lateinit var ivResultIcon: ImageView
    private lateinit var btnFinish: Button
    private lateinit var btnRetry: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_radiographic_interpretation)

        setupToolbar()
        initViews()
        initQuestions()
        setupStudyGuide()
        
        loadQuestion(0)
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initViews() {
        tvQuestionText = findViewById(R.id.tv_question_text)
        rgOptions = findViewById(R.id.rg_options)
        rbOptions = listOf(
            findViewById(R.id.rb_option1),
            findViewById(R.id.rb_option2),
            findViewById(R.id.rb_option3),
            findViewById(R.id.rb_option4)
        )
        tvQuizProgress = findViewById(R.id.tv_quiz_progress)
        tvScoreTrack = findViewById(R.id.tv_score_track)
        quizProgressBar = findViewById(R.id.quiz_progress_bar)
        layoutExplanation = findViewById(R.id.layout_explanation)
        tvExplanationText = findViewById(R.id.tv_explanation_text)
        btnNext = findViewById(R.id.btn_next_question)

        layoutQuiz = findViewById(R.id.layout_quiz)
        layoutResults = findViewById(R.id.layout_results)
        tvResultTitle = findViewById(R.id.tv_result_title)
        tvResultScore = findViewById(R.id.tv_result_score)
        ivResultIcon = findViewById(R.id.iv_result_icon)
        btnFinish = findViewById(R.id.btn_finish_lesson)
        btnRetry = findViewById(R.id.btn_retry_quiz)

        btnNext.setOnClickListener { handleNextAction() }
        btnRetry.setOnClickListener { restartQuiz() }
        btnFinish.setOnClickListener { finish() }
    }

    private fun initQuestions() {
        questions = listOf(
            Question(1, "What is the normal distance from the Alveolar Crest to the CEJ?", 
                listOf("0.5 - 1.0 mm", "1.5 - 2.0 mm", "3.0 - 4.0 mm", "At the CEJ level"), 1, 
                "In a healthy periodontium, the alveolar bone crest is located 1.5-2.0 mm apical to the cemento-enamel junction (CEJ)."),
            Question(2, "What does general widening of the PDL space typically indicate?", 
                listOf("Bone formation", "Normal aging", "Inflammation or trauma", "Healthy attachment"), 2, 
                "Widening of the periodontal ligament (PDL) space is a radiographic sign of inflammation or occlusal trauma."),
            Question(3, "How does the Lamina Dura appear in a healthy radiograph?", 
                listOf("Fuzzy and gray", "Radiolucent line", "Continuous radiopaque line", "Intermittent dots"), 2, 
                "The lamina dura is a thin, continuous radiopaque line that signifies healthy cortical bone lining the tooth socket."),
            Question(4, "Horizontal bone loss is defined as being:", 
                listOf("Parallel to the CEJ line", "At an acute angle to CEJ", "Deep within the root apex", "Touching the pulp"), 0, 
                "Horizontal bone loss occurs in a plane that is parallel to an imaginary line connecting the CEJs of adjacent teeth."),
            Question(5, "Radiographic bone level at 4mm below the CEJ indicates:", 
                listOf("Incipient loss", "Normal health", "Moderate bone loss", "Vertical defect only"), 2, 
                "Since normal is 1.5-2.0mm, a 4mm level represents approximately 2mm of clinical bone loss, categorized as moderate.")
        )
    }

    private fun setupStudyGuide() {
        val btnExpand = findViewById<RelativeLayout>(R.id.btn_expand_study_guide)
        val cvStudyGuide = findViewById<CardView>(R.id.cv_study_guide)
        val ivArrow = findViewById<ImageView>(R.id.iv_study_guide_arrow)

        btnExpand.setOnClickListener {
            if (cvStudyGuide.visibility == View.GONE) {
                cvStudyGuide.visibility = View.VISIBLE
                ivArrow.rotation = 90f
            } else {
                cvStudyGuide.visibility = View.GONE
                ivArrow.rotation = 270f
            }
        }
    }

    private fun loadQuestion(index: Int) {
        if (index >= questions.size) {
            showResults()
            return
        }

        isAnswered = false
        val q = questions[index]
        tvQuestionText.text = q.text
        tvQuizProgress.text = "Question ${index + 1} of ${questions.size}"
        tvScoreTrack.text = "Score: $score/${questions.size}"
        quizProgressBar.progress = index + 1
        
        layoutExplanation.visibility = View.GONE
        btnNext.text = "Check Answer"
        
        rgOptions.clearCheck()
        for (i in rbOptions.indices) {
            rbOptions[i].text = q.options[i]
            rbOptions[i].isEnabled = true
            rbOptions[i].backgroundTintList = null
            rbOptions[i].setTextColor(getColor(R.color.text_dark))
        }
    }

    private fun handleNextAction() {
        if (!isAnswered) {
            val selectedId = rgOptions.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show()
                return
            }
            
            checkAnswer(selectedId)
        } else {
            currentQuestionIndex++
            loadQuestion(currentQuestionIndex)
        }
    }

    private fun checkAnswer(selectedId: Int) {
        isAnswered = true
        val q = questions[currentQuestionIndex]
        val selectedIndex = rbOptions.indexOf(findViewById<RadioButton>(selectedId))
        
        // Disable options
        for (rb in rbOptions) rb.isEnabled = false

        if (selectedIndex == q.correctIndex) {
            score++
            rbOptions[selectedIndex].backgroundTintList = ColorStateList.valueOf(getColor(R.color.success_green))
            rbOptions[selectedIndex].setTextColor(getColor(R.color.white))
            
            // Animation for correct answer
            val bounce = AnimationUtils.loadAnimation(this, android.R.anim.fade_in) 
            rbOptions[selectedIndex].startAnimation(bounce)
        } else {
            rbOptions[selectedIndex].backgroundTintList = ColorStateList.valueOf(getColor(R.color.error_red))
            rbOptions[selectedIndex].setTextColor(getColor(R.color.white))
            
            // Highlight correct one anyway
            rbOptions[q.correctIndex].backgroundTintList = ColorStateList.valueOf(getColor(R.color.success_green))
            rbOptions[q.correctIndex].setTextColor(getColor(R.color.white))
        }

        tvExplanationText.text = q.explanation
        layoutExplanation.visibility = View.VISIBLE
        btnNext.text = if (currentQuestionIndex == questions.size - 1) "Finish Quiz" else "Next Question"
        tvScoreTrack.text = "Score: $score/${questions.size}"
    }

    private fun showResults() {
        layoutQuiz.visibility = View.GONE
        layoutResults.visibility = View.VISIBLE
        
        tvResultScore.text = "Your Score: $score/${questions.size}"
        
        if (score >= 4) {
            tvResultTitle.text = "Lesson Completed!"
            ivResultIcon.setImageResource(R.drawable.ic_check_filled)
            ivResultIcon.imageTintList = ColorStateList.valueOf(getColor(R.color.success_green))
            btnFinish.visibility = View.VISIBLE
            btnRetry.visibility = View.GONE
            
            saveCompletionStatus()
        } else {
            tvResultTitle.text = "Keep Practicing!"
            ivResultIcon.setImageResource(R.drawable.ic_camera_blue) // Placeholder for info/retry icon
            ivResultIcon.imageTintList = ColorStateList.valueOf(getColor(R.color.primary_blue))
            btnFinish.visibility = View.GONE
            btnRetry.visibility = View.VISIBLE
        }
    }

    private fun restartQuiz() {
        score = 0
        currentQuestionIndex = 0
        layoutResults.visibility = View.GONE
        layoutQuiz.visibility = View.VISIBLE
        loadQuestion(0)
    }

    private fun saveCompletionStatus() {
        val sharedPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean("radiographic_lesson_completed", true).apply()
        Toast.makeText(this, "Progress Saved!", Toast.LENGTH_SHORT).show()
    }
}
