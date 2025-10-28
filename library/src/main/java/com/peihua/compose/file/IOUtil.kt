@file:JvmName("IOUtil")
@file:JvmMultifileClass

package com.peihua.compose.file

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Looper
import androidx.annotation.NonNull
import androidx.exifinterface.media.ExifInterface
import com.peihua.compose.utils.LogCat
import com.peihua.compose.utils.dLog
import com.peihua.compose.utils.eLog
import com.peihua.compose.utils.transformBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.Reader
import java.io.Writer
import java.util.zip.CRC32
import java.util.zip.ZipOutputStream
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.math.max

fun Closeable?.close() {
    if (this != null) {
        try {
            close()
        } catch (e: Throwable) {
            e.printStackTrace()
            LogCat.e(e)
        }
    }
}

fun Cursor?.closeQuietly() {
    if (this != null) {
        try {
            this.close()
        } catch (e: Throwable) {
            LogCat.e(e)
        }
    }
}

@Throws(Exception::class)
fun InputStream?.readBytes(): ByteArray? {
    return this?.let {
        BufferedInputStream(this).use { input ->
            val out = ByteArrayOutputStream()
            val buf = ByteArray(1024)
            var len: Int
            while (input.read(buf).also { len = it } != -1) {
                out.write(buf, 0, len)
            }
            out.toByteArray()
        }
    }
}

@Throws(IOException::class)
fun InputStream?.readBytes(skip: Long, size: Int): ByteArray? {
    return this?.let { input ->
        var tempSkip = skip
        val result = ByteArray(size)
        if (tempSkip > 0) {
            var skipped: Long = 0
            while (tempSkip > 0 && input.skip(tempSkip).also { skipped = it } > 0) {
                tempSkip -= skipped
            }
        }
        for (i in 0 until size) {
            result[i] = this.read().toByte()
        }
        result
    }
}

/**
 *
 * @param charset 编码方式
 * @return 如果读取成功，返回读取到的内容，否则返回空字符串
 * @author dingpeihua
 * @date 2020/11/25 20:09
 * @version 1.0
 */
@JvmOverloads
@Throws(IOException::class)
@NonNull
fun InputStream?.readStr(charset: String = "UTF-8"): String {
    return this?.let { input ->
        BufferedInputStream(input).use { input1 ->
            val reader: Reader = InputStreamReader(input1, charset)
            val sb = StringBuilder()
            val buf = CharArray(1024)
            var len: Int
            while (reader.read(buf).also { len = it } >= 0) {
                sb.append(buf, 0, len)
            }
            sb.toString()
        }
    } ?: ""
}

@JvmOverloads
@Throws(IOException::class)
fun OutputStream?.writeStr(str: String?, charset: String = "UTF-8") {
    this?.let {
        val writer: Writer = OutputStreamWriter(this, charset)
        writer.write(str)
        writer.flush()
    }
}

@Throws(IOException::class)
fun InputStream?.copy(out: OutputStream) {
    this?.let { it ->
        BufferedInputStream(it).use { input ->
            BufferedOutputStream(out).use { output ->
                var len = 0
                val buffer = ByteArray(1024)
                while (input.read(buffer).also { len = it } != -1) {
                    output.write(buffer, 0, len)
                }
                output.flush()
            }
        }
    }
}

suspend fun InputStream?.writeToFile(
    file: File?,
    bufferSize: Int = 4096,
    isCloseOs: Boolean = true,
    callback: (progress: Long, speed: Long) -> Unit = { process, isComplete -> },
): Boolean {
    val parentFile = file?.parentFile
    if (file == null || this == null || parentFile == null) {
        return false
    }
    if (file.exists()) {
        file.delete()
    }
    if (parentFile.exists().not()) {
        parentFile.mkdirs()
    }
    val os = FileOutputStream(file)
    return writeToFile(os, bufferSize, isCloseOs, callback)
}

suspend fun InputStream?.writeToFile(
    os: OutputStream?,
    bufferSize: Int = 4096,
    isCloseOs: Boolean = true,
    callback: (progress: Long, speed: Long) -> Unit = { process, speed -> },
): Boolean {
    if (os == null || this == null) {
        return false
    }
    return use {
        if (isCloseOs) {
            os.use {
                writeToFileNoClose(os, bufferSize, callback)
            }
        } else {
            writeToFileNoClose(os, bufferSize, callback)
        }
    }
}


suspend fun InputStream?.writeToZip(
    zos: ZipOutputStream,
    bufferSize: Int = 4096,
    isCloseZip: Boolean = true,
    callback: (progress: Long, speed: Long) -> Unit = { process, speed -> },
): Boolean {
    if (this == null) {
        return false
    }
    return this.use { fins ->
        if (isCloseZip) {
            zos.use { zois ->
                fins.writeToFileNoClose(zois, bufferSize, callback)
            }
        } else {
            writeToFileNoClose(zos, bufferSize, callback)
        }
    }
}

/**
 * InputStream 写入 OutputStream,且不做关闭处理，由外部自行关闭
 */
suspend fun InputStream.writeToFileNoClose(
    ios: OutputStream,
    bufferSize: Int = 4096,
    callback: (progress: Long, speed: Long) -> Unit = { process, speed -> },
): Boolean {
    try {
        val context: CoroutineContext = if (Looper.myLooper() == Looper.getMainLooper()) {
            Dispatchers.IO
        } else {
            coroutineContext
        }
        val fis = this
        return withContext(context) {
            val buffer = ByteArray(bufferSize)
            var length: Int
            var progress = 0L
            while ((fis.read(buffer).also { length = it }) != -1 && isActive) {
                ios.write(buffer, 0, length)
                progress += length.toLong()
                callback(progress, length.toLong())
            }
            callback(progress, length.toLong())
            ios.flush()
            dLog { "writeToFile, save file  to $ios successful" }
            true
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        dLog { "writeToFile, save file  to $ios failed,e:${e.message}" }
        return false
    }
}

@get:Throws(java.lang.Exception::class)
val InputStream.cRC32: CRC32
    get() {
        this.use {
            val crc = CRC32()
            val bytes = ByteArray(1024)
            var length: Int
            while ((read(bytes).also { length = it }) != -1) {
                crc.update(bytes, 0, length)
            }
            return crc
        }
    }





fun InputStream.decodeStreamToBitmap(screenWidth: Int, screenHeight: Int): Bitmap? {
    try {
        val mScreenWidth = screenWidth
        val mScreenHeight = screenHeight
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        BitmapFactory.decodeStream(this, null, o)
        dLog { "decodePathOptionsFile, o: $o" }
        val width_tmp = o.outWidth
        val height_tmp = o.outHeight
        var scale = 1
        if (width_tmp <= mScreenWidth && height_tmp <= mScreenHeight) {
            scale = 1
        } else {
            val widthFit: Double = width_tmp * 1.0 / mScreenWidth
            val heightFit: Double = height_tmp * 1.0 / mScreenHeight
            val fit = max(widthFit, heightFit)
            scale = (fit + 0.5).toInt()
        }
        dLog { "decodePathOptionsFile, scale: $scale,width_tmp:$width_tmp,height_tmp:$height_tmp" }
        var bitmap: Bitmap? = null
        if (scale == 1) {
            bitmap = BitmapFactory.decodeStream((this))
        } else {
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            bitmap = BitmapFactory.decodeStream((this), null, o2)
        }
        if (bitmap != null) {
            eLog { "scale = " + scale + " bitmap.size = " + (bitmap.getRowBytes() * bitmap.getHeight()) }
        }
        dLog { "decodePathOptionsFile, bitmap: $bitmap" }
        return bitmap
    } catch (e: Throwable) {
        eLog { "fileNotFoundException, e: $e" }
    }
    return null
}

fun InputStream.adjustBitmapOrientation(decodeBitmap: Bitmap): Bitmap? {
    return try {
        val matrix = orientationMatrix
        dLog { "adjustBitmapOrientation, adjust degree " + matrix + "to 0." }
        decodeBitmap.transformBitmap(matrix)
    } catch (e: Throwable) {
        e.printStackTrace()
        decodeBitmap
    }
}

val InputStream.orientationMatrix: Matrix
    get() {
        val matrix = Matrix()
        try {
            val exif = ExifInterface(this)
            val orientation =
                exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )

            when (orientation) {
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                    matrix.setRotate(180f)
                    matrix.postScale(-1f, 1f)
                }

                ExifInterface.ORIENTATION_TRANSPOSE -> {
                    matrix.setRotate(90f)
                    matrix.postScale(-1f, 1f)
                }

                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
                ExifInterface.ORIENTATION_TRANSVERSE -> {
                    matrix.setRotate(-90f)
                    matrix.postScale(-1f, 1f)
                }

                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return matrix
    }
