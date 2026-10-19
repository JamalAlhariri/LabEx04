
package ca.gbc.comp3074.roomcustomerlab.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ca.gbc.comp3074.roomcustomerlab.data.Customer

@Composable
fun CustomerApp(viewModel: CustomerViewModel) {

    // Get customers from the database
    val customers by viewModel.searchResults.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    CustomerScreen(
        customers = customers,
        onAddCustomer = viewModel::addCustomer,
        onDeleteCustomer = viewModel::deleteCustomer,
        onSearchCustomers = viewModel::searchCustomers,
        onClearSearch = viewModel::clearSearch,
        onUpdateCustomer = viewModel::updateCustomer
    )
}

@Composable
fun CustomerScreen(
    customers: List<Customer>,
    onAddCustomer: (String, Int, Boolean) -> Unit,
    onDeleteCustomer: (Customer) -> Unit,
    onSearchCustomers: (String) -> Unit,
    onClearSearch: () -> Unit,
    onUpdateCustomer: (Customer) -> Unit
) {

    // Values entered by the user
    var name by rememberSaveable { mutableStateOf("") }
    var ageText by rememberSaveable { mutableStateOf("") }
    var isActive by rememberSaveable { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf("") }
    var searchText by rememberSaveable { mutableStateOf("") }

    // Used when editing a customer
    var editingCustomerId by rememberSaveable { mutableIntStateOf(0) }

    // Clear the form
    fun clearForm() {
        name = ""
        ageText = ""
        isActive = false
        editingCustomerId = 0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
    ) {

        Text(
            text = "Customer Database App",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(10.dp))

        // Search for customers
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search customer") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = {
                    onSearchCustomers(searchText.trim())
                }
            ) {
                Text("Search")
            }

            OutlinedButton(
                onClick = {
                    searchText = ""
                    onClearSearch()
                }
            ) {
                Text("Clear")
            }
        }

        Spacer(Modifier.height(12.dp))

        // Customer name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Customer name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // Customer age
        OutlinedTextField(
            value = ageText,
            onValueChange = { value ->
                if (value.all { it.isDigit() }) {
                    ageText = value
                }
            },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Active or inactive customer
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Active customer")

            Spacer(Modifier.width(8.dp))

            Switch(
                checked = isActive,
                onCheckedChange = { isActive = it }
            )
        }

        // Add or update a customer
        Button(
            onClick = {

                val age = ageText.toIntOrNull()

                if (name.isBlank() || age == null) {
                    message = "Please enter a name and age"
                } else {

                    if (editingCustomerId == 0) {

                        onAddCustomer(
                            name.trim(),
                            age,
                            isActive
                        )

                        message = "Customer added"

                    } else {

                        onUpdateCustomer(
                            Customer(
                                id = editingCustomerId,
                                name = name.trim(),
                                age = age,
                                isActive = isActive
                            )
                        )

                        message = "Customer updated"
                    }

                    clearForm()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (editingCustomerId == 0)
                    "Add Customer"
                else
                    "Save Changes"
            )
        }

        // Cancel editing
        if (editingCustomerId != 0) {

            TextButton(
                onClick = {
                    clearForm()
                    message = "Edit cancelled"
                }
            ) {
                Text("Cancel")
            }
        }

        // Show a message
        if (message.isNotBlank()) {
            Text(message)
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Customer List",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        // Display customers
        CustomerList(
            customers = customers,
            onDeleteCustomer = onDeleteCustomer,
            onEditCustomer = { customer ->

                editingCustomerId = customer.id
                name = customer.name
                ageText = customer.age.toString()
                isActive = customer.isActive

                message = "Editing customer"
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CustomerList(
    customers: List<Customer>,
    onDeleteCustomer: (Customer) -> Unit,
    onEditCustomer: (Customer) -> Unit,
    modifier: Modifier = Modifier
) {

    if (customers.isEmpty()) {

        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text("No customers found")
        }

    } else {

        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(
                items = customers,
                key = { customer -> customer.id }
            ) { customer ->

                CustomerRow(
                    customer = customer,
                    onEdit = {
                        onEditCustomer(customer)
                    },
                    onDelete = {
                        onDeleteCustomer(customer)
                    }
                )
            }
        }
    }
}

@Composable
fun CustomerRow(
    customer: Customer,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    customer.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Text("ID: ${customer.id}")

                Text("Age: ${customer.age}")

                Text(
                    if (customer.isActive)
                        "Active"
                    else
                        "Inactive"
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {

                TextButton(
                    onClick = onEdit
                ) {
                    Text("Edit")
                }

                TextButton(
                    onClick = onDelete
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
