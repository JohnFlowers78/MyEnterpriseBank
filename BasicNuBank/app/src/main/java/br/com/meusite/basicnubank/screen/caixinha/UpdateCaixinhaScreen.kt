package br.com.meusite.basicnubank.screen.caixinha

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.meusite.basicnubank.data.AppDatabase
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nome da Caixinha",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
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
                                // Atualize a caixinha no banco de dados e ajuste o saldo do usuário
                                db.caixinhaDao().updateCaixinha(
                                    caixinha!!.copy(nome = updatedName.text, saldo = valorDouble)
                                )
                                db.userDao().atualizarSaldo(userSaldoAtual - diferenca, userWithRelations?.user?.id ?: 1)
                                navController.popBackStack()
                            } else {
                                errorMessage = "Saldo insuficiente para aumentar o valor da caixinha."
                            }
                        } else {
                            // Atualize a caixinha no banco de dados sem modificar o saldo do usuário
                            db.caixinhaDao().updateCaixinha(
                                caixinha!!.copy(nome = updatedName.text, saldo = valorDouble)
                            )
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
}
