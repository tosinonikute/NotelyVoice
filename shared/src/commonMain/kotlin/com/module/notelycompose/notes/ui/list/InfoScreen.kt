package com.module.notelycompose.notes.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleEventObserver
import com.module.notelycompose.platform.getPlatform
import com.module.notelycompose.notes.ui.detail.AndroidNoteTopBar
import com.module.notelycompose.notes.ui.detail.IOSNoteTopBar
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.platform.BrowserLauncher
import com.module.notelycompose.platform.HandlePlatformBackNavigation
import com.module.notelycompose.platform.presentation.PlatformViewModel
import com.module.notelycompose.resources.vectors.IcFaq
import com.module.notelycompose.resources.vectors.Images
import com.module.notelycompose.web.ui.WebViewScreen
import com.module.notelycompose.resources.Res
import com.module.notelycompose.resources.information_base_url
import com.module.notelycompose.resources.faq_url
import com.module.notelycompose.resources.about_url
import com.module.notelycompose.resources.support_url
import com.module.notelycompose.resources.privacy_url
import com.module.notelycompose.resources.faq
import com.module.notelycompose.resources.about
import com.module.notelycompose.resources.support
import com.module.notelycompose.resources.privacy
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.getKoin
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import com.module.notelycompose.resources.more_info_url

/**
 * A settings bottom sheet that displays a list of options and can navigate to web content
 */
@Composable
fun InfoScreen(
    navigateBack: () -> Unit,
    onNavigateToWebPage: (String, String) -> Unit,
    appVersion: String = getKoin().get(named("AppVersion")),
    platformViewModel: PlatformViewModel = koinViewModel(),
    browserLauncher: BrowserLauncher = koinInject()
) {
    val isAndroid = platformViewModel.state.value.isAndroid
    var showWebView by remember { mutableStateOf(false) }
    var currentPageTitle by remember { mutableStateOf("") }
    var currentPageUrl by remember { mutableStateOf("") }
    var shouldUseCustomBackHandler by remember { mutableStateOf(true) }

    val faq  = stringResource(Res.string.faq)
    val about  = stringResource(Res.string.about)
    val support  = stringResource(Res.string.support)
    val privacy  = stringResource(Res.string.privacy)
    val infoBaseUrl  = stringResource(Res.string.information_base_url)
    val faqUrl  = infoBaseUrl + stringResource(Res.string.faq_url)
    val aboutUrl  = infoBaseUrl + stringResource(Res.string.about_url)
    val supportUrl  = infoBaseUrl + stringResource(Res.string.support_url)
    val privacyUrl  = infoBaseUrl + stringResource(Res.string.privacy_url)
    val moreInfoUrl  = infoBaseUrl + stringResource(Res.string.more_info_url)
    val appVersionStr = "Version $appVersion"

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    // Reset any problematic state when returning to the app
                    showWebView = false
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (showWebView) {
        if(isAndroid) {
            browserLauncher.openUrl(currentPageUrl)
        } else {
            WebViewScreen(
                title = currentPageTitle,
                url = currentPageUrl,
                onBackPressed = { showWebView = false }
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp)
                .background(LocalCustomColors.current.bodyBackgroundColor)
        ) {
            if (getPlatform().isAndroid) {
                AndroidNoteTopBar(
                    title = "",
                    onNavigateBack = navigateBack
                )
            } else {
                IOSNoteTopBar(
                    onNavigateBack = navigateBack
                )
            }

            // List of menu items
            SettingsMenuItem(
                icon = Images.Icons.IcFaq,
                title = "FAQ",
                onClick = {
                    currentPageTitle = faq
                    currentPageUrl = faqUrl
                    showWebView = true
                    onNavigateToWebPage(currentPageTitle, currentPageUrl)
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )

            SettingsMenuItem(
                icon = Icons.Default.Info,
                title = "About Us",
                onClick = {
                    currentPageTitle = about
                    currentPageUrl = aboutUrl
                    showWebView = true
                    onNavigateToWebPage(currentPageTitle, currentPageUrl)
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )

            SettingsMenuItem(
                icon = Icons.Default.Star,
                title = "Support",
                onClick = {
                    currentPageTitle = support
                    currentPageUrl = supportUrl
                    showWebView = true
                    onNavigateToWebPage(currentPageTitle, currentPageUrl)
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )

            SettingsMenuItem(
                icon = Icons.Default.Lock,
                title = "Privacy Policy",
                onClick = {
                    currentPageTitle =  privacy
                    currentPageUrl = privacyUrl
                    showWebView = true
                    onNavigateToWebPage(currentPageTitle, currentPageUrl)
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Informational section about web version
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "This is the free, open-source version. A web version with additional features is also available.",
                    fontSize = 16.sp,
                    color = LocalCustomColors.current.bodyContentColor.copy(alpha = 0.75f),
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 22.sp
                )

                OutlinedButton(
                    onClick = {
                        browserLauncher.openUrl(moreInfoUrl)
                    },
                    modifier = Modifier.height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = LocalCustomColors.current.bodyContentColor
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        LocalCustomColors.current.bodyContentColor.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = "Visit website",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            // App Version at the bottom
            Text(
                text = appVersionStr,
                fontSize = 18.sp,
                color = LocalCustomColors.current.bodyContentColor,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
        }
    }

    HandlePlatformBackNavigation(enabled = shouldUseCustomBackHandler) {
        navigateBack()
    }
}

@Composable
fun SettingsMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Circular black background for icon
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = LocalCustomColors.current.bodyContentColor
        )
    }
}
