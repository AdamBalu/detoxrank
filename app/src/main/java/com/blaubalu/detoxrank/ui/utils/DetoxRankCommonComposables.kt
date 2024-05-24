package com.blaubalu.detoxrank.ui.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.theme.md_theme_dark_tertiary
import com.blaubalu.detoxrank.ui.theme.md_theme_light_tertiary
import com.blaubalu.detoxrank.ui.theme.rank_color

/**
 * Formatted RP gain. It has following format: + {value} RP {RP icon}
 */
@Composable
fun RankPointsGain(
    rankPointsGain: Int,
    plusIconSize: Dp,
    shieldIconSize: Dp,
    fontSize: TextUnit,
    horizontalArrangement: Arrangement.Horizontal
) {
  // rank points gain
  Row(
      horizontalArrangement = horizontalArrangement,
      verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
        imageVector = Icons.Filled.Add,
        contentDescription = null,
        tint = if (isSystemInDarkTheme()) md_theme_dark_tertiary else md_theme_light_tertiary,
        modifier = Modifier
            .size(plusIconSize)
    )
    Text(
        text = "$rankPointsGain RP",
        style = Typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        fontSize = fontSize
    )
    Icon(
        imageVector = Icons.Filled.Shield,
        contentDescription = null,
        tint = rank_color,
        modifier = Modifier
            .padding(start = 3.dp)
            .size(shieldIconSize)
    )
  }
}

/**
 * Animates enter and exit transitions of a Composable object
 * @param content Composable function to be animated
 */
@Composable
fun <T> T.AnimationBox(
    enter: EnterTransition = expandHorizontally() + fadeIn(),
    exit: ExitTransition = fadeOut() + slideOutHorizontally(),
    content: @Composable T.() -> Unit
) {
  val state = remember {
    MutableTransitionState(false).apply {
      // start the animation immediately
      targetState = true
    }
  }

  AnimatedVisibility(
      visibleState = state,
      enter = enter,
      exit = exit
  ) { content() }
}