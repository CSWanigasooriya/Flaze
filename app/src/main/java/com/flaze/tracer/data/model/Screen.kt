package com.flaze.tracer.data.model

import androidx.annotation.StringRes
import com.flaze.tracer.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object SignIn : Screen("sign_in", R.string.common_signin_button_text)
    object Home : Screen("home", R.string.home)
}
