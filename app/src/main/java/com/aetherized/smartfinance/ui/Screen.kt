package com.aetherized.smartfinance.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.LibraryBooks
import androidx.compose.material.icons.automirrored.rounded.LibraryBooks
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.InsertChart
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.InsertChart
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.ui.graphics.vector.ImageVector
import com.aetherized.smartfinance.R

sealed class Screen(
    val route: String,
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null,
    @StringRes val iconTextId: Int = R.string.empty,
    @StringRes val titleTextId: Int = R.string.empty,
) {

    object Accounts : Screen(
        route = navigationRouteAccounts,
        selectedIcon = Icons.Rounded.AccountBalance,
        unselectedIcon = Icons.Outlined.AccountBalance,
        iconTextId = R.string.feature_accounts_title,
        titleTextId = R.string.feature_accounts_title,
    )

    object Transactions: Screen(
        route = navigationRouteTransactions,
        selectedIcon = Icons.AutoMirrored.Rounded.LibraryBooks,
        unselectedIcon = Icons.AutoMirrored.Outlined.LibraryBooks,
        iconTextId = R.string.feature_transactions_title,
        titleTextId = R.string.feature_transactions_title,
    )

    object Reports : Screen(
        route = navigationRouteReports,
        selectedIcon = Icons.Rounded.InsertChart,
        unselectedIcon = Icons.Outlined.InsertChart,
        iconTextId = R.string.feature_reports_title,
        titleTextId = R.string.feature_reports_title,
    )
    object Others : Screen(
        route = navigationRouteOthers,
        selectedIcon = Icons.Rounded.MoreHoriz,
        unselectedIcon = Icons.Outlined.MoreHoriz,
        iconTextId = R.string.feature_others_title,
        titleTextId = R.string.feature_others_title,
    )

}


sealed class TransactionsScreen(
    val route: String,
){
    object List : TransactionsScreen("transactionList")
    // For Form, we use an optional query parameter "transactionId".
    // When not provided, we'll rely on the default value.
    object Form : TransactionsScreen("transactionForm?transactionId={transactionId}") {
        fun createRoute(transactionId: Long? = null): String {
            return if (transactionId == null) "transactionForm"
            else "transactionForm?transactionId=$transactionId"
        }
    }
}



// ----- Route Constants -----
const val navigationRouteHome = "home"

const val navigationRouteAccounts = "accounts"
const val navigationRouteTransactions = "transactions"
const val navigationRouteReports = "reports"
const val navigationRouteOthers = "others"

const val navigationRouteTransactionList = "transactionList"
const val navigationRouteTransactionForm = "transaction_create"


