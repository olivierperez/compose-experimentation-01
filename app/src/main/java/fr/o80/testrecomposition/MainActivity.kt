package fr.o80.testrecomposition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.o80.testrecomposition.ui.theme.TestRecompositionTheme
import kotlin.random.Random

class Value(
    val id: Int,
    val text: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestRecompositionTheme {
                val items =
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
                        .split(' ')
                        .distinct()
                        .mapIndexed { index, s -> Value(index, s) }

                val rememberedItems = remember { mutableStateListOf(*items.toTypedArray()) }
                var tab by remember { mutableStateOf(0) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(topBar = {
                        TabRow(selectedTabIndex = tab) {
                            Tab(selected = tab == 0, onClick = { tab = 0 }) {
                                Text("Simple", modifier = Modifier.padding(vertical = 16.dp))
                            }
                            Tab(selected = tab == 1, onClick = { tab = 1 }) {
                                Text("WithKey", modifier = Modifier.padding(vertical = 16.dp))
                            }
                            Tab(selected = tab == 2, onClick = { tab = 2 }) {
                                Text("LazyColumn", modifier = Modifier.padding(vertical = 16.dp))
                            }
                        }
                    }) { paddingValues ->
                        when (tab) {
                            0 ->
                                ItemsSimple(
                                    Modifier.padding(paddingValues),
                                    rememberedItems
                                ) { rememberedItems.remove(it) }
                            1 ->
                                ItemsWithKey(
                                    Modifier.padding(paddingValues),
                                    rememberedItems
                                ) { rememberedItems.remove(it) }
                            2 ->
                                ItemsInLazyColumn(
                                    Modifier.padding(paddingValues),
                                    rememberedItems
                                ) { rememberedItems.remove(it) }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun ItemsSimple(modifier: Modifier, items: List<Value>, onRemoveClick: (Value) -> Unit) {
    Column(modifier.verticalScroll(rememberScrollState())) {
        items.forEach {
            Item(it, onRemoveClick)
        }
    }
}

@Composable
fun ItemsWithKey(modifier: Modifier, items: List<Value>, onRemoveClick: (Value) -> Unit) {
    Column(modifier.verticalScroll(rememberScrollState())) {
        items.forEach {
            key(it.id) {
                Item(it, onRemoveClick)
            }
        }
    }
}

@Composable
fun ItemsInLazyColumn(modifier: Modifier, items: List<Value>, onRemoveClick: (Value) -> Unit) {
    LazyColumn(modifier) {
        items(items.size, { items[it].id }) {
            Item(items[it], onRemoveClick)
        }
    }
}

@Composable
fun Item(item: Value, onRemoveClick: (Value) -> Unit) {
    val random = remember { Random.nextInt() }
    Row(
        Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = rememberVectorPainter(Icons.Default.Delete),
            contentDescription = "Delete $item",
            modifier = Modifier.clickable { onRemoveClick(item) }
        )
        Text(item.text, Modifier.padding(8.dp))
        Text(random.toString(), textAlign = TextAlign.End, modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItem() {
    TestRecompositionTheme {
        Item(Value(0, "Demo")) {}
    }
}
