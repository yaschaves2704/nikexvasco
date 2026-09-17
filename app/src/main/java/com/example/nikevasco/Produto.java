package com.example.nikevasco;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Produto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_produto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button botaoInicio = findViewById(R.id.button9);
        botaoInicio.setOnClickListener(view -> {
            Intent intent = new Intent(Produto.this,
                    home.class);

            startActivity(intent);
        });
        ImageButton botaoCamisa = findViewById(R.id.imageButton2);
        botaoCamisa.setOnClickListener(view -> {
            Intent intent = new Intent(Produto.this,
                    Menu.class);

            startActivity(intent);
        });
    }
}