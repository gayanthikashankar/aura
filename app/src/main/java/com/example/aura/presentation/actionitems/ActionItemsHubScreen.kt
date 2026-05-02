package com.example.aura.presentation.actionitems

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.aura.domain.model.ActionItem
import com.example.aura.presentation.meetingdetail.ActionItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionItemsHubScreen(
    actionItems: List<ActionItem>
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Action Items Hub") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // Filter chips
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilterChip(selected = true, onClick = { }, label = { Text("All") })
                FilterChip(selected = false, onClick = { }, label = { Text("By me") })
                FilterChip(selected = false, onClick = { }, label = { Text("Overdue") })
                FilterChip(selected = false, onClick = { }, label = { Text("High priority") })
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (actionItems.isEmpty()) {
                Text("No action items found.")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(actionItems.size) { index ->
                        ActionItemCard(actionItems[index])
                    }
                }
            }
        }
    }
}
