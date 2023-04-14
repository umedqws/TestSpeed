import android.os.AsyncTask
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class SpeedTestTask : AsyncTask<Void?, Void?, String>() {

    override fun doInBackground(vararg params: Void?): String {
        var speed = 0.0
        try {
            val url = "https://speedtest.net/"
            val client = OkHttpClient.Builder().build()
            val request = Request.Builder()
                .url(url)
                .build()
            var totalTime = 0L
            var totalSize = 0L
            repeat(3) {
                val startTime = System.nanoTime()
                val response = client.newCall(request).execute()
                response.body?.byteStream()?.readBytes()
                val endTime = System.nanoTime()
                val elapsedTime = endTime - startTime
                totalTime += elapsedTime
                totalSize += response.body?.contentLength() ?: 0L
                response.close()
            }
            speed = (totalSize.toDouble() / 1024.0 / 1024.0) / ((totalTime.toDouble() / 1000000000.0) / 3)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return String.format("%.2f", speed)
    }
}
