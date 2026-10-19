package ca.gbc.comp3074.roomcustomerlab.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ca.gbc.comp3074.roomcustomerlab.data.Customer
import ca.gbc.comp3074.roomcustomerlab.data.CustomerDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class CustomerViewModel(
    private val customerDao: CustomerDao
) : ViewModel() {

    // Room automatically sends a new list when the table changes.
    val customers: Flow<List<Customer>> = customerDao.getAllCustomers()

    // The search controls which Room Flow the screen observes.
    private val searchText = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: Flow<List<Customer>> = searchText.flatMapLatest { query ->
        if (query.isBlank()) customers else customerDao.searchCustomers(query)
    }

    fun searchCustomers(text: String) {
        searchText.value = text.trim()
    }

    fun clearSearch() {
        searchText.value = ""
    }

    fun addCustomer(name: String, age: Int, isActive: Boolean) {
        viewModelScope.launch {
            customerDao.insert(Customer(name = name, age = age, isActive = isActive))
        }
    }

    fun deleteCustomer(customer: Customer) {
        viewModelScope.launch {
            customerDao.delete(customer)
        }
    }

    // Included for the optional update challenge.
    fun updateCustomer(customer: Customer) {
        viewModelScope.launch {
            customerDao.update(customer)
        }
    }
}

// A factory is needed because our ViewModel takes a DAO in its constructor.
class CustomerViewModelFactory(
    private val customerDao: CustomerDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CustomerViewModel::class.java)) {
            return CustomerViewModel(customerDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
