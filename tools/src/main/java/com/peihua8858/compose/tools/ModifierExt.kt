package com.peihua8858.compose.tools

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import kotlin.times

/**
 * 圆角卡片+ 阴影
 */
fun Modifier.surface(
    topStart: Dp = 0.dp,
    topEnd: Dp = 0.dp,
    bottomEnd: Dp = 0.dp,
    bottomStart: Dp = 0.dp,
    elevation: Dp = 0.dp,
    backgroundColor: Color = Color.White,
    border: BorderStroke? = null,
) = surface(
    RoundedCornerShape(topStart, topEnd, bottomEnd, bottomStart),
    backgroundColor, border, elevation
)

/**
 * 圆角卡片+ 阴影
 */
fun Modifier.surface(
    radius: Dp = 0.dp,
    elevation: Dp = 0.dp,
    backgroundColor: Color = Color.White,
    border: BorderStroke? = null,
) = surface(
    RoundedCornerShape(radius),
    backgroundColor, border, elevation
)

/**
 * 圆角卡片+ 阴影
 */
fun Modifier.surface(
    radius: CornerSize = CornerSize(0.dp),
    elevation: Dp = 0.dp,
    backgroundColor: Color = Color.White,
    border: BorderStroke? = null,
) = surface(
    RoundedCornerShape(radius),
    backgroundColor, border, elevation
)

/**
 * 圆角卡片+ 阴影
 */
fun Modifier.surface(
    shape: Shape = RoundedCornerShape(8.dp),
    backgroundColor: Color = Color.White,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
) = this
    .shadow(elevation, shape, clip = false)
    .then(if (border != null) Modifier.border(border, shape) else Modifier)
    .background(color = backgroundColor, shape = shape)
    .clip(shape)


/**
 * 防止重复点击(有的人可能会手抖连点两次,造成奇怪的bug)
 */
@Composable
inline fun Modifier.clickable(
    time: Int = 500,
    enabled: Boolean = true,//中间这三个是clickable自带的参数
    onClickLabel: String? = null,
    role: Role? = null,
    crossinline onClick: () -> Unit,
): Modifier {
    var lastClickTime by remember { mutableLongStateOf(value = 0L) }//使用remember函数记录上次点击的时间
    return clickable(enabled, onClickLabel, role) {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - time >= lastClickTime) {//判断点击间隔,如果在间隔内则不回调
            onClick()
            lastClickTime = currentTimeMillis
        }
    }
}

/**
 * 防止重复点击(有的人可能会手抖连点两次,造成奇怪的bug)
 */
@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
fun Modifier.combinedClickable(
    time: Int = 500,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    hapticFeedbackEnabled: Boolean = true,
    onClick: () -> Unit,
): Modifier {
    var lastClickTime by remember { mutableLongStateOf(value = 0L) }//使用remember函数记录上次点击的时间
    val invokeClick = { click: () -> Unit ->
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - time >= lastClickTime) {//判断点击间隔,如果在间隔内则不回调
            click()
            lastClickTime = currentTimeMillis
        }
    }
    return combinedClickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onLongClickLabel = onLongClickLabel,
        role = role,
        hapticFeedbackEnabled = hapticFeedbackEnabled,
        onLongClick = {
            invokeClick(onLongClick ?: {})
        }, onDoubleClick = {
            invokeClick(onDoubleClick ?: {})
        }) {
        invokeClick(onClick)
    }
}


internal fun Modifier.defaultErrorSemantics(
    isError: Boolean,
    defaultErrorMessage: String,
): Modifier = if (isError) semantics { error(defaultErrorMessage) } else this

/**
 * 顶部淡入淡出遮罩（支持动画）
 *
 * @param alpha 遮罩整体透明度（0f = 完全透明，1f = 完全不透明）
 * @param height 遮罩区域高度
 * @param colors 渐变颜色列表（至少两个），默认从透明到主题背景色
 */
@Composable
fun Modifier.topFadeMask(
    alpha: Float = 1f,
    height: Dp = 20.dp,
    colors: List<Color> = listOf(Color.White, Color.Transparent),
): Modifier {
    val alphaAnim by animateFloatAsState(targetValue = alpha)
    return this.then(
        Modifier.drawWithContent {
            drawContent()
            if (alphaAnim > 0f) {
                // 应用整体 alpha 到整个遮罩
                val pxHeight = height.toPx()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = colors,
                        startY = 0f,
                        endY = pxHeight
                    ),
                    alpha = alphaAnim,
                    size = Size(size.width, pxHeight),
                    topLeft = Offset.Zero
                )
            }
        }
    )
}

/**
 * 底部淡入淡出遮罩（支持动画）
 *
 * @param alpha 遮罩整体透明度（0f = 完全透明，1f = 完全不透明）
 * @param height 遮罩区域高度
 * @param colors 渐变颜色列表（至少两个），默认从主题背景色到透明
 */
@Composable
fun Modifier.bottomFadeMask(
    alpha: Float = 1f,
    height: Dp = 20.dp,
    colors: List<Color> = listOf(Color.Transparent, Color.White),
): Modifier {
    val alphaAnim by animateFloatAsState(targetValue = alpha)
    return this.then(
        Modifier.drawWithContent {
            drawContent()
            if (alphaAnim > 0f) {
                val pxHeight = height.toPx()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = colors,
                        startY = size.height - pxHeight,
                        endY = size.height
                    ),
                    alpha = alphaAnim,
                    size = Size(size.width, pxHeight),
                    topLeft = Offset(0f, size.height - pxHeight)
                )
            }
        }
    )
}

/**
 * 顶部淡入淡出遮罩（Composable版本）
 */
@Composable
fun Modifier.topShadow(
    visible: Boolean = true,
    colors: List<Color> = listOf(Color.White, Color.Transparent),
    height: Dp = 20.dp,
): Modifier {
    val alphaAnim by animateFloatAsState(targetValue = if (visible) 1f else 0f)
    return this.then(
        Modifier.drawWithContent {
            drawContent()
            if (alphaAnim > 0f) {
                val pxHeight = height.toPx()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = colors,
                        startY = 0f,
                        endY = pxHeight
                    ),
                    alpha = alphaAnim,
                    size = Size(size.width, pxHeight),
                    topLeft = Offset.Zero
                )
            }
        }
    )
}

/**
 * 底部淡入淡出遮罩（Composable版本）
 */
@Composable
fun Modifier.bottomShadow(
    visible: Boolean = true,
    colors: List<Color> = listOf(Color.Transparent, Color.White),
    height: Dp = 20.dp,
): Modifier {
    val alphaAnim by animateFloatAsState(targetValue = if (visible) 1f else 0f)
    return this.then(
        Modifier.drawWithContent {
            drawContent()
            if (alphaAnim > 0f) {
                val pxHeight = height.toPx()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = colors,
                        startY = size.height - pxHeight,
                        endY = size.height
                    ),
                    alpha = alphaAnim,
                    size = Size(size.width, pxHeight),
                    topLeft = Offset(0f, size.height - pxHeight)
                )
            }
        }
    )
}