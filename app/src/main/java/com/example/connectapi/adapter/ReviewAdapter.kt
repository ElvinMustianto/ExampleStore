import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.*
import com.example.connectapi.R
import com.example.connectapi.dto.model.Reviews
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ReviewAdapter(private var reviews: List<Reviews>): Adapter<ReviewAdapter.ReviewsViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateReviews(newReviews: List<Reviews>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
    class ReviewsViewHolder(view: View): ViewHolder(view) {
        private val reviewerName: TextView = view.findViewById(R.id.reviewerName)
        private val ratingBar : RatingBar = view.findViewById(R.id.ratingBar)
        private val comment : TextView = view.findViewById(R.id.comment)
        private val date : TextView = view.findViewById(R.id.date)

        fun bind(reviews: Reviews) {
            reviewerName.text = reviews.reviewerName
            ratingBar.rating = reviews.rating.toFloat()
            comment.text = reviews.comment

            // Parse and format the date
            val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            originalFormat.timeZone = TimeZone.getTimeZone("UTC") // Set timezone if necessary
            val targetFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

            val formattedDate = try {
                val date = originalFormat.parse(reviews.date)
                date?.let { targetFormat.format(it) }
            } catch (e: Exception) {
                reviews.date // If parsing fails, show original
            }

            date.text = formattedDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_review, parent, false)
        return ReviewsViewHolder(view)
    }

    override fun getItemCount(): Int = reviews.size

    override fun onBindViewHolder(holder: ReviewsViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
    }
}
