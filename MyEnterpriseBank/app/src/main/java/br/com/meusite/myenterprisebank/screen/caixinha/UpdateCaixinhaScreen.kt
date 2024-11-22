package br.com.meusite.myenterprisebank.screen.caixinha

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.meusite.myenterprisebank.R
import br.com.meusite.myenterprisebank.data.AppDatabase
import kotlinx.coroutines.launch

@Composable
fun UpdateCaixinhaScreen(
    navController: NavController,
    caixinhaId: Int
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    val caixinhaLiveData = db.caixinhaDao().getCaixinhaById(caixinhaId)
    val caixinha by caixinhaLiveData.observeAsState()

    val userWithRelations by db.userDao().getUserWithRelations(1).observeAsState()
    val userSaldoAtual = userWithRelations?.user?.saldo ?: 0.0

    var updatedName by remember { mutableStateOf(TextFieldValue("")) }
    var updatedValor by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }

    // Atualiza os campos de texto com os valores da caixinha quando ela é carregada
    LaunchedEffect(caixinha) {
        if (caixinha != null) {
            updatedName = TextFieldValue(caixinha!!.nome)
            updatedValor = TextFieldValue(caixinha!!.saldo.toString())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        TopBar5(saldo = "R$ ${"%.2f".format(userSaldoAtual)}")

        Spacer(modifier = Modifier.height(16.dp))

        // Conteúdo da tela de atualização
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
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = updatedName,
                onValueChange = { updatedName = it },
                label = { Text("Insira o Nome da Caixinha") },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused && updatedName.text == caixinha?.nome) {
                            updatedName = TextFieldValue("")
                        }
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Valor",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = updatedValor,
                onValueChange = { updatedValor = it },
                label = { Text("Insira um novo valor em sua caixinha") },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused && updatedValor.text == caixinha?.saldo.toString()) {
                            updatedValor = TextFieldValue("")
                        }
                    }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        val valorDouble = updatedValor.text.toDoubleOrNull()
                        if (valorDouble != null && caixinha != null) {
                            val diferenca = valorDouble - caixinha!!.saldo
                            if (diferenca > 0) {
                                if (userSaldoAtual >= diferenca) {
                                    // Atualize a caixinha e ajuste o saldo do usuário
                                    db.caixinhaDao().updateCaixinha(
                                        caixinha!!.copy(nome = updatedName.text, saldo = valorDouble)
                                    )
                                    db.userDao().atualizarSaldo(userSaldoAtual - diferenca, userWithRelations?.user?.id ?: 1)
                                    navController.popBackStack()
                                } else {
                                    errorMessage = "Saldo insuficiente para aumentar o valor da caixinha."
                                }
                            } else {
                                val valorRestante = userSaldoAtual - diferenca
                                db.caixinhaDao().updateCaixinha(
                                    caixinha!!.copy(nome = updatedName.text, saldo = valorDouble)
                                )
                                db.userDao().atualizarSaldo(valorRestante, userWithRelations?.user?.id ?: 1)
                                navController.popBackStack()
                            }
                        } else {
                            errorMessage = "Valor inválido ou caixinha não encontrada."
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Atualizar")
            }

            // Exibir mensagem de erro, se houver
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        // Floating Buttons para navegação
        FloatingButtons8(navController)
    }
}

@Composable
fun TopBar5(saldo: String) {
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
fun FloatingButtons8(navController: NavController) {
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
