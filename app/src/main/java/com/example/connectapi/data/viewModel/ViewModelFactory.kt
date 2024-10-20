import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.connectapi.data.service.ProductService
import com.example.connectapi.data.viewModel.ProductViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val productService: ProductService) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(productService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
