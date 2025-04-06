package com.aetherized.smartfinance.features.category.navigation

import androidx.navigation.NavController


const val navigationRouteCategoryList = "CATEGORY_LIST"

//fun NavController.navigateToCategory(categoryId: Long, navOption: NavOptionsBuilder.() -> Unit = {}){
//    navigate(route = CategoryRoute(id = categoryId)) {
//        navOption()
//    }
//}
fun NavController.navigateToCategoryList(){
    navigate(route = navigationRouteCategoryList)
}
