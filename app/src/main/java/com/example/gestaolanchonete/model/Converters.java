package com.example.gestaolanchonete.model;

import androidx.room.TypeConverter;
import java.time.LocalDate;

public class Converters {

    @TypeConverter
    public static LocalDate fromTimestamp(String value) {
        return value == null ? null : LocalDate.parse(value);
    }

    @TypeConverter
    public static String dateToTimestamp(LocalDate date) {
        return date == null ? null : date.toString();
    }
}