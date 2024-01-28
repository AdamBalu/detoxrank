package com.blaubalu.detoxrank.ui.rank

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.ui.theme.Typography

@Composable
fun DetoxRankHelp(
    rankViewModel: RankViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    AnimatedVisibility(
        visible = rankViewModel.helpDisplayed.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        BackHandler {
            rankViewModel.setHelpDisplayed(false)
        }
        Card(
            modifier = modifier
                .fillMaxSize()
                .padding(7.dp)
                .verticalScroll(scrollState),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp), contentColor = MaterialTheme.colorScheme.onSurface)
        ) {
            Column(modifier = Modifier.padding(35.dp)) {
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Row {
                        Text("Help", style = Typography.headlineMedium, modifier = Modifier.padding(end = 20.dp))
                        Image(
                            painterResource(id = R.drawable.help_icon),
                            contentDescription = null,
                            modifier = Modifier.height(40.dp)
                        )
                    }
                    IconButton(onClick = { rankViewModel.setHelpDisplayed(false) }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = null)
                    }
                }

                HelpSection(sectionId = R.string.tab_rank, descriptionId = R.string.help_rank_description, imageId = R.drawable.ranknavicon)
                HelpSection(sectionId = R.string.tab_tasks, descriptionId = R.string.help_tasks_description, imageId = R.drawable.tasksnavicon)
                HelpSection(sectionId = R.string.tab_timer, descriptionId = R.string.help_timer_description, imageId = R.drawable.timernavicon)
                HelpSection(sectionId = R.string.tab_theory, descriptionId = R.string.help_theory_description, imageId = R.drawable.theorynavicon)
            }
        }
    }
}

@Composable
fun HelpSection(
    @StringRes sectionId: Int,
    @StringRes descriptionId: Int,
    @DrawableRes imageId: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(top = 40.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            stringResource(sectionId),
            style = Typography.headlineSmall
        )
        Image(
            painterResource(id = imageId),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
    }
    Text(
        stringResource(descriptionId),
        style = Typography.bodyMedium,
        modifier = Modifier.padding(start = 15.dp)
    )
}