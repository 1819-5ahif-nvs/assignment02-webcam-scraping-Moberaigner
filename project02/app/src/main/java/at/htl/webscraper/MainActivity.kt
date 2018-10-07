package at.htl.webscraper

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.VideoView
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import android.os.StrictMode


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        val video = findViewById(R.id.videoView) as VideoView

        val t = object : Thread() {
            override fun run() {
                try {
                    while (!isInterrupted) {
                        runOnUiThread {
                            video.setVideoPath(update())
                            video.start()
                        }
                        Thread.sleep(60000) // wait 60s
                    }
                } catch (e: InterruptedException) {
                }

            }
        }
        t.start()
    }

    fun update(): String {
        //get actual link for the video
        val doc: Document
        var output = ""
        try {
            doc = Jsoup.connect("https://webtv.feratel.com/webtv/?cam=5132&design=v3&c0=0&c2=1&lg=en&s=0").get()
            output = doc.getElementById("fer_video").select("source").attr("src")
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return output
    }

}
