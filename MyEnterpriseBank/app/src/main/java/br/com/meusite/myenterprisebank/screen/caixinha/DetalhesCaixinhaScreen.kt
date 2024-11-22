package br.com.meusite.myenterprisebank.screen.caixinha

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import br.com.meusite.myenterprisebank.R
import br.com.meusite.myenterprisebank.data.AppDatabase
import kotlinx.coroutines.launch

@Composable
fun DetalhesCaixinhaScreen(navController: NavHostController, caixinhaId: String?) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    val caixinha by db.caixinhaDao().getCaixinhaById(caixinhaId?.toInt() ?: -1).observeAsState()
    var saldoAtual by remember { mutableStateOf(0.0) }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        saldoAtual = db.userDao().getSaldo()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        TopBar4(saldo = "R$ ${"%.2f".format(saldoAtual)}")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            caixinha?.let {
                // Imagem da Caixinha
                Image(
                    painter = painterResource(id = R.drawable.default_image),
                    contentDescription = "Imagem selecionada",
                    modifier = Modifier
                        .size(133.dp)
                        .padding(bottom = 24.dp)
                )

                Text(
                    text = it.nome,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Saldo \n R$ ${it.saldo}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botões de Ação
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        coroutineScope.launch {
                            val saldoCaixinha = it.saldo
                            val saldoUsuario = db.userDao().getSaldo()

                            // Zera o saldo da caixinha e atualiza o saldo do usuário
                            db.caixinhaDao().updateCaixinha(it.copy(saldo = 0.00))
                            db.userDao().atualizarSaldo(saldoUsuario + saldoCaixinha)
                        }
                    }) {
                        Text("Resgatar")
                    }

                    Button(onClick = {
                        coroutineScope.launch {
                            message = "Este botão AINDA não está funcional!"
                        }
                    }) {
                        Text("Guardar")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Mais Funções",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        navController.navigate("updateCaixinha/${caixinhaId}")
                    }) {
                        Text("Atualizar...")
                    }
                    // Botão Deletar Caixinha
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                caixinha?.let {
                                    db.caixinhaDao().deleteCaixinha(it)
                                }
                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Deletar Caixinha")
                    }
                }
            }
        }
        FloatingButtons7(navController)
    }
}

@Composable
fun TopBar4(saldo: String) {
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
fun FloatingButtons7(navController: NavController) {
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