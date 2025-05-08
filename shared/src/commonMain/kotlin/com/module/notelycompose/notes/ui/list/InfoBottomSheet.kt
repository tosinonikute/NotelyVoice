package com.module.notelycompose.notes.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    bottomSheetState: ModalBottomSheetState
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
                title = faq,
                onClick = {
                    currentPageTitle = faq
                    currentPageUrl = faqUrl
                    showWebView = true
                    onNavigateToWebPage(currentPageTitle, currentPageUrl)
                }
            )

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            SettingsMenuItem(
                icon = Icons.Default.Info,
                title = about,
                onClick = {
                    currentPageTitle = about
                    currentPageUrl = aboutUrl
                    showWebView = true
                    onNavigateToWebPage(currentPageTitle, currentPageUrl)
                }
            )

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            SettingsMenuItem(
                icon = Icons.Default.Star,
                title = support,
                onClick = {
                    currentPageTitle = support
                    currentPageUrl = supportUrl
                    showWebView = true
                    onNavigateToWebPage(currentPageTitle, currentPageUrl)
                }
            )

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            SettingsMenuItem(
                icon = Icons.Default.Lock,
                title = privacy,
                onClick = {
                    currentPageTitle =  privacy
                    currentPageUrl = privacyUrl
                    showWebView = true
                    onNavigateToWebPage(currentPageTitle, currentPageUrl)
                }
            )

            Spacer(Modifier.padding(600.dp))
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
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = LocalCustomColors.current.bodyContentColor
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = LocalCustomColors.current.bodyContentColor
        )
    }
}