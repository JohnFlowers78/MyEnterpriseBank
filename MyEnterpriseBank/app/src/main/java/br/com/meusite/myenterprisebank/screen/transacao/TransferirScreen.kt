package br.com.meusite.myenterprisebank.screen.transacao

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.meusite.myenterprisebank.R
import br.com.meusite.myenterprisebank.data.AppDatabase
import br.com.meusite.myenterprisebank.data.transacao.Transacao
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

    // Ação para obter o saldo
    LaunchedEffect(Unit) {
        saldoAtual = userDao.getSaldo()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TopBar com o saldo
        TopBar(saldo = "R$ ${"%.2f".format(saldoAtual)}")

        Spacer(modifier = Modifier.height(16.dp))

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

            OutlinedTextField(
                value = chavePix,
                onValueChange = { chavePix = it },
                label = { Text("Nome, CPF/CNPJ ou chave Pix") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Valor da Transferência",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

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

                        coroutineScope.launch {

                            saldoAtual = userDao.getSaldo() // Recarrega o saldo do usuário

                            if (saldoAtual >= valor) {
                                val novoSaldo = saldoAtual - valor
                                userDao.atualizarSaldo(novoSaldo, 1)

                                val transacao = Transacao(
                                    id = 0,
                                    descricao = chavePix,
                                    valor = valor,
                                    userId = 1
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
            FloatingButtons4(navController)
        }
    }
}

@Composable
fun TopBar(saldo: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .background(Color(0xFF820AD1))              // Cor roxa similar ao Nubank
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
fun FloatingButtons4(navController: NavHostController) {
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
                    modifier = Modifier.size(24.dp),    // Aumenta o ícone dentro do botão
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
