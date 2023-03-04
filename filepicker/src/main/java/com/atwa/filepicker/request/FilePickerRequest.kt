package com.atwa.filepicker.request

import android.content.Intent
import android.net.Uri
import com.atwa.filepicker.decoder.Decoder
import com.atwa.filepicker.result.FileMeta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import java.io.File

internal class FilePickerRequest(
    private val decoder: Decoder,
    private val onFilePicked: (FileMeta?) -> Unit
) : PickerRequest {

    override val intent: Intent
        get() = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
        }

    override suspend fun invokeCallback(uri: Uri) {
        var result: FileMeta? = null
        decoder.getStorageFile(uri).collect { result = it }
        withContext(Dispatchers.Main) {
            onFilePicked(result)
        }
    }
}