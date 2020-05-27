package app.whiletrue.movielist.data.network

import android.util.Log
import app.whiletrue.movielist.BuildConfig
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import okhttp3.logging.HttpLoggingInterceptor

class LoggerInterceptor : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        val logName = "OkHTTP"
        if (!message.startsWith("{")) {
            if (!BuildConfig.DEBUG) {
                Log.d(logName, message)
            }
        } else {
            try {
                val prettyJson = GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(JsonParser.parseString(message))
                if (!BuildConfig.DEBUG) {
                    Log.d(logName, prettyJson)
                }
            } catch (e: Exception) {
                if (!BuildConfig.DEBUG) {
                    Log.d(logName, e.message ?: "???")
                    Log.d(logName, message)
                }
            }
        }
    }
}