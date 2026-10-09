package com.example.gestaolanchonete;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gestaolanchonete.database.AppDatabase;
import com.example.gestaolanchonete.model.Categoria;
import com.example.gestaolanchonete.model.Produto;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CadastroProdutoActivity extends AppCompatActivity {

    private EditText editNome, editPreco;
    private Spinner spinnerCategoria;
    private Button btnData;
    private TextView textDataSelecionada;
    private RadioButton radioDisponivel, radioEsgotado;
    private CheckBox checkDestaque;

    private LocalDate dataSelecionada;
    private AppDatabase db;
    private List<Categoria> categorias;
    private int produtoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);

        editNome = findViewById(R.id.editNomeProduto);
        editPreco = findViewById(R.id.editPrecoProduto);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnData = findViewById(R.id.btnSelecionarData);
        textDataSelecionada = findViewById(R.id.textDataSelecionada);
        radioDisponivel = findViewById(R.id.radioDisponivel);
        radioEsgotado = findViewById(R.id.radioEsgotado);
        checkDestaque = findViewById(R.id.checkDestaque);

        db = AppDatabase.getDatabase(this);
        carregarCategorias();

        dataSelecionada = LocalDate.now();

        // Verifica se é uma edição de produto
        if (getIntent().hasExtra("id")) {
            produtoId = getIntent().getIntExtra("id", -1);
            editNome.setText(getIntent().getStringExtra("nome"));
            editPreco.setText(String.valueOf(getIntent().getDoubleExtra("preco", 0.0)));

            int catId = getIntent().getIntExtra("categoriaId", -1);
            for (int i = 0; i < categorias.size(); i++) {
                if (categorias.get(i).id == catId) spinnerCategoria.setSelection(i);
            }

            String dataStr = getIntent().getStringExtra("data");
            if (dataStr != null && !dataStr.isEmpty()) dataSelecionada = LocalDate.parse(dataStr);

            if (getIntent().getBooleanExtra("disponivel", true)) radioDisponivel.setChecked(true);
            else radioEsgotado.setChecked(true);

            checkDestaque.setChecked(getIntent().getBooleanExtra("destaque", false));
        }

        textDataSelecionada.setText(dataSelecionada.getDayOfMonth() + "/" + dataSelecionada.getMonthValue() + "/" + dataSelecionada.getYear());

        btnData.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                dataSelecionada = LocalDate.of(year, month + 1, dayOfMonth);
                textDataSelecionada.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(produtoId == -1 ? "Cadastro de Produto" : "Editar Produto");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void carregarCategorias() {
        categorias = db.lanchoneteDao().listarCategorias();
        if (categorias.isEmpty()) {
            Toast.makeText(this, "Aviso: Cadastre uma categoria antes de adicionar um produto!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        List<String> nomesCategorias = new ArrayList<>();
        for (Categoria cat : categorias) nomesCategorias.add(cat.nome);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nomesCategorias);
        spinnerCategoria.setAdapter(adapter);

        // Sistema de SharedPreferences (Lembrar categoria)
        if (produtoId == -1) {
            SharedPreferences prefs = getSharedPreferences("PrefsLanchonete", MODE_PRIVATE);
            if (prefs.getBoolean("lembrar_categoria", false)) {
                int ultimaCat = prefs.getInt("ultima_categoria_id", -1);
                for (int i = 0; i < categorias.size(); i++) {
                    if (categorias.get(i).id == ultimaCat) spinnerCategoria.setSelection(i);
                }
            }
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
            salvarProduto();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void salvarProduto() {
        String nome = editNome.getText().toString();
        String precoStr = editPreco.getText().toString();

        if (nome.isEmpty() || precoStr.isEmpty()) {
            Toast.makeText(this, "Preencha o nome e o preço!", Toast.LENGTH_SHORT).show();
            return;
        }

        double preco = Double.parseDouble(precoStr);
        int categoriaId = categorias.get(spinnerCategoria.getSelectedItemPosition()).id;
        boolean disponivel = radioDisponivel.isChecked();
        boolean destaque = checkDestaque.isChecked();

        Produto novoProduto = new Produto(nome, preco, categoriaId, dataSelecionada, disponivel, destaque);

        if (produtoId != -1) {
            novoProduto.id = produtoId;
            db.lanchoneteDao().atualizarProduto(novoProduto);
            Toast.makeText(this, "Produto atualizado!", Toast.LENGTH_SHORT).show();
        } else {
            db.lanchoneteDao().inserirProduto(novoProduto);
            Toast.makeText(this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show();
        }

        // Salva a preferência
        getSharedPreferences("PrefsLanchonete", MODE_PRIVATE).edit().putInt("ultima_categoria_id", categoriaId).apply();
        finish();
    }
}