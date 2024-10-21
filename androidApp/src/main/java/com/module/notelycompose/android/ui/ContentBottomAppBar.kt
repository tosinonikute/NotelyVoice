package com.module.notelycompose.android.ui

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.android.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentBottomAppBar() {
    Scaffold(
        bottomBar = {
            BottomAppBar(

                contentColor = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomAppBarItem(
                        iconRes = R.drawable.ic_file,
                        label = "Home"
                    )
                    BottomAppBarItem(
                        iconRes = R.drawable.ic_file,
                        label = "Settings"
                    )
                    BottomAppBarItem(
                        iconRes = R.drawable.ic_file,
                        label = "Profile"
                    )
                }
            }
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NoteAppUi()
            }
        }
    )
}

@Composable
fun BottomAppBarItem(
    iconRes: Int,
    label: String,
    modifier: Modifier = Modifier,
    tint: Color = Color.Black
) {
    Column(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(24.dp) // Set the icon size
        )
        Spacer(modifier = Modifier.height(4.dp)) // Space between icon and text
        Text(
            text = label,
            color = tint,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}
