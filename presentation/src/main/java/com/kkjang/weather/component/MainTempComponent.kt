package com.kkjang.weather.component

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun MainTempComponent(modifier: Modifier, temp: String) {
    var displayedNumber by remember { mutableIntStateOf(0) }

    LaunchedEffect(temp) {
        displayedNumber = temp.toInt()
    }

    val animatedNumber by animateIntAsState(
        targetValue = displayedNumber,
        animationSpec = tween(durationMillis = 1000)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center // Row의 자식들이 수평 중앙에 배치됨
    ) {
        Text(
            text = "$animatedNumber°C",
            style = TextStyle(
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainTempComponentPreView() {
    MainTempComponent(modifier = Modifier, temp = "31.5°C")
}