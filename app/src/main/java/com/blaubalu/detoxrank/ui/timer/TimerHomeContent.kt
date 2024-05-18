package com.blaubalu.detoxrank.ui.timer

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blaubalu.detoxrank.ui.DetoxRankBottomNavigationBar
import com.blaubalu.detoxrank.ui.DetoxRankNavigationRail
import com.blaubalu.detoxrank.ui.DetoxRankTopAppBar
import com.blaubalu.detoxrank.ui.DetoxRankUiState
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.DetoxRankViewModelProvider
import com.blaubalu.detoxrank.ui.NavigationDrawerContent
import com.blaubalu.detoxrank.ui.NavigationItemContent
import com.blaubalu.detoxrank.data.Section
import com.blaubalu.detoxrank.service.TimerService
import com.blaubalu.detoxrank.ui.rank.AchievementViewModel
import com.blaubalu.detoxrank.ui.utils.AnimationBox
import com.blaubalu.detoxrank.ui.utils.DetoxRankNavigationType

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun TimerHomeScreen(
    navigationItemContentList: List<NavigationItemContent>,
    detoxRankUiState: DetoxRankUiState,
    detoxRankViewModel: DetoxRankViewModel,
    achievementViewModel: AchievementViewModel,
    onTabPressed: ((Section) -> Unit),
    navigationType: DetoxRankNavigationType,
    timerService: TimerService,
    modifier: Modifier = Modifier
) {
    if (navigationType == DetoxRankNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        PermanentNavigationDrawer(drawerContent = {
            PermanentDrawerSheet(modifier.width(240.dp)) {
                NavigationDrawerContent(
                    selectedDestination = detoxRankUiState.currentSection,
                    onTabPressed = onTabPressed,
                    navigationItemContentList = navigationItemContentList
                )
            }
        }
        ) {
            TimerContent(
                navigationItemContentList = navigationItemContentList,
                detoxRankUiState = detoxRankUiState,
                detoxRankViewModel = detoxRankViewModel,
                achievementViewModel = achievementViewModel,
                onTabPressed = onTabPressed,
                navigationType = navigationType,
                timerService = timerService
            )
        }
    } else {
        TimerContent(
            navigationItemContentList = navigationItemContentList,
            detoxRankUiState = detoxRankUiState,
            detoxRankViewModel = detoxRankViewModel,
            achievementViewModel = achievementViewModel,
            onTabPressed = onTabPressed,
            navigationType = navigationType,
            timerService = timerService
        )
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun TimerContent(
    navigationItemContentList: List<NavigationItemContent>,
    detoxRankViewModel: DetoxRankViewModel,
    detoxRankUiState: DetoxRankUiState,
    achievementViewModel: AchievementViewModel,
    onTabPressed: ((Section) -> Unit),
    navigationType: DetoxRankNavigationType,
    timerService: TimerService,
    modifier: Modifier = Modifier,
    timerViewModel: TimerViewModel = viewModel(factory = DetoxRankViewModelProvider.Factory)
) {
    Row(modifier = modifier.fillMaxSize()) {
        // navigation rail (side)
        AnimatedVisibility(
            visible = navigationType == DetoxRankNavigationType.NAVIGATION_RAIL
        ) {
            DetoxRankNavigationRail(
                currentTab = detoxRankUiState.currentSection,
                onTabPressed = onTabPressed,
                navigationItemContentList = navigationItemContentList
            )
        }
        Scaffold(
            topBar = { DetoxRankTopAppBar(detoxRankViewModel = detoxRankViewModel) },
            bottomBar = {
                if (navigationType == DetoxRankNavigationType.BOTTOM_NAVIGATION)
                    AnimatedVisibility(
                        !timerViewModel.difficultySelectShown,
                        enter = slideInVertically(
                            animationSpec = tween(
                                durationMillis = 500,
                                delayMillis = 500
                            )
                        ) { height -> height } + fadeIn(
                            animationSpec = tween(durationMillis = 700)
                        ),
                        exit = slideOutVertically(animationSpec = tween(durationMillis = 500)) { height -> height }
                    ) {
                        DetoxRankBottomNavigationBar(
                            currentTab = detoxRankUiState.currentSection,
                            onTabPressed = onTabPressed,
                            navigationItemContentList = navigationItemContentList
                        )
                    }
            }
        ) { paddingValues ->
            // keep everything centered when on mobile screen size
            if (navigationType == DetoxRankNavigationType.BOTTOM_NAVIGATION) {
                TimerBody(
                    timerService = timerService,
                    detoxRankUiState = detoxRankUiState,
                    timerViewModel = timerViewModel,
                    detoxRankViewModel = detoxRankViewModel,
                    achievementViewModel = achievementViewModel,
                    modifier = Modifier.padding(paddingValues),
                )
            } else {
                TimerBodyLarge(
                    timerService = timerService,
                    detoxRankUiState = detoxRankUiState,
                    achievementViewModel = achievementViewModel,
                    timerViewModel = timerViewModel,
                    detoxRankViewModel = detoxRankViewModel,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun TimerBody(
    timerService: TimerService,
    detoxRankUiState: DetoxRankUiState,
    detoxRankViewModel: DetoxRankViewModel,
    achievementViewModel: AchievementViewModel,
    timerViewModel: TimerViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.zIndex(1f)
    ) {
        TimerDifficultySelectScreen(
            timerViewModel = timerViewModel,
            timerService = timerService,
            detoxRankViewModel = detoxRankViewModel
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        AnimationBox(
            enter = expandHorizontally(animationSpec = tween(durationMillis = 700)) +
                    fadeIn(animationSpec = tween(durationMillis = 1200))
        ) {
            Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.height(320.dp)) {
                TimerClock(timerService)
                TimerStartStopButton(
                    timerService = timerService,
                    detoxRankViewModel = detoxRankViewModel,
                    achievementViewModel = achievementViewModel,
                    modifier = Modifier
                        .padding(start = 60.dp, end = 60.dp, top = 150.dp)
                        .align(Alignment.Center)
                )
            }
        }
        AnimationBox(
            enter = slideInHorizontally(animationSpec = tween(durationMillis = 700)) + fadeIn(
                animationSpec = tween(durationMillis = 1200)
            )
        ) {
            TimerFooter(
                timerService = timerService,
                detoxRankUiState = detoxRankUiState,
                detoxRankViewModel = detoxRankViewModel,
                timerViewModel = timerViewModel,
            )
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun TimerBodyLarge(
    timerService: TimerService,
    detoxRankUiState: DetoxRankUiState,
    detoxRankViewModel: DetoxRankViewModel,
    achievementViewModel: AchievementViewModel,
    timerViewModel: TimerViewModel,
    modifier: Modifier = Modifier
) {
    val currentScreenHeight = LocalConfiguration.current.screenHeightDp
    val timerTranslationY: Double = if
                                            (currentScreenHeight >= 700) currentScreenHeight.toFloat() / 10.0
    else if (currentScreenHeight >= 500) currentScreenHeight.toFloat() / 6.5
    else if (currentScreenHeight >= 400) currentScreenHeight.toFloat() / 4.0
    else currentScreenHeight.toFloat() / 3.0


    Box(
        modifier = modifier.zIndex(1f)
    ) {
        TimerDifficultySelectScreen(
            timerViewModel = timerViewModel,
            timerService = timerService,
            detoxRankViewModel = detoxRankViewModel
        )
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 0.dp, start = 40.dp)
    ) {
        Column {
            AnimationBox(
                enter = expandVertically(animationSpec = tween(durationMillis = 700)) +
                        fadeIn(animationSpec = tween(durationMillis = 1200))
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .graphicsLayer { translationY = timerTranslationY.toFloat() }) {
                    TimerClockLarge(timerService)
                    TimerStartStopButton(
                        timerService = timerService,
                        detoxRankViewModel = detoxRankViewModel,
                        achievementViewModel = achievementViewModel,
                        modifier = Modifier
                            .padding(start = 0.dp, end = 0.dp, top = 130.dp)
                            .fillMaxWidth(0.5f)
                            .align(Alignment.Center)
                    )
                }
            }
        }

        AnimationBox(enter = slideInVertically { x -> x - 400 } + fadeIn()) {
            TimerFooterLarge(
                timerService = timerService,
                detoxRankUiState = detoxRankUiState,
                detoxRankViewModel = detoxRankViewModel,
                timerViewModel = timerViewModel,
                modifier = Modifier.padding(top = 0.dp)
            )
        }
    }
}

@ExperimentalAnimationApi
fun addAnimation(duration: Int = 600): ContentTransform {
    return (slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height / 20 } + fadeIn(
        animationSpec = tween(durationMillis = duration)
    )).togetherWith(slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> height / 20 } + fadeOut(
        animationSpec = tween(durationMillis = duration)
    ))
}
