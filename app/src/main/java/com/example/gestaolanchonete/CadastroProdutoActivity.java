package com.example.gestaolanchonete;

import android.app.DatePickerDialog;
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
    private RadioButton radioDisponivel;
    private CheckBox checkDestaque;

    private LocalDate dataSelecionada;
    private AppDatabase db;
    private List<Categoria> categorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_produto);

        // REQUISITO: Botão Up (Setinha de voltar) na Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Cadastro de Produto");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editNome = findViewById(R.id.editNomeProduto);
        editPreco = findViewById(R.id.editPrecoProduto);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnData = findViewById(R.id.btnSelecionarData);
        textDataSelecionada = findViewById(R.id.textDataSelecionada);
        radioDisponivel = findViewById(R.id.radioDisponivel);
        checkDestaque = findViewById(R.id.checkDestaque);

        db = AppDatabase.getDatabase(this);
        carregarCategorias();

        // REQUISITO: Manipulação do DatePicker e LocalDate
        dataSelecionada = LocalDate.now();
        textDataSelecionada.setText(dataSelecionada.getDayOfMonth() + "/" + dataSelecionada.getMonthValue() + "/" + dataSelecionada.getYear());

        btnData.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                dataSelecionada = LocalDate.of(year, month + 1, dayOfMonth);
                textDataSelecionada.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        });
    }

    private void carregarCategorias() {
        categorias = db.lanchoneteDao().listarCategorias();
        if (categorias.isEmpty()) {
            Toast.makeText(this, "Aviso: Cadastre uma categoria antes de adicionar um produto!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        List<String> nomesCategorias = new ArrayList<>();
        for (Categoria cat : categorias) {
            nomesCategorias.add(cat.nome);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nomesCategorias);
        spinnerCategoria.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Ação do botão Up (voltar)
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
        int posicaoSelecionada = spinnerCategoria.getSelectedItemPosition();
        int categoriaId = categorias.get(posicaoSelecionada).id;
        boolean disponivel = radioDisponivel.isChecked();
        boolean destaque = checkDestaque.isChecked();

        Produto novoProduto = new Produto(nome, preco, categoriaId, dataSelecionada, disponivel, destaque);
        db.lanchoneteDao().inserirProduto(novoProduto);

        Toast.makeText(this, "Produto salvo com sucesso!", Toast.LENGTH_SHORT).show();
        finish(); // Volta para a tela anterior
    }
}