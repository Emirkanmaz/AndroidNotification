package com.emirkanmaz.androidnotification

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.emirkanmaz.androidnotification.ui.theme.AndroidNotificationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ChatViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
                startActivity(intent)
            }
        }

        enableEdgeToEdge()
        requestNotificationPermissions()

        setContent {
            AndroidNotificationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val state = viewModel.state
                    if (state.isEnteringToken) {
                        EnterTokenDialog(
                            token = state.remoteToken,
                            onTokenChange = viewModel::onRemoteTokenChange,
                            onSubmit = viewModel::onSubmitRemoteToken,
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        ChatScreen(
                            messageText = state.messageText,
                            onMessageSend = { viewModel.sendMessage(isBroadcast = false) },
                            onMessageBroadcast = { viewModel.sendMessage(isBroadcast = true) },
                            onMessageChange = viewModel::onMessageChange,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    private fun requestNotificationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasNotificationPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasNotificationPermission) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

