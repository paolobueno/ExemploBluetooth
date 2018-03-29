package br.senai.sp.informatica.mobile.bluetest;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class ListDevices extends ListActivity  {


        BluetoothAdapter myBluetoothAdapter = null;

        static String MAC_ADDRESS = null;

        @Override
        protected  void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            ArrayAdapter<String> ArrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

            myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            Set<BluetoothDevice> devicesPaireds = myBluetoothAdapter.getBondedDevices();

            if (devicesPaireds.size() > 0) {
                for(BluetoothDevice device : devicesPaireds) {
                    String nameBluetooth = device.getName();
                    String macBluetooth = device.getAddress();
                    ArrayBluetooth.add(nameBluetooth + "\n" + macBluetooth);
                }
            }

            setListAdapter(ArrayBluetooth);
        }

        @Override
        protected void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);

            String generalInformation = ((TextView) v).getText().toString();

            //Toast.makeText(getApplicationContext(), "Info: " + generalInformation, Toast.LENGTH_LONG).show();

            String macAddres = generalInformation.substring(generalInformation.length() - 17);

            //Toast.makeText(getApplicationContext(), "Info: " + addresMac, Toast.LENGTH_LONG).show();

            Intent returnMac = new Intent();
            returnMac.putExtra(MAC_ADDRESS, macAddres);

            setResult(RESULT_OK, returnMac);
            finish();
        }
    }