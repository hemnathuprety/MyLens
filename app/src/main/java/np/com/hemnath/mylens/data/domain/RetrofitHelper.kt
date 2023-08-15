package np.com.hemnath.mylens.data.domain

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class RetrofitHelper {

    companion object {

        fun provideApi(context: Context): Api {
           val retrofit: Retrofit = provideRetrofit(context)
            return retrofit.create(Api::class.java)
        }

        private fun provideRetrofit(context: Context): Retrofit {
            val client: OkHttpClient =
                provideOkHttpClient(provideHeaderInterceptor(), provideCache(context))

            return Retrofit.Builder().baseUrl("").client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).build()
        }

        private val READ_TIMEOUT = 30
        private val WRITE_TIMEOUT = 30
        private val CONNECTION_TIMEOUT = 10
        private val CACHE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB

        private fun provideOkHttpClient(
            headerInterceptor: Interceptor,
            cache: Cache
        ): OkHttpClient {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClientBuilder = OkHttpClient().newBuilder()
            okHttpClientBuilder.connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            okHttpClientBuilder.readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            okHttpClientBuilder.writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            okHttpClientBuilder.cache(cache)
            okHttpClientBuilder.addInterceptor(headerInterceptor)
            okHttpClientBuilder.addInterceptor(interceptor)

            return okHttpClientBuilder.build()
        }

        private fun provideHeaderInterceptor(): Interceptor {
            return Interceptor {
                val requestBuilder = it.request().newBuilder()
                //hear you can add all headers you want by calling 'requestBuilder.addHeader(name ,  value)'
                it.proceed(requestBuilder.build())
            }
        }

        private fun provideCache(context: Context): Cache {
            val httpCacheDirectory = File(context.cacheDir.absolutePath, "HttpCache")
            return Cache(httpCacheDirectory, CACHE_SIZE_BYTES)
        }

    }
}