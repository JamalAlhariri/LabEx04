package ca.gbc.comp3074.roomcustomerlab

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ca.gbc.comp3074.roomcustomerlab.data.AppDatabase
import ca.gbc.comp3074.roomcustomerlab.data.Customer
import ca.gbc.comp3074.roomcustomerlab.data.CustomerDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CustomerDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: CustomerDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao = database.customerDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertShowsCustomer() = runBlocking {
        dao.insert(Customer(name = "Amal", age = 22, isActive = true))
        val list = dao.getAllCustomers().first()
        assertEquals(1, list.size)
        assertEquals("Amal", list[0].name)
    }

    @Test
    fun searchMatchesPartOfName() = runBlocking {
        dao.insert(Customer(name = "Maria", age = 21, isActive = false))
        dao.insert(Customer(name = "Luca", age = 24, isActive = true))
        val list = dao.searchCustomers("mar").first()
        assertEquals(1, list.size)
        assertEquals("Maria", list[0].name)
    }

    @Test
    fun updateKeepsCustomerId() = runBlocking {
        dao.insert(Customer(name = "Tina", age = 20, isActive = false))
        val original = dao.getAllCustomers().first().first()
        dao.update(original.copy(name = "Tina Smith", isActive = true))
        val changed = dao.getAllCustomers().first().first()
        assertEquals(original.id, changed.id)
        assertEquals("Tina Smith", changed.name)
        assertEquals(true, changed.isActive)
    }

    @Test
    fun deleteRemovesCustomer() = runBlocking {
        dao.insert(Customer(name = "Jae", age = 19, isActive = true))
        val saved = dao.getAllCustomers().first().first()
        dao.delete(saved)
        assertEquals(0, dao.getAllCustomers().first().size)
    }
}
