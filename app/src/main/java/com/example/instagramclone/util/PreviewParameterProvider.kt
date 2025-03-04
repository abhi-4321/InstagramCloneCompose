package com.example.instagramclone.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.navigation.NavHostController
import com.example.instagramclone.viewmodel.MainViewModel

class PreviewParameterProvider : PreviewParameterProvider<MainViewModel> {
    override val values: Sequence<MainViewModel>
        get() = sequenceOf(MainViewModel())
}