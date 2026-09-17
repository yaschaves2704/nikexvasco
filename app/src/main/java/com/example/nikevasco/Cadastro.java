package com.example.nikevasco;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Cadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button botaoEntrar = findViewById(R.id.button4);
        botaoEntrar.setOnClickListener(view -> {
            Intent intent = new Intent(Cadastro.this,
                    home.class);

            startActivity(intent);
        });
        Button botaoCadastrase = findViewById(R.id.button6);
        botaoCadastrase.setOnClickListener(view -> {
            Intent intent = new Intent(Cadastro.this,
                    cadastro2.class);

            startActivity(intent);
        });
    }
}