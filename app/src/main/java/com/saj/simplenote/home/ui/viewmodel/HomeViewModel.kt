package com.saj.simplenote.home.ui.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.saj.simplenote.domain.model.NavigationEvent
import com.saj.simplenote.domain.model.NoteUpdateEvent
import com.saj.simplenote.domain.model.NotesUpdateManager
import com.saj.simplenote.domain.model.ParameterizedNavigationEvent
import com.saj.simplenote.domain.model.PreferencesManager
import com.saj.simplenote.domain.ui.viewmodel.SimpleNoteViewModel
import com.saj.simplenote.domain.util.SharedPrefKeys
import com.saj.simplenote.domain.util.TokenManager
import com.saj.simplenote.home.data.model.Note
import com.saj.simplenote.home.data.repository.HomeRepository
import com.saj.simplenote.home.ui.model.HomeUIState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val preferencesManager: PreferencesManager,
    private val homeRepository: HomeRepository,
    private val tokenManager: TokenManager,
    private val notesUpdateManager: NotesUpdateManager,
) : SimpleNoteViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    val navSettings: NavigationEvent = NavigationEvent()
    val navNote: ParameterizedNavigationEvent<String> = ParameterizedNavigationEvent()
    val navLogin: NavigationEvent = NavigationEvent()
    
    private var searchJob: Job? = null
    private companion object {
        const val SEARCH_DELAY_MS = 500L
        const val DEFAULT_PAGE_SIZE = 10
    }

    init {
        loadNotes()
        
        // Listen for note updates from other screens
        viewModelScope.launch {
            notesUpdateManager.noteUpdates.collect { event ->
                when (event) {
                    is NoteUpdateEvent.NoteCreated -> addNoteToList(event.note)
                    is NoteUpdateEvent.NoteUpdated -> updateNoteInList(event.note)
                    is NoteUpdateEvent.NoteDeleted -> removeNoteFromList(event.noteId)
                }
            }
        }
    }

    fun loadNotes(page: Int = 1, pageSize: Int = DEFAULT_PAGE_SIZE) {
        val currentState = _uiState.value
        
        // If there's a search query, use search instead
        if (currentState.searchQuery.isNotEmpty()) {
            searchNotes(currentState.searchQuery, page, pageSize)
            return
        }
        
        viewModelScope.launch {
            if (page == 1) {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            } else {
                _uiState.value = _uiState.value.copy(isLoadingMore = true, errorMessage = null)
            }
            
            homeRepository.getNotes(page, pageSize).fold(
                onSuccess = { notesResponse ->
                    val updatedNotes = if (page == 1) {
                        notesResponse.results
                    } else {
                        _uiState.value.notes + notesResponse.results
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        notes = updatedNotes,
                        currentPage = page,
                        hasMorePages = notesResponse.next != null,
                        totalCount = notesResponse.count,
                        errorMessage = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        errorMessage = exception.message
                    )
                }
            )
        }
    }
    
    private fun searchNotes(query: String, page: Int = 1, pageSize: Int = DEFAULT_PAGE_SIZE) {
        viewModelScope.launch {
            if (page == 1) {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            } else {
                _uiState.value = _uiState.value.copy(isLoadingMore = true, errorMessage = null)
            }
            
            homeRepository.searchNotes(query, page, pageSize).fold(
                onSuccess = { notesResponse ->
                    val updatedNotes = if (page == 1) {
                        notesResponse.results
                    } else {
                        _uiState.value.notes + notesResponse.results
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        notes = updatedNotes,
                        currentPage = page,
                        hasMorePages = notesResponse.next != null,
                        totalCount = notesResponse.count,
                        errorMessage = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        errorMessage = exception.message
                    )
                }
            )
        }
    }
    
    fun refreshNotes() {
        loadNotes(page = 1) // Reset to first page
    }
    
    fun loadMoreNotes() {
        val currentState = _uiState.value
        if (currentState.hasMorePages && !currentState.isLoadingMore && !currentState.isLoading) {
            loadNotes(currentState.currentPage + 1)
        }
    }

    fun onAddNoteClick() {
        navNote.navigate()
    }

    fun onNoteClick(noteId: Int) {
        navNote.navigate(noteId.toString())
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        
        // Cancel previous search job
        searchJob?.cancel()
        
        if (query.isEmpty()) {
            // If query is empty, load all notes
            loadNotes(page = 1)
        } else {
            // Debounce search
            searchJob = viewModelScope.launch {
                delay(SEARCH_DELAY_MS)
                searchNotes(query, page = 1)
            }
        }
    }

    fun onSettingsClick() {
        navSettings.navigate()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun addNoteToList(note: Note) {
        val currentNotes = _uiState.value.notes.toMutableList()
        currentNotes.add(0, note) // Add at the beginning
        _uiState.value = _uiState.value.copy(
            notes = currentNotes,
            totalCount = _uiState.value.totalCount + 1
        )
    }
    
    fun updateNoteInList(updatedNote: Note) {
        val currentNotes = _uiState.value.notes.toMutableList()
        val index = currentNotes.indexOfFirst { it.id == updatedNote.id }
        if (index != -1) {
            currentNotes[index] = updatedNote
            _uiState.value = _uiState.value.copy(notes = currentNotes)
        }
    }
    
    fun removeNoteFromList(noteId: Int) {
        val currentNotes = _uiState.value.notes.toMutableList()
        val removedNote = currentNotes.removeAll { it.id == noteId }
        if (removedNote) {
            _uiState.value = _uiState.value.copy(
                notes = currentNotes,
                totalCount = maxOf(0, _uiState.value.totalCount - 1)
            )
        }
    }

    fun logout() {
        // Clear all stored user data using TokenManager
        tokenManager.clearTokens()
        
        // Navigate to login screen
        navLogin.navigate()
    }
}
