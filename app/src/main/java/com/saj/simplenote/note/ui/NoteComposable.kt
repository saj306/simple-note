package com.saj.simplenote.note.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.saj.simplenote.R
import com.saj.simplenote.note.ui.component.NoteScreen
import com.saj.simplenote.note.ui.model.NoteUIModel
import com.saj.simplenote.note.ui.viewmodel.NoteViewModel

@Composable
fun NoteComposable(
    viewModel: NoteViewModel,
    navController: NavController,
    noteId: Int? = null,
    finish: () -> Unit,
) {
    
    val uiState by viewModel.uiState.collectAsState()

    // Set up navigation action when the screen is composed
    LaunchedEffect(Unit) {
        viewModel.navBack.setNavigateAction {
            navController.popBackStack()
        }
        viewModel.initializeNote(noteId)
    }

    // Handle system back button
    BackHandler {
        viewModel.onBackPressed()
    }

    // Create the UI model with string resources
    val noteData = NoteUIModel(
        title = stringResource(R.string.note_title),
        titlePlaceholder = stringResource(R.string.note_title_placeholder),
        contentPlaceholder = stringResource(R.string.note_content_placeholder),
        deleteButton = stringResource(R.string.note_delete_button),
        backButton = stringResource(R.string.note_back_button),
        lastEditedText = stringResource(R.string.note_last_edited),
        savingText = stringResource(R.string.note_saving_text),
        deleteConfirmationMessage = stringResource(R.string.note_delete_confirmation_message),
        deleteConfirmationConfirm = stringResource(R.string.note_delete_confirmation_confirm)
    )

    // Display error messages (if any)
    uiState.errorMessage?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            // You can show a snackbar or toast here
            // For now, we'll just clear the error after showing
            viewModel.clearError()
        }
    }

    NoteScreen(
        uiState = uiState,
        uiModel = noteData,
        onTitleChanged = viewModel::onTitleChanged,
        onContentChanged = viewModel::onContentChanged,
        onBackClick = viewModel::onBackPressed,
        onDeleteClick = viewModel::showDeleteConfirmation,
        onDeleteConfirm = viewModel::deleteNote,
        onDeleteCancel = viewModel::hideDeleteConfirmation
    )
}
