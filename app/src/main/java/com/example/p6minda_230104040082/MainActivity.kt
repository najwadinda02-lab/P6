package com.example.p6minda_230104040082

// Import DataStore, Navigasi, dan UI (package sudah disesuaikan)

// Import Coroutines
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.p6minda_230104040082.data.UserPrefsRepository
import com.example.p6minda_230104040082.ui.theme.BottomNavBar
import com.example.p6minda_230104040082.ui.theme.navigation.AppNavHost
import com.example.p6minda_230104040082.ui.theme.navigation.Routes
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // --- Repo & state (DataStore) ---
                    val userPrefs = remember { UserPrefsRepository(this@MainActivity) }
                    val scope = rememberCoroutineScope()

                    // Menggunakan collectAsState untuk flow
                    val storedName by userPrefs.userNameFlow.collectAsState(initial = null)
                    val onboardDone by userPrefs.onboardingCompletedFlow.collectAsState(initial = false)

                    // --- Nav ---
                    val navController = rememberNavController()
                    val currentRoute by navController.currentBackStackEntryAsState()
                    val route = currentRoute?.destination?.route

                    Scaffold(
                        bottomBar = {
                            if (shouldShowBottomBar(route)) {
                                BottomNavBar(navController = navController)
                            }
                        },
                        floatingActionButton = {
                            if (shouldShowBottomBar(route)) {
                                FloatingActionButton(
                                    onClick = { navController.navigate(Routes.NEW) },
                                    modifier = Modifier.offset(y = 40.dp),
                                    containerColor = Color(0xFF6750A4),
                                    contentColor = Color.White
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "New entry")
                                }
                            }
                        },
                        floatingActionButtonPosition = FabPosition.Center
                    ) { innerPadding ->
                        AppNavHost(
                            navController = navController,
                            storedName = storedName,
                            hasCompletedOnboarding = onboardDone, //
                            onSaveUserName = { name ->
                                scope.launch { userPrefs.saveUserName(name) }
                            },
                            onSetOnboardingCompleted = {
                                scope.launch { userPrefs.setOnboardingCompleted(true) }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        )
                    }
                }
            }
        }
    }

    /** Bottom bar hanya tampil di 4 tab utama. */
    private fun shouldShowBottomBar(route: String?): Boolean {
        return route in setOf(Routes.HOME, Routes.CALENDAR, Routes.INSIGHTS, Routes.SETTINGS)
    }
}