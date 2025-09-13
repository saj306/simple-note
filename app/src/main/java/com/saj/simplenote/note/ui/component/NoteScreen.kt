package com.saj.simplenote.note.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saj.simplenote.R
import com.saj.simplenote.domain.ui.theme.MyGrey
import com.saj.simplenote.domain.ui.theme.MyGreyDivider
import com.saj.simplenote.domain.ui.theme.MyPurple
import com.saj.simplenote.domain.ui.theme.MyWhite
import com.saj.simplenote.note.ui.model.NoteUIModel
import com.saj.simplenote.note.ui.model.NoteUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    uiState: NoteUIState,
    uiModel: NoteUIModel,
    onTitleChanged: (String) -> Unit,
    onContentChanged: (String) -> Unit,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val titleFocusRequester = remember { FocusRequester() }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // Request focus on title field when creating a new note
    LaunchedEffect(uiState.noteId) {
        if (uiState.noteId == null && uiState.title.isEmpty()) {
            titleFocusRequester.requestFocus()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MyWhite)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top bar with back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .clickable { onBackClick() },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_simple_arrow_back),
                    contentDescription = uiModel.backButton,
                    tint = MyPurple,
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = uiModel.backButton,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MyPurple,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = MyGreyDivider
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Title input field
            BasicTextField(
                value = uiState.title,
                onValueChange = onTitleChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .focusRequester(titleFocusRequester),
                textStyle = TextStyle(
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (uiState.title.isEmpty()) {
                            Text(
                                text = uiModel.titlePlaceholder,
                                style = TextStyle(
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black.copy(alpha = 0.4f)
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Content input field
            BasicTextField(
                value = uiState.content,
                onValueChange = onContentChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                textStyle = TextStyle(
                    fontSize = 17.sp,
                    color = Color.Black,
                    lineHeight = 24.sp
                ),
                decorationBox = { innerTextField ->
                    Box {
                        if (uiState.content.isEmpty()) {
                            Text(
                                text = uiModel.contentPlaceholder,
                                style = TextStyle(
                                    fontSize = 17.sp,
                                    color = MyGrey.copy(alpha = 0.6f),
                                    lineHeight = 24.sp
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )

            // --- Corrected Bottom Section ---

            HorizontalDivider(
                thickness = 1.dp,
                color = MyGreyDivider
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp), // Set a fixed height for consistency
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side: Text and saving indicator
                Row(
                    modifier = Modifier.padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = uiState.lastEdited,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Black,
                            fontSize = 13.sp
                        )
                    )

                    // Show saving indicator
                    if (uiState.isSaving) {
                        Spacer(modifier = Modifier.width(8.dp))
                        CircularProgressIndicator(
                            color = MyGrey,
                            modifier = Modifier.size(12.dp),
                            strokeWidth = 1.dp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = uiModel.savingText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MyGrey,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                // Right side: Delete button
                if (uiState.noteId != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight() // Fill the height of the parent Row
                            .background(MyPurple)
                            .clickable { onDeleteClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = uiModel.deleteButton,
                            tint = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 16.dp) // Only horizontal padding needed
                                .size(24.dp)
                        )
                    }
                }
            }
        }

        // Loading overlay
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MyWhite.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MyPurple,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }

    // Delete confirmation bottom sheet
    if (uiState.showDeleteConfirmationBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onDeleteCancel,
            sheetState = bottomSheetState,
            containerColor = MyWhite,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        ) {
            DeleteConfirmationBottomSheet(
                title = uiModel.deleteConfirmationMessage,
                onCancel = onDeleteCancel,
                onConfirm = onDeleteConfirm
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteScreenPreview() {
    val uiState = NoteUIState(
        title = "Title",
        content = "",
        lastEdited = "Last edited on 19.30",
        isDirty = true,
        noteId = 5 // Set to null to show the layout WITHOUT the delete icon
    )

    val uiModel = NoteUIModel(
        title = "Note",
        titlePlaceholder = "Title",
        contentPlaceholder = "Feel Free to Write Here...",
        deleteButton = "Delete",
        backButton = "Back",
        lastEditedText = "Last edited on",
        savingText = "Saving...",
        deleteConfirmationMessage = "Want to Delete this Note?",
        deleteConfirmationConfirm = "Delete Note"
    )

    NoteScreen(
        uiState = uiState,
        uiModel = uiModel,
        onTitleChanged = {},
        onContentChanged = {},
        onBackClick = {},
        onDeleteClick = {},
        onDeleteConfirm = {},
        onDeleteCancel = {}
    )
}