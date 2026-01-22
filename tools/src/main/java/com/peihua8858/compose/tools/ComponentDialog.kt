package com.peihua8858.compose.tools

import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout.LayoutParams
import androidx.activity.ComponentDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

fun ComponentDialog.setContent(
    parent: CompositionContext? = null,
    content: @Composable () -> Unit,
) {
    val existingComposeView = window?.decorView
        ?.findViewById<ViewGroup>(android.R.id.content)
        ?.getChildAt(0) as? ComposeView

    if (existingComposeView != null) with(existingComposeView) {
        disposeComposition()
        setParentCompositionContext(parent)
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent(content)
    } else ComposeView(this.context).apply {
        // Set content and parent **before** setContentView
        // to have ComposeView create the composition on attach
        setParentCompositionContext(parent)
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)
        setContent(content)
        // Set the view tree owners before setting the content view so that the inflation process
        // and attach listeners will see them already present
        setOwners()
        setContentView(this, LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        })
    }
}

private fun ComponentDialog.setOwners() {
    window?.decorView?.let { decorView ->
        if (decorView.findViewTreeLifecycleOwner() == null) {
            decorView.setViewTreeLifecycleOwner(this)
        }
        if (this is ViewModelStoreOwner) {
            if (decorView.findViewTreeViewModelStoreOwner() == null) {
                decorView.setViewTreeViewModelStoreOwner(this)
            }
        }
        if (decorView.findViewTreeSavedStateRegistryOwner() == null) {
            decorView.setViewTreeSavedStateRegistryOwner(this)
        }
    }
}