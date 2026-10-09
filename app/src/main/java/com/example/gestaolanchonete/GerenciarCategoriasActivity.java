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
        // Utilizando o nosso novo layout escuro/claro
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_categoria, nomes);
        listaCategorias.setAdapter(adapter);
    }

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
                if (item.getItemId() == R.id.action_editar) {
                    if (listaCategorias.getCheckedItemCount() == 1) {
                        for (int i = 0; i < categorias.size(); i++) {
                            if (listaCategorias.isItemChecked(i)) {
                                Intent intent = new Intent(GerenciarCategoriasActivity.this, NovaCategoriaActivity.class);
                                intent.putExtra("id", categorias.get(i).id);
                                intent.putExtra("nome", categorias.get(i).nome);
                                startActivity(intent);
                                break;
                            }
                        }
                        mode.finish();
                    } else {
                        Toast.makeText(GerenciarCategoriasActivity.this, "Selecione apenas 1 para editar", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else if (item.getItemId() == R.id.action_deletar) {
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
                            .setNegativeButton("Não", null).show();
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