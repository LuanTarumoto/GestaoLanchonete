package com.example.gestaolanchonete;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gestaolanchonete.database.AppDatabase;
import com.example.gestaolanchonete.model.Categoria;

public class NovaCategoriaActivity extends AppCompatActivity {
    private EditText editNomeCategoria;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_categoria);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Nova Categoria");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editNomeCategoria = findViewById(R.id.editNomeCategoria);
        db = AppDatabase.getDatabase(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro, menu); // Reutiliza o botão de salvar do produto
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_salvar) {
            String nome = editNomeCategoria.getText().toString();
            if (nome.isEmpty()) {
                Toast.makeText(this, "Preencha o nome da categoria!", Toast.LENGTH_SHORT).show();
                return true;
            }
            db.lanchoneteDao().inserirCategoria(new Categoria(nome));
            Toast.makeText(this, "Categoria salva!", Toast.LENGTH_SHORT).show();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}