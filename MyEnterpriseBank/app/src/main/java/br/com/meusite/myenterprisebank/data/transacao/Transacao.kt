package br.com.meusite.myenterprisebank.data.transacao

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "transacoes")
data class Transacao(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val descricao: String,
    val valor: Double,
    val data: String = getCurrentDate(),
    val hora: String = getCurrentTime(),
    val userId: Int         // Foreign key para User
) {
    companion object {
        fun getCurrentDate(): String {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")  // Formato de data
            return current.format(formatter)
        }

        fun getCurrentTime(): String {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")  // Formato de hora
            return current.format(formatter)
        }
    }
}