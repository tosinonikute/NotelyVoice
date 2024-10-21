package com.module.notelycompose.android.ui.detail

import com.module.notelycompose.android.ui.ContentBottomAppBar
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.module.notelycompose.android.R
import com.module.notelycompose.android.theme.MyApplicationTheme
import com.module.notelycompose.core.DateTimeUtil
import com.module.notelycompose.notes.domain.Note
import com.module.notelycompose.notes.presentation.list.ui.NoteList
import dagger.hilt.android.AndroidEntryPoint
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.TimeZone

@AndroidEntryPoint
class DetailActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // NoteAppUi()
                    // ContentBottomAppBar()
                    NoteDetailScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteDetailScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My note") },
                navigationIcon = {
                    IconButton(onClick = { /* Navigate back */ }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Add Note */ }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomNavigationBar()
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Milan",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { /* Play audio */ }) {
                        Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = "Play")
                    }
                    Text("46 sec")
                }
                Text(
                    text = "The most beautiful places to visit:",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                ListItem(
                    text = { Text("The Milan Cathedral. It is the third largest cathedral in the world. I need to buy tickets online or booking a guided tour of the cathedral to save a lot of time!!") },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                ListItem(
                    text = { Text("Galleria Vittorio Emanuele II. Just outside the cathedral, on the Piazza del Duomo. There I can find the most famous fashion designers stores including Vuitton and Prada to do a little shopping") },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                ListItem(
                    text = { Text("Santa Maria delle Grazie church The painting of the Last Supper by Leonardo da Vinci. Remember to book this visit in advance because can only visit by appointment and in small groups of twenty people for 15 minutes!!") },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                        EditingToolbar()
                    }
                }
            }
        }
    )
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Upload */ }) {
            Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "Upload")
        }
        IconButton(onClick = { /* Delete */ }) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
        }
        IconButton(onClick = { /* Text */ }) {
            Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "Text")
        }
        IconButton(onClick = { /* Undo */ }) {
            Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "Undo")
        }
        IconButton(onClick = { /* Redo */ }) {
            Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "Redo")
        }
    }
}

@Composable
fun EditingToolbar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* Handle A click */ }) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Align Left",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { /* Handle B click */ }) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Bold",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { /* Handle I click */ }) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Italic",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { /* Handle U click */ }) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Underline",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { /* Handle List click */ }) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "List",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { /* Handle Align Center click */ }) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Align Center",
                tint = Color.LightGray
            )
        }
        IconButton(onClick = { /* Handle Align Right click */ }) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Align Right",
                tint = Color.LightGray
            )
        }
    }
}
