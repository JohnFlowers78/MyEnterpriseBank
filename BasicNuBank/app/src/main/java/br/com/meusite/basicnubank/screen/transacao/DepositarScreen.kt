package br.com.meusite.basicnubank.screen.transacao

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.meusite.basicnubank.data.AppDatabase
import br.com.meusite.basicnubank.data.transacao.Transacao
import kotlinx.coroutines.launch

@Composable
fun DepositarScreen(navController: NavHostController) {

    var valorDeposito by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }

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
        Text(
            text = "Valor do Depósito",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
        )

        OutlinedTextField(
            value = valorDeposito,
            onValueChange = { valorDeposito = it },
            label = { Text(text = "*Digite em Reais o valor a ser depositado") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Descrição",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text(text = "*Adicione uma descrição à esta transação") },
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val valor = valorDeposito.toDoubleOrNull()
                if (valor != null && valor > 0) {

                    // Inicia a corrotina para atualizar o saldo e registrar a transação
                    coroutineScope.launch {

                        saldoAtual = userDao.getSaldo() // ou userDao.getSaldoUsuario(1) para obter o saldo pelo ID

                        val novoSaldo = saldoAtual + valor
                        userDao.atualizarSaldo(novoSaldo, 1) // temos apenas um User no db ou seja, o ID do usuário é 1 <--

                        val transacao = Transacao(
                            id = 0,
                            descricao = descricao,
                            valor = valor,
                            userId = 1 // temos apenas um User no db ou seja, o ID do usuário é 1 <--
                        )
                        transacaoDao.addTransacao(transacao)
                        message = "Depósito realizado com sucesso!"
                        navController.popBackStack()
                    }
                } else {
                    message = "Por favor, insira um valor válido para o depósito."
                }
            },
            modifier = Modifier
                .width(119.dp)
                .height(51.dp)
        ) {
            Text(text = "Depositar", color = Color.White)
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
