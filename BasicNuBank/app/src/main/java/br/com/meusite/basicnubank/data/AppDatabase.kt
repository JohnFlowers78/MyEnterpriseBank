package br.com.meusite.basicnubank.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.meusite.basicnubank.R
import br.com.meusite.basicnubank.data.caixinha.Caixinha
import br.com.meusite.basicnubank.data.caixinha.CaixinhaDAO
import br.com.meusite.basicnubank.data.transacao.Transacao
import br.com.meusite.basicnubank.data.transacao.TransacaoDAO
import br.com.meusite.basicnubank.data.user.User
import br.com.meusite.basicnubank.data.user.UserDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Transacao::class, Caixinha::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){

    abstract fun transacaoDao(): TransacaoDAO
    abstract fun caixinhaDao(): CaixinhaDAO
    abstract fun userDao(): UserDAO

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }else {

                synchronized(this) {

                    val instance = Room.databaseBuilder(         // método que constrói o DataBase
                        context.applicationContext,               // Recebe o ctx da aplicação
                        AppDatabase::class.java,                   // Recebe a classe do banco de dados
                        "mybasicnubank_db"                     // Nome do Banco
                    )
                        .addCallback(DatabaseCallback(context))      // -->  Callback para adicionar a pré-população
                        .build()

                    INSTANCE = instance
                    return instance
                }
            }
        }
        private class DatabaseCallback(
            private val context: Context
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Inicia uma Coroutine para inserir os dados iniciais
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(getDatabase(context))
                }
            }

            // Função para pré-população dos dados
            private suspend fun populateDatabase(database: AppDatabase) {

                val userInicial = User(1, "José", "123.456.789-00", 2000.0, imgPerfil = R.drawable.jose)

                val transacoesIniciais = listOf(
                    Transacao(0, "Supermercado Pão de Açúcar", 120.50, "09/06/2024", "18:44:16", userId = 1),
                    Transacao(0, "Restaurante Japonês Sushi Way", 95.30, "08/06/2024", "13:20:26", userId = 1),
                    Transacao(0, "Combustível Posto Shell", 300.00, "07/06/2024", "09:15:57", userId = 1),
                    Transacao(0, "Cinema Cinemark", 45.00, "05/06/2024", "20:45:10", userId = 1),
                    Transacao(0, "Lanchonete Burger King", 32.90, "04/06/2024", "12:30:29", userId = 1),
                    Transacao(0, "Farmácia Drogasil", 67.80, "03/06/2024", "16:00:32", userId = 1),
                    Transacao(0, "Loja de Roupas Zara", 250.00, "02/06/2024", "15:10:43", userId = 1),
                    Transacao(0, "Eletrônicos Fast Shop", 1599.99, "01/06/2024", "10:00:05", userId = 1)
                )

                val caixinhasIniciais = listOf(
                    Caixinha(0, "Viagem", 500.0, userId = 1),
                    Caixinha(0, "Reserva de Emergência", 1000.0, userId = 1),
                    Caixinha(0, "Novo Celular", 200.0, userId = 1)
                )

                // Inserir os dados iniciais usando coroutines
                database.userDao().addUser(userInicial)
                database.transacaoDao().insertAll(transacoesIniciais)
                database.caixinhaDao().insertAll(caixinhasIniciais)
            }
        }
    }
}