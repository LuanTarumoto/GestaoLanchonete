package com.example.gestaolanchonete;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

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

        carregarProdutos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualiza a lista sempre que o usuário voltar para a tela inicial
        carregarProdutos();
    }

    private void carregarProdutos() {
        listaProdutos = db.lanchoneteDao().listarProdutos();

        // Busca a lista de categorias e monta um "dicionário" para cruzar os IDs com os Nomes
        List<com.example.gestaolanchonete.model.Categoria> listaCategorias = db.lanchoneteDao().listarCategorias();
        java.util.Map<Integer, String> mapaCategorias = new java.util.HashMap<>();
        for (com.example.gestaolanchonete.model.Categoria cat : listaCategorias) {
            mapaCategorias.put(cat.id, cat.nome);
        }

        // Envia os produtos e o mapa de categorias para o Adapter
        adapter = new ProdutoAdapter(this, listaProdutos, mapaCategorias);
        listaCatalogo.setAdapter(adapter);
    }

    // --- REQUISITO: Uso de Menu de Opções ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_adicionar) {
            startActivity(new android.content.Intent(this, CadastroProdutoActivity.class));
            return true;
        } else if (id == R.id.action_configuracoes) {
            startActivity(new android.content.Intent(this, ConfiguracoesActivity.class));
            return true;
        } else if (id == R.id.action_categorias) {
            startActivity(new android.content.Intent(this, GerenciarCategoriasActivity.class));
            return true;
        } else if (id == R.id.action_sobre) {
            startActivity(new android.content.Intent(this, SobreActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}