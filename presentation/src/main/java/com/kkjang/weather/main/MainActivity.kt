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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.kkjang.data.api.RetrofitService
import com.kkjang.domain.model.WeatherVo
import com.kkjang.weather.R
import com.kkjang.weather.component.AnotherWeatherComponent
import com.kkjang.weather.component.CommonFailComponent
import com.kkjang.weather.component.MainTempComponent
import com.kkjang.weather.component.WeatherIconComponent
import com.kkjang.weather.feature.weather.GetAnotherWeatherState
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
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Blue.copy(alpha = 0.5f), Transparent
                                )
                            )
                        ),
                    color = Transparent
                ) {
                    var showLoading by remember { mutableStateOf(true) }

                    LaunchedEffect(permissionState) {
                        delay(1000) // Wait for 1 second
                        showLoading = false
                    }

                    if (showLoading) {
                        LoadingHomeScreen()
                    } else {
                        Log.i("permissionState ", "$permissionState")
                        when (permissionState) {
                            PermissionState.LOADING -> {
                                LoadingHomeScreen()
                            }

                            PermissionState.GRANTED -> {
                                LaunchedEffect(Unit) {
                                    viewModel.getLocation(this@MainActivity)
                                }
                                val locationState by viewModel.locationState.collectAsState()
                                locationState?.let { location ->
                                    LaunchedEffect(location) {
                                        viewModel.getAddress(this@MainActivity, location.latitude, location.longitude)
                                        viewModel.getLocationWeather(
                                            lat = location.latitude,
                                            lon = location.longitude,
                                            lang = "kr",
                                            units = "metric",
                                            appId = RetrofitService.API_KEY
                                        )
                                    }
                                    LocationWeatherViewModel(viewModel = viewModel)
                                }
                            }

                            PermissionState.DENIED -> {
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
        Toast.makeText(
            this@MainActivity,
            "위치 정보 제공이 거부되었습니다. \n$deniedPermissions",
            Toast.LENGTH_SHORT
        ).show()
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
                        Toast.makeText(
                            this@MainActivity,
                            "권한 거부 \n$deniedPermissions",
                            Toast.LENGTH_SHORT
                        ).show()
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
    val anotherWeatherState by viewModel.anotherWeatherState.collectAsState()
    when (anotherWeatherState) {
        is GetAnotherWeatherState.Init -> {
        }

        is GetAnotherWeatherState.Loading -> {
            LoadingHomeScreen()
        }

        is GetAnotherWeatherState.Success -> {
            MainScreen(weatherVo = (anotherWeatherState as GetAnotherWeatherState.Success).response, viewModel = viewModel)
        }

        is GetAnotherWeatherState.Fail -> {
            CommonFailComponent()
        }
    }
}

@Composable
fun LocationComponent(weatherVo: WeatherVo) {
    // State를 사용하여 address 값을 관리
    var address by remember { mutableStateOf(weatherVo.name) }

    // address 값이 변경될 때마다 호출
    LaunchedEffect(weatherVo.name) {
        address = weatherVo.name
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(), // Box가 화면 전체를 채우도록 설정
        contentAlignment = Alignment.Center // Box의 콘텐츠를 가운데 정렬
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.wrapContentSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.ic_x48_location) // ContextCompat.getDrawable을 사용하지 않고 직접 리소스 ID를 지정
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_x48_location),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp)) // 이미지와 텍스트 사이의 여유 공간을 8.dp로 설정
            Text(
                text = address,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationComponentPreView() {
    Box(
        modifier = Modifier
            .fillMaxWidth(), // Box가 화면 전체를 채우도록 설정
        contentAlignment = Alignment.Center // Box의 콘텐츠를 가운데 정렬
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.wrapContentSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.ic_x48_location) // ContextCompat.getDrawable을 사용하지 않고 직접 리소스 ID를 지정
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_x48_location),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp)) // 이미지와 텍스트 사이의 여유 공간을 8.dp로 설정
            Text(
                text = "인천",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp
                )
            )
        }
    }
}


@Composable
fun MainScreen(weatherVo: WeatherVo, viewModel: WeatherViewModel) {
    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding(),
    ) {
        Column {
            // 현재 나의 위치
            Row(modifier = Modifier.padding(top = 120.dp)) {
                LocationComponent(weatherVo = weatherVo)
            }

            // 현재 날씨 아이콘
            Row {
                WeatherIconComponent(
                    modifier = Modifier.wrapContentSize(),
                    imgUrl = RetrofitService.IMG_URL + weatherVo.weahter.first().icon + "@2x.png"
                )
            }

            // 현재 기온
            Row {
                MainTempComponent(
                    modifier = Modifier.wrapContentSize(),
                    temp = weatherVo.main.temp.toString().substring(0, 2)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))


            // 다른 지역 날씨
            Row {

                viewModel.loadWeatherData(RetrofitService.API_KEY)
                WeatherList(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeatherList(viewModel: WeatherViewModel) {
    val weatherStates by viewModel.searchWeatherStates.collectAsState()

    // Prepare weather data
    val weatherData = weatherStates.map { (location, state) ->
        when (state) {
            is GetWeatherState.Success -> {
                val weather = state.response.weahter.firstOrNull()
                val iconUrl = RetrofitService.IMG_URL + (weather?.icon ?: "default_icon") + "@2x.png"
                val description = weather?.description ?: "No description"

                location to Pair(iconUrl, description)
            }
            is GetWeatherState.Fail -> {
                location to Pair("default_icon_url", "Error fetching data")
            }
            is GetWeatherState.Loading -> {
                location to Pair("default_icon_url", "Loading...")
            }
            GetWeatherState.Init -> {
                location to Pair("default_icon_url", "No data")
            }
        }
    }

    LazyColumn {
        items(weatherData, key = { it.first }) { (location, description) ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(durationMillis = 1500)),
                exit = fadeOut(animationSpec = tween(durationMillis = 1500))
            ) {
                AnotherWeatherComponent(
                    image = {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(description.first)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Weather icon",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .animateItemPlacement()
                        )
                    },
                    content = {
                        Column(
                            modifier = Modifier
                                .animateItemPlacement()
                                .animateContentSize()
                        ) {
                            Text(
                                text = location,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = description.second,
                                style = TextStyle(
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}



@Composable
fun LoadingHomeScreen() {
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

