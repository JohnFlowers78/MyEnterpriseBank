package br.com.meusite.myenterprisebank.data.transacao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TransacaoDAO {

    @Query("SELECT * FROM transacoes ORDER BY id DESC")
    fun listTransacoes(): LiveData<List<Transacao>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTransacao(transacao: Transacao)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(transacoes: List<Transacao>)          // Inserção de múltiplos itens   -->   Para popular a base de dados
}