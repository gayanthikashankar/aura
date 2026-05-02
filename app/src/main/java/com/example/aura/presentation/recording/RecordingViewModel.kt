package com.example.aura.presentation.recording

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aura.domain.model.Meeting
import com.example.aura.domain.model.PipelineStatus
import com.example.aura.domain.repository.MeetingRepository
import com.example.aura.domain.usecase.RunPipelineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

enum class PipelineStep { IDLE, EXTRACTING, COMPOSING, DONE }

@HiltViewModel
class RecordingViewModel @Inject constructor(
    private val runPipelineUseCase: RunPipelineUseCase,
    private val meetingRepository: MeetingRepository
) : ViewModel() {

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    private val _elapsedSeconds = MutableStateFlow(0)
    val elapsedSeconds: StateFlow<Int> = _elapsedSeconds.asStateFlow()

    private val _pipelineStep = MutableStateFlow(PipelineStep.IDLE)
    val pipelineStep: StateFlow<PipelineStep> = _pipelineStep.asStateFlow()

    private var timerJob: Job? = null

    init {
        startTimer()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _elapsedSeconds.value++
            }
        }
    }

    fun stopAndAnalyse(onComplete: () -> Unit = {}) {
        timerJob?.cancel()
        _isProcessing.value = true
        _pipelineStep.value = PipelineStep.EXTRACTING

        viewModelScope.launch {
            val meeting = Meeting(
                id = UUID.randomUUID(),
                title = "Meeting ${java.text.SimpleDateFormat("h:mm a", java.util.Locale.getDefault()).format(java.util.Date())}",
                transcript = "This is a mock transcript. The team agreed to push the launch by two weeks due to backend API delays. " +
                        "Priya will own the communication plan and send updates by Friday. " +
                        "Gayanthika will fix the API integration issue by next Monday. " +
                        "The team decided to use Gemini Nano for on-device inference going forward.",
                pipelineStatus = PipelineStatus.PROCESSING
            )
            meetingRepository.insertMeeting(meeting)

            _pipelineStep.value = PipelineStep.COMPOSING
            runPipelineUseCase.execute(meeting)

            _pipelineStep.value = PipelineStep.DONE
            delay(1200)
            _isProcessing.value = false
            onComplete()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

