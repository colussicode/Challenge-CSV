package com.example.challengecsv.presentation

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.challengecsv.data.DailyControlItem
import com.example.challengecsv.data.MilkingDAO
import com.example.challengecsv.data.MilkingItem
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader

class MainViewModel(
    private val milkingDAO: MilkingDAO,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val context: Context
) : ViewModel() {

    val barEntriesListLiveData: MutableLiveData<List<BarEntry>> = MutableLiveData<List<BarEntry>>()
    private val barEntriesList: ArrayList<BarEntry> = arrayListOf()
    var totalLiveData = MutableLiveData(0.0f)
    var maxLiveData = MutableLiveData(0.0f)
    var minLiveData = MutableLiveData(0.0f)
    var averageLiveData = MutableLiveData(0.0f)
    private var total = 0.0f

    fun readDataFromDatabaseAndConvert(){
        viewModelScope.launch(dispatcher) {
            val list = milkingDAO.listMilkItemFromDatabase()
            convertToBarEntry(list)
            fillValues(list)
            barEntriesListLiveData.postValue(barEntriesList)
        }
    }

    private fun convertToBarEntry(list: List<MilkingItem>) {
        list.forEach { item ->
            val barEntry = BarEntry(item.secondMilking.toFloat(), item.totalPerDay.toFloat())
            barEntriesList.add(barEntry)
        }
    }

    private fun fillValues(list: List<MilkingItem>) {
        list.forEach { item ->
            total += (item.totalPerDay.toFloat() + item.secondMilking.toFloat())
            if(item.totalPerDay.toFloat() > maxLiveData.value!!) {
                maxLiveData.postValue(item.totalPerDay.toFloat())
            }
            if(item.totalAnimals.toFloat() > minLiveData.value!!) {
                minLiveData.postValue(item.firstMilking.toFloat())
            }
            if(item.average.toFloat() > averageLiveData.value!!) {
                averageLiveData.postValue(item.average.toFloat())
            }
        }
        totalLiveData.postValue(total)
    }

    fun insertMilkingItemToDatabase(list: List<MilkingItem>) {
        viewModelScope.launch(dispatcher) {
            milkingDAO.insertMilkItemToDatabase(list)
        }
    }

    private fun insertDailyControlItemToDatabase(list: List<DailyControlItem>) {
        viewModelScope.launch(dispatcher) {
            milkingDAO.insertDailyControlItemToDatabase(list)
        }
    }

    fun readDataFromCSV(csvFilePath: String) {
        val bufferReader = BufferedReader(context.assets.open(csvFilePath).reader())
        val csvParser = CSVParser.parse(bufferReader, CSVFormat.DEFAULT.withIgnoreHeaderCase().withTrim())

        val milkingItemList = mutableListOf<MilkingItem>()
        val dailyControlItemList = mutableListOf<DailyControlItem>()

        if(csvFilePath == "prod_diaria.csv") {
            csvParser.forEach {
                val milkingItem = MilkingItem(
                    stringId = it.get(0),
                    totalAnimals = it.get(1),
                    firstMilking = convertToDecimalFormat(it.get(2)),
                    secondMilking = convertToDecimalFormat(it.get(3)),
                    totalPerDay = convertToDecimalFormat(it.get(4)),
                    average = convertToDecimalFormat(it.get(5)),
                    date = it.get(6)
                )
                milkingItemList.add(milkingItem)
            }
            milkingItemList.removeFirst()
            insertMilkingItemToDatabase(milkingItemList)
        } else {
            csvParser.forEach {
                val dailyControlItem = DailyControlItem(
                    stringId = it.get(0),
                    microchip = it.get(1),
                    animalNumber = it.get(2),
                    name = it.get(3),
                    birthDate = it.get(4),
                    bay = it.get(5),
                    firstMilking = convertToDecimalFormat(it.get(6)),
                    secondMilking = convertToDecimalFormat(it.get(7)),
                    total = convertToDecimalFormat(it.get(8)),
                    controlDate = it.get(9),
                    del = convertToDecimalFormat(it.get(10))
                )
                dailyControlItemList.add(dailyControlItem)
            }
                dailyControlItemList.removeFirst()
            viewModelScope.launch(dispatcher) {
                insertDailyControlItemToDatabase(dailyControlItemList)
            }
        }
    }

    private fun convertToDecimalFormat(value: String): String {
        return value.replace(",", ".")
    }
}