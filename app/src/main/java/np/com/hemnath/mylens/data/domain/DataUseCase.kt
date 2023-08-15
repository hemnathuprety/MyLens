package np.com.hemnath.mylens.data.domain

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.flow

class DataUseCase(context: Context) {
    private val dataRepository = DataRepository(context)

    operator fun invoke(source: String) = flow {
        Log.d("API", "searchImage: ${Gson().toJson(source)}")
        try {
            emit("Loading")
            val apiResponse = dataRepository.upload(source = source)
            if (apiResponse.isSuccessful) {
                val result = apiResponse.body() as ApiResponse
                Log.d("API", "searchImage: success")
                emit(result)
            } else {
                Log.d("API", "searchImage: error")
                emit("Error")
            }
        } catch (e: Exception) {
            Log.d("API", "searchImage: ${e.localizedMessage}")
            emit("Error")
        }
    }
}