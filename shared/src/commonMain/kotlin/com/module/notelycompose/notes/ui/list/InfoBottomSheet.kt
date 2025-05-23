package com.module.notelycompose.notes.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.getPlatform
import com.module.notelycompose.notes.ui.detail.AndroidNoteTopBar
import com.module.notelycompose.notes.ui.detail.IOSNoteTopBar
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.resources.vectors.IcFaq
import com.module.notelycompose.resources.vectors.Images
import com.module.notelycompose.web.ui.WebViewScreen
import notelycompose.shared.generated.resources.Res
import notelycompose.shared.generated.resources.information_base_url
import notelycompose.shared.generated.resources.faq_url
import notelycompose.shared.generated.resources.about_url
import notelycompose.shared.generated.resources.support_url
import notelycompose.shared.generated.resources.privacy_url
import notelycompose.shared.generated.resources.faq
import notelycompose.shared.generated.resources.about
import notelycompose.shared.generated.resources.support
import notelycompose.shared.generated.resources.privacy
import org.jetbrains.compose.resources.stringResource

/**
 * A settings bottom sheet that displays a list of options and can navigate to web content
 */
@Composable
fun InfoBottomSheet(
    onDismiss: () -> Unit,
    onNavigateToWebPage: (String, String) -> Unit,
    bottomSheetState: ModalBottomSheetState,
    appVersion: String
) {
    var showWebView by remember { mutableStateOf(false) }
    var currentPageTitle by remember { mutableStateOf("") }
    var currentPageUrl by remember { mutableStateOf("") }

    val faq  = stringResource(Res.string.faq)
    val about  = stringResource(Res.string.about)
    val support  = stringResource(Res.string.support)
    val privacy  = stringResource(Res.string.privacy)
    val infoBaseUrl  = stringResource(Res.string.information_base_url)
    val faqUrl  = infoBaseUrl + stringResource(Res.string.faq_url)
    val aboutUrl  = infoBaseUrl + stringResource(Res.string.about_url)
    val supportUrl  = infoBaseUrl + stringResource(Res.string.support_url)
    val privacyUrl  = infoBaseUrl + stringResource(Res.string.privacy_url)
    val appVersionStr = "Version $appVersion"

    LaunchedEffect(bottomSheetState) {
        snapshotFlow { bottomSheetState.currentValue }
            .collect { sheetValue ->
                if (sheetValue == ModalBottomSheetValue.Hidden) {
                    showWebView = false
                }
            }
    }

    if (showWebView) {
        WebViewScreen(
            title = currentPageTitle,
            url = currentPageUrl,
            onBackPressed = { showWebView = false }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 0.dp)
        ) {
            if (getPlatform().isAndroid) {
                AndroidNoteTopBar(
                    title = "",
                    onNavigateBack = onDismiss
                )
            } else {
                IOSNoteTopBar(
                    onNavigateBack = onDismiss
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
