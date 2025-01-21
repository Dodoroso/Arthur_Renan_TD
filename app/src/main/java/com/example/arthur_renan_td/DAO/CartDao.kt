import androidx.room.*

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Query("SELECT * FROM cart_items")
    suspend fun getAllCartItems(): List<CartItem>

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)
}
