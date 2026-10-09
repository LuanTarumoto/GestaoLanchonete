package com.example.gestaolanchonete.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabela_categorias")
public class Categoria {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nome;

    // Construtor
    public Categoria(String nome) {
        this.nome = nome;
    }
}