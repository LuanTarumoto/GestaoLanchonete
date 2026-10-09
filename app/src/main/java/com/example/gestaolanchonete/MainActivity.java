package com.example.gestaolanchonete;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gestaolanchonete.adapter.ProdutoAdapter;
import com.example.gestaolanchonete.database.AppDatabase;
import com.example.gestaolanchonete.model.Produto;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listaCatalogo;
    private AppDatabase db;
    private ProdutoAdapter adapter;
    private List<Produto> listaProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaCatalogo = findViewById(R.id.listaCatalogo);
        db = AppDatabase.getDatabase(this);

        configurarMenuContextual();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarProdutos();
    }

    private void carregarProdutos() {
        listaProdutos = db.lanchoneteDao().listarProdutos();

        List<com.example.gestaolanchonete.model.Categoria> listaCategorias = db.lanchoneteDao().listarCategorias();
        java.util.Map<Integer, String> mapaCategorias = new java.util.HashMap<>();
        for (com.example.gestaolanchonete.model.Categoria cat : listaCategorias) {
            mapaCategorias.put(cat.id, cat.nome);
        }

        adapter = new ProdutoAdapter(this, listaProdutos, mapaCategorias);
        listaCatalogo.setAdapter(adapter);
    }

    private void configurarMenuContextual() {
        listaCatalogo.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listaCatalogo.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                mode.setTitle(listaCatalogo.getCheckedItemCount() + " selecionado(s)");
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
                    if (listaCatalogo.getCheckedItemCount() == 1) {
                        for (int i = 0; i < listaProdutos.size(); i++) {
                            if (listaCatalogo.isItemChecked(i)) {
                                Produto prod = listaProdutos.get(i);
                                Intent intent = new Intent(MainActivity.this, CadastroProdutoActivity.class);
                                intent.putExtra("id", prod.id);
                                intent.putExtra("nome", prod.nome);
                                intent.putExtra("preco", prod.preco);
                                intent.putExtra("categoriaId", prod.categoriaId);
                                intent.putExtra("data", prod.dataCadastro != null ? prod.dataCadastro.toString() : "");
                                intent.putExtra("disponivel", prod.disponivel);
                                intent.putExtra("destaque", prod.destaque);
                                startActivity(intent);
                                break;
                            }
                        }
                        mode.finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Selecione apenas 1 produto para editar", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else if (item.getItemId() == R.id.action_deletar) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Excluir Produto")
                            .setMessage("Tem certeza? Esta ação removerá o produto permanentemente.")
                            .setPositiveButton("Sim", (dialog, which) -> {
                                for (int i = listaProdutos.size() - 1; i >= 0; i--) {
                                    if (listaCatalogo.isItemChecked(i)) {
                                        db.lanchoneteDao().deletarProduto(listaProdutos.get(i));
                                    }
                                }
                                mode.finish();
                                carregarProdutos();
                                Toast.makeText(MainActivity.this, "Produto excluído!", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_adicionar) {
            startActivity(new Intent(this, CadastroProdutoActivity.class));
            return true;
        } else if (id == R.id.action_configuracoes) {
            startActivity(new Intent(this, ConfiguracoesActivity.class));
            return true;
        } else if (id == R.id.action_categorias) {
            startActivity(new Intent(this, GerenciarCategoriasActivity.class));
            return true;
        } else if (id == R.id.action_sobre) {
            startActivity(new Intent(this, SobreActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}