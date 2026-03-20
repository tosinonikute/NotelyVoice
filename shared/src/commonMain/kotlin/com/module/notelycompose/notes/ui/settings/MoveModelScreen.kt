package com.module.notelycompose.notes.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.modelDownloader.MoveModelEffect
import com.module.notelycompose.modelDownloader.MoveModelViewModel
import com.module.notelycompose.notes.ui.theme.LocalCustomColors
import org.koin.compose.koinInject

@Composable
fun MoveModelScreen(
    navigateBack: () -> Unit,
    viewModel: MoveModelViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is MoveModelEffect.Success -> {
                    showSuccess = true
                    errorMessage = null
                }
                is MoveModelEffect.Error -> {
                    errorMessage = effect.message
                    showSuccess = false
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalCustomColors.current.bodyBackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = LocalCustomColors.current.bodyContentColor
                )
            }
            Text(
                text = "Move Model",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = LocalCustomColors.current.bodyContentColor,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showSuccess) {
                SuccessContent(navigateBack = navigateBack)
            } else if (uiState.isMoving) {
                MovingContent()
            } else {
                InitialContent(
                    onMoveClick = { viewModel.moveModelToProtectedStorage() },
                    errorMessage = errorMessage
                )
            }
        }
    }
}

@Composable
private fun InitialContent(
    onMoveClick: () -> Unit,
    errorMessage: String?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalCustomColors.current.bodyBackgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📦",
                fontSize = 64.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Move Model to Protected Storage",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = LocalCustomColors.current.bodyContentColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Prevents your OS from automatically clearing the voice model. Requires 150MB or ~500MB of internal storage.",
                fontSize = 16.sp,
                color = LocalCustomColors.current.settingsBodyTextColor,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Recommended for users on GrapheneOS or MIUI.",
                fontSize = 14.sp,
                color = LocalCustomColors.current.settingsBodyTextColor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onMoveClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6366F1)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Move Now",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier.padding(8.dp)
                )
            }

            errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    fontSize = 14.sp,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun MovingContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = Color(0xFF6366F1),
            strokeWidth = 6.dp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Moving model to protected storage...",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = LocalCustomColors.current.bodyContentColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "This may take a few minutes.\nPlease wait and do not close the app.",
            fontSize = 16.sp,
            color = LocalCustomColors.current.settingsBodyTextColor,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

@Composable
private fun SuccessContent(navigateBack: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            tint = Color(0xFF10B981),
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Success!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = LocalCustomColors.current.bodyContentColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your voice model is now stored in protected internal storage and won't be automatically cleared by your OS.",
            fontSize = 16.sp,
            color = LocalCustomColors.current.settingsBodyTextColor,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = navigateBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF10B981)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Done",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
