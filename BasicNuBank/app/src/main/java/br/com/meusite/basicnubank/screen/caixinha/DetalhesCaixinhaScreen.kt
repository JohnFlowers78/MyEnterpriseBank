package br.com.meusite.basicnubank.screen.caixinha

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.meusite.basicnubank.R
import br.com.meusite.basicnubank.data.AppDatabase
import kotlinx.coroutines.launch

@Composable
fun DetalhesCaixinhaScreen(navController: NavHostController, caixinhaId: String?) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    val caixinha by db.caixinhaDao().getCaixinhaById(caixinhaId?.toInt() ?: -1).observeAsState()

    caixinha?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagem da Caixinha
            Image(
                painter = painterResource(id = R.drawable.default_image),   // ainda vamos dar a escolha da imagem para o user... !
                contentDescription = "Imagem selecionada",
                modifier = Modifier
                    .size(133.dp)
                    .padding(bottom = 24.dp)
            )

            // Nome da Caixinha
            Text(
                text = it.nome,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Saldo da Caixinha
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
                    // Função de resgatar dinheiro da caixinha
                    coroutineScope.launch {
                        val saldoCaixinha = it.saldo
                        val saldoUsuario = db.userDao().getSaldo()

                        // Zera o saldo da caixinha
                        db.caixinhaDao().updateCaixinha(it.copy(saldo = 0.00))

                        val novoSaldo = saldoUsuario + saldoCaixinha
                        db.userDao().atualizarSaldo(novoSaldo)
                    }
                }) {
                    Text("Resgatar")
                }
                Button(onClick = {
                    // Função de guardar dinheiro na caixinha
                    coroutineScope.launch {
                        //                    db.caixinhaDao().updateCaixinha(it.copy(saldo = it.saldo)) // Atualiza o saldo
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
                // Botão Atualizar Caixinha
                Button(onClick = {
                    navController.navigate("updateCaixinha/${caixinhaId}")
                }) {
                    Text("Atualizar")
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
}
