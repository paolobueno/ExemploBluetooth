//package br.senai.sp.informatica.mobile.bluetest;
//
//import android.app.Activity;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BlueActivity extends Activity {
//
//    public BluetoothAdapter bluetoothAdapter;
//
//    public BlueActivity() {
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        if (bluetoothAdapter == null) {
//            Toast.makeText(this, "Bluetooth não disponível neste aparelho", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (bluetoothAdapter.isEnabled()) {
//            Toast.makeText(this, "Bluetooth está ligado", Toast.LENGTH_SHORT).show();
//        } else {
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, 0);
//        }
//
//        finish();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode != Activity.RESULT_OK) {
//            Toast.makeText(this, "O bluetooth não foi ativado,", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//}