package com.example.gestaolanchonete.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.gestaolanchonete.model.Categoria;
import com.example.gestaolanchonete.model.Converters;
import com.example.gestaolanchonete.model.Produto;

@Database(entities = {Categoria.class, Produto.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract LanchoneteDao lanchoneteDao();

    // Padrão Singleton para garantir que exista apenas uma conexão aberta com o banco
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "banco_lanchonete")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}