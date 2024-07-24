package com.kkjang.weather.main

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.kkjang.data.api.RetrofitService
import com.kkjang.weather.R
import com.kkjang.weather.feature.weather.GetWeatherState
import com.kkjang.weather.feature.weather.PermissionState
import com.kkjang.weather.feature.weather.WeatherViewModel
import com.kkjang.weather.ui.theme.WeatherTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PermissionListener {
    private val viewModel: WeatherViewModel by viewModels()
    private var permissionState by mutableStateOf(PermissionState.LOADING)

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkPermissions()

        setContent {
            WeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showLoading by remember { mutableStateOf(true) }

                    LaunchedEffect(Unit) {
                        delay(1000) // Wait for 1 second
                        showLoading = false
                    }

                    if (showLoading) {
                        LoadingHomeScreen()
                    } else {
                        Log.i("permissionState ", "$permissionState")
                        when (permissionState) {
                            PermissionState.LOADING -> {
                                Text(text = "Checking permissions...")
                            }

                            // 위치 정보 권한 허용
                            PermissionState.GRANTED -> {
                                viewModel.getLocation(this@MainActivity)
                                val getLocation = viewModel.locationState.collectAsState()
                                getLocation.value?.let {
                                    Log.d("나의 경도 위도 정보: ", "${it.latitude}, ${it.longitude}")
                                    viewModel.getLocationWeather(
                                        lat = it.latitude,
                                        lon = it.longitude,
                                        lang = "kr",
                                        units = "metric",
                                        appId = RetrofitService.API_KEY
                                    )
                                    LocationWeatherViewModel(viewModel = viewModel)
                                }
                            }
                            PermissionState.DENIED -> {
                                // Display permission denied message or screen
                                PermissionDeniedScreen()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onPermissionGranted() {
        Toast.makeText(this@MainActivity, "위치 정보 제공이 완료되었습니다.", Toast.LENGTH_SHORT).show()
        permissionState = PermissionState.GRANTED
    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
        Toast.makeText(this@MainActivity, "위치 정보 제공이 거부되었습니다. \n$deniedPermissions", Toast.LENGTH_SHORT).show()
        permissionState = PermissionState.DENIED
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= 25) {
            TedPermission.create()
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {
                        Toast.makeText(this@MainActivity, "권한 허가", Toast.LENGTH_SHORT).show()
                        permissionState = PermissionState.GRANTED
                    }

                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                        Toast.makeText(this@MainActivity, "권한 거부 \n$deniedPermissions", Toast.LENGTH_SHORT).show()
                        permissionState = PermissionState.DENIED
                    }
                })
                .setRationaleMessage("위치 정보 제공이 필요한 서비스입니다.")
                .setDeniedMessage("[설정] -> [권한]에서 권한 변경이 가능합니다.")
                .setDeniedCloseButtonText("닫기")
                .setGotoSettingButtonText("설정")
                .setRationaleTitle("권한 알림")
                .setPermissions(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                .check()
        }
    }
}

@Composable
fun LocationWeatherViewModel(viewModel: WeatherViewModel) {
    val weatherState by viewModel.weatherState.collectAsState()
    when (weatherState) {
        is GetWeatherState.Init -> {
            Log.i("Location API : ", "Init")
        }
        is GetWeatherState.Loading -> {
            Log.v("Location API : ", "Loading")
        }
        is GetWeatherState.Success -> {
            Log.d("Location API : ", "${(weatherState as GetWeatherState.Success).response}")
        }
        is GetWeatherState.Fail -> {
            Log.e("Location API Fail", (weatherState as GetWeatherState.Fail).message)
        }
    }
}


@Composable
fun TopScreen() {
    Row(modifier = Modifier.fillMaxWidth().height(80.dp)) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(ContextCompat.getDrawable(LocalContext.current, R.drawable.ic_x48_location))
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_x48_location),
            contentDescription = stringResource(R.string.app_name),
            contentScale = ContentScale.Crop,
            modifier = Modifier.clip(CircleShape).padding(16.dp)
        )
    }

}

@Preview(showBackground = true)
@Composable
fun TopScreenPreView() {
    TopScreen()
}

@Composable
private fun LoadingHomeScreen() {
    val strokeWidth = 5.dp

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp),
            color = Color.LightGray,
            strokeWidth = strokeWidth
        )
    }
}

@Preview
@Composable
fun PermissionDeniedScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "권한이 거부되었습니다.\n앱 설정에서 권한을 허용해 주세요.",
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}


