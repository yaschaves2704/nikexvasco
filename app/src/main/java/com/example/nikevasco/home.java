package com.example.nikevasco;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button botaoColecao = findViewById(R.id.button2);
        botaoColecao.setOnClickListener(view -> {
            Intent intent = new Intent(home.this,
                    Menu.class);

            startActivity(intent);
        });
        Button botaoCamisa = findViewById(R.id.button8);
        botaoCamisa.setOnClickListener(view -> {
            Intent intent = new Intent(home.this,
                    Produto.class);

            startActivity(intent);
        });
        Button botaoEncontrar = findViewById(R.id.button13);
        botaoEncontrar.setOnClickListener(view -> {
            Intent intent = new Intent(home.this,
                    Local.class);

            startActivity(intent);
        });

    }
}