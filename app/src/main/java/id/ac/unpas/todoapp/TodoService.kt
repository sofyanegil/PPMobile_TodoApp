package id.ac.unpas.todoapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface TodoService {
    @GET("todo-item")
    suspend fun all(): TodoGetResponse

    @POST("todo-item")
    @FormUrlEncoded
    suspend fun insert(
        @Field("id") id: String,
        @Field("name") name: String,
        @Field("is_done") isDone: Int
    ): TodoPostResponse

    companion object {
        var todoService: TodoService? = null
        fun getInstance(): TodoService {
            if (todoService == null) {
                todoService = Retrofit.Builder()
                    .baseUrl("http://192.168.0.102:8001/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TodoService::class.java)
            }
            return todoService!!
        }
    }
}