package br.com.meusite.myenterprisebank.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: Int = 1,
    val nome: String,
    val cpf: String,
    val saldo: Double,
    val imgPerfil: Int      // ID do recurso drawable para a imagem
)
