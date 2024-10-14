package com.example.lab0310bt02

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.lab0310bt02.ui.theme.Lab0310bt02Theme
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.TransitionManager

import androidx.core.content.ContextCompat
class MainActivity : AppCompatActivity() {
    private var isMoved = false
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var button: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)
        button = findViewById(R.id.button)


        // Thiết lập sự kiện imageview khi click
        imageView.setBackgroundResource(R.drawable.animated_button) // đặt drawable vào imageView
        val imageViewAnimation = imageView.background as? AnimationDrawable

        imageView.setOnClickListener {
            imageViewAnimation?.let { animation ->
                if (animation.isRunning) {
                    animation.stop()
                } else {
                    animation.start()
                }
            } ?: run {
                // Nếu imageViewAnimation là null
                textView.text = "Không thể lấy AnimationDrawable từ ImageView"
            }
        }

        button.setOnClickListener{

            animateTextView(textView)
            performTransition()
        }

    }

    private fun performTransition() {
        //tạo constraintSet để thay đổi layout
        val constraintSet = ConstraintSet()
        val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout)

        //sao chép trạng thái hiện tại
        constraintSet.clone(constraintLayout)

        //thay đổi vị trí của textview và button
        if(!isMoved){
            constraintSet.connect(textView.id, ConstraintSet.TOP, constraintLayout.id, ConstraintSet.TOP, 200)
            constraintSet.connect(button.id, ConstraintSet.TOP, textView.id, ConstraintSet.BOTTOM,10)
            constraintSet.connect(button.id, ConstraintSet.END, constraintLayout.id, ConstraintSet.END,10)
        } else{
            constraintSet.connect(textView.id, ConstraintSet.TOP, constraintLayout.id, ConstraintSet.TOP, 16)
            constraintSet.connect(button.id, ConstraintSet.TOP, textView.id, ConstraintSet.BOTTOM,0)
            constraintSet.connect(button.id, ConstraintSet.START, constraintLayout.id, ConstraintSet.START,16)
        }

        //áp dụng chuyển đổi với TransitioManager
        TransitionManager.beginDelayedTransition(constraintLayout)
        constraintSet.applyTo(constraintLayout)

        //đảo ngược trạng thái
        isMoved = !isMoved
    }

    private fun animateTextView(textView: TextView) {
        textView.animate()
            .scaleX(1.5f) // thay đổi kích thước chiều rộng
            .scaleY(1.5f) // thay đổi kích thước chiều cao
            .setDuration(300) // thời gian thay đổi kích thước
            .setInterpolator(AccelerateDecelerateInterpolator()) // hiệu ứng thay đổi kích thước
            .withEndAction {
                textView.setTextColor(ContextCompat.getColor(this, R.color.red)) // thay đổi màu chữ


                //tạo animation vị trí
                val constraintLayout = findViewById<ConstraintLayout>(R.id.constraintLayout) // sửa tên thành constraintLayout
                val constraintSet = ConstraintSet()
                constraintSet.clone(constraintLayout)

                //tạo vị trí mới cho text view
                constraintSet.connect(textView.id, ConstraintSet.TOP, constraintLayout.id, ConstraintSet.TOP, 200)
                constraintSet.connect(textView.id, ConstraintSet.START, constraintLayout.id, ConstraintSet.START, 160)

                constraintSet.applyTo(constraintLayout)
            }
    }

}
