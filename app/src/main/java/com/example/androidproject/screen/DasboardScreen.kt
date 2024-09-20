package com.example.androidproject.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androidproject.nav.Screens
import com.example.androidproject.utils.DashboardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun DashboardScreen(navController: NavController, dashboardViewModel: DashboardViewModel) {
    val auth = FirebaseAuth.getInstance()

    val user = auth.currentUser
    var role by remember { mutableStateOf<String?>(null) }
    val userID = user?.uid

    // Fetch the user's role from Firestore
    LaunchedEffect(key1 = userID) {
        userID?.let { uid ->
            val db = FirebaseFirestore.getInstance()
            val userDocRef = db.collection("users").document(uid)

            userDocRef.get()
                .addOnSuccessListener { document ->
                    role = document?.getString("role")
                }
                .addOnFailureListener {
                    // Handle failure
                    role = null
                }
        }
    }

    // Display different content based on the user's role
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (role) {
            "admin" -> AdminDashboard(navController, dashboardViewModel)
            "alumni" -> AlumniDashboard(navController, dashboardViewModel, userID ?: "")
            else -> CircularProgressIndicator()
        }
    }
}

@Composable
fun AdminDashboard(navController: NavController, dashboardViewModel: DashboardViewModel) {
    Scaffold(
        topBar = {
            IstTopBar(navController, "")
        },
        bottomBar = {
            IstBottomBar(dashboardViewModel, navController)
        }
    ) { paddingValues ->
        AdminDisplay(navController, paddingValues)
    }
}

@Composable
fun AdminDisplay(navController: NavController, paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            navController.navigate("jobPosting_screen")
        }) {
            Text("Post New Job")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Navigate to another Admin-specific screen */ }) {
            Text("Review Alumni Applications")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LogoutButton(navController)
    }
}

@Composable
fun AlumniDisplay(navController: NavController, paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(4.dp, shape = RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp),  // Creates consistent space between items
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Alumni Dashboard",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .fillMaxWidth()
                )

                // Buttons
                Button(
                    onClick = { /* Navigate to Job Postings screen */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "View Job Postings",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }

                Button(
                    onClick = { /* Navigate to Review Applications screen */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Review Applications",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Logout Button
                LogoutButton(navController)
            }
        }
    }
}


@Composable
fun AlumniDashboard(navController: NavController, dashboardViewModel: DashboardViewModel, userID: String) {
    Scaffold(
        topBar = {
            IstTopBar(navController, userID) // Pass userID to the top bar
        },
        bottomBar = {
            IstBottomBar(dashboardViewModel, navController)
        }
    ) { paddingValues ->
        AlumniDisplay(navController, paddingValues)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IstTopBar(navController: NavController, userID: String) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            titleContentColor = MaterialTheme.colorScheme.surface,
        ),
        title = {
            Text(
                "IST",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* Handle navigation */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        actions = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Blue)
                    .clickable {
                        navController.navigate("viewProfile_screen/$userID")
                    },
                contentAlignment = Alignment.Center
            ) {
                // Optionally, add something inside the circle, e.g., initials or an icon
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun IstBottomBar(dashboardViewModel: DashboardViewModel, navController: NavController) {
    BottomAppBar(
        content = {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home
                BottomBarIcon(
                    imageVector = Icons.Filled.Home,
                    label = "Home",
                    onClick = {
                        dashboardViewModel.updateScreen("home")
                        navController.navigate(Screens.DashboardScreen.route)
                    }
                )

                // Network
                BottomBarIcon(
                    imageVector = Icons.Filled.Person,
                    label = "Network",
                    onClick = {
                        dashboardViewModel.updateScreen("network")
                        navController.navigate(Screens.DashboardScreen.route)
                    }
                )

                // Add
                BottomBarIcon(
                    imageVector = Icons.Filled.Add,
                    label = "Add",
                    onClick = {
                        dashboardViewModel.updateScreen("add")
                        navController.navigate(Screens.DashboardScreen.route)
                    }
                )

                // Jobs
                BottomBarIcon(
                    imageVector = Icons.Filled.Phone,
                    label = "Jobs",
                    onClick = {
                        dashboardViewModel.updateScreen("jobs")
                        navController.navigate(Screens.DashboardScreen.route)
                    }
                )

                // Inbox
                BottomBarIcon(
                    imageVector = Icons.Filled.Notifications,
                    label = "Inbox",
                    onClick = {
                        dashboardViewModel.updateScreen("inbox")
                        navController.navigate(Screens.DashboardScreen.route)
                    }
                )
            }
        }
    )
}

@Composable
fun BottomBarIcon(imageVector: ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = imageVector,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(32.dp)
            )
        }
        Text(text = label, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun LogoutButton(navController: NavController) {
    Button(
        onClick = {
            FirebaseAuth.getInstance().signOut()
            navController.navigate(Screens.RegisterScreen.route) {
                popUpTo(Screens.DashboardScreen.route) {
                    inclusive = true
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Logout")
    }
}