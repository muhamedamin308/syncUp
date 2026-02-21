package com.syncup.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.syncup.app.presentation.chat.ChatScreen
import com.syncup.app.presentation.conversations.ConversationListScreen
import com.syncup.app.presentation.profile.ProfileScreen

/**
 * @author Muhamed Amin Hassan on 21,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

@Composable
fun SyncUpNavGraph(
    navController : NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.ConversationList.route
    ) {
        composable(NavRoutes.ConversationList.route) {
            ConversationListScreen(
                onConversationClick = { conversationId, otherUserId ->
                    navController.navigate(
                        NavRoutes.Chat.createRoute(conversationId, otherUserId)
                    )
                },
                onProfileClick = {
                    navController.navigate(NavRoutes.Profile.route)
                }
            )
        }

        composable(
            route = NavRoutes.Chat.route,
            arguments = listOf(
                navArgument("conversationId") { type = NavType.StringType },
                navArgument("otherUserId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            ChatScreen(
                conversationId = backStackEntry.arguments?.getString("conversationId") ?: "",
                otherUserId = backStackEntry.arguments?.getString("otherUserId") ?: "",
                onBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.Profile.route) {
            ProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}