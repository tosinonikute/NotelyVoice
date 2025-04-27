package com.module.notelycompose.notes.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import com.module.notelycompose.web.ui.WebViewScreen

/**
 * A settings bottom sheet that displays a list of options and can navigate to web content
 */
@Composable
fun InfoBottomSheet(
    onDismiss: () -> Unit,
    onNavigateToWebPage: (String, String) -> Unit
) {
    var showWebView by remember { mutableStateOf(false) }
    var currentPageTitle by remember { mutableStateOf("") }
    var currentPageUrl by remember { mutableStateOf("") }

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
                .padding(vertical = 16.dp)
        ) {
            Divider(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.Gray.copy(alpha = 0.3f))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // List of menu items
            SettingsMenuItem(
                icon = Icons.Default.Info,
                title = "FAQ",
                onClick = {
                    currentPageTitle = "FAQ"
                    currentPageUrl = "https://example.com/faq"
                    showWebView = true
                    onNavigateToWebPage(currentPageTitle, currentPageUrl)
                }
            )

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            SettingsMenuItem(
                icon = Icons.Default.Info,
                title = "About Us",
                onClick = {
                    currentPageTitle = "About Us"
                    currentPageUrl = "https://example.com/about"
                    showWebView = true
                    onNavigateToWebPage(currentPageTitle, currentPageUrl)
                }
            )

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            SettingsMenuItem(
                icon = Icons.Default.Star,
                title = "Support",
                onClick = {
                    currentPageTitle = "Support"
                    currentPageUrl = "https://example.com/rate"
                    showWebView = true
                    onNavigateToWebPage(currentPageTitle, currentPageUrl)
                }
            )

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            SettingsMenuItem(
                icon = Icons.Default.Lock,
                title = "Privacy Policy",
                onClick = {
                    currentPageTitle = "Privacy Policy"
                    currentPageUrl = "https://tosinonikute.com/notely/"
                    showWebView = true
                    onNavigateToWebPage(currentPageTitle, currentPageUrl)
                }
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