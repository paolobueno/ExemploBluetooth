package br.senai.sp.informatica.mobile.bluetest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;

public class MainActivity extends AppCompatActivity {
    private Button btBuscar;
    private Button btLigar;
    private Button btListar;
    private final Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btBuscar = (Button) findViewById(R.id.btBuscar);
        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, Teste.class);
                startActivity(i);
            }
        });

        btLigar = (Button) findViewById(R.id.btLigar);
        btLigar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, Teste.class);
                startActivity(i);
            }
        });
    }
}
