package com.example.proyectodamn.room;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.example.proyectodamn.login.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
@Entity(tableName = "user_table")
public class UserTable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "email")
    private final String mEmail;

    @NonNull
    @ColumnInfo(name = "token")
    private final String mToken;

}