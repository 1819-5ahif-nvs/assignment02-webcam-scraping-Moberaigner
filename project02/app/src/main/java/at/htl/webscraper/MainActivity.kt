package at.htl.webscraper

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.VideoView



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val uri = Uri.parse("http://172.18.3.169:8000")
        val video = findViewById(R.id.videoView) as VideoView
        video.setVideoURI(uri)
        video.start()
    }
}
