package br.senai.sp.informatica.mobile.bluetest;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class Teste extends AppCompatActivity {
        private static final int ENABLE_BLUETOOTH = 1;
        private static final int ENABLE_CONNECTION = 2;
        private static final int MESSAGE_READ = 3;

        ConnectedThread connectedThread;

        Handler mHandler;
        StringBuilder dataBluetooth = new StringBuilder();

        BluetoothAdapter myBluetoothAdapter = null;
        BluetoothDevice myBluetoothDevice = null;
        BluetoothSocket myBluetoothSocket = null;

        Button btnList;
        TextView txtlist;

        boolean connection = false;
        private static String MAC = null;

        private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            btnList = (Button) findViewById(R.id.btBuscar);
            txtlist = (TextView) findViewById(R.id.txtlist);

            myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            /**
             * Verificando se o aparelho possui compatibilidade com bluetooth
             */
            if (myBluetoothAdapter == null) {
                Toast.makeText(getApplicationContext(), "Seu dispositivo não suporta Bluetooth", Toast.LENGTH_LONG).show();
                finish();
            }

            /**
             * Verificando se o Bluetooth esta ativado
             */
            if (!myBluetoothAdapter.isEnabled()) {
                Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetoothIntent, ENABLE_BLUETOOTH);
            }

            btnList.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {

                    if (connection) {
                        try {
                            myBluetoothSocket.close();
                            connection = false;
                            btnList.setText("Listar Devices");
                            txtlist.setText("0");
                            Toast.makeText(getApplicationContext(), "A conexão Bluetooth foi desfeita", Toast.LENGTH_LONG).show();
                        } catch (IOException error) {
                            Toast.makeText(getApplicationContext(), "Ocorreu um erro" + error, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Intent showListDevice = new Intent(Teste.this, ListDevices.class);
                        startActivityForResult(showListDevice, ENABLE_CONNECTION);
                    }
                }
            });

            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg){
                    if(msg.what == MESSAGE_READ) {
                        String receive = (String) msg.obj;

                        dataBluetooth.append(receive);

                        int endMessage = dataBluetooth.indexOf("}");

                        if (endMessage > 0) {
                            String dataReceive = dataBluetooth.substring(0, endMessage);

                            int sizeData = dataReceive.length();

                            if (dataBluetooth.charAt(0) == '{') {
                                String finalDataReceive = dataBluetooth.substring(1, sizeData);
                                Log.d("Recebido", finalDataReceive);
                                txtlist.setText(finalDataReceive);
                            }
                            dataBluetooth.delete(0, dataBluetooth.length());
                        }
                    }
                }
            };
        }

        @Override
        protected void onActivityResult (int requestCode, int resultCode, Intent data) {

            switch (requestCode) {
                case ENABLE_BLUETOOTH:
                    if (resultCode == Activity.RESULT_OK) {
                        Toast.makeText(getApplicationContext(), "Bluetooth ativado com sucesso", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Bluetooth não foi ativado, o App será encerrado", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    break;
                case ENABLE_CONNECTION:
                    if (resultCode == Activity.RESULT_OK) {
                        MAC = data.getExtras().getString(ListDevices.MAC_ADDRESS);

                        myBluetoothDevice = myBluetoothAdapter.getRemoteDevice(MAC);

                        try {
                            myBluetoothSocket = myBluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);

                            myBluetoothSocket.connect();

                            connection = true;

                            connectedThread = new ConnectedThread(myBluetoothSocket);
                            connectedThread.start();

                            btnList.setText("Desconectar");

                            if (connection) {
                                connectedThread.write("Sensor");
                            }
                        } catch (IOException error) {
                            connection = false;
                            Toast.makeText(getApplicationContext(), "Falha ao estabelecer conexão com o Device: " + error, Toast.LENGTH_LONG).show();
                        }

                    } else {
                        MAC = null;
                        Toast.makeText(getApplicationContext(), "Falha ao obter o Mac", Toast.LENGTH_LONG).show();
                    }
            }
        }

        private class ConnectedThread extends Thread {
            private final InputStream mmInStream;
            private final OutputStream mmOutStream;

            public ConnectedThread(BluetoothSocket socket) {

                InputStream tmpIn = null;
                OutputStream tmpOut = null;

                // Get the input and output streams, using temp objects because
                // member streams are final
                try {
                    tmpIn = socket.getInputStream();
                    tmpOut = socket.getOutputStream();
                } catch (IOException e) { }

                mmInStream = tmpIn;
                mmOutStream = tmpOut;
            }

            public void run() {
                byte[] buffer = new byte[1024];  // buffer store for the stream
                int bytes; // bytes returned from read()

                // Keep listening to the InputStream until an exception occurs
                while (true) {
                    try {
                        // Read from the InputStream
                        bytes = mmInStream.read(buffer);

                        String data = new String(buffer, 0, bytes);

                        // Send the obtained bytes to the UI activity
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, data).sendToTarget();

                        connectedThread.write("Sensor");
                    } catch (IOException e) {

                    }
                }
            }

            /* Call this from the main activity to send data to the remote device */
            public void write(String input) {
                byte[] messageBuffer = input.getBytes();

                try {
                    mmOutStream.write(messageBuffer);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"Erro ao enviar mensagem",Toast.LENGTH_LONG).show();
                }
            }
        }
    }