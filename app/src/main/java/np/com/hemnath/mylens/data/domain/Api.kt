package np.com.hemnath.mylens.data.domain

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api {
    @POST("upload/")
    suspend fun upload(
        @Query("key") key: String,
        @Query("action") action: String,
        @Query("source") source: String,
        @Query("format") format: String,
    ): Response<ApiResponse>
}