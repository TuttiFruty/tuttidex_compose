package fr.tuttifruty.pokeapp.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@ExperimentalPermissionsApi
@Composable
fun Permission(
    permission: String = android.Manifest.permission.CAMERA,
    rationale: String = "This permission will allow to create new Pokemon ! <3",
    permissionNotAvailableContent: @Composable () -> Unit = { },
    content: @Composable () -> Unit = {},
) {
    val permissionState = rememberPermissionState(permission = permission)
    when (permissionState.status) {
        PermissionStatus.Granted -> {
            content.invoke()
        }
        is PermissionStatus.Denied -> {
            Column {
                Text("Need permissions to take photo of pokemon !")
                Button(onClick = {
                    permissionState.launchPermissionRequest()
                }) {
                    Text("Request permission")
                }
            }
        }
    }
}