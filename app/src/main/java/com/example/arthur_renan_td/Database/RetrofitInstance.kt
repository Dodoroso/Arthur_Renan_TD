import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private const val BASE_URL = "https://fakestoreapi.com/"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .callTimeout(25L, TimeUnit.SECONDS)
            .readTimeout(25L, TimeUnit.SECONDS)
            .connectTimeout(25L, TimeUnit.SECONDS)
            .build()
    }

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Ajout du client configuré
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
