package com.example.gestaolanchonete.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gestaolanchonete.R;
import com.example.gestaolanchonete.model.Produto;

import java.util.List;

public class ProdutoAdapter extends ArrayAdapter<Produto> {

    public ProdutoAdapter(Context context, List<Produto> produtos) {
        super(context, 0, produtos);
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

        // Preenche os dados no layout
        textNome.setText(produto.nome);

        String status = produto.disponivel ? "Disponível" : "Esgotado";
        String data = produto.dataCadastro != null ? produto.dataCadastro.toString() : "";
        textDetalhes.setText("Cat ID: " + produto.categoriaId + " | " + status + " | " + data);

        textPreco.setText(String.format("R$ %.2f", produto.preco));

        return convertView;
    }
}