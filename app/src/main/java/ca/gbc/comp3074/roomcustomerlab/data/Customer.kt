package ca.gbc.comp3074.roomcustomerlab.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// One customer object becomes one row in the customers table.
@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val age: Int,
    val isActive: Boolean
)
