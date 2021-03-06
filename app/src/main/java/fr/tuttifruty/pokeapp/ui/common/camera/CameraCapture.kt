package fr.tuttifruty.pokeapp.ui.common.camera

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import fr.tuttifruty.pokeapp.ui.common.Permission
import fr.tuttifruty.pokeapp.ui.common.executor
import fr.tuttifruty.pokeapp.ui.common.getCameraProvider
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

@ExperimentalPermissionsApi
@Composable
fun CameraCapture(
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA,
    onImageTaken: (File) -> Unit,
) {
    val context = LocalContext.current

    Permission {
        Box(modifier = modifier) {
            val lifecycleOwner = LocalLifecycleOwner.current
            val coroutineScope = rememberCoroutineScope()
            var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
            val imageCaptureUseCase by remember {
                mutableStateOf(
                    ImageCapture.Builder()
                        .setCaptureMode(CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()
                )
            }

            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                onUseCase = {
                    previewUseCase = it
                })



            Button(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.BottomCenter),
                onClick = {
                    coroutineScope.launch {
                        imageCaptureUseCase.takePhoto(context.executor).also {
                            onImageTaken(it)
                        }
                    }
                },
            ) {
                Text(text = "Capture pokemon !")
            }

            LaunchedEffect(previewUseCase) {
                val cameraProvider = context.getCameraProvider()
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        previewUseCase,
                        imageCaptureUseCase,
                    )
                } catch (ex: Exception) {
                    Timber.e(ex, "Use case binding failed")
                }
            }
        }
    }
}