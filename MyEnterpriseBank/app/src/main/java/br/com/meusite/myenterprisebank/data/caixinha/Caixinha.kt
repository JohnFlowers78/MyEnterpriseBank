package br.com.meusite.myenterprisebank.data.caixinha

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "caixinhas")
data class Caixinha(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nome: String,
    val saldo: Double,
//    val imgCaixinha: Int,      // recurso drawable para a imagem da caixinha
    val userId: Int         // Foreign key para User
)