package com.example.gestaolanchonete.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gestaolanchonete.R;
import com.example.gestaolanchonete.model.Produto;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ProdutoAdapter extends ArrayAdapter<Produto> {

    private Map<Integer, String> mapaCategorias;

    public ProdutoAdapter(Context context, List<Produto> produtos, Map<Integer, String> mapaCategorias) {
        super(context, 0, produtos);
        this.mapaCategorias = mapaCategorias;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Produto produto = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_produto, parent, false);
        }

        TextView textNome = convertView.findViewById(R.id.textNomeProduto);
        TextView textDetalhes = convertView.findViewById(R.id.textDetalhesProduto);
        TextView textPreco = convertView.findViewById(R.id.textPrecoProduto);

        textNome.setText(produto.nome);

        // Pega o nome da categoria no mapa ou exibe erro se não achar
        String nomeCategoria = mapaCategorias.containsKey(produto.categoriaId) ? mapaCategorias.get(produto.categoriaId) : "Sem Categoria";

        String status = produto.disponivel ? "Disponível" : "Esgotado";

        // Formata a data com barras (dd/MM/yyyy)
        String data = "";
        if (produto.dataCadastro != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            data = produto.dataCadastro.format(formatter);
        }

        textDetalhes.setText(nomeCategoria + " | " + status + " | " + data);
        textPreco.setText(String.format("R$ %.2f", produto.preco));

        return convertView;
    }
}