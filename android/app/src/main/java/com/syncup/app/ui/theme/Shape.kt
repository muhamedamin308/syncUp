package com.syncup.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * @author Muhamed Amin Hassan on 21,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

val SyncUpShapes = Shapes(
    // Input fields, small chips
    extraSmall = RoundedCornerShape(6.dp),
    // Conversation cards
    small = RoundedCornerShape(12.dp),
    // Bottom sheets, dialogs
    medium = RoundedCornerShape(16.dp),
    // Large cards
    large = RoundedCornerShape(20.dp),
    // Full pill (send button, badges)
    extraLarge = RoundedCornerShape(50.dp)
)

// Message bubble shapes — asymmetric, like real chat apps
val BubbleSentShape = RoundedCornerShape(
    topStart = 18.dp,
    topEnd = 4.dp,
    bottomStart = 18.dp,
    bottomEnd = 18.dp
)

val BubbleReceivedShape = RoundedCornerShape(
    topStart = 4.dp,
    topEnd = 18.dp,
    bottomStart = 18.dp,
    bottomEnd = 18.dp
)