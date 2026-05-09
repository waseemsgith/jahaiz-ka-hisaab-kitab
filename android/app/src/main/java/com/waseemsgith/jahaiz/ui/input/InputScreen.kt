package com.waseemsgith.jahaiz.ui.input

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.activity.result.PickVisualMediaRequest
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.waseemsgith.jahaiz.R
import com.waseemsgith.jahaiz.core.ui.theme.JahaizAccent
import com.waseemsgith.jahaiz.core.ui.theme.JahaizBlack
import com.waseemsgith.jahaiz.core.ui.theme.JahaizGold
import com.waseemsgith.jahaiz.data.model.OccupationType
import com.waseemsgith.jahaiz.data.model.UserInput
import com.waseemsgith.jahaiz.ui.components.GlassCard
import com.waseemsgith.jahaiz.ui.components.OccupationPicker
import java.io.File
import kotlin.math.roundToInt

private fun pickerPermissions(): List<String> =
    buildList {
        add(Manifest.permission.CAMERA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun InputScreen(
    onSubmitted: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    vm: InputViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val photoPerms =
        rememberMultiplePermissionsState(pickerPermissions()) { _: Map<String, Boolean> -> }

    LaunchedEffect(Unit) {
        if (!photoPerms.allPermissionsGranted) {
            photoPerms.launchMultiplePermissionRequest()
        }
    }

    val captureFile =
        remember {
            File(context.cacheDir, "jahaiz_capture_${System.currentTimeMillis()}.jpg").apply {
                parentFile?.mkdirs()
                createNewFile()
            }
        }
    val captureUri =
        remember(captureFile) {
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", captureFile)
        }

    val gallery =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            photoUri = uri
        }

    val camera =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
            if (ok) {
                photoUri = captureUri
            }
        }

    var occupationKey by remember { mutableStateOf(OccupationType.SOFTWARE_ENGINEER.name) }

    var salaryLakhs by remember { mutableFloatStateOf(0.5f) } // 0 .. 10 (₹ lakhs/month)
    var abroadExpanded by remember { mutableStateOf(false) }
    var abroad by remember { mutableStateOf(FormOptions.abroadOptions.first()) }

    var ego by remember { mutableFloatStateOf(5f) }
    var family by remember { mutableFloatStateOf(5f) }
    var luxury by remember { mutableFloatStateOf(5f) }

    var gold by remember { mutableDoubleStateOf(0.5) }
    var sqft by remember { mutableIntStateOf(900) }

    var carExpanded by remember { mutableStateOf(false) }
    var weddingExpanded by remember { mutableStateOf(false) }

    var car by remember { mutableStateOf(FormOptions.cars.first()) }
    var weddingLevel by remember { mutableStateOf(FormOptions.weddingLevels.first()) }

    val scroll = rememberScrollState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = JahaizBlack,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = JahaizBlack),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = JahaizGold)
                    }
                },
                title = {
                    Text(stringResource(id = R.string.screen_input_title_dual), color = JahaizGold)
                },
            )
        },
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            GlassCard {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = stringResource(id = R.string.section_basic_dual), style = MaterialTheme.typography.titleMedium, color = JahaizGold)
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(id = R.string.man_name_dual)) },
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            enabled = cameraPermissionGranted(context, photoPerms),
                            onClick = {
                                if (!cameraPermissionGranted(context, photoPerms)) {
                                    photoPerms.launchMultiplePermissionRequest()
                                } else {
                                    camera.launch(captureUri)
                                }
                            },
                            colors =
                                ButtonDefaults.buttonColors(containerColor = JahaizAccent.copy(alpha = 0.75f)),
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null)
                            Text(stringResource(id = R.string.pick_camera_dual), Modifier.padding(start = 6.dp), color = JahaizGold)
                        }
                        Button(
                            onClick = { gallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                            colors =
                                ButtonDefaults.buttonColors(containerColor = JahaizAccent.copy(alpha = 0.55f)),
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                            Text(stringResource(id = R.string.pick_gallery_dual), Modifier.padding(start = 6.dp), color = JahaizGold)
                        }
                    }

                    photoUri?.let {
                        Text(
                            text = stringResource(id = R.string.photo_attached_dual, it.lastPathSegment ?: ""),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }

            GlassCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = stringResource(id = R.string.section_occupation_dual), style = MaterialTheme.typography.titleMedium, color = JahaizGold)
                    OccupationPicker(selectedKey = occupationKey, onSelected = { occupationKey = it })
                }
            }

            GlassCard {
                Column {
                    Text(stringResource(id = R.string.financial_dual), style = MaterialTheme.typography.titleMedium, color = JahaizGold)

                    Text(
                        stringResource(id = R.string.monthly_salary_dual, salaryLakhs),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    )
                    Slider(
                        value = salaryLakhs,
                        onValueChange = { salaryLakhs = it },
                        valueRange = 0f..10f,
                        steps = 99,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(id = R.string.abroad_dual), color = JahaizGold.copy(alpha = 0.74f))
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    ExposedDropdownMenuBox(expanded = abroadExpanded, onExpandedChange = { abroadExpanded = it }) {
                        OutlinedTextField(
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            value = abroad,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = abroadExpanded) },
                        )
                        ExposedDropdownMenu(
                            expanded = abroadExpanded,
                            onDismissRequest = { abroadExpanded = false },
                        ) {
                            FormOptions.abroadOptions.forEach { choice ->
                                DropdownMenuItem(text = { Text(choice, color = JahaizGold) }, onClick = { abroad = choice; abroadExpanded = false })
                            }
                        }
                    }
                }
            }

            GlassCard {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = stringResource(id = R.string.ego_meter_dual), style = MaterialTheme.typography.titleMedium, color = JahaizGold)

                    MeterRow(title = stringResource(id = R.string.ego_lvl_dual), value = ego, onChange = { ego = it })

                    MeterRow(title = stringResource(id = R.string.family_dual), value = family, onChange = { family = it })

                    MeterRow(title = stringResource(id = R.string.luxury_dual), value = luxury, onChange = { luxury = it })
                }
            }

            GlassCard {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(text = stringResource(id = R.string.section_demand_dual), style = MaterialTheme.typography.titleMedium, color = JahaizGold)

                    Text(text = stringResource(id = R.string.gold_dual, gold))
                    Slider(
                        value = gold.toFloat(),
                        onValueChange = { gold = it.toDouble() },
                        valueRange = 0f..20f,
                        steps = 199,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Column {
                        Text(stringResource(id = R.string.expected_car_dual), color = JahaizGold.copy(alpha = 0.74f))

                        Spacer(modifier = Modifier.height(6.dp))

                        ExposedDropdownMenuBox(expanded = carExpanded, onExpandedChange = { carExpanded = it }) {
                            OutlinedTextField(
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                value = car,
                                readOnly = true,
                                onValueChange = {},
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = carExpanded) },
                            )
                            ExposedDropdownMenu(expanded = carExpanded, onDismissRequest = { carExpanded = false }) {
                                FormOptions.cars.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it, color = JahaizGold) },
                                        onClick = { car = it; carExpanded = false },
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = sqft.toString(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(id = R.string.prop_sqft_dual)) },
                        onValueChange = { v -> sqft = v.filter { ch -> ch.isDigit() }.toIntOrNull() ?: sqft },
                    )

                    Column {
                        Text(stringResource(id = R.string.wedding_level_dual), color = JahaizGold.copy(alpha = 0.74f))
                        Spacer(modifier = Modifier.height(6.dp))
                        ExposedDropdownMenuBox(expanded = weddingExpanded, onExpandedChange = { weddingExpanded = it }) {
                            OutlinedTextField(
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                value = weddingLevel,
                                readOnly = true,
                                onValueChange = {},
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = weddingExpanded)
                                },
                            )
                            ExposedDropdownMenu(
                                expanded = weddingExpanded,
                                onDismissRequest = { weddingExpanded = false },
                            ) {
                                FormOptions.weddingLevels.forEach {
                                    DropdownMenuItem(
                                        text = { Text(it, color = JahaizGold) },
                                        onClick = { weddingLevel = it; weddingExpanded = false },
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    if (name.isBlank()) {
                        val errors = listOf(
                            "Arre bhai naam toh batao pehle 😭",
                            "Rishta bina info ke kaise judge karein? 📋",
                            "Gumnaam rishtey hum analyze nahi karte 😏",
                            "Naam chupane se package kam nahi hota! 💸"
                        )
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(errors.random())
                        }
                        return@Button
                    }
                    vm.saveDraft(
                        UserInput(
                            name = name.trim(),
                            photoUri = photoUri,
                            occupationKey = occupationKey,
                            salaryMonthly = (salaryLakhs.toDouble() * 100_000).roundToInt(),
                            abroadStatus = abroad,
                            egoLevel = ego.toInt(),
                            familyExpectation = family.toInt(),
                            luxuryDemand = luxury.toInt(),
                            goldKg = gold,
                            car = car,
                            propertySqft = sqft.coerceAtLeast(0),
                            weddingLevel = weddingLevel,
                        ),
                    )
                    onSubmitted()
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = JahaizAccent.copy(alpha = 0.76f)),
            ) {
                Text(stringResource(id = R.string.cta_analyze_dual), color = JahaizGold)
            }

            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}

@Composable
private fun MeterRow(
    title: String,
    value: Float,
    onChange: (Float) -> Unit,
) {
    Column {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(title, color = JahaizGold.copy(alpha = 0.78f))
            Text(value.toInt().toString())
        }
        Slider(
            value = value,
            onValueChange = onChange,
            valueRange = 1f..10f,
            steps = 9,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun cameraPermissionGranted(
    context: Context,
    state:
        com.google.accompanist.permissions.MultiplePermissionsState,
): Boolean {
    val cam = Manifest.permission.CAMERA
    return state.permissions.any { it.permission == cam && it.status.isGranted }
}
