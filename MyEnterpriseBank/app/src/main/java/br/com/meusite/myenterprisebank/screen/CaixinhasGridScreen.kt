package br.com.meusite.myenterprisebank.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import br.com.meusite.myenterprisebank.R
import br.com.meusite.myenterprisebank.data.AppDatabase
import br.com.meusite.myenterprisebank.data.caixinha.Caixinha

@Composable
fun CaixinhasGridScreen(navController: NavHostController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val caixinhasLiveData: LiveData<List<Caixinha>> = db.caixinhaDao().listCaixinhas()
    val caixinhas by caixinhasLiveData.observeAsState(emptyList())


    val saldoTotal = caixinhas.sumOf { it.saldo }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 100.dp)
    ) {
        SaldoHeader(saldo = saldoTotal)
        CaixinhasGrid(caixinhas, navController)
    }

    // FAB para adicionar Caixinha
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = { navController.navigate("addCaixinha") },
            modifier = Modifier.padding(bottom = 80.dp)
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_input_add),
                contentDescription = "Add Caixinha",
                tint = Color.White
            )
        }
    }

    // Botões de navegação no rodapé
    FloatingButtons3(navController)
}

@Composable
fun SaldoHeader(saldo: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(125.dp)
            .background(Color(0xFF673AB7))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "Soma Total das Caixinhas\nR$ ${String.format("%.2f", saldo)}",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 28.sp
        )
    }
}

@Composable
fun CaixinhasGrid(caixinhas: List<Caixinha>, navController: NavHostController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(caixinhas.size) { index ->
            val caixinha = caixinhas[index]
            CaixinhaItem(
                caixinha = caixinha,
                onClick = {
                    navController.navigate("detalhesCaixinha/${caixinha.id}")
                }
            )
        }
    }
}

@Composable
fun CaixinhaItem(caixinha: Caixinha, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .padding(8.dp)
            .clickable { onClick() },
    horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.default_image),                //  br.com.meusite.myenterprisebank.
            contentDescription = "img_caixinha",
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 4.dp)
        )
        Text(
            text = caixinha.nome,
            fontSize = 16.sp,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = "R$ ${caixinha.saldo}",
            fontSize = 14.sp,
            modifier = Modifier.width(100.dp)
        )
    }
}

@Composable
fun FloatingButtons3(navController: NavHostController) {
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
