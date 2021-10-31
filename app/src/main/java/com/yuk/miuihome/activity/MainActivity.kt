package com.yuk.miuihome.activity

import android.content.*
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.yuk.miuihome.R
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.Keep
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarTransparent()
        setContent {
            MessageCard()
        }

    }

    @Preview(name = "Light Mode")
    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode"
    )

    @Composable
    fun MessageCard() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Column(
                    Modifier.padding(13.dp, 28.dp, 13.dp, 23.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        stringResource(R.string.app_name),
                        fontWeight = FontWeight.Bold,
                        fontSize = 35.sp
                    )
                    Text(
                        "Hook for MIUI Launcher",
                        fontSize = 15.sp
                    )
                }
            }
            Column(
                Modifier.padding(13.dp, 5.dp, 13.dp, 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    stringResource(R.string.MainActivity1),
                    Modifier.padding(10.dp),
                    fontSize = 14.sp
                )
                Row(
                    Modifier.padding(13.dp, 5.dp, 13.dp, 5.dp)
                ) {
                    ElevatedButton(
                        onClick = {
                            val intent = Intent()
                            intent.component = ComponentName(
                                "com.miui.home",
                                "com.miui.home.settings.MiuiHomeSettingActivity"
                            )
                            startActivity(intent)
                        }, contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 15.dp,
                            end = 20.dp,
                            bottom = 15.dp
                        )
                    ) { Text(stringResource(R.string.OpenMiuiHomeSettings), fontSize = 14.sp) }
                    Spacer(Modifier.size(10.dp))
                    if (!getSP().getBoolean("shouldHide", false)) {
                        ElevatedButton(
                            onClick = {
                                try {
                                    packageManager.setComponentEnabledSetting(
                                        ComponentName(
                                            "com.yuk.miuihome",
                                            "com.yuk.miuihome.activity.EntryActivity"
                                        ),
                                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                        PackageManager.DONT_KILL_APP
                                    )
                                } catch (e: ActivityNotFoundException) {
                                    e.printStackTrace()
                                }
                                getSP().edit().putBoolean("shouldHide", true).apply()
                            }, contentPadding = PaddingValues(
                                start = 20.dp,
                                top = 15.dp,
                                end = 20.dp,
                                bottom = 15.dp
                            )
                        ) { Text(stringResource(R.string.HideAppIcon), fontSize = 14.sp) }
                    } else {
                        ElevatedButton(
                            onClick = {
                                packageManager.setComponentEnabledSetting(
                                    ComponentName(
                                        packageName,
                                        "com.yuk.miuihome.activity.EntryActivity"
                                    ),
                                    PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                                    PackageManager.DONT_KILL_APP
                                )
                                getSP().edit().putBoolean("shouldHide", false).apply()
                                finish()
                                val intent = Intent()
                                intent.component =
                                    ComponentName(
                                        packageName,
                                        "com.yuk.miuihome.activity.EntryActivity"
                                    )
                                startActivity(intent)
                            }, contentPadding = PaddingValues(
                                start = 20.dp,
                                top = 15.dp,
                                end = 20.dp,
                                bottom = 15.dp
                            )
                        ) { Text(stringResource(R.string.ShowAppIcon), fontSize = 14.sp) }
                    }
                }
                Row(Modifier.padding(13.dp, 5.dp, 13.dp, 5.dp)) {
                    ElevatedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.addCategory(Intent.CATEGORY_BROWSABLE)
                            intent.data = Uri.parse("https://github.com/1767523953/MiuiHome")
                            startActivity(intent)
                        }, contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 15.dp,
                            end = 20.dp,
                            bottom = 15.dp
                        )
                    ) { Text(stringResource(R.string.Github), fontSize = 14.sp) }
                    Spacer(Modifier.size(10.dp))
                    ElevatedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.addCategory(Intent.CATEGORY_BROWSABLE)
                            intent.data =
                                Uri.parse("coolmarket://www.coolapk.com/apk/com.yuk.miuihome")
                            startActivity(intent)
                        }, contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 15.dp,
                            end = 20.dp,
                            bottom = 15.dp
                        )
                    ) { Text(stringResource(R.string.Coolapk), fontSize = 14.sp) }
                    Spacer(Modifier.size(10.dp))
                    ElevatedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.addCategory(Intent.CATEGORY_BROWSABLE)
                            intent.data = Uri.parse("https://t.me/MiuiHome_Xposed")
                            startActivity(intent)
                        }, contentPadding = PaddingValues(
                            start = 20.dp,
                            top = 15.dp,
                            end = 20.dp,
                            bottom = 15.dp
                        )
                    ) { Text(stringResource(R.string.Telegram), fontSize = 14.sp) }
                }
                Row {
                    Text(
                        stringResource(R.string.State) + " :",
                        Modifier.padding(10.dp, 10.dp, 1.dp),
                        fontSize = 14.sp,
                    )
                    if (moduleEnable()) {
                        Text(
                            stringResource(R.string.ModuleEnable),
                            Modifier.padding(10.dp, 10.dp, 1.dp),
                            fontSize = 14.sp,
                            color = Green
                        )
                    } else {
                        Text(
                            stringResource(R.string.ModuleNotEnable),
                            Modifier.padding(10.dp, 10.dp, 1.dp),
                            fontSize = 14.sp,
                            color = Red
                        )
                    }
                }
                Column(
                    Modifier.padding(13.dp, 10.dp, 13.dp, 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(stringResource(R.string.MainActivity2), fontSize = 14.sp)
                }
            }
        }
    }

    private fun getSP(): SharedPreferences {
        return getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    @Keep
    private fun moduleEnable(): Boolean {
        return false
    }

    private fun changeStatusBarTransparent() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val option =
            window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.decorView.systemUiVisibility = option
        window.statusBarColor = Color.parseColor("#00000000")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}