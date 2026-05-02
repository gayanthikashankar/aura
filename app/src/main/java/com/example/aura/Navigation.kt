package com.example.aura

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.aura.presentation.dashboard.DashboardScreen
import com.example.aura.presentation.dashboard.DashboardViewModel
import com.example.aura.presentation.meetingdetail.MeetingDetailScreen
import com.example.aura.presentation.meetingdetail.MeetingDetailViewModel
import com.example.aura.presentation.recording.RecordingScreen
import com.example.aura.presentation.recording.RecordingViewModel
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable object Dashboard : NavKey
@Serializable object Recording : NavKey
@Serializable data class MeetingDetail(val meetingId: String) : NavKey

@Composable
fun MainNavigation() {
    val backStack = rememberNavBackStack(Dashboard)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {

            entry<Dashboard> {
                val vm: DashboardViewModel = hiltViewModel()
                val meetings by vm.meetings.collectAsState()
                DashboardScreen(
                    meetings = meetings,
                    onRecordClick = { backStack.add(Recording) },
                    onMeetingClick = { meeting ->
                        backStack.add(MeetingDetail(meeting.id.toString()))
                    }
                )
            }

            entry<Recording> {
                val vm: RecordingViewModel = hiltViewModel()
                val isProcessing by vm.isProcessing.collectAsState()
                val elapsedSeconds by vm.elapsedSeconds.collectAsState()
                val pipelineStep by vm.pipelineStep.collectAsState()

                RecordingScreen(
                    isProcessing = isProcessing,
                    elapsedSeconds = elapsedSeconds,
                    pipelineStep = pipelineStep,
                    onStopClick = {
                        vm.stopAndAnalyse {
                            backStack.removeLastOrNull()
                        }
                    },
                    onCancelClick = { backStack.removeLastOrNull() }
                )
            }

            entry<MeetingDetail> { key ->
                val vm: MeetingDetailViewModel = hiltViewModel()
                val context = LocalContext.current
                LaunchedEffect(key.meetingId) {
                    vm.setMeetingId(UUID.fromString(key.meetingId))
                }
                val meeting by vm.meeting.collectAsState()
                val actionItems by vm.actionItems.collectAsState()
                val decisions by vm.decisions.collectAsState()
                val drafts by vm.drafts.collectAsState()
                val isReanalysing by vm.isReanalysing.collectAsState()

                if (meeting == null) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    MeetingDetailScreen(
                        meeting = meeting!!,
                        actionItems = actionItems,
                        decisions = decisions,
                        drafts = drafts,
                        onBack = { backStack.removeLastOrNull() },
                        onDispatch = { draft ->
                            vm.dispatchMessage(draft)
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, draft.subject ?: "Follow up from ${meeting!!.title}")
                                putExtra(Intent.EXTRA_TEXT, draft.body)
                            }
                            context.startActivity(Intent.createChooser(intent, "Send via..."))
                        },
                        onEdit = { updatedDraft -> vm.updateDraft(updatedDraft) },
                        isReanalysing = isReanalysing,
                        onReanalyse = { vm.reAnalyse() },
                        onExport = {
                            val text = vm.buildShareText()
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, text)
                                putExtra(Intent.EXTRA_SUBJECT, "Meeting Summary — Aura")
                            }
                            context.startActivity(Intent.createChooser(intent, "Export Meeting"))
                        }
                    )
                }
            }
        }
    )
}
