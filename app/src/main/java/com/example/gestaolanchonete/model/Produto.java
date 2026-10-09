package com.example.gestaolanchonete.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import java.time.LocalDate;

@Entity(tableName = "tabela_produtos",
        foreignKeys = @ForeignKey(entity = Categoria.class,
                parentColumns = "id",
                childColumns = "categoriaId",
                onDelete = ForeignKey.CASCADE))
public class Produto {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nome;
    public double preco;
    public int categoriaId; // Aqui está a Chave Estrangeira
    public LocalDate dataCadastro; // O atributo LocalDate exigido
    public boolean disponivel;
    public boolean destaque;

    // Construtor
    public Produto(String nome, double preco, int categoriaId, LocalDate dataCadastro, boolean disponivel, boolean destaque) {
        this.nome = nome;
        this.preco = preco;
        this.categoriaId = categoriaId;
        this.dataCadastro = dataCadastro;
        this.disponivel = disponivel;
        this.destaque = destaque;
    }
}