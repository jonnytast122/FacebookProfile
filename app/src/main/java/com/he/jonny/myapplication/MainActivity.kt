import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.gson.annotations.SerializedName
import com.he.jonny.myapplication.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class UserProfile(
    @SerializedName("username") val username: String,
    @SerializedName("friend_count") val friendCount: Int,
    @SerializedName("bio") val bio: String,
    @SerializedName("job") val job: String,
    @SerializedName("work_place") val workPlace: String,
    @SerializedName("marital_status") val maritalStatus: String
)

annotation class SerializedName(val value: String)

interface ApiService {
    @GET("api/fb-profile.json")
    fun getUserProfile(): Call<UserProfile>
}

object ApiClient {
    private const val BASE_URL = "https://smlp-pub.s3.ap-southeast-1.amazonaws.com/"

    fun create(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usernameTextView = findViewById<TextView>(R.id.usernameTop)
        val usernameBellowTextView = findViewById<TextView>(R.id.usernameBelow)
        val friendCountTextView = findViewById<TextView>(R.id.friendCount)
        val bioTextView = findViewById<TextView>(R.id.bio)
        val jobTextView = findViewById<TextView>(R.id.job)
        val workPlaceTextView = findViewById<TextView>(R.id.workPlace)
        val maritalStatusTextView = findViewById<TextView>(R.id.maritalStatus)

        val apiService = ApiClient.create()
        val call = apiService.getUserProfile()

        call.enqueue(object : Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                if (response.isSuccessful) {
                    val userProfile = response.body()
                    userProfile?.let {
                        usernameTextView.text = it.username
                        friendCountTextView.text = it.friendCount.toString()
                        bioTextView.text = it.bio
                        jobTextView.text = it.job
                        workPlaceTextView.text = it.workPlace
                        maritalStatusTextView.text = it.maritalStatus
                    }
                } else {

                }
            }

            override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                // Handle network errors
            }
        })
    }
}
