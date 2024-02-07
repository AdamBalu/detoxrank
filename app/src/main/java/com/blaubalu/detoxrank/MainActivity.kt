/**
 * Detox Rank
 *
 * @author Adam Balušeskul
 */

package com.blaubalu.detoxrank

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.blaubalu.detoxrank.service.TimerService
import com.blaubalu.detoxrank.ui.DetoxRankAppContent
import com.blaubalu.detoxrank.ui.theme.DetoxRankTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var bound by mutableStateOf(false)
    private lateinit var timerService: TimerService

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as TimerService.StopwatchBinder
            timerService = binder.getService()
            bound = true
        }
        override fun onServiceDisconnected(arg0: ComponentName?) {
            bound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, TimerService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    @ExperimentalMaterial3WindowSizeClassApi
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        CoroutineScope(Dispatchers.IO).launch {
//            val database = AppDatabase.getDatabase(this@MainActivity)
//            database.userDataDao().insert(UserData())
//        }

        setContent {
            DetoxRankTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val windowSize = calculateWindowSizeClass(activity = this)
                    if (bound) {
                        DetoxRankAppContent(
                            windowSize = windowSize.widthSizeClass,
                            timerService = timerService
                        )
                    }
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun requestPermissions(vararg permissions: String) {
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            result.entries.forEach {
//                Log.d("MainActivity", "${it.key} = ${it.value}")
            }
        }
        requestPermissionLauncher.launch(permissions.asList().toTypedArray())
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        bound = false
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DetoxRankPreview() {
    DetoxRankTheme {
        DetoxRankAppContent(
            windowSize = WindowWidthSizeClass.Compact,
            timerService = TimerService()
        )
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Preview(showBackground = true, widthDp = 700)
@Composable
fun DetoxRankMediumPreview() {
    DetoxRankTheme {
        DetoxRankAppContent(
            windowSize = WindowWidthSizeClass.Medium,
            timerService = TimerService()
        )
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Preview(showBackground = true, widthDp = 1000)
@Composable
fun DetoxRankExpandedPreview() {
    DetoxRankTheme {
        DetoxRankAppContent(
            windowSize = WindowWidthSizeClass.Expanded,
            timerService = TimerService()
        )
    }
}
