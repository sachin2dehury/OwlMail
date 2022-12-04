package github.owlmail.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class OwlMailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owl_mail)
        findViewById<View>(R.id.tv_text).run {
        }
    }
}
