package com.example.gestaolanchonete.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gestaolanchonete.model.Categoria;
import com.example.gestaolanchonete.model.Produto;

import java.util.List;

@Dao
public interface LanchoneteDao {

    // REQUISITO: Inserção - pelo menos 2 tabelas
    @Insert
    void inserirCategoria(Categoria categoria);

    @Insert
    void inserirProduto(Produto produto);

    // REQUISITO: Alteração - pelo menos 2 tabelas
    @Update
    void atualizarCategoria(Categoria categoria);

    @Update
    void atualizarProduto(Produto produto);

    // REQUISITO: Remoção - pelo menos 2 tabelas
    @Delete
    void deletarCategoria(Categoria categoria);

    @Delete
    void deletarProduto(Produto produto);

    // REQUISITO: Consulta - pelo menos 2 tabelas
    @Query("SELECT * FROM tabela_categorias")
    List<Categoria> listarCategorias();

    @Query("SELECT * FROM tabela_produtos")
    List<Produto> listarProdutos();
}