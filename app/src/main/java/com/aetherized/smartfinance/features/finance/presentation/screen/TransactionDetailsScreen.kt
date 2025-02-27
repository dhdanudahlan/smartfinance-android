package com.aetherized.smartfinance.features.finance.presentation.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aetherized.smartfinance.features.finance.domain.model.Category
import com.aetherized.smartfinance.features.finance.domain.model.Transaction
import com.aetherized.smartfinance.features.finance.presentation.TransactionDetailsUIState
import com.aetherized.smartfinance.features.finance.presentation.TransactionDetailsViewModel
import com.aetherized.smartfinance.ui.component.LoadingScreen

@Composable
fun TransactionDetailsScreenReady(
    viewModel: TransactionDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    when (uiState) {
        is TransactionDetailsUIState.Loading -> {
            LoadingScreen("Loading transactions")
        }
        is TransactionDetailsUIState.Success -> {
            // Pass all necessary data to the detailed screen.
            val successState = uiState as TransactionDetailsUIState.Success
            TransactionDetailsScreen(
                transaction = successState.transaction,
                category = successState.category,
                updateTransaction = {
                    viewModel.updateTransaction(it)
                },
                deleteTransaction = viewModel::deleteTransaction,
                onNavigateBack = onNavigateBack
            )
        }
        is TransactionDetailsUIState.Error -> {
            val errorState = uiState as TransactionDetailsUIState.Error
            Log.d("TransactionDetailsUIState.Error.Screen", errorState.message)
            // Display an error message on-screen.
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = (uiState as TransactionDetailsUIState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailsScreen(
    transaction: Transaction,
    category: Category,
    updateTransaction: (Transaction) -> Unit,
    deleteTransaction: () -> Unit,
    onNavigateBack: () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction Details", textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            TransactionDetailsContent(
                transaction = transaction,
                category = category,
                updateTransaction = updateTransaction,
                deleteTransaction = deleteTransaction
            )
        }

    }
}

@Composable
fun TransactionDetailsContent(
    transaction: Transaction,
    category: Category,
    updateTransaction: (Transaction) -> Unit,
    deleteTransaction: () -> Unit
) {
    // Local state for edit mode.
    var isEditing by remember { mutableStateOf(false) }
    var amountText by remember { mutableStateOf(transaction.amount.toString()) }
    var noteText by remember { mutableStateOf(transaction.note ?: "") }

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (isEditing) {
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Note") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        // Create updated transaction object.
                        val updatedTransaction = transaction.copy(
                            amount = amountText.toIntOrNull() ?: transaction.amount,
                            note = noteText
                        )
                        updateTransaction(updatedTransaction)
                        isEditing = false
                    }) {
                        Text("Save")
                    }
                    Button(onClick = { isEditing = false }) {
                        Text("Cancel")
                    }
                }
            } else {
                // Display details.
                Text(text = "Amount: ${transaction.amount}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Note: ${transaction.note ?: "No note"}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Category: ${category.name}", fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Date: ${transaction.timestamp}", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { isEditing = true }) {
                        Text("Edit")
                    }
                    Button(onClick = { deleteTransaction() }) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}
