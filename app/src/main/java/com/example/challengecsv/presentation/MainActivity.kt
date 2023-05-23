package com.example.challengecsv.presentation

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.challengecsv.MainApplication
import com.example.challengecsv.data.MilkingDAO
import com.example.challengecsv.databinding.ActivityMainBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class MainActivity : AppCompatActivity() {

    private lateinit var dao: MilkingDAO
    private lateinit var binding: ActivityMainBinding

    private val factory = object: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(
                milkingDAO = dao,
                context = applicationContext
            ) as T
        }
    }
    private val viewModel: MainViewModel by viewModels{ factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = (application as MainApplication).milkingDAO

        readFromCSV()
        readFromDatabase()
        setupObservables()
        fillValues()
    }

    private fun readFromCSV() {
        viewModel.readDataFromCSV("prod_diaria.csv")
    }

    private fun readFromDatabase() {
        viewModel.readDataFromDatabaseAndConvert()
    }


    private fun setupObservables() {
        viewModel.barEntriesListLiveData.observe(this) {
            setBarDataset(it)
        }
    }

    private fun fillValues(){
        viewModel.totalLiveData.observe(this) {
            binding.textviewTotalResult.text = it.toString()
        }
        viewModel.minLiveData.observe(this) {
            binding.textviewMinResult.text = it.toString()
        }
        viewModel.maxLiveData.observe(this) {
            binding.textviewMaxResult.text = it.toString()
        }
        viewModel.averageLiveData.observe(this) {
            binding.textviewAverageResult.text = it.toString()
        }
    }

    private fun setBarDataset(entries: List<BarEntry>) {
        val dataset = BarDataSet(entries, "Média por dia")
        dataset.colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
        dataset.valueTextColor = Color.BLACK
        dataset.valueTextSize = 22f
        dataset.barBorderWidth

        val data = BarData(dataset)
        binding.barChart.apply {
            setFitBars(true)
            setData(data)
            animateY(2000)
            description.text = "Melhores Ordenhas"
            barData.barWidth = 10f
        }
    }
}