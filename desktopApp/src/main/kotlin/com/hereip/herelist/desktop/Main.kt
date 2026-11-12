package com.hereip.herelist.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.hereip.herelist.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "HereList"
    ) {
        App()
    }
}
