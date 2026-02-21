package com.syncup.app.presentation.navigation

/**
 * @author Muhamed Amin Hassan on 21,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
sealed class NavRoutes(
    val route: String
) {
    object ConversationList : NavRoutes("conversation_list")
    object Profile: NavRoutes("profile")
    object Chat : NavRoutes("chat/{conversationId}/{otherUserId}") {
        fun createRoute(conversationId: String, otherUserId: String) =
            "chat/$conversationId/$otherUserId"
    }
}