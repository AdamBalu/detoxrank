package com.blaubalu.detoxrank.ui.rank

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Help
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.ui.DetoxRankBottomNavigationBar
import com.blaubalu.detoxrank.ui.DetoxRankNavigationRail
import com.blaubalu.detoxrank.ui.DetoxRankTopAppBar
import com.blaubalu.detoxrank.ui.DetoxRankUiState
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.DetoxRankViewModelProvider
import com.blaubalu.detoxrank.ui.NavigationDrawerContent
import com.blaubalu.detoxrank.ui.NavigationItemContent
import com.blaubalu.detoxrank.data.Section
import com.blaubalu.detoxrank.ui.*
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.utils.AnimationBox
import com.blaubalu.detoxrank.ui.utils.Constants.LEGEND_LOWER_CAP
import com.blaubalu.detoxrank.ui.utils.DetoxRankNavigationType

/**
 * Home screen content of Rank section
 */
@ExperimentalMaterial3Api
@Composable
fun RankHomeScreen(
  navigationItemContentList: List<NavigationItemContent>,
  detoxRankUiState: DetoxRankUiState,
  onTabPressed: ((Section) -> Unit),
  navigationType: DetoxRankNavigationType,
  detoxRankViewModel: DetoxRankViewModel,
  achievementViewModel: AchievementViewModel,
  modifier: Modifier = Modifier,
  rankViewModel: RankViewModel = viewModel(factory = DetoxRankViewModelProvider.Factory)
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
      RankContent(
        navigationItemContentList = navigationItemContentList,
        detoxRankUiState = detoxRankUiState,
        onTabPressed = onTabPressed,
        navigationType = navigationType,
        detoxRankViewModel = detoxRankViewModel,
        achievementViewModel = achievementViewModel,
        rankViewModel = rankViewModel
      )
    }
  } else {
    RankContent(
      navigationItemContentList = navigationItemContentList,
      detoxRankUiState = detoxRankUiState,
      onTabPressed = onTabPressed,
      navigationType = navigationType,
      detoxRankViewModel = detoxRankViewModel,
      achievementViewModel = achievementViewModel,
      rankViewModel = rankViewModel
    )
  }
}

@Composable
fun RankContent(
  navigationItemContentList: List<NavigationItemContent>,
  detoxRankViewModel: DetoxRankViewModel,
  achievementViewModel: AchievementViewModel,
  detoxRankUiState: DetoxRankUiState,
  rankViewModel: RankViewModel,
  onTabPressed: ((Section) -> Unit),
  navigationType: DetoxRankNavigationType,
  modifier: Modifier = Modifier
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
      topBar = {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          DetoxRankTopAppBar(detoxRankViewModel = detoxRankViewModel)
          Row {
            IconButton(
              onClick = { rankViewModel.setRanksDisplayed(true) },
              modifier = Modifier
                .padding(end = 15.dp)
                .size(27.dp)
            ) {
              Icon(
                Icons.Filled.Shield,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondaryContainer,
              )
            }
            IconButton(
              onClick = { rankViewModel.setHelpDisplayed(true) },
              modifier = Modifier
                .padding(end = 15.dp)
                .size(27.dp)
            ) {
              Icon(
                Icons.AutoMirrored.Rounded.Help,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondaryContainer,
              )
            }
          }
        }
      },
      bottomBar = {
        if (navigationType == DetoxRankNavigationType.BOTTOM_NAVIGATION)
          DetoxRankBottomNavigationBar(
            currentTab = detoxRankUiState.currentSection,
            onTabPressed = onTabPressed,
            navigationItemContentList = navigationItemContentList,
            modifier = Modifier.padding(bottom = 0.dp)
          )
      }
    ) { paddingValues ->
      // keep everything centered when on mobile screen size
      if (navigationType == DetoxRankNavigationType.BOTTOM_NAVIGATION) {
        RankMainScreenBody(
          detoxRankViewModel = detoxRankViewModel,
          achievementViewModel = achievementViewModel,
          rankViewModel = rankViewModel,
          modifier = Modifier.padding(paddingValues)
        )
      } else {
        RankMainScreenBodyLarge(
          detoxRankViewModel = detoxRankViewModel,
          achievementViewModel = achievementViewModel,
          rankViewModel = rankViewModel,
          modifier = Modifier.padding(paddingValues)
        )
      }

    }
  }
}

/**
 * Main screen components of rank section for small screens
 */
@Composable
fun RankMainScreenBody(
  detoxRankViewModel: DetoxRankViewModel,
  achievementViewModel: AchievementViewModel,
  rankViewModel: RankViewModel,
  modifier: Modifier = Modifier
) {
  val coroutineScope = rememberCoroutineScope()

  val currentScreenHeight = LocalConfiguration.current.screenHeightDp
  val achievementsPadding = if (currentScreenHeight < 500) 4.dp
  else if (currentScreenHeight < 600) 8.dp
  else if (currentScreenHeight < 700) 10.dp
  else if (currentScreenHeight < 800) 12.dp
  else 15.dp

  LaunchedEffect(Unit) {
    rankViewModel.setLocalRankPoints()
    val rankPoints = detoxRankViewModel.getUserRankPoints()
    val currRankPair = detoxRankViewModel.getCurrentRank(rankPoints)
    val currentRank = currRankPair.first
    rankViewModel.setLocalRankBounds(currRankPair.second)
    detoxRankViewModel.setCurrentRank(currentRank)
    val progressBarPercentage = if (rankPoints >= LEGEND_LOWER_CAP) {
      1.0f
    } else {
      (rankPoints - currRankPair.second.first).toFloat() / (currRankPair.second.second - currRankPair.second.first)
    }
    detoxRankViewModel.setRankProgressBar(progressBarPercentage)
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .zIndex(1f)
  ) {
    DetoxRankHelp(rankViewModel = rankViewModel)
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .zIndex(1f)
  ) {
    AllRanks(rankViewModel = rankViewModel)
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .zIndex(1f)
  ) {
    AchievementsScreen(
      rankViewModel = rankViewModel,
      achievementViewModel = achievementViewModel,
      detoxRankViewModel = detoxRankViewModel
    )
  }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween,
    modifier = modifier.fillMaxSize()
  ) {
    AnimationBox(enter = slideInVertically { x -> x / 40 } + fadeIn()) {
      RankWithProgressBar(
        detoxRankViewModel = detoxRankViewModel,
        rankViewModel = rankViewModel
      )
    }

    AnimationBox(enter = slideInVertically { x -> x / 2 }) {
      Button(
        onClick = {
          rankViewModel.setAchievementsDisplayed(true)
//                    coroutineScope.launch {// DATA to remove achievs
//                        achievementViewModel.deleteAllAchievementsInDb()
//                    }
        },
        shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
        modifier = Modifier
          .padding(top = 5.dp)
          .fillMaxWidth(0.75f),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.secondaryContainer,
          contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
      ) {
        Row(horizontalArrangement = Arrangement.Center) {
          Icon(
            Icons.Filled.KeyboardArrowUp,
            contentDescription = null,
            modifier = Modifier.padding(end = 15.dp, top = achievementsPadding - 4.dp),
            tint = MaterialTheme.colorScheme.inversePrimary
          )
          Text(
            stringResource(R.string.achievements),
            modifier = Modifier.padding(
              top = achievementsPadding,
              bottom = achievementsPadding + 4.dp
            ),
            style = Typography.bodyMedium
          )
          Icon(
            Icons.Filled.KeyboardArrowUp,
            contentDescription = null,
            modifier = Modifier.padding(
              start = 15.dp,
              top = achievementsPadding - 4.dp
            ),
            tint = MaterialTheme.colorScheme.inversePrimary
          )
        }
      }
    }
  }
}

/**
 * Main screen components of rank section for large screens
 */
@Composable
fun RankMainScreenBodyLarge(
  detoxRankViewModel: DetoxRankViewModel,
  achievementViewModel: AchievementViewModel,
  rankViewModel: RankViewModel,
  modifier: Modifier = Modifier
) {
  LaunchedEffect(Unit) {
    rankViewModel.setLocalRankPoints()
    val rankPoints = detoxRankViewModel.getUserRankPoints()
    val currRankPair = detoxRankViewModel.getCurrentRank(rankPoints)
    val currentRank = currRankPair.first
    rankViewModel.setLocalRankBounds(currRankPair.second)
    detoxRankViewModel.setCurrentRank(currentRank)
    val progressBarPercentage = if (rankPoints >= LEGEND_LOWER_CAP) {
      1.0f
    } else {
      (rankPoints - currRankPair.second.first).toFloat() / (currRankPair.second.second - currRankPair.second.first)
    }
    detoxRankViewModel.setRankProgressBar(progressBarPercentage)
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .zIndex(1f)
  ) {
    DetoxRankHelp(rankViewModel = rankViewModel)
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .zIndex(1f)
  ) {
    AllRanks(rankViewModel = rankViewModel)
  }

  Box(
    modifier = Modifier
      .fillMaxSize()
      .zIndex(1f)
  ) {
    AchievementsScreen(
      rankViewModel = rankViewModel,
      achievementViewModel = achievementViewModel,
      detoxRankViewModel = detoxRankViewModel
    )
  }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween,
    modifier = modifier.fillMaxSize()
  ) {
    AnimationBox(enter = slideInVertically() { x -> x / 40 } + fadeIn()) {
      RankWithProgressBarLarge(
        detoxRankViewModel = detoxRankViewModel,
        rankViewModel = rankViewModel
      )
    }

    AnimationBox(enter = slideInVertically() { x -> x / 2 }) {
      Button(
        onClick = {
          rankViewModel.setAchievementsDisplayed(true)
        },
        shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
        modifier = Modifier
          .padding(top = 5.dp)
          .fillMaxWidth(0.55f)
          .height(50.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = MaterialTheme.colorScheme.secondaryContainer,
          contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
      ) {
        Row(horizontalArrangement = Arrangement.Center) {
          Icon(
            Icons.Filled.KeyboardArrowUp,
            contentDescription = null,
            modifier = Modifier.padding(end = 15.dp, top = 5.dp),
            tint = MaterialTheme.colorScheme.inversePrimary
          )
          Text(
            stringResource(R.string.achievements),
            modifier = Modifier.padding(top = 9.dp, bottom = 5.dp),
            style = Typography.bodyMedium
          )
          Icon(
            Icons.Filled.KeyboardArrowUp,
            contentDescription = null,
            modifier = Modifier.padding(start = 15.dp, top = 5.dp),
            tint = MaterialTheme.colorScheme.inversePrimary
          )
        }
      }
    }
  }
}