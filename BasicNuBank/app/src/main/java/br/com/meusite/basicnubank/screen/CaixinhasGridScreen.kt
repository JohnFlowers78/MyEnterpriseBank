package br.com.meusite.basicnubank.screen

import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import br.com.meusite.basicnubank.data.AppDatabase
import br.com.meusite.basicnubank.data.caixinha.Caixinha

@Composable
fun CaixinhasGridScreen(navController: NavHostController) {

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)

    val caixinhasLiveData: LiveData<List<Caixinha>> = db.caixinhaDao().listCaixinhas()
    val caixinhas by caixinhasLiveData.observeAsState(emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        CaixinhasGrid(caixinhas, navController)

        // FloatingActionButton removed from this screen as per your specification
        // If you wish to keep it for navigating to add screen, you can uncomment below

        FloatingActionButton(
            onClick = { navController.navigate("addCaixinha") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_input_add),
                contentDescription = "Add Caixinha",
                tint = Color.White
            )
        }
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
                    // Navigate to the details screen when clicked
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
            painter = painterResource(id = br.com.meusite.basicnubank.R.drawable.default_image),
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
            text = "R$ ${caixinha.saldo}",      // Converte saldo para formato monetário
            fontSize = 14.sp,
            modifier = Modifier.width(100.dp)
        )
    }
}
