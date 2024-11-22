package br.com.meusite.myenterprisebank.screen.transacao

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.meusite.myenterprisebank.R
import br.com.meusite.myenterprisebank.data.AppDatabase
import br.com.meusite.myenterprisebank.data.transacao.Transacao
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

    LaunchedEffect(Unit) {
        saldoAtual = userDao.getSaldo() // ou userDao.getSaldoUsuario(1) para obter o saldo pelo ID
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Adiciona a TopBar
        TopBar2(saldo = "R$ $saldoAtual")

        // Inputs para depósito e descrição
        Text(
            text = "Valor do Depósito",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
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
            color = Color.Black,
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

        // Botão de depósito
        Button(
            onClick = {
                val valor = valorDeposito.toDoubleOrNull()
                if (valor != null && valor > 0) {

                    coroutineScope.launch {

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
        FloatingButtons5(navController)
    }
}

@Composable
fun TopBar2(saldo: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .background(Color(0xFF820AD1))          // Cor roxa similar ao Nubank
            .padding(top = 16.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Saldo Disponível",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = saldo,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun FloatingButtons5(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    navController.navigate("principal")
                },
                modifier = Modifier.size(56.dp).padding(end = 0.dp),
                containerColor = Color(0xFF9F30D5)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_transacao),
                    contentDescription = "Transações/Extrato",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White,
                )
            }
            FloatingActionButton(
                onClick = {
                    navController.navigate("caixinhasList")
                },
                modifier = Modifier.size(56.dp).padding(start = 0.dp),
                containerColor = Color(0xFF9F30D5)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_sifrao),
                    contentDescription = "Caixinhas",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
        }
    }
}
