package com.waseemsgith.jahaiz.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.waseemsgith.jahaiz.R
import com.waseemsgith.jahaiz.core.ui.theme.JahaizGold
import com.waseemsgith.jahaiz.data.model.OccupationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OccupationPicker(
    selectedKey: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var open by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    val sel = OccupationType.fromKey(selectedKey)
    val filtered =
        remember(query) {
            OccupationType.entries.filter {
                it.displayName.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
            }
        }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = sel.displayName,
        onValueChange = {},
        readOnly = true,
        label = { Text(stringResource(id = R.string.occupation_label)) },
        trailingIcon = {
            IconButton(onClick = { open = true }) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = JahaizGold)
            }
        },
    )

    if (open) {
        ModalBottomSheet(
            onDismissRequest = { open = false },
            sheetState = sheetState,
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                label = { Text(stringResource(id = R.string.search_hint)) },
            )
            LazyColumn(modifier = Modifier.padding(bottom = 24.dp)) {
                items(filtered, key = { it.name }) { occ ->
                    Text(
                        text = "${occ.displayName} — ${occ.category}",
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelected(occ.name)
                                    open = false
                                }
                                .padding(horizontal = 20.dp, vertical = 12.dp),
                        color = JahaizGold,
                    )
                }
            }
        }
    }
}
