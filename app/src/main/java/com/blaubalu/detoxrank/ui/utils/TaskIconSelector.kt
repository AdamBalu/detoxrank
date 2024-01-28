package com.blaubalu.detoxrank.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.blaubalu.detoxrank.data.task.TaskIconCategory

/**
 * Returns icon (ImageVector) of a task depending on its category
 */
fun getIcon(iconCategory: TaskIconCategory): ImageVector {
    return when (iconCategory) {
        TaskIconCategory.Exercise -> Icons.Filled.FitnessCenter
        TaskIconCategory.Health -> Icons.Filled.PersonAdd
        TaskIconCategory.Running -> Icons.Filled.DirectionsRun
        TaskIconCategory.Walking -> Icons.Filled.DirectionsWalk
        TaskIconCategory.Swimming -> Icons.Filled.Pool
        TaskIconCategory.Meditation -> Icons.Filled.SelfImprovement
        TaskIconCategory.HealthyFood -> Icons.Filled.DinnerDining
        TaskIconCategory.UnhealthyFood -> Icons.Filled.LunchDining
        TaskIconCategory.Cleaning -> Icons.Filled.CleaningServices
        TaskIconCategory.Cycling -> Icons.Filled.PedalBike
        TaskIconCategory.Reading -> Icons.Filled.Book
        TaskIconCategory.Hiking -> Icons.Filled.Hiking
        TaskIconCategory.Fun -> Icons.Filled.SentimentVerySatisfied
        TaskIconCategory.Places -> Icons.Filled.LocationCity
        TaskIconCategory.Other -> Icons.Filled.Task
        TaskIconCategory.Creating -> Icons.Filled.Draw
        TaskIconCategory.Music -> Icons.Filled.MusicNote
        TaskIconCategory.Sports -> Icons.Filled.SportsBasketball
        TaskIconCategory.Photography -> Icons.Filled.PhotoCamera
        else -> Icons.Filled.Task
    }
}
