package com.module.notelycompose.web

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.*
import platform.Foundation.NSURL
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.CoreGraphics.CGRectMake

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WebViewContent(url: String) {
    val webViewConfig = remember { WKWebViewConfiguration() }

    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            WKWebView(
                frame = CGRectMake(0.0, 0.0, 0.0, 0.0),
                configuration = webViewConfig
            ).apply {
                // Load the URL
                val nsUrl = NSURL(string = url)
                if (nsUrl != null) {
                    loadRequest(platform.Foundation.NSURLRequest(uRL = nsUrl))
                }
            }
        },
        update = { webView ->
            // Update logic when the URL changes
            val nsUrl = NSURL(string = url)
            if (nsUrl != null) {
                (webView as WKWebView).loadRequest(platform.Foundation.NSURLRequest(uRL = nsUrl))
            }
        }
    )
}
