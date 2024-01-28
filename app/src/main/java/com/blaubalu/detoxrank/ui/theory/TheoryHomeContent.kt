package com.blaubalu.detoxrank.ui.theory

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.blaubalu.detoxrank.ui.DetoxRankBottomNavigationBar
import com.blaubalu.detoxrank.ui.DetoxRankNavigationRail
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.NavigationDrawerContent
import com.blaubalu.detoxrank.ui.NavigationItemContent
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.Section
import com.blaubalu.detoxrank.ui.DetoxRankUiState
import com.blaubalu.detoxrank.ui.DetoxRankViewModelProvider
import com.blaubalu.detoxrank.ui.*
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.theme.md_theme_dark_tertiary
import com.blaubalu.detoxrank.ui.theme.md_theme_light_tertiary
import com.blaubalu.detoxrank.ui.theory.screens.chapter_dopamine.*
import com.blaubalu.detoxrank.ui.theory.screens.chapter_reinforcement.*
import com.blaubalu.detoxrank.ui.theory.screens.chapter_solution.*
import com.blaubalu.detoxrank.ui.utils.DetoxRankNavigationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheoryHomeScreen(
    navigationItemContentList: List<NavigationItemContent>,
    detoxRankUiState: DetoxRankUiState,
    detoxRankViewModel: DetoxRankViewModel,
    onTabPressed: ((Section) -> Unit),
    navigationType: DetoxRankNavigationType,
    modifier: Modifier = Modifier
) {
    val navController: NavHostController = rememberNavController()
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
            TheoryContent(
                navigationItemContentList = navigationItemContentList,
                detoxRankUiState = detoxRankUiState,
                detoxRankViewModel = detoxRankViewModel,
                onTabPressed = onTabPressed,
                navigationType = navigationType,
                navController = navController
            )
        }
    } else {
        TheoryContent(
            navigationItemContentList = navigationItemContentList,
            detoxRankUiState = detoxRankUiState,
            detoxRankViewModel = detoxRankViewModel,
            onTabPressed = onTabPressed,
            navigationType = navigationType,
            navController = navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheoryContent(
    navigationItemContentList: List<NavigationItemContent>,
    detoxRankUiState: DetoxRankUiState,
    detoxRankViewModel: DetoxRankViewModel,
    onTabPressed: ((Section) -> Unit),
    navigationType: DetoxRankNavigationType,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    theoryViewModel: TheoryViewModel = viewModel(factory = DetoxRankViewModelProvider.Factory)
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = TheoryScreen.valueOf(
        backStackEntry
            ?.destination
            ?.route
            ?: TheoryScreen.Chapters.name
    )

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
            topBar = {
                TheoryAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = {
                        navController.navigateUp()
                        theoryViewModel.updateProgressBarProgression(
                            -theoryViewModel.calculateProgressBarAddition(theoryViewModel.currentChapterScreenNum.value)
                        )
                    },
                    theoryViewModel = theoryViewModel
                )
            },
            bottomBar = {
                if (currentScreen == TheoryScreen.Chapters &&
                    navigationType == DetoxRankNavigationType.BOTTOM_NAVIGATION
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
                TheoryMainNavigation(
                    theoryViewModel = theoryViewModel,
                    detoxRankViewModel = detoxRankViewModel,
                    navController = navController,
                    modifier = Modifier.padding(paddingValues)
                )
            } else {
                TheoryMainNavigation(
                    theoryViewModel = theoryViewModel,
                    detoxRankViewModel = detoxRankViewModel,
                    navController = navController,
                    modifier = Modifier.padding(paddingValues)
                )
            }

        }
    }
}

/**
 * Image format for theory with optional label as a description
 *
 * for label to work correctly, this component needs to be wrapped in a column
 */
@Composable
fun TheoryImage(
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier,
    @StringRes contentDescription: Int? = null,
    @StringRes imageLabel: Int? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painterResource(id = imageRes),
            contentDescription = stringResource(contentDescription ?: R.string.empty_message),
            modifier = Modifier
                .padding(top = 25.dp, bottom = 12.dp)
        )

        if (imageLabel != null)
            Text(
                text = "Image: " + stringResource(id = imageLabel),
                style = Typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 25.dp)
            )
        else
            Spacer(Modifier.height(25.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheoryAppBar(
    currentScreen: TheoryScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    theoryViewModel: TheoryViewModel
) {
    val coroutineScope = rememberCoroutineScope() // DATA for custom button

    val animatedProgress = animateFloatAsState(
        targetValue = theoryViewModel.getProgressBarValue(),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value
    Column(
        modifier = modifier.padding(bottom = 7.dp)
    ) {
//        // FILLDB fill chapters with this button after db reset
//        Button(onClick = {
//            val chaptersToAdd = LocalChapterDataProvider.allChapters
//            coroutineScope.launch {
//                chaptersToAdd.forEach {
//                    theoryViewModel.updateUiState(it.toChapterUiState())
//                    theoryViewModel.insertChapterToChapterDatabase()
//                }
//            }
//        }) {
//            Text("Add CH to DB")
//        }
        TopAppBar(
            title = {
                Text(
                    text = stringResource(currentScreen.title),
                    style = Typography.titleMedium
                )
            },
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            }
        )
        if (currentScreen != TheoryScreen.Chapters)
            LinearProgressIndicator(
                progress = animatedProgress,
                color = if (isSystemInDarkTheme()) md_theme_dark_tertiary else md_theme_light_tertiary,
                modifier = modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .padding(start = 16.dp, end = 16.dp)
                    .clip(RoundedCornerShape(50.dp))
            )
    }
}

@Preview
@Composable
fun TheoryImagePreview() {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        TheoryImage(
            imageRes = R.drawable.reward_circuit,
            imageLabel = R.string.reward_circuit_label
        )
    }
}
