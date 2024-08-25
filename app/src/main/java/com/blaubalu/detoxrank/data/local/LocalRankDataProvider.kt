package com.blaubalu.detoxrank.data.local

import com.blaubalu.detoxrank.data.user.Rank

object LocalRankDataProvider {
  val ranksSeparated = listOf(
    listOf(
      Rank.Bronze1,
      Rank.Bronze2,
      Rank.Bronze3
    ),
    listOf(
      Rank.Silver1,
      Rank.Silver2,
      Rank.Silver3
    ),
    listOf(
      Rank.Gold1,
      Rank.Gold2,
      Rank.Gold3
    ),
    listOf(
      Rank.Platinum1,
      Rank.Platinum2,
      Rank.Platinum3
    ),
    listOf(
      Rank.Diamond1,
      Rank.Diamond2,
      Rank.Diamond3
    ),
    listOf(Rank.Master),
    listOf(Rank.Legend))
}
