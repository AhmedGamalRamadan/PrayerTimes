package com.ag.projects.aatask.util.helper

import androidx.lifecycle.ViewModel
import com.google.gson.JsonParseException
import kotlinx.coroutines.flow.MutableStateFlow
import com.ag.projects.aatask.util.Result
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> ViewModel.handleRequest(
    request: suspend () -> T,
    state: MutableStateFlow<Result<T>>
) {
    try {
        state.emit(Result.Success(request()))
    } catch (networkException: IOException) {
        state.emit(Result.Error("Network error", networkException))
    } catch (jsonException: JsonParseException) {
        state.emit(Result.Error("Data parsing error", jsonException))
    } catch (httpException: HttpException) {
        // Extract only the "message" field from the error response
        val errorMessage = try {
            val errorBody = httpException.response()?.errorBody()?.string()
            val jsonObject = JSONObject(errorBody)
            jsonObject.optString("message", "Unknown error")
        } catch (e: Exception) {
            "Unknown error"
        }
        state.emit(Result.Error(errorMessage, httpException))
    } catch (e: Exception) {
        state.emit(Result.Error("Unexpected error", e))
    }
}