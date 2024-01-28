package com.blaubalu.detoxrank.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.Section
import com.blaubalu.detoxrank.data.task.TaskDurationCategory
import com.blaubalu.detoxrank.service.TimerService
import com.blaubalu.detoxrank.ui.rank.AchievementViewModel
import com.blaubalu.detoxrank.ui.rank.RankHomeScreen
import com.blaubalu.detoxrank.ui.tasks.home.TasksHomeScreen
import com.blaubalu.detoxrank.ui.tasks.task.TaskViewModel
import com.blaubalu.detoxrank.ui.theme.*
import com.blaubalu.detoxrank.ui.theory.TheoryHomeScreen
import com.blaubalu.detoxrank.ui.timer.TimerHomeScreen
import com.blaubalu.detoxrank.ui.utils.Constants.HIGH_LEVEL_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.HIGH_LEVEL_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.LOW_LEVEL_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.LOW_LEVEL_UPPER_CAP
import com.blaubalu.detoxrank.ui.utils.Constants.MIN_LEVEL_TO_UNLOCK_SPECIAL_TASKS
import com.blaubalu.detoxrank.ui.utils.DetoxRankNavigationType
import com.blaubalu.detoxrank.ui.utils.getCurrentLevelFromXP
import com.blaubalu.detoxrank.ui.utils.getCurrentProgressBarProgression
import com.blaubalu.detoxrank.ui.utils.getLevelDrawableId
import kotlinx.coroutines.flow.first
import java.util.Calendar

/**
 * Main content of the app. Handles the first run setup together
 * with calendar initializations and tasks refreshes. Sets up the navigation bars
 * and handles the navigation between individual sections.
 */
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@Composable
fun DetoxRankAppContent(
    windowSize: WindowWidthSizeClass,
    timerService: TimerService,
    modifier: Modifier = Modifier,
    detoxRankViewModel: DetoxRankViewModel = viewModel(factory = DetoxRankViewModelProvider.Factory),
    taskViewModel: TaskViewModel = viewModel(factory = DetoxRankViewModelProvider.Factory),
    achievementViewModel: AchievementViewModel = viewModel(factory = DetoxRankViewModelProvider.Factory)
) {
    LaunchedEffect(Unit) {
        detoxRankViewModel.firstRunGetTasks()
        val userXp = detoxRankViewModel.getUserXpPoints()
        val level = getCurrentLevelFromXP(userXp)

        val specialTaskList =
            taskViewModel.getCompletedTasksByDuration(TaskDurationCategory.Special).first()
        val noSpecialTasksCompleted = specialTaskList.none { it.completed }

        // load special tasks if user just reached the level
        if (level >= MIN_LEVEL_TO_UNLOCK_SPECIAL_TASKS && noSpecialTasksCompleted && !taskViewModel.wereTasksOpened.value) {
            taskViewModel.selectSpecialTasks()
            taskViewModel.wereTasksOpened.value = true
        }

        val calendarDaily = Calendar.getInstance().apply {
            timeInMillis =
                detoxRankViewModel.getUserTasksRefreshedTimeInstance(TaskDurationCategory.Daily)
        }
        val calendarWeekly = Calendar.getInstance().apply {
            timeInMillis =
                detoxRankViewModel.getUserTasksRefreshedTimeInstance(TaskDurationCategory.Weekly)
            firstDayOfWeek = Calendar.MONDAY
        }
        val calendarMonthly = Calendar.getInstance().apply {
            timeInMillis =
                detoxRankViewModel.getUserTasksRefreshedTimeInstance(TaskDurationCategory.Monthly)
        }
        detoxRankViewModel.refreshTasks(calendarDaily, calendarWeekly, calendarMonthly)
    }

    val detoxRankUiState = detoxRankViewModel.uiState.collectAsState().value
    val onTabPressed =
        { section: Section -> detoxRankViewModel.updateCurrentSection(section = section) } // TODO reset home screen states if needed

    val navigationType = when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            DetoxRankNavigationType.BOTTOM_NAVIGATION
        }

        WindowWidthSizeClass.Medium -> {
            DetoxRankNavigationType.NAVIGATION_RAIL
        }

        WindowWidthSizeClass.Expanded -> {
            DetoxRankNavigationType.PERMANENT_NAVIGATION_DRAWER
        }

        else -> {
            DetoxRankNavigationType.BOTTOM_NAVIGATION
        }
    }

    val navigationItemContentList = listOf(
        NavigationItemContent(
            section = Section.Rank,
            image = ImageVector.vectorResource(id = R.drawable.rank_points_icon),
            text = stringResource(id = R.string.tab_rank)
        ),
        NavigationItemContent(
            section = Section.Tasks,
            image = ImageVector.vectorResource(id = R.drawable.tasksnavicon),
            text = stringResource(id = R.string.tab_tasks)
        ),
        NavigationItemContent(
            section = Section.Timer,
            image = ImageVector.vectorResource(id = R.drawable.timernavicon),
            text = stringResource(id = R.string.tab_timer)
        ),
        NavigationItemContent(
            section = Section.Theory,
            image = ImageVector.vectorResource(id = R.drawable.theorynavicon),
            text = stringResource(id = R.string.tab_theory),
        )
    )

    when (detoxRankUiState.currentSection) {
        Section.Rank -> {
            RankHomeScreen(
                navigationItemContentList = navigationItemContentList,
                onTabPressed = onTabPressed,
                navigationType = navigationType,
                detoxRankUiState = detoxRankUiState,
                achievementViewModel = achievementViewModel,
                detoxRankViewModel = detoxRankViewModel
            )
        }

        Section.Tasks -> {
            TasksHomeScreen(
                modifier = modifier,
                timerService = timerService,
                detoxRankUiState = detoxRankUiState,
                detoxRankViewModel = detoxRankViewModel,
                achievementViewModel = achievementViewModel,
                taskViewModel = taskViewModel,
                navigationType = navigationType,
                onTabPressed = onTabPressed,
                navigationItemContentList = navigationItemContentList
            )
        }

        Section.Timer -> {
            TimerHomeScreen(
                timerService = timerService,
                onTabPressed = onTabPressed,
                navigationItemContentList = navigationItemContentList,
                navigationType = navigationType,
                detoxRankUiState = detoxRankUiState,
                detoxRankViewModel = detoxRankViewModel,
                achievementViewModel = achievementViewModel
            )
        }

        Section.Theory -> {
            TheoryHomeScreen(
                modifier = modifier,
                onTabPressed = onTabPressed,
                navigationItemContentList = navigationItemContentList,
                navigationType = navigationType,
                detoxRankUiState = detoxRankUiState,
                detoxRankViewModel = detoxRankViewModel
            )
        }
    }
}

@Composable
fun DetoxRankBottomNavigationBar(
    currentTab: Section,
    onTabPressed: ((Section) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier.fillMaxWidth()) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentTab == navItem.section,
                onClick = { onTabPressed(navItem.section) },
                icon = {
                    Image(
                        imageVector = navItem.image,
                        contentDescription = navItem.text,
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 4.dp)
                            .size(25.dp)
                    )
                }
            )
        }
    }
}

/**
 * Component that displays Navigation Rail
 */
@Composable
fun DetoxRankNavigationRail(
    currentTab: Section,
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier,
    onTabPressed: ((Section) -> Unit) = {}
) {
    NavigationRail(modifier = modifier.fillMaxHeight()) {
        for (navItem in navigationItemContentList) {
            NavigationRailItem(
                selected = currentTab == navItem.section,
                onClick = { onTabPressed(navItem.section) },
                icon = {
                    Image(
                        imageVector = navItem.image,
                        contentDescription = navItem.text,
                        modifier = Modifier
                            .padding(top = 2.dp, bottom = 2.dp)
                            .size(25.dp)
                    )
                },
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
            )
        }
    }
}

/**
 * Component that displays Navigation Drawer
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerContent(
    selectedDestination: Section,
    onTabPressed: ((Section) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .wrapContentWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .padding(12.dp)
    ) {
        for (navItem in navigationItemContentList) {
            NavigationDrawerItem(
                selected = selectedDestination == navItem.section,
                label = {
                    Text(
                        text = navItem.text,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                },
                icon = {
                    Image(
                        imageVector = navItem.image,
                        contentDescription = navItem.text,
                        modifier = Modifier
                            .padding(top = 4.dp, bottom = 4.dp)
                            .size(30.dp)
                    )
                },
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = Color.Transparent
                ),
                onClick = { onTabPressed(navItem.section) },
                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
            )
        }
    }
}

@Composable
fun DetoxRankTopAppBar(
    detoxRankViewModel: DetoxRankViewModel,
    modifier: Modifier = Modifier
) {
    val currentLevel = detoxRankViewModel.getCurrentLevel()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val xpPoints = detoxRankViewModel.getUserXPPoints()
        val currentLevelToUpdate = getCurrentLevelFromXP(xpPoints = xpPoints)
        detoxRankViewModel.setCurrentLevel(currentLevelToUpdate)

        val progress = getCurrentProgressBarProgression(xpPoints)
        detoxRankViewModel.setLevelProgressBar(progress)
    }

    val animatedProgress = animateFloatAsState(
        targetValue = detoxRankViewModel.getLevelProgressBarValue(),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    ).value

    val levelBadgeSize: Dp
    val xpBarPaddingStart: Dp
    val xpBarPaddingTop: Dp
    val xpBarHeight: Dp
    when (currentLevel) {
        in LOW_LEVEL_LOWER_CAP..LOW_LEVEL_UPPER_CAP -> {
            levelBadgeSize = 42.dp
            xpBarPaddingStart = 32.dp
            xpBarPaddingTop = 12.dp
            xpBarHeight = 25.dp
        }

        in HIGH_LEVEL_LOWER_CAP..HIGH_LEVEL_UPPER_CAP -> {
            levelBadgeSize = 65.dp
            xpBarPaddingStart = 45.dp
            xpBarPaddingTop = 25.dp
            xpBarHeight = 40.dp
        }

        else -> {
            levelBadgeSize = 40.dp
            xpBarPaddingStart = 30.dp
            xpBarPaddingTop = 25.dp
            xpBarHeight = 45.dp
        }
    }
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier.padding(top = 12.dp, start = 20.dp)
    ) {
        Box {
            Image(
                painterResource(getLevelDrawableId(currentLevel)),
                null,
                modifier = Modifier
                    .size(levelBadgeSize)
                    .zIndex(1f)
            )

            if (currentLevel != 25) {
                LinearProgressIndicator(
                    progress = animatedProgress,
                    color = MaterialTheme.colorScheme.tertiary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .height(xpBarHeight)
                        .padding(start = xpBarPaddingStart, end = 16.dp, top = xpBarPaddingTop)
                        .fillMaxWidth(0.35f)
                        .clip(RoundedCornerShape(2.dp))
                        .border(
                            BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                            RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}

data class NavigationItemContent(
    val section: Section,
    val image: ImageVector,
    val text: String
)