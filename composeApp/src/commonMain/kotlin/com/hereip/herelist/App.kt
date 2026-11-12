package com.hereip.herelist

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hereip.herelist.data.DemoTracks
import com.hereip.herelist.data.Track

private val HereListDarkColorScheme: ColorScheme = darkColorScheme(
    primary = Color(0xFFB8C4FF),
    onPrimary = Color(0xFF18234D),
    secondary = Color(0xFFB7C9C7),
    background = Color(0xFF101116),
    surface = Color(0xFF191B22),
    surfaceVariant = Color(0xFF292C35),
    onSurface = Color(0xFFE4E1E9),
    onSurfaceVariant = Color(0xFFC4C5D0)
)

private val HereListLightColorScheme: ColorScheme = lightColorScheme(
    primary = Color(0xFF344B9A),
    onPrimary = Color.White,
    secondary = Color(0xFF526A68),
    background = Color(0xFFF7F7FA),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE9EAF0),
    onSurface = Color(0xFF191A20),
    onSurfaceVariant = Color(0xFF5E606B)
)

@Composable
fun App() {
    var isDarkTheme by remember { mutableStateOf(true) }
    var accentColor by remember { mutableStateOf(Color(0xFFB8C4FF)) }
    var animationsEnabled by remember { mutableStateOf(true) }
    val baseColorScheme = if (isDarkTheme) HereListDarkColorScheme else HereListLightColorScheme
    val colorScheme = baseColorScheme.copy(primary = accentColor)

    MaterialTheme(colorScheme = colorScheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MusicHomeScreen(
                isDarkTheme = isDarkTheme,
                onToggleTheme = { isDarkTheme = !isDarkTheme },
                accentColor = accentColor,
                onAccentColorChange = { accentColor = it },
                animationsEnabled = animationsEnabled,
                onAnimationsChange = { animationsEnabled = it }
            )
        }
    }
}

@Composable
private fun MusicHomeScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    accentColor: Color,
    onAccentColorChange: (Color) -> Unit,
    animationsEnabled: Boolean,
    onAnimationsChange: (Boolean) -> Unit
) {
    var showSettings by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("Главная") }
    var searchQuery by remember { mutableStateOf("") }
    var currentTrack by remember { mutableStateOf<Track?>(null) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 760.dp

        Row(modifier = Modifier.fillMaxSize()) {
            if (isWideScreen) {
                SideNavigation(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = if (isWideScreen) 32.dp else 20.dp)
                ) {
                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "HereList",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Твоя музыка. Твоё пространство.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        IconButton(onClick = { showSettings = true }) {
                            Text(
                                text = "⚙",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 23.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(18.dp),
                        placeholder = { Text("Треки, исполнители и альбомы") },
                        label = { Text("Поиск") }
                    )

                    Spacer(Modifier.height(28.dp))

                    SectionTitle("Подборка для знакомства")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        AlbumCard(
                            title = "Night Drive",
                            subtitle = "Электроника",
                            colors = listOf(Color(0xFF343B68), Color(0xFF171A2C)),
                            onClick = { currentTrack = DemoTracks[0] }
                        )
                        AlbumCard(
                            title = "Blue Horizon",
                            subtitle = "Спокойное настроение",
                            colors = listOf(Color(0xFF285A68), Color(0xFF172B3A)),
                            onClick = { currentTrack = DemoTracks[1] }
                        )
                        AlbumCard(
                            title = "Afterglow",
                            subtitle = "Вечерняя подборка",
                            colors = listOf(Color(0xFF70465F), Color(0xFF2C1D36)),
                            onClick = { currentTrack = DemoTracks[2] }
                        )
                    }

                    Spacer(Modifier.height(28.dp))

                    SectionTitle(if (searchQuery.isBlank()) "Треки для знакомства" else "Результаты поиска")
                    val visibleTracks = DemoTracks.filter {
                        it.title.contains(searchQuery, ignoreCase = true) ||
                            it.artist.contains(searchQuery, ignoreCase = true)
                    }

                    if (visibleTracks.isEmpty()) {
                        Text(
                            text = "Ничего не найдено",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 20.dp)
                        )
                    } else {
                        visibleTracks.forEachIndexed { index, track ->
                            TrackRow(
                                track = track,
                                index = index + 1,
                                onClick = { currentTrack = track }
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Это демонстрационный интерфейс. Воспроизведение музыки пока не подключено.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }

                MiniPlayer(
                    track = currentTrack,
                    modifier = Modifier.fillMaxWidth(),
                    animationsEnabled = animationsEnabled
                )

                if (!isWideScreen) {
                    BottomNavigation(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )
                }
            }
        }
    }

    if (showSettings) {
        SettingsDialog(
            isDarkTheme = isDarkTheme,
            onToggleTheme = onToggleTheme,
            accentColor = accentColor,
            onAccentColorChange = onAccentColorChange,
            animationsEnabled = animationsEnabled,
            onAnimationsChange = onAnimationsChange,
            onDismiss = { showSettings = false }
        )
    }
}

@Composable
private fun SettingsDialog(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    accentColor: Color,
    onAccentColorChange: (Color) -> Unit,
    animationsEnabled: Boolean,
    onAnimationsChange: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Настройки HereList") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("ВНЕШНИЙ ВИД", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Тёмная тема", fontWeight = FontWeight.Medium)
                        Text("Переключение светлой и тёмной темы", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(checked = isDarkTheme, onCheckedChange = { onToggleTheme() })
                }
                Text("Цвет оформления", fontWeight = FontWeight.Medium)
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    listOf(Color(0xFF344B9A), Color(0xFF8B5CF6), Color(0xFF16866A), Color(0xFFD05B78), Color(0xFFCC8735)).forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(color, RoundedCornerShape(50))
                                .clickable { onAccentColorChange(color) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (accentColor == color) Text("✓", color = Color.White, fontSize = 15.sp)
                        }
                    }
                }
                Text("АНИМАЦИИ", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Плавные переходы", fontWeight = FontWeight.Medium)
                        Text("Анимация смены треков", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(checked = animationsEnabled, onCheckedChange = onAnimationsChange)
                }
                Text("ВОСПРОИЗВЕДЕНИЕ", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text("Настройки плеера появятся после реализации воспроизведения.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("О ПРИЛОЖЕНИИ", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text("HereList", fontWeight = FontWeight.SemiBold)
                Text("Версия: 0.1.0 (ранняя разработка)", style = MaterialTheme.typography.bodySmall)
                Text("Открытый проект. Лицензия MIT.", style = MaterialTheme.typography.bodySmall)
                Text("GitHub: github.com/hereip/HereList", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("Готово") } }
    )
}

@Composable
private fun SideNavigation(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxHeight()
    ) {
        Spacer(Modifier.height(20.dp))
        NavigationRailItem(
            selected = selectedTab == "Главная",
            onClick = { onTabSelected("Главная") },
            icon = { Text("⌂", fontSize = 22.sp) },
            label = { Text("Главная") }
        )
        NavigationRailItem(
            selected = selectedTab == "Поиск",
            onClick = { onTabSelected("Поиск") },
            icon = { Text("⌕", fontSize = 22.sp) },
            label = { Text("Поиск") }
        )
        NavigationRailItem(
            selected = selectedTab == "Медиатека",
            onClick = { onTabSelected("Медиатека") },
            icon = { Text("▤", fontSize = 22.sp) },
            label = { Text("Медиатека") }
        )
    }
}

@Composable
private fun BottomNavigation(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        val tabs = listOf("Главная" to "⌂", "Поиск" to "⌕", "Медиатека" to "▤")
        tabs.forEach { (title, symbol) ->
            NavigationBarItem(
                selected = selectedTab == title,
                onClick = { onTabSelected(title) },
                icon = { Text(symbol, fontSize = 20.sp) },
                label = { Text(title) }
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 14.dp)
    )
}

@Composable
private fun AlbumCard(
    title: String,
    subtitle: String,
    colors: List<Color>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(172.dp)
            .animateContentSize(animationSpec = tween(durationMillis = 220))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(132.dp)
                    .background(
                        brush = Brush.linearGradient(colors),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "HL",
                    color = Color.White.copy(alpha = 0.88f),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(title, fontWeight = FontWeight.SemiBold, maxLines = 1)
            Text(
                subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TrackRow(
    track: Track,
    index: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(durationMillis = 180))
            .clickable(onClick = onClick)
            .padding(vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = index.toString(),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 14.dp)
        ) {
            Text(track.title, fontWeight = FontWeight.Medium, maxLines = 1)
            Text(
                text = track.artist,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
        }
        Text(
            text = track.duration,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun MiniPlayer(
    track: Track?,
    modifier: Modifier = Modifier,
    animationsEnabled: Boolean
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("HL", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }

            Crossfade(
                targetState = track,
                animationSpec = tween(durationMillis = if (animationsEnabled) 220 else 0),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                label = "mini-player-track"
            ) { animatedTrack ->
                Column {
                    Text(
                        text = animatedTrack?.title ?: "Ничего не воспроизводится",
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                    Text(
                        text = animatedTrack?.artist ?: "Выбери трек из списка",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                }
            }

            IconButton(onClick = {}) {
                Text("▶", color = MaterialTheme.colorScheme.primary, fontSize = 20.sp)
            }
        }
    }
}
