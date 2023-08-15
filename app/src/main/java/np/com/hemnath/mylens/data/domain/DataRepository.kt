package np.com.hemnath.mylens.data.domain

import android.content.Context
import retrofit2.Response

class DataRepository(context: Context) {
    private val apIs = RetrofitHelper.provideApi(context)

    suspend fun upload(
        source: String,
        token: String = "6d207e02198a847aa98d0a2a901485a5",
        action: String = "upload",
        format: String = "json",
    ): Response<ApiResponse> {
        return apIs.upload(token, action, source, format)
    }
}