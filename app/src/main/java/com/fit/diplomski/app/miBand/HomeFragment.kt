package com.fit.diplomski.app.miBand

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fit.diplomski.app.ApplicationClass
import com.fit.diplomski.app.R
import com.fit.diplomski.app.databinding.FragmentHomeBinding
import com.khmelenko.lab.miband.MiBand
import java.util.*

class HomeFragment : Fragment(), MiBandsListAdapter.BluetoothDeviceClickInterface, LocationListener
{
    private var homeBinding: FragmentHomeBinding? = null
    private val viewBinding get() = homeBinding!!

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory((requireContext().applicationContext as ApplicationClass).repository)
    }

    private var scanningCountDownTimer: CountDownTimer? = null

    private var btAdapter: BluetoothAdapter? = null

    private lateinit var miBand: MiBand

    private lateinit var locationManager: LocationManager
    private val provider: String = LocationManager.GPS_PROVIDER

    private lateinit var mActivity: AppCompatActivity
    private lateinit var mContext: Context
    private lateinit var deviceAdapter : MiBandsListAdapter

    private lateinit var device: BluetoothDevice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(activity is AppCompatActivity)
            mActivity = activity as AppCompatActivity

        locationManager = mActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        deviceAdapter = MiBandsListAdapter(ArrayList())
        deviceAdapter.setBluetoothDeviceClickInterface(this)

        btAdapter = BluetoothAdapter.getDefaultAdapter()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (btAdapter == null) {
            viewBinding.btIconState.setImageResource(android.R.drawable.stat_notify_error)
            viewBinding.btTextMsg.text = getString(R.string.yourPhoneDoesNotSupportBluetoothText)
            viewBinding.btSwitch.visibility = View.GONE
            viewBinding.miBandHolder.visibility = View.GONE
        }
        else
        {
            viewBinding.bluetoothDevicesRV.apply {
                adapter = deviceAdapter
                layoutManager = LinearLayoutManager(mActivity)
            }

            viewBinding.btSwitch.isChecked = btAdapter?.isEnabled == true
            if (!viewBinding.btSwitch.isChecked)
            {
                viewBinding.btTextMsg.text = getString(R.string.bluetoothIsTurnedOffText)
                viewBinding.btIconState.alpha = 0.5f
                viewBinding.scanCardView.visibility = View.GONE
                deviceAdapter.setList(ArrayList<BluetoothDevice>())
            }
            else
                getBluetoothDevices()
        }

        onClickListeners()
    }

    private fun onClickListeners()
    {
        viewBinding.pairCardView.setOnClickListener {
//            pairDevice()
            val d = miBand.batteryInfo
                .subscribe({ batteryInfo -> viewBinding.scanTextMsg.text = "battery info = ${batteryInfo.toString()}" },
                    { throwable -> viewBinding.scanTextMsg.text = throwable.message })
        }

        viewBinding.scanCardView.setOnClickListener {
                startScanningForDevice()
        }

        viewBinding.btSwitch.setOnClickListener{
            if(viewBinding.btSwitch.isChecked) {
                turnOnBluetooth()
                viewBinding.btTextMsg.text = getString(R.string.bluetoothIsTurnedOnText)
                viewBinding.btIconState.alpha = 1f
                viewBinding.scanCardView.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(mActivity, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(provider, 400, 1f, this)
        }
        else
            checkLocationPermission()
    }

    override fun onPause() {
        super.onPause()
        if (ContextCompat.checkSelfPermission(mActivity, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(this)
        }
    }

    private fun startScanningForDevice() {
        if(homeViewModel.isScanRunning)
            return
        homeViewModel.isScanRunning = true
        homeViewModel.scanProgress = 0
        updateScanProgress()
        val seconds = 60L
        val duration = seconds * 1000
        viewBinding.habitProgressBar.max = seconds.toInt()
        scanningCountDownTimer = object : CountDownTimer(duration, 1000)
        {
            override fun onTick(millisUntilFinished: Long) {
                updateScanProgress()
            }

            override fun onFinish() {
                if(homeViewModel.isScanRunning)
                {
                    miBand.stopScan()
                    viewBinding.scanTextMsg.text = getString(R.string.tapToScanText)
                }
                resetScan()
                viewBinding.scanTextMsg.text = getString(R.string.scanningFinishedText)
            }
        }.start()
        viewBinding.scanTextMsg.text = getString(R.string.scanningInProgressText)
        val d = miBand.startScan().subscribe { result ->
            val tempList = ArrayList(deviceAdapter.getList())
            tempList.add(result.device)
            deviceAdapter.setList(tempList)
        }
    }

    private fun connectDevice(pickedDevice: BluetoothDevice)
    {
        device = pickedDevice
        miBand.stopScan()
        val d = miBand.connect(device).subscribe(
            { connected ->
                if (connected) {
                    viewBinding.pairCardView.post {
                        viewBinding.pairCardView.visibility = View.VISIBLE
                    }
                    Toast.makeText(
                        mActivity,
                        getString(R.string.connectionSuccessfulText),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        mActivity,
                        getString(R.string.connectionErrorText),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            {
                it.printStackTrace()
                Toast.makeText(
                    mActivity,
                    getString(R.string.connectionErrorText),
                    Toast.LENGTH_SHORT
                ).show()
            })
    }

    //todo 21.08.2021. vidi napravi view u gornjem uglu koji ima da oznacava stanje aplikacije i mi band narukvice

    private fun updateScanProgress()
    {
        homeViewModel.scanProgress++
        viewBinding.habitProgressBar.progress = homeViewModel.scanProgress
        Log.v("ScanningProgressLog", "Progress = ${viewBinding.habitProgressBar.progress}")
    }

    private fun resetScan()
    {
        homeViewModel.isScanRunning = false
        homeViewModel.scanProgress = -1
        updateScanProgress()
    }

    override fun bluetoothDeviceOnClick(bluetoothDeviceItem: BluetoothDevice) {
        connectDevice(bluetoothDeviceItem)
        viewBinding.scanCardView.visibility = View.GONE
        viewBinding.bluetoothDevicesRV.visibility = View.GONE
        Toast.makeText(mActivity, getString(R.string.connectingInProgressText), Toast.LENGTH_SHORT).show()
    }

    //region Bluetooth

    private fun getBluetoothDevices()
    {
        viewBinding.bluetoothCardView.visibility = View.GONE
        val deviceItemList = ArrayList<BluetoothDevice>()
        val pairedDevices = btAdapter?.bondedDevices
        if (pairedDevices?.size!! > 0) {
            for (device in pairedDevices) {
                deviceItemList.add(device)
            }
        }
    }

    private var bluetoothOnResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            getBluetoothDevices()
        }
    }

    private fun turnOnBluetooth() {
        val enableBT = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        bluetoothOnResult.launch(enableBT)
    }

    //endregion

    //region Location

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mActivity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(mActivity)
                    .setTitle("Location permission")
                    .setMessage("Mi Band needs to access your location in order to continue working.")
                    .setPositiveButton("OK") { _, i ->
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(mActivity,
                            arrayOf(ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_LOCATION
                        )
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(mActivity, arrayOf(ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_LOCATION)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mActivity, "Location permission granted", Toast.LENGTH_SHORT).show()
                    locationManager.requestLocationUpdates(provider, 400, 1f, this)
                } else {
                    Toast.makeText(mActivity, "Location permission denied. Please grant location permission.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        val lat = location.latitude
        val lng = location.longitude

    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        // do nothing
    }

    override fun onProviderEnabled(provider: String) {
        // do nothing
    }

    override fun onProviderDisabled(provider: String) {
        // do nothing
    }

    companion object {
        const val PERMISSIONS_REQUEST_LOCATION = 99
    }

    //endregion

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        miBand = MiBand(requireContext().applicationContext) {
            viewBinding.scanTextMsg.text = "battery info = $it"
        }
    }

    override fun onDestroy() {
        miBand.stopScan()
        if(scanningCountDownTimer != null)
            scanningCountDownTimer?.cancel()
        super.onDestroy()
    }
}