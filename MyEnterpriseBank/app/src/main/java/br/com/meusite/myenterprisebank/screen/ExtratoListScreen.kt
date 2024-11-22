package br.com.meusite.myenterprisebank.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.meusite.myenterprisebank.R
import br.com.meusite.myenterprisebank.data.AppDatabase
import br.com.meusite.myenterprisebank.data.transacao.Transacao
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController

@Composable
fun ExtratoListScreen(navController: NavHostController) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val transacaoDAO = db.transacaoDao()

    val transacoes by transacaoDAO.listTransacoes().observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF673AB7))
    ) {

        HeaderSection()

        Spacer(modifier = Modifier.height(8.dp))

        TransacaoList(transacoes)
    }
    FloatingButtons2(navController)
}

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.jose),
            contentDescription = "Imagem do usuário",
            modifier = Modifier
                .size(54.dp)
                .align(Alignment.Start),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Bem vindo aos seus Extratos, José...",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TransacaoList(transacoes: List<Transacao>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(transacoes) { transacao ->
            TransacaoItem(transacao)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// Composable para cada item da transação
@Composable
fun TransacaoItem(transacao: Transacao) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
    ) {
        Text(
            text = "Descrição: ${transacao.descricao}",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Valor: R$ ${transacao.valor}",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Data: ${transacao.data}",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Hora: ${transacao.hora}",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun FloatingButtons2(navController: NavHostController) {
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