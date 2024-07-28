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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.substring
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.kkjang.data.api.RetrofitService
import com.kkjang.domain.model.CloudsVo
import com.kkjang.domain.model.CoordVo
import com.kkjang.domain.model.MainVo
import com.kkjang.domain.model.RainVo
import com.kkjang.domain.model.SysVo
import com.kkjang.domain.model.WeatherItemVo
import com.kkjang.domain.model.WeatherVo
import com.kkjang.domain.model.WindVo
import com.kkjang.weather.R
import com.kkjang.weather.component.CommonFailComponent
import com.kkjang.weather.component.MainTempComponent
import com.kkjang.weather.component.WeatherIconComponent
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
    private var addressString: String = ""

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
                 // color = MaterialTheme.colorScheme.inverseOnSurface
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
                                LoadingHomeScreen()
                            }

                            // 위치 정보 권한 허용
                            PermissionState.GRANTED -> {
                                viewModel.getLocation(this@MainActivity)
                                val getLocation = viewModel.locationState.collectAsState()

                                getLocation.value?.let { location ->
                                    viewModel.getAddress(this@MainActivity, location.latitude, location.longitude)

                                    Log.e("나의 위치 정보: ", "${viewModel.getAddress(this@MainActivity, location.latitude, location.longitude)}")

                                    val addressVo = viewModel.getAddress(this@MainActivity,location.latitude,location.longitude)?.get(0)
                                    addressVo?.let { vo ->
                                        viewModel.setAddress(vo.getAddressLine(0).toString())
                                    }


                                    Log.d("나의 경도 위도 정보: ", "${location.latitude}, ${location.longitude}")
                                    viewModel.getLocationWeather(
                                        lat = location.latitude,
                                        lon = location.longitude,
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
    val weatherState by viewModel.weatherState.collectAsState()
    when (weatherState) {
        is GetWeatherState.Init -> {
            Log.i("Location API : ", "Init")
        }

        is GetWeatherState.Loading -> {
            LoadingHomeScreen()
            Log.v("Location API : ", "Loading")
        }

        is GetWeatherState.Success -> {
            Log.d("Location API : ", "${(weatherState as GetWeatherState.Success).response}")
            MainScreen(weatherVo = (weatherState as GetWeatherState.Success).response, viewModel = viewModel)
        }

        is GetWeatherState.Fail -> {
            Log.e("Location API Fail", (weatherState as GetWeatherState.Fail).message)
            CommonFailComponent()
        }
    }
}


@Composable
fun LocationComponent(weatherVo: WeatherVo, viewModel: WeatherViewModel) {

    // "대한민국"이 포함되어 있는지 확인하고, 포함되어 있으면 제거
    val formattedAddress = if (viewModel.addressLine.collectAsState().value.startsWith("대한민국 ")) {
        viewModel.addressLine.collectAsState().value.removePrefix("대한민국 ")
    } else {
        viewModel.addressLine.collectAsState().value
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 150.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center // Row의 자식들이 수평 중앙에 배치됨
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
        Spacer(modifier = Modifier.width(8.dp)) // 이미지와 텍스트 사이의 여유 공간을 16.dp로 설정
        Text(
            modifier = Modifier
                .width(150.dp)
                .wrapContentHeight()
            ,
            text = formattedAddress,
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LocationComponentPreView() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 150.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center // Row의 자식들이 수평 중앙에 배치됨
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
        Spacer(modifier = Modifier.width(8.dp)) // 이미지와 텍스트 사이의 여유 공간을 16.dp로 설정
        Text(
            modifier = Modifier
                .width(150.dp)
                .wrapContentHeight()
            ,
            text = "서울특별시 영등포구 의사당대로 82dsadasdasdasdsadasdaddada",
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp
            )
        )
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
            Row {
                LocationComponent(weatherVo = weatherVo, viewModel = viewModel)
            }

            // 현재 날씨 아이콘
            Row {
                WeatherIconComponent(
                    modifier = Modifier.wrapContentSize(),
                    imgUrl = RetrofitService.IMG_URL+ weatherVo.weahter.first().icon + "@2x.png"
                )
                Log.e("아이콘 경로 : ",
                    RetrofitService.IMG_URL+ weatherVo.weahter.first().icon + "@2x.png"
                )
            }

            // 현재 기온
            Row {
                MainTempComponent(
                    modifier = Modifier.wrapContentSize(),
                    temp = weatherVo.main.temp.toString().substring(0,2)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}

@Composable
fun MainScreenPreView(weatherViewModel: WeatherViewModel) {
    MainScreen(
        weatherVo = WeatherVo(
            coord = CoordVo(lon = 0.0, lat = 0.0),
            weahter = listOf(WeatherItemVo(id = 1, main = "Clear", description = "Clear sky", icon = "01d")),
            base = "stations",
            main = MainVo(
                temp = 23.5,
                feels_life = 22.3,
                temp_min = 22.0,
                temp_max = 24.0,
                pressure = 1013,
                humidity = 60,
                grnd_level = 1000, // 예시 값
                sea_level = 1010   // 예시 값
            ),
            visibility = 10000,
            wind = WindVo(speed = 5.5, deg = 180),
            rain = RainVo(0.0),
            clouds = CloudsVo(all = 0),
            dt = 1638316800,
            sys = SysVo(type = 1, id = 1, country = "KR", sunrise = 1638299342, sunset = 1638342921),
            timezone = -14400,
            id = 123456,
            name = "Location",
            cod = 200
        ),
        viewModel = weatherViewModel
    )
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


