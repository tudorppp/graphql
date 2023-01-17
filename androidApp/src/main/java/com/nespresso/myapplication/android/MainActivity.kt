package com.nespresso.myapplication.android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.nespresso.myapplication.Greeting
import com.nespresso.myapplication.module.CartModule
import com.nespresso.myapplication.module.CustomerModule
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GreetingView(Greeting().greet()) {
//                        lifecycleScope.launch {
//                            CatalogModule.products(
//                                listOf("MTYz", "MTgy"),
//                                listOf(
//                                    "color",
//                                    "intensity",
//                                    "aromatic_profile",
//                                    "aromatic_filter",
//                                    "vertuo_cup_size",
//                                    "cup_size_dup"
//                                )
//                            )
//                        }
                        lifecycleScope.launch {

                            val response = CustomerModule.resetPassword("nglmbltst+uaepre@gmail.com")
                            //println("dadadada response is $response")
//                            val response = CartModule.addCoupon(
//                                CartModule.CartType.Guest("AxRnTRanZVPS7yNwQKB3DYeDG41GoFrc"),
//                                "someCoupon"
//                            )
//                            val response = CartModule.addProductsMutation(
//                                CartModule.CartType.Guest("AxRnTRanZVPS7yNwQKB3DYeDG41GoFrc"),
//                                products = listOf(
//                                    CartModule.ProductType.Simple("6723870", 5),
//                                    CartModule.ProductType.Simple("12472050", 5),
//                                )
//                            )
//                            val response = CartModule.updateItems(
//                                CartModule.CartType.Guest("AxRnTRanZVPS7yNwQKB3DYeDG41GoFrc"),
//                                items = listOf(5235009 to 5, 5235010 to 7)
//                            )
//                            val response = CartModule.removeItemMutation(
//                                CartModule.CartType.Guest("yyFGD7IQMYEuk9Dmg2cH31jocw0MGcoN"),
//                                5234986
//                            )
                            //val response = CartModule.cartQuery(CartModule.CartType.Guest("yyFGD7IQMYEuk9Dmg2cH31jocw0MGcoN"))
                            //val response = CartModule.cartQuery(CartModule.CartType.Guest("yyFGD7IQMYEuk9Dmg2cH31jocw0MGcoN"))
                            //val response = CartModule.createCartMutation()
                            //val response = AdditionalModule.telephonePrefixQuery()
                            //val response = AdditionalModule.currencyQuery()
                            //val response = AdditionalModule.regionsQuery()
                            //val response = AdditionalModule.districtFinderQuery()
                            //val response = AdditionalModule.areaFinderQuery()
                            //val response = AdditionalModule.storeConfigQuery()
                            //val response = AdditionalModule.cmsBlocksQuery(listOf("privacy-policy","faq"))
                            Log.d("dada", response.toString())
                            //CatalogModule.categoryListQuery(listOf(10))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GreetingView(text: String, onClick: () -> Unit) {
    Text(
        modifier = Modifier.clickable {
            onClick()
        },
        text = text
    )
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!") {}
    }
}
