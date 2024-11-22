package br.com.meusite.myenterprisebank.screen.caixinha

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
import androidx.navigation.NavController
import br.com.meusite.myenterprisebank.R
import br.com.meusite.myenterprisebank.data.AppDatabase
import br.com.meusite.myenterprisebank.data.caixinha.Caixinha
import kotlinx.coroutines.launch

@Composable
fun AddCaixinhaScreen(navController: NavController) {
    var nomeNewCaixinha by remember { mutableStateOf("") }
    var valorInicial by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var saldoAtual by remember { mutableStateOf(0.0) }

    val context = LocalContext.current
    val userDao = AppDatabase.getDatabase(context).userDao()
    val caixinhaDao = AppDatabase.getDatabase(context).caixinhaDao()
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

        TopBar(saldo = "R$ ${"%.2f".format(saldoAtual)}")

        Spacer(modifier = Modifier.height(16.dp))

        // Conteúdo da tela de adicionar caixinha
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nome da Caixinha",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp)
            )

            OutlinedTextField(
                value = nomeNewCaixinha,
                onValueChange = { nomeNewCaixinha = it },
                label = { Text("Insira o Nome da Caixinha") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Valor Inicial",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp)
            )

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
                            if (saldoAtual >= valorDouble) {
                                val novaCaixinha = Caixinha(id = 0, nome = nomeNewCaixinha, saldo = valorDouble, userId = 1)

                                caixinhaDao.addCaixinha(novaCaixinha)
                                userDao.atualizarSaldo(saldoAtual - valorDouble, 1)

                                message = "Caixinha criada com sucesso!"
                                navController.popBackStack()
                            } else {
                                message = "Saldo insuficiente para criar a caixinha."
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

        FloatingButtons6(navController)
    }
}

@Composable
fun TopBar(saldo: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .background(Color(0xFF820AD1))                  // Cor roxa similar ao Nubank
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
fun FloatingButtons6(navController: NavController) {
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
