package com.blaubalu.detoxrank.ui.rank

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.CardDefaults.elevatedCardColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.achievements.AchievementDifficulty
import com.blaubalu.detoxrank.data.local.LocalAchievementDataProvider
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.theme.common_green
import com.blaubalu.detoxrank.ui.theme.epic_purple
import com.blaubalu.detoxrank.ui.theme.legendary_orange
import com.blaubalu.detoxrank.ui.theme.rare_blue
import com.blaubalu.detoxrank.ui.theme.rarer_blue
import com.blaubalu.detoxrank.ui.theme.very_epic_magenta
import com.blaubalu.detoxrank.ui.utils.getAchievementDrawableFromId

@Composable
fun AchievementsScreen(
    rankViewModel: RankViewModel,
    achievementViewModel: AchievementViewModel,
    detoxRankViewModel: DetoxRankViewModel,
    modifier: Modifier = Modifier
) {
    val achievementsToAdd = LocalAchievementDataProvider.allAchievements
    val coroutineScope = rememberCoroutineScope()

    val state by rankViewModel.achievementsHomeUiState.collectAsState()

    LaunchedEffect(Unit) {
        achievementViewModel.finishAllChaptersAchievement()
        achievementViewModel.finishBookReadingAchievements()
    }

    AnimatedVisibility(
        visible = rankViewModel.achievementsDisplayed.value,
        enter = slideInVertically(initialOffsetY = { 2 * (it / 2) }, animationSpec = tween(durationMillis = 600)),
        exit = slideOutVertically(targetOffsetY = {-it * 2}, animationSpec = tween(durationMillis = 600) {x -> -x})
    ) {
        BackHandler {
            rankViewModel.setAchievementsDisplayed(false)
        }
        Box(modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.align(Alignment.TopCenter)) {
//                Button(
//                    onClick = {
//                        coroutineScope.launch { // FILLDB uncomment to fill achievement db
////                      achievementViewModel.deleteAllAchievementsInDb()
//                            achievementsToAdd.forEach {
//                                detoxRankViewModel.updateAchievementUiState(it.toAchievementUiState())
//                                detoxRankViewModel.insertAchievementToDatabase()
//                            }
//                        }
//                    },
//                    modifier = Modifier.height(40.dp)
//                ) {
//                    Text("Fill db")
//                }
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    tint = MaterialTheme.colorScheme.inversePrimary,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .size(30.dp)
                        .clickable {
                            rankViewModel.setAchievementsDisplayed(false)
                        }
                )
                LazyColumn {
                    item {
                        Text(
                            "Achievements",
                            style = Typography.headlineLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp, top = 2.dp)
                        )
                    }
                    items(state.achievementList) { achievement ->
                        val cardBorderColor = when (achievement.difficulty) {
                            AchievementDifficulty.EASY -> common_green
                            AchievementDifficulty.MEDIUM -> rare_blue
                            AchievementDifficulty.HARD -> rarer_blue
                            AchievementDifficulty.VERY_HARD -> epic_purple
                            AchievementDifficulty.INSANE -> very_epic_magenta
                            AchievementDifficulty.LEGENDARY -> legendary_orange
                        }
                        Card(
                            elevation = cardElevation(8.dp),
                            colors = elevatedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            border = if (achievement.achieved) {
                                BorderStroke(4.dp, cardBorderColor)
                            } else { BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceVariant) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                        ) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                if (!achievement.achieved) {
                                    Box(modifier = Modifier.padding(10.dp)) {
                                        Image(
                                            painterResource(id = getAchievementDrawableFromId(achievement.id, isSystemInDarkTheme())),
                                            contentDescription = null,
                                            colorFilter = ColorFilter.tint(Color.Gray),
                                            modifier = Modifier.size(80.dp)
                                        )
                                        Image(
                                            painterResource(id = R.drawable.lock),
                                            contentDescription = null,
                                            modifier = Modifier.size(30.dp),
                                            alignment = Alignment.Center
                                        )
                                    }
                                } else {
                                    Box(modifier = Modifier.padding(start = 0.dp, end = 10.dp, bottom = 10.dp, top = 2.dp)) {
                                        Image(
                                            painterResource(id = getAchievementDrawableFromId(achievement.id, isSystemInDarkTheme())),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(90.dp)
                                                .padding(start = 10.dp, top = 0.dp)
                                        )
                                    }
                                }
                                Column(modifier = Modifier.padding(end = 10.dp, bottom = 6.dp, start = 10.dp)) {
                                    Text(achievement.name, style = Typography.headlineSmall, modifier = Modifier.padding(bottom = 7.dp))
                                    Text(achievement.description, style = Typography.bodyMedium)
                                }
                            }
                        }
                    }
                    item {
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}