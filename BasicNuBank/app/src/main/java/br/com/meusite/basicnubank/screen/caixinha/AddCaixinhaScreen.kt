package br.com.meusite.basicnubank.screen.caixinha

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.meusite.basicnubank.data.AppDatabase
import br.com.meusite.basicnubank.data.caixinha.Caixinha
import kotlinx.coroutines.launch

@Composable
fun AddCaixinhaScreen(navController: NavController) {

    // Variáveis de estado para armazenar os valores dos campos
    var nomeNewCaixinha by remember { mutableStateOf("") }
    var valorInicial by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val context = LocalContext.current
    val userDao = AppDatabase.getDatabase(context).userDao()
    val caixinhaDao = AppDatabase.getDatabase(context).caixinhaDao() // Para interagir com Caixinha

    val userWithRelations by userDao.getUserWithRelations(1).observeAsState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título para o campo "Nome da Caixinha"
        Text(
            text = "Nome da Caixinha",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Campo de texto para o nome da caixinha
        OutlinedTextField(
            value = nomeNewCaixinha,
            onValueChange = { nomeNewCaixinha = it },
            label = { Text("Insira o Nome da Caixinha") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Título para o campo "Valor Inicial"
        Text(
            text = "Valor Inicial",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Campo de texto para o valor inicial
        OutlinedTextField(
            value = valorInicial,
            onValueChange = { valorInicial = it },
            label = { Text("Insira um valor inicial em sua caixinha") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    val valorDouble = valorInicial.toDoubleOrNull()
                    if (valorDouble != null) {
                        userWithRelations?.let { user ->
                            if (user.user.saldo >= valorDouble) {

                                val novaCaixinha = Caixinha(id = 0, nome = nomeNewCaixinha, saldo = valorDouble, userId = user.user.id)

                                // Adiciona a caixinha e atualiza o saldo
                                caixinhaDao.addCaixinha(novaCaixinha)
                                userDao.atualizarSaldo(user.user.saldo - valorDouble, user.user.id)

                                message = "Caixinha criada com sucesso!"
                                navController.popBackStack()
                            } else {
                                message = "Saldo insuficiente para criar a caixinha."
                            }
                        }
                    } else {
                        message = "Valor inicial inválido."
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Criar Caixinha")
        }

        // Mensagem de feedback
        Text(text = message, color = MaterialTheme.colorScheme.error)
    }
}