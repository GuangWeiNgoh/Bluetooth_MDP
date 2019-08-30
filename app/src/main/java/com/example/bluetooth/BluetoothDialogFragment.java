package com.example.bluetooth;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class BluetoothDialogFragment extends DialogFragment {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    TextView mStatusBlueTv, mPairedTv;
    ImageView mBlueTv;
    Button mOnBtn, mOffBtn, mDiscoverBtn, mPairedBtn;

    BluetoothAdapter mBlueAdapter;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View bluetoothView = inflater.inflate(R.layout.bluetooth_popup, null);
        builder.setView(bluetoothView);

        mStatusBlueTv = bluetoothView.findViewById(R.id.statusBluetoothTv);
        mPairedTv = bluetoothView.findViewById(R.id.pairedTv);
        mBlueTv = bluetoothView.findViewById(R.id.bluetoothTv);
        mOnBtn = bluetoothView.findViewById(R.id.onBtn);
        mOffBtn = bluetoothView.findViewById(R.id.offBtn);
        mDiscoverBtn = bluetoothView.findViewById(R.id.discoverableBtn);
        mPairedBtn = bluetoothView.findViewById(R.id.pairedBtn);

        //check if bluetooth is available or not
        if (mBlueAdapter == null) {
            mStatusBlueTv.setText("BlueTooth is not available");
        } else {
            mStatusBlueTv.setText("BlueTooth is available");
        }


        // set image according to bluetooth status on/off
        if (mBlueAdapter.isEnabled()) {
            mBlueTv.setImageResource(R.drawable.ic_action_on);
        } else {
            mBlueTv.setImageResource(R.drawable.ic_action_off);
        }

        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mBlueAdapter.isEnabled()){
                    Toast.makeText(getActivity(),"Turning on BlueTooth...",Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent,REQUEST_ENABLE_BT);
                }

                else{
                    Toast.makeText(getActivity(),"BlueTooth is already on",Toast.LENGTH_LONG).show();
                }

            }
        });

        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBlueAdapter.isEnabled()){
                    mBlueAdapter.disable();
                    Toast.makeText(getActivity(),"Turning off BlueTooth",Toast.LENGTH_LONG).show();
                    mBlueTv.setImageResource(R.drawable.ic_action_off);
                }

                else{
                    Toast.makeText(getActivity(),"BlueTooth is already off",Toast.LENGTH_LONG).show();
                }

            }
        });

        mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mBlueAdapter.isDiscovering()){
                    Toast.makeText(getActivity(),"Making your device discoverable",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
                }


            }
        });

        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBlueAdapter.isEnabled()){
                    mPairedTv.setText("Paired Devices");
                    Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
                    for( BluetoothDevice device: devices){
                        mPairedTv.append("\nDevice: " + device.getName() + "," + device);
                    }
                }

                else{
                    //bluetooth is off so cannot connect to paired device
                    Toast.makeText(getActivity(),"Turn on BlueTooth to get paried devices",Toast.LENGTH_LONG).show();
                }

            }
        });


        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    //bluetooth is on
                    mBlueTv.setImageResource(R.drawable.ic_action_on);
                    Toast.makeText(getActivity(),"BlueTooth is on",Toast.LENGTH_LONG).show();
                }

                else{
                    //user denied to turn bluetooth on
                    Toast.makeText(getActivity(),"Couldn't on BlueTooth",Toast.LENGTH_LONG).show();
                }

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
