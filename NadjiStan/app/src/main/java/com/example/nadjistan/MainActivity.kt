package com.example.nadjistan

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nadjistan.data.objects.ContextProvider
import com.example.nadjistan.data.repositories.AuthRepository
import com.example.nadjistan.data.repositories.PonudeRepository
import com.example.nadjistan.data.objects.RepositoryProvider
import com.example.nadjistan.data.services.DataRefreshService
import com.example.nadjistan.data.tools.LocationProvider
import com.example.nadjistan.data.tools.Screens
import com.example.nadjistan.data.services.LocationTrackerService
import com.example.nadjistan.data.tools.DataStoreProvider
import com.example.nadjistan.data.worker.WorkGenerator
import com.example.nadjistan.ui.theme.NadjiStanTheme
import com.example.nadjistan.ui.theme.screens.Main
import com.example.nadjistan.ui.theme.screens.auth.Login
import com.example.nadjistan.ui.theme.screens.auth.PasswordRecovery
import com.example.nadjistan.ui.theme.screens.auth.Register
import com.example.nadjistan.ui.theme.screens.maps.AddMap
import com.example.nadjistan.ui.theme.screens.maps.ShowAllMap
import com.example.nadjistan.ui.theme.screens.maps.ShowOneMap
import com.example.nadjistan.ui.theme.screens.myoffers.MyOffers
import com.example.nadjistan.ui.theme.screens.myoffers.NewOffer
import com.example.nadjistan.ui.theme.screens.offers.Filters
import com.example.nadjistan.ui.theme.screens.offers.Offer
import com.example.nadjistan.ui.theme.screens.offers.Offers
import com.example.nadjistan.ui.theme.screens.profile.Profile
import com.example.nadjistan.ui.theme.screens.profile.Rank
import com.example.nadjistan.ui.theme.viewmodels.auth.LoginViewModel
import com.example.nadjistan.ui.theme.viewmodels.auth.LoginViewModelFactory
import com.example.nadjistan.ui.theme.viewmodels.auth.RegisterViewModel
import com.example.nadjistan.ui.theme.viewmodels.auth.RegisterViewModelFactory
import com.example.nadjistan.ui.theme.viewmodels.myoffers.MyOffersViewModelFactory
import com.example.nadjistan.ui.theme.viewmodels.myoffers.NewOfferViewModel
import com.example.nadjistan.ui.theme.viewmodels.myoffers.NewOfferViewModelFactory
import com.example.nadjistan.ui.theme.viewmodels.myoffers.myOffersViewModel
import com.example.nadjistan.ui.theme.viewmodels.offers.OfferViewModel
import com.example.nadjistan.ui.theme.viewmodels.offers.OffersViewModel
import com.example.nadjistan.ui.theme.viewmodels.offers.OffersViewModelFactory
import com.example.nadjistan.ui.theme.viewmodels.profile.ProfileViewModel
import com.example.nadjistan.ui.theme.viewmodels.profile.ProfileViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

var lp: LocationProvider = LocationProvider()
var image = mutableStateOf("")
val Context.dataStore by preferencesDataStore(name = "PreferenceDataStore")
var loggedIn : MutableState<Boolean> = mutableStateOf(false)

class MainActivity : ComponentActivity() {
    private var i: Intent? = null
    private var j: Intent? = null
    lateinit var data : PonudeRepository
    lateinit var auth : AuthRepository
    lateinit var dsp : DataStoreProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ContextProvider.set(this)

        data = RepositoryProvider.GetRepository()

        auth = RepositoryProvider.GetAuth()

        val one : OfferViewModel by viewModels()
        dsp = DataStoreProvider(this)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
            ),
            0
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            requestBGLocationPermission()
        }

        if (savedInstanceState == null){
            val context = this
            CoroutineScope(Dispatchers.IO).launch{
                dsp.isNotificationAllowed {
                    rez -> if(rez == null)
                    {
                        CoroutineScope(Dispatchers.IO).launch{
                        dsp.setNotificationAllowed(true)
                            WorkGenerator(context, dsp)
                        }
                    }
                }

            }
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                image.value = uri.toString()
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

                setContent {
            NadjiStanTheme {
                NavApp(lp,pickMedia,dsp,one,data){
                    b ->
                    if(b) {
                        StartDataRefreshService()
                    }
                    else{
                       StopDataRefreshService()
                    }
                }
            }
        }
    }

    fun StartDataRefreshService(){
        Toast.makeText(this, "Servis pokrenut", Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.IO).launch{
            dsp.getDRSF {
                    rez ->
                if(rez == null) {
                    CoroutineScope(Dispatchers.IO).launch{
                         dsp.saveDRSF( true)
                         j = Intent(applicationContext, DataRefreshService::class.java)
                         startService(j)
                    }
                 }
                else if( rez == true ) {
                    j = Intent(applicationContext, DataRefreshService::class.java)
                    startService(j)
                }
            }

        }
    }
    fun StopDataRefreshService(){
        Toast.makeText(this, "Servis zaustavljen", Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.IO).launch{
            dsp.getDRSF { rez ->
                if (!rez!!){
                    j = Intent(applicationContext, DataRefreshService::class.java)
                    stopService(j)
                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        loggedIn.value = auth.isloggedIn()
        i = Intent(applicationContext, LocationTrackerService::class.java)
        startService(i)
        StartDataRefreshService()
    }

    override fun onStop() {
        Log.d("MainActivity","OnStop")
        stopService(i)
        super.onStop()
    }

    override fun onDestroy() {
        StopDataRefreshService()
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun requestBGLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.FOREGROUND_SERVICE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS
            ),
            0
        )
    }
}

@Composable
fun NavApp(
    lp : LocationProvider,
    pickMedia : ActivityResultLauncher<PickVisualMediaRequest>,
    dataStoreProvider: DataStoreProvider,
    one : OfferViewModel,
    repository: PonudeRepository,
    drs : (Boolean) -> Unit
)
{
    val navController = rememberNavController()

    val login: LoginViewModel = viewModel(factory = LoginViewModelFactory(go = { loggedIn.value = true}))
    val register : RegisterViewModel = viewModel(factory = RegisterViewModelFactory(go = { loggedIn.value = true; navController.popBackStack()}))
    val offers : OffersViewModel = viewModel(factory = OffersViewModelFactory(repository,lp))
    val myoffers : myOffersViewModel = viewModel(factory = MyOffersViewModelFactory(repository))
    //val one : OfferViewModel by viewModels()
    val new : NewOfferViewModel = viewModel(factory = NewOfferViewModelFactory(lp,repository))
    repository.Execute {
        myoffers.update()
        offers.update()
    }
    val pr : ProfileViewModel = viewModel(factory = ProfileViewModelFactory(dataStoreProvider,drs))
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = Screens.Main_Login.name){
        composable(Screens.Register.name){
            Register(register = register, goLog = {navController.popBackStack()})
        }
        composable(Screens.Main_Login.name){
            if (loggedIn.value)
                Main(
                    goOffer = {navController.navigate(Screens.Offers.name)},
                    goMyOffer = {navController.navigate(Screens.MyOffers.name)},
                    goProfil = {navController.navigate(Screens.Profile.name)},
                    GetData = {pr.getData(context)},
                    GetPonude = {offers.update()},
                    GetMyPonude = {myoffers.update()}
                )
            else
                Login(
                    login = login,
                    goReg = {navController.navigate(Screens.Register.name)},
                    goPR = {navController.navigate(Screens.PasswordRecovery.name)}
                )
        }
        composable(Screens.Offers.name){
            Offers(
                ovm = offers,
                goBack = {navController.popBackStack()},
                filter = {navController.navigate(Screens.Filters.name) },
                goOne = {navController.navigate(Screens.Offer.name)},
                seto = one::set,
                map = {navController.navigate(Screens.ShowAllMap.name)}
            )
        }
        composable(Screens.MyOffers.name){
            MyOffers(
                ovm = myoffers,
                goBack = {navController.popBackStack()},
                filter = {navController.navigate(Screens.NewOffer.name)},
                set = new::setPonuda,
                reset = {new.resetPonuda()},
                map = {navController.navigate(Screens.ShowAllMyMap.name)}
            )
        }
        composable(Screens.Profile.name){
            Profile(vm = pr, pickMedia = pickMedia, goBack = {
                loggedIn.value = false
                navController.popBackStack()
            },
            goRank = { navController.navigate(Screens.Rank.name)}
            )
        }
        composable(Screens.PasswordRecovery.name){
            PasswordRecovery(pr = login)
        }
        composable(Screens.Offer.name){
            Offer(ovm = one, goBack = {navController.popBackStack()}, lok = {navController.navigate(Screens.ShowOneMap.name)})
        }
        composable(Screens.Filters.name){
            Filters(v = offers, goBack = {navController.popBackStack()})
        }
        composable(Screens.NewOffer.name){
            NewOffer(ovm = new, goBack = {navController.popBackStack()}, lok = {navController.navigate(Screens.AddMap.name)}, pickMedia = pickMedia)
        }
        composable(Screens.ShowOneMap.name){
            ShowOneMap(ponuda = one.ponuda.value, goBack = {navController.popBackStack()})
        }
        composable(Screens.ShowAllMap.name){
            ShowAllMap(ponude = offers.ponude.value.toTypedArray(), goBack = {navController.popBackStack()}, lp, filter = {navController.navigate(Screens.Filters.name)}, img = Icons.Default.Search)
        }
        composable(Screens.ShowAllMyMap.name){
            ShowAllMap(ponude = myoffers.ponude.value, goBack = {navController.popBackStack()}, lp, filter = {navController.popBackStack(); navController.popBackStack()}, img = Icons.Default.Menu )
        }
        composable(Screens.AddMap.name){
            AddMap (vm = new,goBack = {navController.popBackStack()})
        }
        composable(Screens.Rank.name){
            Rank(vm = pr, goBack = { navController.popBackStack() }, goMenu = { navController.popBackStack(); navController.popBackStack() })
        }
    }
}

fun ProvideLocation() : LocationProvider
{
    return lp
}

fun ProvideImage() : String {
    return image.value
}