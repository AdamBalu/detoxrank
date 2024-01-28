package com.blaubalu.detoxrank.ui.rank

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.data.user.Rank
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.theme.Typography
import com.blaubalu.detoxrank.ui.theme.rank_color
import com.blaubalu.detoxrank.ui.theme.rank_color_shade
import com.blaubalu.detoxrank.ui.theme.rank_color_ultra_dark
import com.blaubalu.detoxrank.ui.utils.AnimationBox

@Composable
fun RankWithProgressBar(
    detoxRankViewModel: DetoxRankViewModel,
    rankViewModel: RankViewModel,
    modifier: Modifier = Modifier
) {
    val animatedProgress = animateFloatAsState(
        targetValue = detoxRankViewModel.getRankProgressBarValue(),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

    val currentRank = detoxRankViewModel.getCurrentRank()

    val currentScreenHeight = LocalConfiguration.current.screenHeightDp

    // adapts rank icon size depending on current screen height
    val rankWidthFraction = if (currentScreenHeight < 500) {
        0.4f
    } else if (currentScreenHeight < 700) {
        0.6f
    } else if (currentScreenHeight < 800) {
        0.8f
    } else if (currentScreenHeight < 900) {
        0.9f
    } else 1f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.rank_label),
            textAlign = TextAlign.Center,
            style = Typography.bodySmall,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal,
            color = MaterialTheme.colorScheme.outline,
            letterSpacing = 2.sp
        )
        Text(
            currentRank.rankName,
            textAlign = TextAlign.Center,
            style = Typography.headlineLarge,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        Image(
            painterResource(id = getRankDrawableId(currentRank)),
            contentDescription = null,
            modifier = Modifier.padding(bottom = 25.dp, start = 16.dp, end = 16.dp).fillMaxWidth(rankWidthFraction)
        )
        Box {
            Text(
                stringResource(R.string.rank_points_label),
                style = Typography.bodySmall,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.align(Alignment.TopCenter),
                fontStyle = FontStyle.Normal
            )
            LinearProgressIndicator(
                progress = animatedProgress,
                color = rank_color,
                trackColor = rank_color_ultra_dark,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(47.dp)
                    .padding(start = 16.dp, end = 16.dp, top = 17.dp)
                    .clip(RoundedCornerShape(19.dp))
                    .border(BorderStroke(4.dp, rank_color), RoundedCornerShape(19.dp))
            )
            LinearProgressIndicator(
                progress = animatedProgress - 0.1f,
                color = rank_color_shade,
                trackColor = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(31.dp)
                    .padding(start = 30.dp, top = 23.dp)
                    .clip(RoundedCornerShape(29.dp))
            )
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                AnimationBox(
                    enter = slideInVertically(
                        animationSpec = tween(
                            durationMillis = 500,
                            delayMillis = 700
                        )
                    ) { x -> x / 20 } + fadeIn(
                        animationSpec = tween(
                            durationMillis = 500,
                            delayMillis = 700
                        )
                    )
                ) {
                    Column {
                        Row(modifier = Modifier.padding(top = 50.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                rankViewModel.getFormattedRankPointsProgress(),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.outline,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 5.dp)
                            )
                            Image(
                                painterResource(id = R.drawable.rank_points_icon),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RankWithProgressBarLarge(
    detoxRankViewModel: DetoxRankViewModel,
    rankViewModel: RankViewModel,
    modifier: Modifier = Modifier
) {
    val animatedProgress = animateFloatAsState(
        targetValue = detoxRankViewModel.getRankProgressBarValue(),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    ).value

    val currentRank = detoxRankViewModel.getCurrentRank()
    val rankImageWidth = minOf(LocalConfiguration.current.screenHeightDp / (2.0), LocalConfiguration.current.screenWidthDp / 2.9).toInt().dp

    Column(modifier = modifier.fillMaxWidth().padding(top = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            Image(
                painterResource(id = getRankDrawableId(currentRank)),
                contentDescription = null,
                modifier = Modifier.padding(bottom = 5.dp, start = 16.dp, end = 16.dp).width(rankImageWidth)
            )
            Column {
                Text(
                    stringResource(R.string.rank_label),
                    textAlign = TextAlign.Center,
                    style = Typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                    color = MaterialTheme.colorScheme.outline,
                    letterSpacing = 2.sp
                )
                Text(
                    currentRank.rankName,
                    textAlign = TextAlign.Center,
                    style = Typography.headlineLarge,
                    modifier = Modifier.padding(end = 50.dp)
                )
            }
        }

        Box {
            Text(
                stringResource(R.string.rank_points_label),
                style = Typography.bodySmall,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.align(Alignment.TopCenter),
                fontStyle = FontStyle.Normal
            )
            LinearProgressIndicator(
                progress = animatedProgress,
                color = rank_color,
                trackColor = rank_color_ultra_dark,
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(47.dp)
                    .padding(start = 16.dp, end = 16.dp, top = 17.dp)
                    .clip(RoundedCornerShape(19.dp))
                    .border(BorderStroke(4.dp, rank_color), RoundedCornerShape(19.dp))
            )
            LinearProgressIndicator(
                progress = animatedProgress - 0.1f,
                color = rank_color_shade,
                trackColor = Color.Transparent,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .height(31.dp)
                    .padding(start = 30.dp, top = 23.dp)
                    .clip(RoundedCornerShape(29.dp))
            )
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                AnimationBox(
                    enter = slideInVertically(
                        animationSpec = tween(
                            durationMillis = 500,
                            delayMillis = 700
                        )
                    ) { x -> x / 20 } + fadeIn(
                        animationSpec = tween(
                            durationMillis = 500,
                            delayMillis = 700
                        )
                    )
                ) {
                    Column {
                        Row(modifier = Modifier.padding(top = 50.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                rankViewModel.getFormattedRankPointsProgress(),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.outline,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 5.dp)
                            )
                            Image(
                                painterResource(id = R.drawable.rank_points_icon),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

    }
}

/**
 * Returns drawable ID of a rank
 * @param rank rank to get the drawable ID of
 * @return drawable ID of a given rank
 */
@DrawableRes
private fun getRankDrawableId(rank: Rank): Int {
    return when (rank) {
        Rank.Bronze1 -> R.drawable.bronze1
        Rank.Bronze2 -> R.drawable.bronze2
        Rank.Bronze3 -> R.drawable.bronze3
        Rank.Silver1 -> R.drawable.silver1
        Rank.Silver2 -> R.drawable.silver2
        Rank.Silver3 -> R.drawable.silver3
        Rank.Gold1 -> R.drawable.gold1
        Rank.Gold2 -> R.drawable.gold2
        Rank.Gold3 -> R.drawable.gold3
        Rank.Platinum1 -> R.drawable.plat1
        Rank.Platinum2 -> R.drawable.plat2
        Rank.Platinum3 -> R.drawable.plat3
        Rank.Diamond1 -> R.drawable.diamond1
        Rank.Diamond2 -> R.drawable.diamond2
        Rank.Diamond3 -> R.drawable.diamond3
        Rank.Master -> R.drawable.master
        Rank.Legend -> R.drawable.legend
    }
}