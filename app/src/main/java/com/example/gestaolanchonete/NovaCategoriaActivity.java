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
    private int categoriaId = -1; // -1 significa que é um novo cadastro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_categoria);

        editNomeCategoria = findViewById(R.id.editNomeCategoria);
        db = AppDatabase.getDatabase(this);

        // Verifica se veio uma ordem de EDIÇÃO
        if (getIntent().hasExtra("id")) {
            categoriaId = getIntent().getIntExtra("id", -1);
            editNomeCategoria.setText(getIntent().getStringExtra("nome"));
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(categoriaId == -1 ? "Nova Categoria" : "Editar Categoria");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
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

            Categoria cat = new Categoria(nome);
            if (categoriaId != -1) {
                cat.id = categoriaId;
                db.lanchoneteDao().atualizarCategoria(cat);
                Toast.makeText(this, "Categoria atualizada!", Toast.LENGTH_SHORT).show();
            } else {
                db.lanchoneteDao().inserirCategoria(cat);
                Toast.makeText(this, "Categoria salva!", Toast.LENGTH_SHORT).show();
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}