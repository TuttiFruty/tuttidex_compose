package fr.tuttifruty.pokeapp.ui.common.camera

import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun ImageCapture.takePhoto(
    executor: Executor,
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
): File {
    val photoFile = withContext(dispatcher) {
        kotlin.runCatching {
            File.createTempFile("image", ".png")
        }.getOrElse { exception ->
            Timber.e("Failed to create temporary file", exception)
            File("/dev/null")
        }
    }

    return suspendCoroutine { continuation ->
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                continuation.resume(photoFile)
            }

            override fun onError(exception: ImageCaptureException) {
                Timber.e("Image captured failed", exception)
                continuation.resumeWithException(exception = exception)
            }
        })
    }
}