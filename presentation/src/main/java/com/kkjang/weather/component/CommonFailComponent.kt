package com.kkjang.weather.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.kkjang.weather.R


@Composable
fun CommonFailComponent() {
    Box(
        modifier = Modifier
            .fillMaxSize() // Box가 부모 크기를 꽉 차게 만듭니다
            .padding(16.dp), // 아이콘 주위에 여백을 추가합니다 (선택 사항)
        contentAlignment = Alignment.Center // Box의 자식 요소를 중앙에 배치합니다
    ) {
        Image(
            painter = rememberImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.ic_x48_empty) // 리소스 ID를 직접 지정
                    .crossfade(true) // 페이드 인 효과
                    .build()
            ),
            contentDescription = stringResource(R.string.app_name),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp) // 크기를 적절히 조정합니다
                .clip(CircleShape) // 원형으로 자르기
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonFailPreView() {
    CommonFailComponent()
}