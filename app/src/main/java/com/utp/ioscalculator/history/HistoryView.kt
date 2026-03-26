package com.utp.ioscalculator.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HistoryView(historyViewModel: HistoryViewModel = HistoryViewModel()) {
    // 1. Obtenemos la lista directamente del ViewModel de Java [cite: 1, 3]
    val operations = historyViewModel.historyList

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Verificamos si la lista está vacía
        if (operations.isEmpty()) {
            Text(
                text = "No hay operaciones en el historial",
                color = Color.White,
                fontSize = 18.sp
            )
        } else {
            // 2. Lista scrolleable para mostrar los datos
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                item {
                    Text(
                        text = "Historial",
                        color = Color.White,
                        fontSize = 28.sp,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }

                items(operations) { item ->
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
                        // Accedemos a los datos de HistoryModel [cite: 6, 9]
                        Text(text = item.expression, color = Color.Gray, fontSize = 16.sp)
                        Text(text = "= ${item.result}", color = Color.White, fontSize = 22.sp)
                        HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Test(){
    HistoryView()
}