package com.saj.simplenote.home.ui.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saj.simplenote.R
import com.saj.simplenote.domain.ui.theme.MyPurple
import com.saj.simplenote.domain.ui.theme.MyWhite
import com.saj.simplenote.domain.ui.theme.SimpleNoteTheme
import com.saj.simplenote.home.data.model.Note
import com.saj.simplenote.home.ui.model.HomeUIModel
import com.saj.simplenote.home.ui.model.HomeUIState
import com.saj.simplenote.domain.util.DateUtil
import com.saj.simplenote.domain.util.NoteColorUtil

// --- Custom Shape for the Bottom Bar Notch ---
class FabNotchShape(
    private val fabSize: Dp,
    private val fabMargin: Dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(Path().apply {
            val fabRadius = with(density) { (fabSize / 2).toPx() }
            val notchRadius = fabRadius + with(density) { fabMargin.toPx() }

            // The notch is centered horizontally
            val notchCenterX = size.width / 2f

            // Move to the top left of the bar
            moveTo(0f, 0f)

            // Draw a line to the start of the notch
            lineTo(notchCenterX - notchRadius, 0f)

            // Draw the semicircle arc for the notch
            arcTo(
                rect = Rect(
                    left = notchCenterX - notchRadius,
                    top = -notchRadius,
                    right = notchCenterX + notchRadius,
                    bottom = notchRadius
                ),
                startAngleDegrees = 180.0f,
                sweepAngleDegrees = -180.0f,
                forceMoveTo = false
            )

            // Continue the line to the top right of the bar
            lineTo(size.width, 0f)

            // Complete the rectangle
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        })
    }
}

@Composable
fun HomeScreen(
    data: HomeUIModel,
    uiState: HomeUIState,
    onAddNoteClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHomeClick: () -> Unit,
    onNoteClick: (Int) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit,
) {
    val backgroundColor = Color(0xFFF9F9FF)
    val primaryTextColor = Color(0xFF3F3F3F)
    val secondaryTextColor = Color(0xFF8A8A8A)
    val inactiveNavColor = Color(0xFFBDBDBD)

    // Define dimensions for the bottom bar and FAB
    val fabSize = 68.dp
    val bottomBarHeight = 80.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomBarHeight / 2)
        ) {
            // Show different content based on whether there are notes
            if (uiState.notes.isEmpty() && !uiState.isLoading) {
                // Empty state - show the start journey content
                EmptyStateContent(
                    data = data,
                    primaryTextColor = primaryTextColor,
                    secondaryTextColor = secondaryTextColor
                )
            } else {
                // Notes exist - show search and notes list
                NotesListContent(
                    uiState = uiState,
                    onSearchQueryChanged = onSearchQueryChanged,
                    onNoteClick = onNoteClick,
                    onLoadMore = onLoadMore,
                    onRefresh = onRefresh,
                )
            }
        }

        // --- Custom Bottom Bar with Notch ---
        CustomBottomBar(
            data = data,
            fabSize = fabSize,
            bottomBarHeight = bottomBarHeight,
            inactiveNavColor = inactiveNavColor,
            onHomeClick = onHomeClick,
            onSettingsClick = onSettingsClick,
            onAddNoteClick = onAddNoteClick
        )
    }
}

@Composable
private fun EmptyStateContent(
    data: HomeUIModel,
    primaryTextColor: Color,
    secondaryTextColor: Color
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(140.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_home),
            contentDescription = "Start Your Journey Illustration",
            modifier = Modifier.size(280.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = data.title,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            ),
            color = primaryTextColor,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = data.subtitle,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 17.sp,
                lineHeight = 24.sp
            ),
            color = secondaryTextColor,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_direction),
            contentDescription = "Arrow pointing to add button",
            modifier = Modifier.size(150.dp).padding(start = 110.dp)
        )
    }
}

@Composable
private fun NotesListContent(
    uiState: HomeUIState,
    onSearchQueryChanged: (String) -> Unit,
    onNoteClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit,
) {
    val gridState = rememberLazyStaggeredGridState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(bottom = 40.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Search bar with external icon
        // Search bar with external icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search icon outside the text field
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF111827),
                modifier = Modifier.size(30.dp)
            )

            // Text field without leading icon
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChanged,
                placeholder = {
                    Text(
                        text = "Search...",
                        color = Color(0xFF8A8A8A),
                        fontSize = 16.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MyPurple,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color(0xFFEFEEF0),
                    cursorColor = MyPurple
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Notes",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            ),
            color = Color(0xFF3F3F3F),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Notes staggered grid
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            state = gridState,
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalItemSpacing = 12.dp
        ) {
            items(uiState.notes) { note ->
                NoteCard(
                    note = note,
                    onClick = { onNoteClick(note.id) }
                )
            }

            // Load more indicator
            if (uiState.isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MyPurple,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }

    // Trigger load more when reaching the end
    val shouldLoadMore = remember(uiState, gridState) {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1

            // Check if we're near the end (within 2 items) and there are more pages to load
            val isNearEnd = lastVisibleItemIndex >= totalItems - 2
            val canLoadMore = uiState.hasMorePages && !uiState.isLoadingMore && !uiState.isLoading
            val hasItems = uiState.notes.isNotEmpty()

            isNearEnd && canLoadMore && hasItems
        }
    }

    LaunchedEffect(shouldLoadMore.value, uiState.hasMorePages, uiState.isLoadingMore) {
        if (shouldLoadMore.value) {
            onLoadMore()
        }
    }
}

@Composable
private fun NoteCard(
    note: Note,
    onClick: () -> Unit
) {
    // Generate a consistent color based on note ID
    val backgroundColor = NoteColorUtil.getColorForNoteId(note.id)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = Color(0xFF3F3F3F),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            if (note.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = note.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    ),
                    color = Color(0xFF8A8A8A),
                    // Remove maxLines to allow natural height variation
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = DateUtil.formatForDisplay(note.updated_at),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp
                ),
                color = Color(0xFFBDBDBD)
            )
        }
    }
}

@Composable
private fun CustomBottomBar(
    data: HomeUIModel,
    fabSize: Dp,
    bottomBarHeight: Dp,
    inactiveNavColor: Color,
    onHomeClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAddNoteClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Bottom bar with notch
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(bottomBarHeight),
            color = Color.White,
            shadowElevation = 8.dp,
            shape = FabNotchShape(fabSize = fabSize, fabMargin = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onHomeClick() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_home_logo),
                        contentDescription = data.homeBottomNav,
                        tint = MyPurple,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = data.homeBottomNav,
                        color = MyPurple,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Settings button
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onSettingsClick() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_setting_logo),
                        contentDescription = data.settingsBottomNav,
                        tint = inactiveNavColor,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = data.settingsBottomNav,
                        color = inactiveNavColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = onAddNoteClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = bottomBarHeight - (fabSize / 2))
                .size(fabSize),
            shape = CircleShape,
            containerColor = MyPurple,
            contentColor = MyWhite,
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = data.addNoteButton,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}

// Old inline date formatting replaced by centralized DateUtil.


@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun HomeScreenPreview() {
    SimpleNoteTheme {
        HomeScreen(
            data = HomeUIModel(
                title = "Start Your Journey",
                subtitle = "Every big step start with small step.\nNotes your first idea and start\nyour journey!",
                homeBottomNav = "Home",
                settingsBottomNav = "Settings",
                addNoteButton = "Add Note"
            ),
            uiState = HomeUIState(),
            onAddNoteClick = {},
            onSettingsClick = {},
            onHomeClick = {},
            onNoteClick = {},
            onSearchQueryChanged = {},
            onLoadMore = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun HomeScreenWithNotesPreview() {
    SimpleNoteTheme {
        HomeScreen(
            data = HomeUIModel(
                title = "Start Your Journey",
                subtitle = "Every big step start with small step.\nNotes your first idea and start\nyour journey!",
                homeBottomNav = "Home",
                settingsBottomNav = "Settings",
                addNoteButton = "Add Note"
            ),
            uiState = HomeUIState(
                notes = listOf(
                    Note(
                        id = 1,
                        title = "New Product Idea Design",
                        description = "Create a mobile app UI Kit that provide a basic notes functionality but with some improvement.\n\nThere will be a space where user can select what kind of notes that user needed, so the experience while taking notes can be unique based on the needs.",
                        created_at = "2024-01-15T10:30:00Z",
                        updated_at = "2024-01-15T10:30:00Z",
                        creator_name = "Jafari Ali",
                        creator_username = "ali"
                    ),
                    Note(
                        id = 2,
                        title = "Travel Planning",
                        description = "Plan the summer vacation trip to Europe. Research flights, hotels, and attractions in Paris, Rome, and Barcelona.",
                        created_at = "2024-01-14T14:30:00Z",
                        updated_at = "2024-01-14T14:30:00Z",
                        creator_name = "Jafari Ali",
                        creator_username = "ali"
                    ),
                    Note(
                        id = 3,
                        title = "Meeting Notes",
                        description = "Discuss the project requirements and timeline with the team.",
                        created_at = "2024-01-13T09:15:00Z",
                        updated_at = "2024-01-13T09:15:00Z",
                        creator_name = "Jafari Ali",
                        creator_username = "ali"
                    ),
                    Note(
                        id = 4,
                        title = "Shopping List",
                        description = "Buy groceries for the week including fruits, vegetables, milk, and bread.",
                        created_at = "2024-01-12T16:45:00Z",
                        updated_at = "2024-01-12T16:45:00Z",
                        creator_name = "Jafari Ali",
                        creator_username = "ali"
                    ),
                    Note(
                        id = 5,
                        title = "Weekend Plans",
                        description = "Visit the park, have lunch with friends, and watch a movie in the evening. Also need to finish reading the book.",
                        created_at = "2024-01-11T11:20:00Z",
                        updated_at = "2024-01-11T11:20:00Z",
                        creator_name = "Jafari Ali",
                        creator_username = "ali"
                    ),
                    Note(
                        id = 6,
                        title = "Workout Routine",
                        description = "Monday: Chest and triceps\nTuesday: Back and biceps\nWednesday: Legs\nThursday: Shoulders\nFriday: Arms",
                        created_at = "2024-01-10T08:00:00Z",
                        updated_at = "2024-01-10T08:00:00Z",
                        creator_name = "Jafari Ali",
                        creator_username = "ali"
                    )
                )
            ),
            onAddNoteClick = {},
            onSettingsClick = {},
            onHomeClick = {},
            onNoteClick = {},
            onSearchQueryChanged = {},
            onLoadMore = {},
            onRefresh = {}
        )
    }
}