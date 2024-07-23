package com.kkjang.weather.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.res.ResourcesCompat
import com.kkjang.data.api.RetrofitService
import com.kkjang.weather.R
import com.kkjang.weather.feature.weather.GetWeatherState
import com.kkjang.weather.feature.weather.WeatherViewModel
import com.kkjang.weather.ui.theme.WeatherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    viewModel.getWeather(q = "seoul", appId = RetrofitService.API_KEY)

                    WeatherViewModel(viewModel = viewModel)
                }
            }
        }
    }
}


@Composable
fun WeatherViewModel(viewModel: WeatherViewModel) {
    val weatherState by viewModel.weatherState.collectAsState()
    when(weatherState) {
        is GetWeatherState.Init -> {
            Log.i("Weather API : " ,"Init")
        }
        is GetWeatherState.Loading -> {
            Log.v("Weather API : ","Loading")
        }
        is GetWeatherState.Success -> {
            Log.d("Weather API : ","${(weatherState as GetWeatherState.Success).response.main}")
        }
        is GetWeatherState.Fail -> {
            Log.e("Weather API Fail", (weatherState as GetWeatherState.Fail).message)
        }
    }

}
