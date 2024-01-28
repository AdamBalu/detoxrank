package com.blaubalu.detoxrank.ui.theory

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.blaubalu.detoxrank.R
import com.blaubalu.detoxrank.ui.DetoxRankViewModel
import com.blaubalu.detoxrank.ui.theory.screens.TheoryChapterSelectScreen
import com.blaubalu.detoxrank.ui.theory.screens.chapter_dopamine.CHDopamineBrain
import com.blaubalu.detoxrank.ui.theory.screens.chapter_dopamine.CHDopamineIntro
import com.blaubalu.detoxrank.ui.theory.screens.chapter_dopamine.CHDopamineNeurotransmitter
import com.blaubalu.detoxrank.ui.theory.screens.chapter_dopamine.CHDopaminePoint
import com.blaubalu.detoxrank.ui.theory.screens.chapter_dopamine.CHDopamineSummary
import com.blaubalu.detoxrank.ui.theory.screens.chapter_hedonic_circuit.CHHedonicCircuitExample
import com.blaubalu.detoxrank.ui.theory.screens.chapter_hedonic_circuit.CHHedonicCircuitIntro
import com.blaubalu.detoxrank.ui.theory.screens.chapter_hedonic_circuit.CHHedonicCircuitPoint
import com.blaubalu.detoxrank.ui.theory.screens.chapter_hedonic_circuit.CHHedonicCircuitSummary
import com.blaubalu.detoxrank.ui.theory.screens.chapter_introduction.CHIntroDilemma
import com.blaubalu.detoxrank.ui.theory.screens.chapter_introduction.CHIntroDilemmaCont
import com.blaubalu.detoxrank.ui.theory.screens.chapter_reinforcement.CHReinforcementIntro
import com.blaubalu.detoxrank.ui.theory.screens.chapter_reinforcement.CHReinforcementProblems
import com.blaubalu.detoxrank.ui.theory.screens.chapter_reinforcement.CHReinforcementRewardCircuit
import com.blaubalu.detoxrank.ui.theory.screens.chapter_reinforcement.CHReinforcementSummary
import com.blaubalu.detoxrank.ui.theory.screens.chapter_solution.CHSolutionAdvice
import com.blaubalu.detoxrank.ui.theory.screens.chapter_solution.CHSolutionAdviceCont
import com.blaubalu.detoxrank.ui.theory.screens.chapter_solution.CHSolutionAdviceContCont
import com.blaubalu.detoxrank.ui.theory.screens.chapter_solution.CHSolutionIntro
import com.blaubalu.detoxrank.ui.theory.screens.chapter_solution.CHSolutionSummary
import com.blaubalu.detoxrank.ui.theory.screens.chapter_introduction.CHIntroIntro
import com.blaubalu.detoxrank.ui.theory.screens.chapter_reinforcement.CHReinforcementExample
import com.blaubalu.detoxrank.ui.theory.screens.chapter_tolerance.CHToleranceCorrelation
import com.blaubalu.detoxrank.ui.theory.screens.chapter_tolerance.CHToleranceExample
import com.blaubalu.detoxrank.ui.theory.screens.chapter_tolerance.CHToleranceIntro
import com.blaubalu.detoxrank.ui.theory.screens.chapter_tolerance.CHToleranceSummary

/**
 * enum values that represent the individual screens in the theory
 */
enum class TheoryScreen(@StringRes val title: Int) {
    // chapter select
    Chapters(R.string.chapter_select),

    // intro
    CHIntro(R.string.topbar_introduction),
    CHIntroDilemma(R.string.topbar_dilemma),
    CHIntroDilemmaCont(R.string.topbar_dilemma_pt_2),

    // dopamine
    CHDopamine(R.string.topbar_introduction),
    CHDopamineBrain(R.string.topbar_brain),
    CHDopamineNeurotransmitter(R.string.topbar_neurotransmitter),
    CHDopaminePoint(R.string.topbar_point),
    CHDopamineSummary(R.string.topbar_summary),

    // reinforcement
    CHReinforcement(R.string.topbar_introduction),
    CHReinforcementRewardCircuit(R.string.topbar_reward_circuit),
    CHReinforcementExample(R.string.topbar_example),
    CHReinforcementProblems(R.string.topbar_problems),
    CHReinforcementSummary(R.string.topbar_summary),

    // tolerance
    CHTolerance(R.string.topbar_introduction),
    CHToleranceExample(R.string.topbar_example),
    CHToleranceCorrelation(R.string.topbar_correlation),
    CHToleranceSummary(R.string.topbar_summary),

    // hedonic circuit
    CHHedonicCircuit(R.string.topbar_introduction),
    CHHedonicCircuitExample(R.string.topbar_example),
    CHHedonicCircuitPoint(R.string.topbar_point),
    CHHedonicCircuitSummary(R.string.topbar_summary),

    CHSolution(R.string.topbar_introduction),
    CHSolutionAdvice(R.string.topbar_advice),
    CHSolutionAdviceCont(R.string.topbar_advice_pt2),
    CHSolutionAdviceContCont(R.string.topbar_advice_pt3),
    CHSolutionSummary(R.string.topbar_summary)
}

private fun backHandler(
    navController: NavHostController,
    viewModel: TheoryViewModel,
    screenNum: Int
) {
    navController.navigateUp()
    viewModel.updateProgressBarProgression(
        -viewModel.calculateProgressBarAddition(screenNum)
    )
}

private fun onChaptersDone(
    navController: NavHostController,
    viewModel: TheoryViewModel
) {
    navController.popBackStack(TheoryScreen.Chapters.name, inclusive = false)
    viewModel.resetProgressBarProgression()
}

private fun onChapterContinue(
    navController: NavHostController,
    viewModel: TheoryViewModel,
    screenNum: Int,
    chapterName: String
) {
    navController.navigate(chapterName)
    viewModel.updateProgressBarProgression(
        viewModel.calculateProgressBarAddition(screenNum)
    )
}

@Composable
fun TheoryMainNavigation(
    theoryViewModel: TheoryViewModel,
    detoxRankViewModel: DetoxRankViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val currentChapterScreenNum = theoryViewModel.currentChapterScreenNum.value
    NavHost(
        navController = navController,
        startDestination = TheoryScreen.Chapters.name,
        modifier = modifier.padding(top = 0.dp)
    ) {
        composable(route = TheoryScreen.Chapters.name) {
            theoryViewModel.resetProgressBarProgression()
            TheoryChapterSelectScreen(
                onCHIntroSelected = { navController.navigate(TheoryScreen.CHIntro.name) },
                onCHDopamineSelected = { navController.navigate(TheoryScreen.CHDopamine.name) },
                onCHReinforcementSelected = { navController.navigate(TheoryScreen.CHReinforcement.name) },
                onCHToleranceSelected = { navController.navigate(TheoryScreen.CHTolerance.name) },
                onCHHedonicCircuitSelected = { navController.navigate(TheoryScreen.CHHedonicCircuit.name) },
                onCHSolutionSelected = { navController.navigate(TheoryScreen.CHSolution.name) },
                theoryViewModel = theoryViewModel
            )
        }

        /**
         * Chapter Introduction
         */
        composable(route = TheoryScreen.CHIntro.name) {
            CHIntroIntro(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHIntroDilemma.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHIntroDilemma.name) {
            CHIntroDilemma(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHIntroDilemmaCont.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHIntroDilemmaCont.name) {
            CHIntroDilemmaCont(
                onChapterDone = { onChaptersDone(navController, theoryViewModel) },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) },
                chapterName = theoryViewModel.currentChapterName.value,
                theoryViewModel = theoryViewModel,
                detoxRankViewModel = detoxRankViewModel
            )
        }

        /**
         * Chapter Dopamine
         */
        composable(route = TheoryScreen.CHDopamine.name) {
            CHDopamineIntro(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHDopamineBrain.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHDopamineBrain.name) {
            CHDopamineBrain(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHDopamineNeurotransmitter.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHDopamineNeurotransmitter.name) {
            CHDopamineNeurotransmitter(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHDopaminePoint.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHDopaminePoint.name) {
            CHDopaminePoint(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHDopamineSummary.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }

        composable(route = TheoryScreen.CHDopamineSummary.name) {
            CHDopamineSummary(
                onChapterDone = { onChaptersDone(navController, theoryViewModel) },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) },
                chapterName = theoryViewModel.currentChapterName.value,
                theoryViewModel = theoryViewModel,
                detoxRankViewModel = detoxRankViewModel
            )
        }

        /**
         * Chapter Reinforcement
         */
        composable(route = TheoryScreen.CHReinforcement.name) {
            CHReinforcementIntro(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHReinforcementRewardCircuit.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHReinforcementRewardCircuit.name) {
            CHReinforcementRewardCircuit(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHReinforcementExample.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHReinforcementExample.name) {
            CHReinforcementExample(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHReinforcementProblems.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHReinforcementProblems.name) {
            CHReinforcementProblems(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHReinforcementSummary.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHReinforcementSummary.name) {
            CHReinforcementSummary(
                onChapterDone = { onChaptersDone(navController, theoryViewModel) },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) },
                chapterName = theoryViewModel.currentChapterName.value,
                theoryViewModel = theoryViewModel,
                detoxRankViewModel = detoxRankViewModel
            )
        }

        /**
         * Chapter Tolerance
         */
        composable(route = TheoryScreen.CHTolerance.name) {
            CHToleranceIntro(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHToleranceExample.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHToleranceExample.name) {
            CHToleranceExample(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHToleranceCorrelation.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHToleranceCorrelation.name) {
            CHToleranceCorrelation(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHToleranceSummary.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHToleranceSummary.name) {
            CHToleranceSummary(
                onChapterDone = { onChaptersDone(navController, theoryViewModel) },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) },
                chapterName = theoryViewModel.currentChapterName.value,
                theoryViewModel = theoryViewModel,
                detoxRankViewModel = detoxRankViewModel
            )
        }

        /**
         * Chapter Hedonic Circuit
         */
        composable(route = TheoryScreen.CHHedonicCircuit.name) {
            CHHedonicCircuitIntro(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHHedonicCircuitExample.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHHedonicCircuitExample.name) {
            CHHedonicCircuitExample(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHHedonicCircuitPoint.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHHedonicCircuitPoint.name) {
            CHHedonicCircuitPoint(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHHedonicCircuitSummary.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHHedonicCircuitSummary.name) {
            CHHedonicCircuitSummary(
                onChapterDone = { onChaptersDone(navController, theoryViewModel) },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) },
                chapterName = theoryViewModel.currentChapterName.value,
                theoryViewModel = theoryViewModel,
                detoxRankViewModel = detoxRankViewModel
            )
        }

        /**
         * Chapter Solution
         */
        composable(route = TheoryScreen.CHSolution.name) {
            CHSolutionIntro(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHSolutionAdvice.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHSolutionAdvice.name) {
            CHSolutionAdvice(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHSolutionAdviceCont.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHSolutionAdviceCont.name) {
            CHSolutionAdviceCont(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHSolutionAdviceContCont.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHSolutionAdviceContCont.name) {
            CHSolutionAdviceContCont(
                onChapterContinue = {
                    onChapterContinue(navController, theoryViewModel, currentChapterScreenNum, TheoryScreen.CHSolutionSummary.name)
                },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) }
            )
        }
        composable(route = TheoryScreen.CHSolutionSummary.name) {
            CHSolutionSummary(
                onChapterDone = { onChaptersDone(navController, theoryViewModel) },
                backHandler = { backHandler(navController, theoryViewModel, currentChapterScreenNum) },
                chapterName = theoryViewModel.currentChapterName.value,
                theoryViewModel = theoryViewModel,
                detoxRankViewModel = detoxRankViewModel
            )
        }
    }
}
