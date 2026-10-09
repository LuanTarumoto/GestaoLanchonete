package com.example.gestaolanchonete;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class ConfiguracoesActivity extends AppCompatActivity {

    private Switch switchSugestao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Configurações");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        switchSugestao = findViewById(R.id.switchSugestao);

        // REQUISITO: SharedPreferences
        SharedPreferences prefs = getSharedPreferences("PrefsLanchonete", MODE_PRIVATE);
        boolean lembrarCategoria = prefs.getBoolean("lembrar_categoria", false);
        switchSugestao.setChecked(lembrarCategoria);

        switchSugestao.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("lembrar_categoria", isChecked);
            editor.apply();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}