import com.example.arthur_renan_td.Entity.Product
import retrofit2.http.GET

interface ApiService {
    @GET("products/1")
    suspend fun getProduct(): Product
}