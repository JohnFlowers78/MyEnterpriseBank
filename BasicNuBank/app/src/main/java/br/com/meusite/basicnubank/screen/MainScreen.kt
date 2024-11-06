package br.com.meusite.basicnubank.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.meusite.basicnubank.R
import br.com.meusite.basicnubank.data.AppDatabase

// Arrumar o Extrato Button
// arrumar o getSaldo  -->  Precisa de Coroutines
// arrumar o weight

@Composable
fun MainScreen(navController: NavHostController) {

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    // Criar uma variável de estado reativa para o saldo
    val balance = remember { mutableStateOf("R$ 0,00") }

    // Atualizar o saldo de forma assíncrona com coroutines
    LaunchedEffect(Unit) {
        val saldoAtual = db.userDao().getSaldo() // Supondo que getSaldo() retorna um valor válido
        balance.value = "R$ $saldoAtual" // Atualiza o valor do saldo de forma reativa
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar()
        BalanceSection(balance = balance.value)
        NavigationButtons(navController)
        ActionButtons(navController)

        ExtratoButton(
            onClick = {
                navController.navigate("extratoList")
            }
        )
    }
}

@Composable
fun TopBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF820AD1)) // Purple color  ->  IGUAL O NUBANK
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Image Avatar
        Image(
            painter = painterResource(id = R.drawable.jose),
            contentDescription = "Imagem do usuário",
            modifier = Modifier
                .size(54.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Olá, José",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BalanceSection(balance: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Saldo",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = balance,
            color = Color.Black,
            fontSize = 18.sp
        )
    }
}

@Composable
fun NavigationButtons(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        FloatingActionButton(
            onClick = {
                navController.navigate("caixinhasList")
            },
            modifier = Modifier.size(56.dp),
            containerColor = Color(0xFF820AD1) // Cor personalizada usando containerColor diretamente
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_sifrao),
                contentDescription = "Caixinhas",
                tint = Color.White
            )
        }
        FloatingActionButton(
            onClick = {
                navController.navigate("principal")
            },
            modifier = Modifier.size(56.dp),
            containerColor = Color(0xFF820AD1)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_transacao),
                contentDescription = "Transações/Extrato",
                tint = Color.White
            )
        }
    }
}

@Composable
fun ActionButtons(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ActionButton(
            text = "Depositar",
            iconResId = R.drawable.icon_depositar,
            onClick = { navController.navigate("depositar") }
        )
        ActionButton(
            text = "Transferir",
            iconResId = R.drawable.icon_transferir,
            onClick = { navController.navigate("transferir") }
        )
        ActionButton(
            text = "Pix",
            iconResId = R.drawable.icon_pix,
            onClick = { navController.navigate("transferir") }
        )
    }
}

@Composable
fun ActionButton(text: String, iconResId: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(56.dp),
            containerColor = Color(0xFF820AD1)
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = text,
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ExtratoButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)) // Purple color
    ) {
        Text(
            text = "Extrato → >>",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}