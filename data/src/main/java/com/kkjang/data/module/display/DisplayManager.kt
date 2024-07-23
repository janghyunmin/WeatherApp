package com.kkjang.data.module.display

class DisplayManager {
    fun getDeviceType(width: Int): Int {
        return if (width > 1600) { 900 } // 태블릿, 폴드 펼침
        else if (width < 980) { 800 } // 미니, 폴드 닫힘
        else { 1030 } // 일반
    }
}