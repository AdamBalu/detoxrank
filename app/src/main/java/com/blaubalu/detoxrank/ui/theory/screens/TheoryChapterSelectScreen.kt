package com.blaubalu.detoxrank.ui.theory.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.twotone.DoneOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.chapter.Chapter
import com.blaubalu.detoxrank.data.chapter.ChapterDifficulty
import com.blaubalu.detoxrank.data.chapter.ChapterTag
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.theme.md_theme_dark_tertiary
import com.blaubalu.detoxrank.ui.theme.md_theme_light_tertiary
import com.blaubalu.detoxrank.ui.theme.rank_color
import com.blaubalu.detoxrank.ui.theme.*
import com.blaubalu.detoxrank.ui.theory.TheoryViewModel
import com.blaubalu.detoxrank.ui.utils.AnimationBox
import com.blaubalu.detoxrank.ui.utils.Constants.CHAPTER_DIFFICULTY_EASY_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.CHAPTER_DIFFICULTY_EASY_XP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.CHAPTER_DIFFICULTY_HARD_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.CHAPTER_DIFFICULTY_HARD_XP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.CHAPTER_DIFFICULTY_MEDIUM_RP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.CHAPTER_DIFFICULTY_MEDIUM_XP_GAIN
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_1
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_2
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_3
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_4
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_5
import com.blaubalu.detoxrank.ui.utils.Constants.ID_FINISH_CH_6
import com.blaubalu.detoxrank.ui.utils.RankPointsGain
import kotlinx.coroutines.launch

/**
 * Handles routes to different theory chapter screens
 */
@Composable
fun TheoryChapterSelectScreen(
    onCHIntroSelected: () -> Unit,
    onCHDopamineSelected: () -> Unit,
    onCHReinforcementSelected: () -> Unit,
    onCHToleranceSelected: () -> Unit,
    onCHHedonicCircuitSelected: () -> Unit,
    onCHSolutionSelected: () -> Unit,
    theoryViewModel: TheoryViewModel,
    modifier: Modifier = Modifier
) {
    val theoryUiState by theoryViewModel.theoryHomeUiState.collectAsState()
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(theoryUiState.chapterList) {chapter ->
            val chapterButtonBehavior = when (chapter.tag) {
                ChapterTag.Introduction -> onCHIntroSelected
                ChapterTag.Dopamine -> onCHDopamineSelected
                ChapterTag.Reinforcement -> onCHReinforcementSelected
                ChapterTag.Tolerance -> onCHToleranceSelected
                ChapterTag.HedonicCircuit -> onCHHedonicCircuitSelected
                ChapterTag.Solutions -> onCHSolutionSelected
            }
            AnimationBox(
                enter = expandHorizontally() + fadeIn()
            ) {
                TheoryChapter(
                    onChapterSelected = chapterButtonBehavior,
                    theoryViewModel = theoryViewModel,
                    chapter = chapter
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheoryChapter(
    onChapterSelected: () -> Unit,
    chapter: Chapter,
    theoryViewModel: TheoryViewModel,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (chapter.wasCompleted) {
                MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
            } else {
                MaterialTheme.colorScheme.surfaceColorAtElevation(12.dp)
            },
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 16.dp),
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = chapter.name,
                    style = Typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Icon(
                    imageVector = if (expanded)
                        Icons.Filled.ExpandLess
                    else
                        Icons.Filled.ExpandMore,
                    contentDescription = null
                )
            }
            if (expanded) {
                Text(
                    text = chapter.description,
                    style = Typography.bodyMedium,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                )
                FilledTonalButton(
                    onClick = {
                        onChapterSelected()
                        theoryViewModel.setCurrentChapterName(chapter.name)
                        coroutineScope.launch {
                            theoryViewModel.setCurrentChapterScreenNum()
                        }
                    },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = chapter.startChapterButtonLabel)
                }
            }
            TheoryChapterFooter(chapter)

        }
    }
}

@Composable
fun TheoryChapterFooter(
    chapter: Chapter,
    modifier: Modifier = Modifier
) {
    val rankPointsGain = when (chapter.difficulty) {
        ChapterDifficulty.Easy -> 200
        ChapterDifficulty.Medium -> 350
        ChapterDifficulty.Hard -> 500
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        // stars (difficulty level) footer
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = rank_color,
                modifier = Modifier.size(20.dp)
            )
            Icon(
                imageVector = if (
                    chapter.difficulty == ChapterDifficulty.Medium ||
                    chapter.difficulty == ChapterDifficulty.Hard)
                    Icons.Filled.Star
                else
                    Icons.Outlined.Grade,
                contentDescription = null,
                tint = rank_color,
                modifier = Modifier.size(20.dp)
            )
            Icon(
                imageVector = if (chapter.difficulty == ChapterDifficulty.Hard)
                    Icons.Filled.Star
                else
                    Icons.Outlined.Grade,
                contentDescription = null,
                tint = rank_color,
                modifier = Modifier.size(20.dp)
            )
        }

        if (!chapter.wasCompleted) {
            RankPointsGain(
                rankPointsGain = rankPointsGain,
                plusIconSize = 16.dp,
                shieldIconSize = 20.dp,
                fontSize = 16.sp,
                horizontalArrangement = Arrangement.End
            )

        } else {
            // chapter done - display checkmark
            Icon(
                imageVector = Icons.TwoTone.DoneOutline,
                contentDescription = null,
                tint = if (isSystemInDarkTheme()) md_theme_dark_tertiary else md_theme_light_tertiary
            )
        }
    }
}

@Composable
fun ContinueIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedIconButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 35.dp, start = 16.dp, end = 16.dp, bottom = 25.dp)
            .height(60.dp)
    ) {
        Row {
            Text(
                text = stringResource(R.string.button_continue),
                style = Typography.bodyMedium,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
            Icon(
                imageVector = Icons.Filled.ArrowRight,
                contentDescription = null
            )
        }

    }
}

@Composable
fun CompleteChapterIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    chapterName: String,
    detoxRankViewModel: DetoxRankViewModel,
    theoryViewModel: TheoryViewModel
) {
    val chapter = theoryViewModel.getChapterByName(chapterName).collectAsState(null)
    val coroutineScope = rememberCoroutineScope()
    val rankPointsGain = when (chapter.value?.difficulty) {
        ChapterDifficulty.Easy -> CHAPTER_DIFFICULTY_EASY_RP_GAIN
        ChapterDifficulty.Medium -> CHAPTER_DIFFICULTY_MEDIUM_RP_GAIN
        ChapterDifficulty.Hard -> CHAPTER_DIFFICULTY_HARD_RP_GAIN
        else -> 0
    }
    val xpPointsGain = when (chapter.value?.difficulty) {
        ChapterDifficulty.Easy -> CHAPTER_DIFFICULTY_EASY_XP_GAIN
        ChapterDifficulty.Medium -> CHAPTER_DIFFICULTY_MEDIUM_XP_GAIN
        ChapterDifficulty.Hard -> CHAPTER_DIFFICULTY_HARD_XP_GAIN
        else -> 0
    }
    FilledIconButton(
        onClick = {
            onClick()
            theoryViewModel.setChapterCompletionValue(chapter.value)
            coroutineScope.launch {
                if (chapter.value != null) {
                    if (!chapter.value!!.wasCompleted) {
                        detoxRankViewModel.updateUserRankPoints(rankPointsGain)
                        detoxRankViewModel.updateUserXPPoints(xpPointsGain)
                    }
                }
                val idOfAchievementToComplete = when (chapter.value?.id) {
                    1 -> ID_FINISH_CH_1
                    2 -> ID_FINISH_CH_2
                    3 -> ID_FINISH_CH_3
                    4 -> ID_FINISH_CH_4
                    5 -> ID_FINISH_CH_5
                    6 -> ID_FINISH_CH_6
                    else -> ID_FINISH_CH_1
                }
                detoxRankViewModel.completeAchievement(idOfAchievementToComplete)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 35.dp, start = 16.dp, end = 16.dp, bottom = 25.dp)
            .height(60.dp)
    ) {
        Row {
            Text(
                text = stringResource(R.string.button_complete_chapter),
                style = Typography.bodyMedium,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(end = 5.dp)
                    .align(Alignment.CenterVertically)
            )
            Icon(
                imageVector = Icons.Filled.Verified,
                contentDescription = null,
            )
        }

    }
}