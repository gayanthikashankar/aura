package com.example.aura.presentation.meetingdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aura.domain.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingDetailScreen(
    meeting: Meeting,
    actionItems: List<ActionItem>,
    decisions: List<Decision>,
    drafts: List<MessageDraft>,
    onBack: () -> Unit,
    onDispatch: (MessageDraft) -> Unit,
    onEdit: (MessageDraft) -> Unit = {},
    onReanalyse: () -> Unit = {},
    onExport: () -> Unit = {},
    isReanalysing: Boolean = false
) {
    var editingDraft by remember { mutableStateOf<MessageDraft?>(null) }
    var editBody by remember { mutableStateOf("") }

    if (editingDraft != null) {
        AlertDialog(
            onDismissRequest = { editingDraft = null },
            title = { Text("Edit draft for ${editingDraft!!.owner}") },
            text = {
                OutlinedTextField(
                    value = editBody,
                    onValueChange = { editBody = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    label = { Text("Message body") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    onEdit(editingDraft!!.copy(body = editBody))
                    editingDraft = null
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { editingDraft = null }) { Text("Cancel") }
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meeting Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = onReanalyse, enabled = !isReanalysing) {
                    if (isReanalysing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Analysing...")
                    } else {
                        Text("\uD83D\uDD04 Re-analyse")
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                FilledTonalButton(onClick = onExport) {
                    Text("\uD83D\uDCE4 Export Summary")
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Top Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(meeting.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text("Today 2:30pm · 47 min", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        AssistChip(
                            onClick = { },
                            label = { Text(if(meeting.sentiment == Sentiment.PRODUCTIVE) "\uD83D\uDE0A Productive" else "\uD83D\uDE24 Tense") },
                            colors = AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        )
                    }
                }
            }

            item {
                Text("Summary", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Text(meeting.summary ?: "No summary available.")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                Text("Decisions Made", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                decisions.forEach { decision ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = true, onCheckedChange = null)
                        Text(decision.decisionText)
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                Text("Action Items", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            }

            items(actionItems.size) { index ->
                ActionItemCard(actionItems[index])
            }

            item {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Draft Follow-ups", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            }

            items(drafts.size) { index ->
                MessageDraftCard(
                    draft = drafts[index],
                    onDispatch = onDispatch,
                    onEditClick = {
                        editBody = drafts[index].body
                        editingDraft = drafts[index]
                    }
                )
            }
        }
    }
}

@Composable
fun ActionItemCard(item: ActionItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(item.owner?.take(1) ?: "?", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(item.owner ?: "Unassigned", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(item.task)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                AssistChip(onClick = {}, label = { Text("\uD83D\uDCC5 Friday") })
                Spacer(modifier = Modifier.width(8.dp))
                AssistChip(
                    onClick = {},
                    label = { Text(item.priority.name) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                )
                Spacer(modifier = Modifier.weight(1f))
                LinearProgressIndicator(
                    progress = item.confidence,
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun MessageDraftCard(draft: MessageDraft, onDispatch: (MessageDraft) -> Unit, onEditClick: () -> Unit = {}) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("\u2709\uFE0F ${draft.owner}", fontWeight = FontWeight.Bold)
                Text("[${draft.channel}]", color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(draft.body, maxLines = 3, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onEditClick) { Text("Edit") }
                Button(onClick = { onDispatch(draft) }) {
                    Icon(Icons.Default.Send, contentDescription = "Send", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Send via...")
                }
            }
        }
    }
}
