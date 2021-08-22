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
import com.khmelenko.lab.miband.listeners.RealtimeStepsNotifyListener
import java.util.*

class HomeFragment : Fragment(), MiBandsListAdapter.BluetoothDeviceClickInterface, LocationListener,
        MiBand.BatteryLevelInterface
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
                deviceAdapter.setList(ArrayList<BluetoothDevice>())
            }
            else
                getBluetoothDevices()
        }

        onClickListeners()
    }

    private fun onClickListeners()
    {
        viewBinding.currentStateTextView.setOnClickListener {
                startScanningForDevice()
        }

        viewBinding.btSwitch.setOnClickListener{
            if(viewBinding.btSwitch.isChecked) {
                turnOnBluetooth()
                viewBinding.btTextMsg.text = getString(R.string.bluetoothIsTurnedOnText)
                viewBinding.btIconState.alpha = 1f
            }
        }

        viewBinding.readBattery.setOnClickListener{
            readBatteryLevel()
        }

        viewBinding.getSteps.setOnClickListener{
            getStepsValue()
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
        viewBinding.scanAndConnectHolder.visibility = View.VISIBLE
        scanningCountDownTimer = object : CountDownTimer(30000, 1000)
        {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                resetScan()
                updateStateText(getString(R.string.scanningFinishedText))
                setProgressIndeterminate(false)
            }
        }.start()

        setProgressIndeterminate(true)

        updateStateText(getString(R.string.scanningInProgressText))
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
        val d = miBand.connect(device).subscribe( { connected ->
                if (connected) {
                    viewBinding.miBandBatteryLevelCardView.post {
                        viewBinding.miBandHolder.visibility = View.VISIBLE
                    }
                    viewBinding.progressIndicator.post {
                        viewBinding.progressIndicator.visibility = View.GONE
                    }
                    updateStateText(getString(R.string.connectionSuccessfulText))
                    readBatteryLevel()
                } else {
                    connectionError()
                }
            },
            {
                connectionError()
            })
    }

    private fun getStepsValue() {

        miBand.setRealtimeStepsNotifyListener(object : RealtimeStepsNotifyListener {
            override fun onNotify(steps: Int) {
                viewBinding.stepCounterCurrentText.post {
                    viewBinding.stepCounterCurrentText.text = steps.toString()
                }
            }
        })

        val d = miBand.enableRealtimeStepsNotify().subscribe({
            viewBinding.stepsStatusTextView.post{
                viewBinding.stepsStatusTextView.text = if(it) "TRUE" else "FALSE"
            }
        },
            {
                viewBinding.stepsStatusTextView.post{
                    viewBinding.stepsStatusTextView.text = it.message
                }
            })

    }

    private fun readBatteryLevel() {
        val d = miBand.batteryInfo
            .subscribe({ batteryInfo -> updateStateText("battery info = ${batteryInfo.toString()}") },
                { throwable ->
                    if(throwable.message != null)
                        updateStateText(throwable.message!!)
                })
    }

    private fun connectionError()
    {
        setProgressIndeterminate(false)
        updateStateText(getString(R.string.connectionErrorText))
        viewBinding.bluetoothDevicesRV.post {
            viewBinding.bluetoothDevicesRV.visibility = View.VISIBLE
        }
    }

    private fun updateStateText(text: String)
    {
        viewBinding.currentStateTextView.post {
            viewBinding.currentStateTextView.text = text
        }
    }

    private fun setProgressIndeterminate(isIndeterminate: Boolean)
    {
        viewBinding.progressIndicator.post {
            viewBinding.progressIndicator.visibility = View.GONE
            viewBinding.progressIndicator.isIndeterminate = isIndeterminate
            viewBinding.progressIndicator.visibility = View.VISIBLE
        }
    }

    private fun resetScan()
    {
        homeViewModel.isScanRunning = false
    }

    override fun bluetoothDeviceOnClick(bluetoothDeviceItem: BluetoothDevice) {
        setProgressIndeterminate(true)
        connectDevice(bluetoothDeviceItem)
        if(scanningCountDownTimer != null)
            scanningCountDownTimer?.cancel()
        viewBinding.bluetoothDevicesRV.visibility = View.GONE
        updateStateText(getString(R.string.connectingInProgressText))
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
        miBand = MiBand(requireContext().applicationContext)
        miBand.setBatteryLevelInterface(this)
    }

    override fun onDestroy() {
        miBand.stopScan()
        if(scanningCountDownTimer != null)
            scanningCountDownTimer?.cancel()
        super.onDestroy()
    }

    override fun batteryLevelReadCompleted(batteryValue: Int) {
        viewBinding.miBandBatteryLevelCurrentText.post {
            viewBinding.miBandBatteryLevelCurrentText.text = "$batteryValue%"
        }

        getStepsValue()

    }
}