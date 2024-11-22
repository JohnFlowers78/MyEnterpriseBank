package br.com.meusite.myenterprisebank.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import br.com.meusite.myenterprisebank.R
import br.com.meusite.myenterprisebank.data.AppDatabase

@Composable
fun MainScreen(navController: NavHostController) {

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    // Criar uma variável de estado reativa para o saldo
    val balance = remember { mutableStateOf("R$ 0,00") }

    // Atualizar o saldo de forma assíncrona com coroutines
    LaunchedEffect(Unit) {
        val saldoAtual = db.userDao().getSaldo()
        balance.value = "R$ $saldoAtual"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(bottom = 16.dp) // ajuste para evitar floating buttons colados ao rodapé
    ) {
        TopBar()
        BalanceSection(balance = balance.value, onExtratoClick = {
            navController.navigate("extratoList")
        })

        Spacer(modifier = Modifier.height(100.dp))
        ActionButtons(navController)

        Spacer(modifier = Modifier.height(32.dp))

        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            thickness = 1.dp
        )

        FloatingButtons(navController)
    }
}

//@Preview(showBackground = true)
@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .background(Color(0xFF820AD1))       // Purple color similar to Nubank
            .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),   // espaço para evitar invasão na área de status
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.jose),
            contentDescription = "Imagem do usuário",
            modifier = Modifier.size(54.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Olá, José",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BalanceSection(balance: String, onExtratoClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
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
        Button(
            onClick = onExtratoClick,
            modifier = Modifier.width(120.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9868B6))
        ) {
            Text(
                text = "Extrato →",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
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
            text = "Área Pix",
            iconResId = R.drawable.icon_pix,
            onClick = { navController.navigate("transferir") }
        )
        ActionButton(
            text = "Transferir",
            iconResId = R.drawable.icon_transferir,
            onClick = { navController.navigate("transferir") }
        )
        ActionButton(
            text = "Depositar",
            iconResId = R.drawable.icon_depositar,
            onClick = { navController.navigate("depositar") }
        )
    }
}

@Composable
fun ActionButton(text: String, iconResId: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .size(64.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.Black
            )
        ) {
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = text,
                modifier = Modifier.size(42.dp),
                tint = Color.Black
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
fun FloatingButtons(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)             // Alinha os botões no centro na parte inferior
                .padding(bottom = 44.dp)
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
