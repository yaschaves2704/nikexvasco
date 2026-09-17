package com.example.nikevasco;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.Map;

public class Local extends AppCompatActivity {

    private TextView textGps;

    // ADICIONADO
    private WebView webViewMapa;

    private FusedLocationProviderClient fusedLocationClient;
    private ActivityResultLauncher<String[]> localizacaoLauncher;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_local);

        // =====================================================
        // ADICIONADO: CARREGA O mapa.html E HABILITA JAVASCRIPT
        // =====================================================

        webViewMapa = findViewById(R.id.webViewMapa);

        WebSettings webSettings = webViewMapa.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        // Captura erros/mensagens do JavaScript do mapa.html
        webViewMapa.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onConsoleMessage(
                    ConsoleMessage consoleMessage) {

                Log.d(
                        "MapaJS",
                        consoleMessage.message()
                                + " -- linha "
                                + consoleMessage.lineNumber()
                );

                return true;
            }
        });

        // Carrega o mapa.html
        webViewMapa.loadUrl(
                "file:///android_asset/mapa.html"
        );


        // =====================================================
        // SEU CÓDIGO ORIGINAL
        // =====================================================

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
                            );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        Button botaoVoltar = findViewById(R.id.button14);
        botaoVoltar.setOnClickListener(view -> {
            Intent intent = new Intent(Local.this,
                    home.class);

            startActivity(intent);
        });

        // =====================================================
        // LOCALIZAÇÃO - SEU CÓDIGO ORIGINAL
        // =====================================================

        textGps = findViewById(R.id.textGps);

        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);


        // =====================================================
        // LOCATION CALLBACK
        // =====================================================
        // Aqui foi ADICIONADO o evaluateJavascript()
        // =====================================================

        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(
                    LocationResult locationResult) {

                if (locationResult == null) {
                    return;
                }

                for (android.location.Location location :
                        locationResult.getLocations()) {

                    double lat = location.getLatitude();

                    double lng = location.getLongitude();

                    // Mostra latitude e longitude na tela
                    textGps.setText(
                            "Lat: " + lat
                                    + " | Long: " + lng
                    );


                    // =================================================
                    // ADICIONADO:
                    // Chama updateLocation() dentro do mapa.html
                    // =================================================

                    webViewMapa.evaluateJavascript(
                            "updateLocation("
                                    + lat
                                    + ", "
                                    + lng
                                    + ")",
                            null
                    );
                }

                // Para de escutar novas atualizações
                fusedLocationClient.removeLocationUpdates(
                        locationCallback
                );
            }
        };


        // =====================================================
        // PERMISSÃO DE LOCALIZAÇÃO - SEU CÓDIGO ORIGINAL
        // =====================================================

        localizacaoLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),

                (Map<String, Boolean> resultado) -> {

                    Boolean fineConcedida =
                            resultado.get(
                                    Manifest.permission
                                            .ACCESS_FINE_LOCATION
                            );

                    Boolean coarseConcedida =
                            resultado.get(
                                    Manifest.permission
                                            .ACCESS_COARSE_LOCATION
                            );

                    // Aceita se qualquer uma das permissões
                    // foi concedida
                    if (Boolean.TRUE.equals(fineConcedida)
                            || Boolean.TRUE.equals(coarseConcedida)) {

                        solicitarAtualizacaoLocalizacao();

                    } else {

                        textGps.setText(
                                "Permissão de localização negada"
                        );
                    }
                }
        );


        // =====================================================
        // BOTÃO PEGAR LOCALIZAÇÃO - SEU CÓDIGO ORIGINAL
        // =====================================================

        Button botaoLocalizacao =
                findViewById(
                        R.id.btnPegarLocalizacao15
                );

        botaoLocalizacao.setOnClickListener(view -> {

            // Verifica se a permissão já foi concedida
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {

                solicitarAtualizacaoLocalizacao();

            } else {

                // Ainda não tem permissão:
                // dispara o popup de solicitação
                localizacaoLauncher.launch(
                        new String[]{
                                Manifest.permission
                                        .ACCESS_FINE_LOCATION,

                                Manifest.permission
                                        .ACCESS_COARSE_LOCATION
                        }
                );
            }
        });
    }


    // =========================================================
    // SOLICITA UMA ÚNICA ATUALIZAÇÃO DE LOCALIZAÇÃO
    // =========================================================

    private void solicitarAtualizacaoLocalizacao() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        textGps.setText(
                "Buscando localização..."
        );


        // Alta precisão, buscando a cada 2 segundos
        LocationRequest locationRequest =
                new LocationRequest.Builder(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        2000
                )
                        .setMaxUpdates(1)
                        .build();


        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
        );
    }
}
