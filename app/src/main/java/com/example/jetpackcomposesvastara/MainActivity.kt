package com.example.jetpackcomposesvastara

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jetpackcomposesvastara.data.*
import com.example.jetpackcomposesvastara.presentation.composable.journals.JournalItemDetails
import com.example.jetpackcomposesvastara.presentation.composable.navigation.*
import com.example.jetpackcomposesvastara.presentation.theme.DiplomskiTheme
import com.example.jetpackcomposesvastara.presentation.viewModel.MainViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.fitness.FitnessActivities
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val TAG = "DiplomskiMainActivity"

@AndroidEntryPoint
@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {

    companion object {

        private const val AUTH_TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001

        private const val GOOGLE_FIT_CODE = 6327

        private const val ACTIVITY_RECOGNITION_CODE = 3727

        var isGoogleFitLoginComplete = false

    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_local))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth
        startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)


        FitnessActivities.ZUMBA
    }

    //region Auth
    override fun onStart() {
        super.onStart()

        updateUI(auth.currentUser)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(AUTH_TAG, "firebaseAuthWithGoogle:" + account.id)
                Toast.makeText(this@MainActivity, "Sign in success", Toast.LENGTH_SHORT).show()
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(AUTH_TAG, "Google sign in failed", e)
                Toast.makeText(this@MainActivity, "Sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            if(requestCode == GOOGLE_FIT_CODE)
            {
                if(resultCode == RESULT_OK)
                    startReadingData()
                else
                    Toast.makeText(this, "Google oauth failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(AUTH_TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(AUTH_TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if(user != null)
        {
            checkGoogleFitPermissions()
//            val photoUrl = auth.currentUser?.photoUrl
//            setContent()
            checkPermissionsAndRun()
        }
    }

    private fun setContent()
    {
        setContent {
            MainScreen()
        }
    }

    //endregion

    //region GoogleFitSignIn

    private fun checkGoogleFitPermissions()
    {
        val googleSignInAccount: GoogleSignInAccount = getGoogleAccount()

        if(!GoogleSignIn.hasPermissions(googleSignInAccount, fitnessOption))
        {
            GoogleSignIn.requestPermissions(
                this,
                GOOGLE_FIT_CODE,
                googleSignInAccount,
                fitnessOption)
        }
        else
            startReadingData()
    }

    private fun getGoogleAccount(): GoogleSignInAccount = GoogleSignIn.getAccountForExtension(this, fitnessOption)

    private fun startReadingData() {
//        getTodayData()
//        subscribeAndGetRealTimeData(TYPE_STEP_COUNT_DELTA)
    }

    //endregion

    //region Composable

    private val items = listOf(
        NavigationItem.Home,
        NavigationItem.Goals,
        NavigationItem.Journal,
        NavigationItem.Profile
    )

    @ExperimentalComposeUiApi
    @Composable
    fun MainScreen() {
        val navController = rememberNavController()
        DiplomskiTheme {
            Surface(
                color = MaterialTheme.colors.background,
                modifier = Modifier.fillMaxSize()
            ) {
                Scaffold(
                    topBar = {},
                    bottomBar = {
                        var isBottomNavCurrentRoute = false
                        for(navItem in items)
                        {
                            if (currentRoute(navController) == navItem.route)
                            {
                                isBottomNavCurrentRoute = true
                                break
                            }
                        }
                        if(isBottomNavCurrentRoute)
                            BottomNavigationBar(navController)
                    }
                ) {
                    Navigation(navController = navController)
                }
            }
        }
    }

    @ExperimentalComposeUiApi
    @Composable
    fun Navigation(navController: NavHostController) {
        NavHost(navController, startDestination = NavigationItem.Home.route) {
            composable(NavigationItem.Home.route) {
                HomeScreen(mainViewModel)
            }
            composable(NavigationItem.Goals.route) {
                GoalsScreen()
            }
            composable(NavigationItem.Journal.route) {
                JournalScreen(navController)
            }
            composable(NavigationItem.Profile.route) {
                ProfileScreen()
            }
            composable(route = "journal_details/{journalUid}/{journalIdentifier}",
            arguments = listOf(
                navArgument("journalUid") {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument("journalIdentifier") {
                    type = NavType.StringType
                    defaultValue = "noID"
                }
            )) {
                val uid = remember {
                    it.arguments?.getInt("journalUid") ?: -1
                }
                val journalIdentifier = remember {
                    it.arguments?.getString("journalIdentifier") ?: "noID"
                }
                JournalItemDetails(
                    navController = navController,
                    uid = uid,
                    identifier = journalIdentifier)
            }
        }
    }


    @Composable
    fun currentRoute(navController: NavHostController): String? {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        return navBackStackEntry?.destination?.route
    }



    @Composable
    fun BottomNavigationBar(navController: NavController) {
        BottomNavigation(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onBackground,
            modifier = Modifier.graphicsLayer {
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                clip = true
            }
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        BottomNavBarIcon(iconId = item.icon,
                            isSelected = currentRoute == item.route,
                            text = item.title) },
                    selectedContentColor = MaterialTheme.colors.onBackground,
                    unselectedContentColor = MaterialTheme.colors.onBackground.copy(0.4f),
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun BottomNavigationBarPreview() {
        BottomNavigationBar()
    }

    //endregion

    //region PERMISSION

    private val runningQOrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    private val permissionArray = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACTIVITY_RECOGNITION
    )

    private fun checkPermissionsAndRun() {
        if (!permissionApproved())
            requestRuntimePermissions()
        else
            setContent()
    }

    private fun permissionApproved(): Boolean
    {
        var isGreenLight = true
        for(permission in permissionArray)
        {
            if(PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(this, permission))
            {
                isGreenLight = false
                break
            }
        }

        return isGreenLight
    }

    private fun requestRuntimePermissions() {
        ActivityCompat.requestPermissions(this,
            permissionArray,
            ACTIVITY_RECOGNITION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == ACTIVITY_RECOGNITION_CODE)
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request
                    // is cancelled and you receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                }
                grantResults.size == permissionArray.size -> {

                    // Permission was granted.

                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED)
                        setContent()
                }
            }
    }

    //endregion

}





