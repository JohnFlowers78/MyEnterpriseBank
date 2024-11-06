package br.com.meusite.basicnubank

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.meusite.basicnubank.screen.CaixinhasGridScreen
import br.com.meusite.basicnubank.screen.ExtratoListScreen
import br.com.meusite.basicnubank.screen.MainScreen
import br.com.meusite.basicnubank.screen.caixinha.AddCaixinhaScreen
import br.com.meusite.basicnubank.screen.caixinha.DetalhesCaixinhaScreen
import br.com.meusite.basicnubank.screen.caixinha.UpdateCaixinhaScreen
import br.com.meusite.basicnubank.screen.transacao.DepositarScreen
import br.com.meusite.basicnubank.screen.transacao.TransferirScreen
import br.com.meusite.basicnubank.ui.theme.BasicNuBankTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasicNuBankTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NuBankApp()
                }
            }
        }
    }
}

@Composable
fun NuBankApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "principal") {
        composable("principal") { MainScreen(navController) }
        composable("depositar") { DepositarScreen(navController) }
        composable("transferir") { TransferirScreen(navController) }
        composable("extratoList") { ExtratoListScreen() }
        composable("caixinhasList") { CaixinhasGridScreen(navController) }
        composable("addCaixinha") { AddCaixinhaScreen(navController) }
        composable("detalhesCaixinha/{caixinhaId}") { backStackEntry ->
            val caixinhaId = backStackEntry.arguments?.getString("caixinhaId")
            Log.d("caixinha 1-- ", "$caixinhaId")
            DetalhesCaixinhaScreen(navController, caixinhaId)
        }
        composable("updateCaixinha/{caixinhaId}") { backStackEntry ->
            val caixinhaId = backStackEntry.arguments?.getString("caixinhaId")?.toInt() ?: -1
            UpdateCaixinhaScreen(navController, caixinhaId)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BasicNuBankTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            NuBankApp()
        }
    }
}