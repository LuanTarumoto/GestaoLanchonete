package com.example.gestaolanchonete;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gestaolanchonete.database.AppDatabase;
import com.example.gestaolanchonete.model.Categoria;
import java.util.List;

public class GerenciarCategoriasActivity extends AppCompatActivity {
    private ListView listaCategorias;
    private AppDatabase db;
    private List<Categoria> categorias;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_categorias);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Lista de Categorias");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listaCategorias = findViewById(R.id.listaCategorias);
        db = AppDatabase.getDatabase(this);

        configurarMenuContextual();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarCategorias();
    }

    private void carregarCategorias() {
        categorias = db.lanchoneteDao().listarCategorias();
        String[] nomes = new String[categorias.size()];
        for (int i = 0; i < categorias.size(); i++) {
            nomes[i] = categorias.get(i).nome;
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nomes);
        listaCategorias.setAdapter(adapter);
    }

    // REQUISITOS: Menu de Ação Contextual (Segurar para apagar) e AlertDialog
    private void configurarMenuContextual() {
        listaCategorias.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listaCategorias.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                mode.setTitle(listaCategorias.getCheckedItemCount() + " selecionado(s)");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_contextual, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.action_deletar) {
                    new AlertDialog.Builder(GerenciarCategoriasActivity.this)
                            .setTitle("Excluir Categoria")
                            .setMessage("Tem certeza? Esta ação removerá a categoria do banco de dados.")
                            .setPositiveButton("Sim", (dialog, which) -> {
                                for (int i = categorias.size() - 1; i >= 0; i--) {
                                    if (listaCategorias.isItemChecked(i)) {
                                        db.lanchoneteDao().deletarCategoria(categorias.get(i));
                                    }
                                }
                                mode.finish();
                                carregarCategorias();
                                Toast.makeText(GerenciarCategoriasActivity.this, "Excluído com sucesso", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Não", null)
                            .show();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) { }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gerenciar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_nova_categoria) {
            startActivity(new Intent(this, NovaCategoriaActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}