package ca.gbc.comp3074.roomcustomerlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.gbc.comp3074.roomcustomerlab.data.AppDatabase
import ca.gbc.comp3074.roomcustomerlab.ui.CustomerApp
import ca.gbc.comp3074.roomcustomerlab.ui.CustomerViewModel
import ca.gbc.comp3074.roomcustomerlab.ui.CustomerViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(applicationContext)

        setContent {
            MaterialTheme {
                val customerViewModel: CustomerViewModel = viewModel(
                    factory = CustomerViewModelFactory(database.customerDao())
                )
                CustomerApp(customerViewModel)
            }
        }
    }
}
