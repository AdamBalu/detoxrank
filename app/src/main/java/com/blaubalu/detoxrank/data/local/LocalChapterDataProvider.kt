package com.blaubalu.detoxrank.data.local

import com.blaubalu.detoxrank.data.chapter.Chapter
import com.blaubalu.detoxrank.data.chapter.ChapterDifficulty
import com.blaubalu.detoxrank.data.chapter.ChapterTag


/**
 * Data provider for Chapter. Used to fill the database with chapters
 */
object LocalChapterDataProvider {
    val allChapters = listOf(
        Chapter(
            name = "Introduction",
            description = "Welcome to dopamine detox theory! Here you will find some motivation for why you should study this section.",
            difficulty = ChapterDifficulty.Easy,
            tag = ChapterTag.Introduction,
            screenNum = 3,
            startChapterButtonLabel = "Get Started!"
        ),
        Chapter(
            name = "Dopamine",
            description = "In this chapter, we will talk about how neurons and neurotransmitters work. We also get a brief look into what dopamine does in the brain and how it influences our motivation.",
            difficulty = ChapterDifficulty.Medium,
            tag = ChapterTag.Dopamine,
            screenNum = 5,
            startChapterButtonLabel = "Learn About Dopamine"
        ),
        Chapter(
            name = "Reinforcement",
            description = "This chapter looks deeper into brain's reward circuitry and explores the main behavior that this circuit executes - reinforcement learning.",
            difficulty = ChapterDifficulty.Hard,
            tag = ChapterTag.Reinforcement,
            screenNum = 5,
            startChapterButtonLabel = "Study Reinforcement"
        ),
        Chapter(
            name = "Tolerance",
            description = "Here we will talk about tolerance which is developed in the brain when exposing yourself to a large quantum of dopamine.",
            difficulty = ChapterDifficulty.Medium,
            tag = ChapterTag.Tolerance,
            screenNum = 4,
            startChapterButtonLabel = "Find Out About Tolerance"
        ),
        Chapter(
            name = "Hedonic circuit",
            description = "Let's learn about the hedonic circuit which is responsible for producing enjoyment in the brain!",
            difficulty = ChapterDifficulty.Hard,
            tag = ChapterTag.HedonicCircuit,
            screenNum = 4,
            startChapterButtonLabel = "Study Hedonic Circuit"
        ),
        Chapter(
            name = "Solution - Detox",
            description = "The chapter talks about how to do a dopamine detox correctly.",
            difficulty = ChapterDifficulty.Easy,
            tag = ChapterTag.Solutions,
            screenNum = 5,
            startChapterButtonLabel = "Learn How To Detox"
        )
    )
}
