package com.example.challengecsv

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.challengecsv.data.MilkingDAO
import com.example.challengecsv.data.MilkingItem
import com.example.challengecsv.presentation.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class MainViewModelTest {
    private val milkingDAO: MilkingDAO = mock()
    private val context: Context = mock()

    private val underTest: MainViewModel by lazy {
        MainViewModel(
            milkingDAO,
            UnconfinedTestDispatcher(),
            context
        )
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun read_from_db() = runTest {
        underTest.readDataFromDatabaseAndConvert()

        verify(milkingDAO).listMilkItemFromDatabase()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insert_to_db() = runTest {
        val milkingItemList = listOf(
            MilkingItem(
                stringId = "asdpokopasd1",
                totalAnimals = "40",
                firstMilking = "31.4",
                secondMilking = "12.4",
                average = "18.7",
                totalPerDay = "400.12",
                date = "22/09/2000"
            ),
            MilkingItem(
                stringId = "asdpokopasd1",
                totalAnimals = "40",
                firstMilking = "31.4",
                secondMilking = "12.4",
                average = "18.7",
                totalPerDay = "400.12",
                date = "22/09/2000"
            ),
            MilkingItem(
                stringId = "asdpokopasd1",
                totalAnimals = "40",
                firstMilking = "31.4",
                secondMilking = "12.4",
                average = "18.7",
                totalPerDay = "400.12",
                date = "22/09/2000"
            ),
            MilkingItem(
                stringId = "asdpokopasd1",
                totalAnimals = "40",
                firstMilking = "31.4",
                secondMilking = "12.4",
                average = "18.7",
                totalPerDay = "400.12",
                date = "22/09/2000"
            )
        )

        underTest.insertMilkingItemToDatabase(milkingItemList)

        verify(milkingDAO).insertMilkItemToDatabase(milkingItemList)
    }
}