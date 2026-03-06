package com.example.avflauncher.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.avflauncher.core.VmConfig
import com.example.avflauncher.ui.screens.VmJsonEditorScreen
import com.example.avflauncher.ui.screens.VmListScreen

sealed class Screen(val route: String) {
    object List : Screen("list")
    object Editor : Screen("editor/{vmId}") {
        fun createRoute(vmId: String) = "editor/$vmId"
    }
    object Console : Screen("console/{vmId}") {
        fun createRoute(vmId: String) = "console/$vmId"
    }
}

@Composable
fun AvfNavHost(
    navController: NavHostController = rememberNavController(),
    vmList: List<VmConfig>,
    onAddVm: () -> Unit,
    onStartVm: (VmConfig) -> Unit,
    onSaveJson: (String, VmConfig) -> Unit
) {
    NavHost(navController = navController, startDestination = Screen.List.route) {
        composable(Screen.List.route) {
            VmListScreen(
                vmList = vmList,
                onAddVm = onAddVm,
                onStartVm = { 
                    onStartVm(it)
                    navController.navigate(Screen.Console.createRoute(it.id))
                },
                onEditConfig = { 
                    navController.navigate(Screen.Editor.createRoute(it.id))
                }
            )
        }
        
        composable(Screen.Editor.route) { backStackEntry ->
            val vmId = backStackEntry.arguments?.getString("vmId")
            val vm = vmList.find { it.id == vmId }
            if (vm != null) {
                VmJsonEditorScreen(
                    initialJson = vm.configJson.ifEmpty { "{}" },
                    onSave = { newJson ->
                        onSaveJson(newJson, vm)
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.Console.route) { backStackEntry ->
            val vmId = backStackEntry.arguments?.getString("vmId")
            val vm = vmList.find { it.id == vmId }
            if (vm != null) {
                VmConsoleScreen(
                    vm = vm,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
