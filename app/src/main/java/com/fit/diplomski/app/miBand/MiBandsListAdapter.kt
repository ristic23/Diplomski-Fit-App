package com.fit.diplomski.app.miBand

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fit.diplomski.app.databinding.DeviceListItemBinding
import java.util.ArrayList

class MiBandsListAdapter(private var dataSet: ArrayList<BluetoothDevice>) : RecyclerView.Adapter<MiBandsListAdapter.ViewHolder>()
{
    interface BluetoothDeviceClickInterface
    {
        fun bluetoothDeviceOnClick(bluetoothDeviceItem: BluetoothDevice)
    }

    private lateinit var bluetoothDeviceClickInterface: BluetoothDeviceClickInterface

    fun setBluetoothDeviceClickInterface(bluetoothDeviceClickInterface: BluetoothDeviceClickInterface)
    {
        this.bluetoothDeviceClickInterface = bluetoothDeviceClickInterface
    }

    public fun setList(dataSet: ArrayList<BluetoothDevice>)
    {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    public fun getList() :ArrayList<BluetoothDevice> = dataSet

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DeviceListItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false));
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val btDataItem = dataSet[position]

        viewHolder.itemBinding.titleTextView.text = btDataItem.name
        viewHolder.itemBinding.macAddress.text = btDataItem.address

        viewHolder.itemBinding.root.setOnClickListener {
            if(bluetoothDeviceClickInterface != null)
                bluetoothDeviceClickInterface.bluetoothDeviceOnClick(dataSet[position])
        }
    }

    override fun getItemCount() = dataSet.size


    class ViewHolder(val itemBinding: DeviceListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)
}