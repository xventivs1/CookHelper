package ru.tech.cookhelper.presentation.dishes_based_on_fridge

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.twotone.Error
import androidx.compose.material.icons.twotone.FilterAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.olshevski.navigation.reimagined.hilt.hiltViewModel
import ru.tech.cookhelper.R
import ru.tech.cookhelper.presentation.app.components.Loading
import ru.tech.cookhelper.presentation.app.components.Placeholder
import ru.tech.cookhelper.presentation.app.components.Size
import ru.tech.cookhelper.presentation.app.components.TopAppBar
import ru.tech.cookhelper.presentation.dishes_based_on_fridge.viewModel.OnFridgeBasedDishesViewModel
import ru.tech.cookhelper.presentation.recipes_list.components.RecipeItem

@ExperimentalMaterial3Api
@Composable
fun OnFridgeBasedDishes(
    onRecipeClicked: (id: Int) -> Unit,
    onBack: () -> Unit,
    viewModel: OnFridgeBasedDishesViewModel = hiltViewModel()
) {

    val state = viewModel.dishes.value

    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior by remember {
        mutableStateOf(
            TopAppBarDefaults.pinnedScrollBehavior(
                topAppBarState
            )
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    size = Size.Small,
                    title = { Text(stringResource(R.string.matched_recipes)) },
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(Icons.Rounded.ArrowBack, null)
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (state.recipeList?.isNotEmpty() == true) {
                    LazyColumn {
                        items(state.recipeList) { item ->
                            Row {
                                Text(
                                    "${item.second}%",
                                    modifier = Modifier.padding(top = 10.dp, start = 10.dp)
                                )
                                RecipeItem(item.first) {
                                    onRecipeClicked(it)
                                }
                            }
                        }
                    }
                } else if (!state.isLoading) {
                    Placeholder(
                        icon = Icons.TwoTone.FilterAlt,
                        text = stringResource(R.string.empty_matched_recipes)
                    )
                }

                if (state.error.isNotBlank()) {
                    Placeholder(icon = Icons.TwoTone.Error, text = state.error)
                }

                if (state.isLoading) {
                    Loading()
                }
            }
        }
    }

    BackHandler { onBack() }

}