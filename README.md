# :technologist: 날씨 앱 #토이 프로젝트

> 나의 위치를 조회하여 해당 위치의 날씨를 알려주는 앱
<!-- ![](https://user-images.githubusercontent.com/38487811/90950455-85d36700-e48c-11ea-9b79-72b5dcb6c6d6.png) -->


https://github.com/user-attachments/assets/8b4d1e65-b259-4962-8e2d-37eb52f31cb2

https://github.com/user-attachments/assets/90a0f867-0a50-45f2-ba72-d82877b06c20


## ⭐ Main Feature
### 나의 위치 정보를 조회
- Location 객체가 담고있는 정보에는 위도(latitude), 경도(longitude), <br>
  고도(altitude), 측정 정확도(accuracy), 측정 시간(time), 이동 속도(speed) 등 실시간 정보

### 현재 날씨 조회
- {지역} 의 날씨
- 날씨는 {xxx} 입니다. (ex.맑음)
- 현재 온도는 {n} 입니다.
- 체감 온도는 {n} 입니다.
- 최저 기온은 {n} 입니다.
- 최고 기온은 {n} 입니다.
- 습도는 {nn} 이며 , 기압은 {nn} 입니다.
- 풍향은 {nn} 이며 , 풍속은 {nn} 입니다.

### 날씨에 따라 배경 변화


## 🔧 Stack
- **lib version** : kotlin 1.9.0 / composeBom 2024.06.00 / hilt 1.2.0
- **Language** : Kotlin , Compose
- **Framework** : Android Studio
- **API** : https://openweathermap.org/current
- **Skils** : LocationServices


## :open_file_folder: Project Structure

```markdown
src
├── presentation
│   ├── main
│   ├── feature
│       ├── weather
│   └── ui.theme
│       ├── type
│       ├── theme
│       ├── colors
│       └── shape
│   └── App
├── buildSrc
├── data
│   ├── api
│   ├── entity
│   ├── mapper
│   ├── module
│   ├── repository
│         ├── local
│               ├── dataSource
│               ├── dataSourceImpl
│         ├── remote
│               ├── remoteSource
│               ├── remoteSourceImpl
│       ├── repositoryImpl
│   ├── utils
├── domain
│   ├── dto
│   ├── repository
│   └── usecase

```
