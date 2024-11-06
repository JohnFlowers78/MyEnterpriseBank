package br.com.meusite.basicnubank.screen.transacao

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.meusite.basicnubank.data.AppDatabase
import br.com.meusite.basicnubank.data.transacao.Transacao
import kotlinx.coroutines.launch

@Composable
fun TransferirScreen(navController: NavHostController) {
    var chavePix by remember { mutableStateOf("") }
    var valorTransfer by remember { mutableStateOf("") }

    var message by remember { mutableStateOf("") }
    var saldoAtual by remember { mutableStateOf(0.0) }

    val context = LocalContext.current
    val userDao = AppDatabase.getDatabase(context).userDao()
    val transacaoDao = AppDatabase.getDatabase(context).transacaoDao()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Texto para "Chave Pix"
        Text(
            text = "Chave Pix",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        // Campo para inserir a chave Pix
        OutlinedTextField(
            value = chavePix,
            onValueChange = { chavePix = it },
            label = { Text("Nome, CPF/CNPJ ou chave Pix") },
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Texto para "Valor da Transferência"
        Text(
            text = "Valor da Transferência",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        // Campo para inserir o valor da transferência
        OutlinedTextField(
            value = valorTransfer,
            onValueChange = { valorTransfer = it },
            label = { Text("Digite o valor da transferência, em Reais") },
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val valor = valorTransfer.toDoubleOrNull()
                if (valor != null && valor > 0) {

                    // Inicia a corrotina para atualizar o saldo e registrar a transação
                    coroutineScope.launch {

                        saldoAtual = userDao.getSaldo() // ou userDao.getSaldoUsuario(1) para obter o saldo pelo ID

                        if (saldoAtual >= valor) {
                            val novoSaldo = saldoAtual - valor
                            userDao.atualizarSaldo(novoSaldo, 1)      // temos apenas um User no db ou seja, o ID do usuário é 1 <--

                            val transacao = Transacao(
                                id = 0,
                                descricao = chavePix,
                                valor = valor,
                                userId = 1       // temos apenas um User no db ou seja, o ID do usuário é 1 <--
                            )
                            transacaoDao.addTransacao(transacao)

                            message = "Transferência realizada com sucesso!"
                            navController.popBackStack()
                        } else {
                            message = "Saldo insuficiente para realizar a transferência."
                        }
                    }
                } else {
                    message = "Por favor, insira um valor válido para a transferência."
                }
            },
            modifier = Modifier
                .width(150.dp)
                .height(50.dp)
        ) {
            Text(text = "Transferir")
        }
        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}