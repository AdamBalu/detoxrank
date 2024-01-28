package com.blaubalu.detoxrank.ui.utils

object Constants {
    const val ACTION_SERVICE_START = "ACTION_SERVICE_START"
    const val ACTION_SERVICE_CANCEL = "ACTION_SERVICE_CANCEL"

    const val TIMER_STATE = "STOPWATCH_STATE"

    const val NOTIFICATION_CHANNEL_ID = "STOPWATCH_NOTIFICATION_ID"
    const val NOTIFICATION_CHANNEL_NAME = "STOPWATCH_NOTIFICATION"
    const val NOTIFICATION_ID = 10

    const val CLICK_REQUEST_CODE = 100
    const val CANCEL_REQUEST_CODE = 101

    const val NUMBER_OF_TASKS_DAILY = 2
    const val NUMBER_OF_TASKS_WEEKLY = 4
    const val NUMBER_OF_TASKS_MONTHLY = 3

    const val KEY_TASK_DURATION = "KEY_TASK_DURATION"

    const val DAILY_TASK_RP_GAIN = 70
    const val WEEKLY_TASK_RP_GAIN = 250
    const val MONTHLY_TASK_RP_GAIN = 500
    const val UNCATEGORIZED_TASK_RP_GAIN = 30
    const val SPECIAL_TASK_RP_GAIN = 1000

    const val DAILY_TASK_XP_GAIN = 70
    const val WEEKLY_TASK_XP_GAIN = 100
    const val MONTHLY_TASK_XP_GAIN = 130

    // achievements fixed IDs (to ensure achievement completion by their IDs)
    const val ID_RUN_3_KM = 1
    const val ID_RUN_5_KM = 2
    const val ID_RUN_7_KM = 3
    const val ID_RUN_10_KM = 4
    const val ID_READ_20_PAGES = 5
    const val ID_READ_50_PAGES = 6
    const val ID_READ_100_PAGES = 7
    const val ID_READ_250_PAGES = 8
    const val ID_READ_A_BOOK = 9
    const val ID_READ_5_BOOKS = 10
    const val ID_READ_10_BOOKS = 11
    const val ID_FINISH_CH_1 = 12
    const val ID_FINISH_CH_2 = 13
    const val ID_FINISH_CH_3 = 14
    const val ID_FINISH_CH_4 = 15
    const val ID_FINISH_CH_5 = 16
    const val ID_FINISH_CH_6 = 17
    const val ID_FINISH_ALL_CH = 18
    const val ID_FINISH_FIRST_TASK = 19
    const val ID_FINISH_5_TASKS = 20
    const val ID_FINISH_10_TASKS = 21
    const val ID_FINISH_25_TASKS = 22
    const val ID_FINISH_50_TASKS = 23
    const val ID_FINISH_100_TASKS = 24
    const val ID_FINISH_250_TASKS = 25
    const val ID_START_TIMER = 26
    const val ID_TIMER_3_DAYS = 27
    const val ID_TIMER_7_DAYS = 28
    const val ID_TIMER_14_DAYS = 29
    const val ID_TIMER_30_DAYS = 30

    const val ID_READ_10_PAGES = 31

    const val PAGES_10 = 10
    const val PAGES_20 = 20
    const val PAGES_50 = 50
    const val PAGES_100 = 100
    const val PAGES_250 = 250
    const val BOOKS_1_PAGE_COUNT = 500
    const val BOOKS_5_PAGE_COUNT = 2500
    const val BOOKS_10_PAGE_COUNT = 5000

    const val RP_PERCENTAGE_GAIN_TIMER_EASY_DIFFICULTY = 10
    const val RP_PERCENTAGE_GAIN_TIMER_MEDIUM_DIFFICULTY = 20
    const val RP_PERCENTAGE_GAIN_TIMER_HARD_DIFFICULTY = 30

    const val TIMER_HOURLY_RP_GAIN = 8


    // RANK LOWER RP CAPS
    const val BRONZE_I_LOWER_CAP = 0
    const val BRONZE_II_LOWER_CAP = 300
    const val BRONZE_III_LOWER_CAP = 1000

    const val SILVER_I_LOWER_CAP = 2000
    const val SILVER_II_LOWER_CAP = 3000
    const val SILVER_III_LOWER_CAP = 4000

    const val GOLD_I_LOWER_CAP = 5200
    const val GOLD_II_LOWER_CAP = 6300
    const val GOLD_III_LOWER_CAP = 7400

    const val PLAT_I_LOWER_CAP = 8600
    const val PLAT_II_LOWER_CAP = 9900
    const val PLAT_III_LOWER_CAP = 11200

    const val DIA_I_LOWER_CAP = 13000
    const val DIA_II_LOWER_CAP = 15500
    const val DIA_III_LOWER_CAP = 17000

    const val MASTER_LOWER_CAP = 19000

    const val LEGEND_LOWER_CAP = 25000

    // RANK UPPER RP CAPS
    const val BRONZE_I_UPPER_CAP = BRONZE_II_LOWER_CAP - 1
    const val BRONZE_II_UPPER_CAP = BRONZE_III_LOWER_CAP - 1
    const val BRONZE_III_UPPER_CAP = SILVER_I_LOWER_CAP - 1


    const val SILVER_I_UPPER_CAP = SILVER_II_LOWER_CAP - 1
    const val SILVER_II_UPPER_CAP = SILVER_III_LOWER_CAP - 1
    const val SILVER_III_UPPER_CAP = GOLD_I_LOWER_CAP - 1


    const val GOLD_I_UPPER_CAP = GOLD_II_LOWER_CAP - 1
    const val GOLD_II_UPPER_CAP = GOLD_III_LOWER_CAP - 1
    const val GOLD_III_UPPER_CAP = PLAT_I_LOWER_CAP - 1

    const val PLAT_I_UPPER_CAP = PLAT_II_LOWER_CAP - 1
    const val PLAT_II_UPPER_CAP = PLAT_III_LOWER_CAP - 1
    const val PLAT_III_UPPER_CAP = DIA_I_LOWER_CAP - 1

    const val DIA_I_UPPER_CAP = DIA_II_LOWER_CAP - 1
    const val DIA_II_UPPER_CAP = DIA_III_LOWER_CAP - 1
    const val DIA_III_UPPER_CAP = MASTER_LOWER_CAP - 1

    const val MASTER_UPPER_CAP = LEGEND_LOWER_CAP - 1


    const val CHAPTER_DIFFICULTY_EASY_RP_GAIN = 200
    const val CHAPTER_DIFFICULTY_MEDIUM_RP_GAIN = 350
    const val CHAPTER_DIFFICULTY_HARD_RP_GAIN = 500

    const val CHAPTER_DIFFICULTY_EASY_XP_GAIN = 80
    const val CHAPTER_DIFFICULTY_MEDIUM_XP_GAIN = 120
    const val CHAPTER_DIFFICULTY_HARD_XP_GAIN = 175

    const val MIN_LEVEL_TO_UNLOCK_SPECIAL_TASKS = 20

    const val LOW_LEVEL_LOWER_CAP = 0
    const val LOW_LEVEL_UPPER_CAP = 14
    const val HIGH_LEVEL_LOWER_CAP = 15
    const val HIGH_LEVEL_UPPER_CAP = 25
}
