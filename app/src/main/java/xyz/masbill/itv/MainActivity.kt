package xyz.masbill.itv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import xyz.masbill.itv.data.datasource.M3uChannelDataSource
import xyz.masbill.itv.data.repository.ChannelRepositoryImpl
import xyz.masbill.itv.domain.usecase.GetCategoriesUseCase
import xyz.masbill.itv.domain.usecase.GetChannelsUseCase
import xyz.masbill.itv.ui.home.HomeScreen
import xyz.masbill.itv.ui.home.HomeViewModel
import xyz.masbill.itv.ui.player.PlayerScreen
import xyz.masbill.itv.ui.theme.ITVTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataSource = M3uChannelDataSource()
        val repository = ChannelRepositoryImpl(dataSource)
        val getCategoriesUseCase = GetCategoriesUseCase(repository)
        val getChannelsUseCase = GetChannelsUseCase(repository)

        setContent {
            val viewModel = remember {
                HomeViewModel(
                    getCategoriesUseCase = getCategoriesUseCase,
                    getChannelsUseCase = getChannelsUseCase
                )
            }

            ITVTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    TvAppContent(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun TvAppContent(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    val activeChannel = uiState.selectedChannelForPlayer

    if (activeChannel != null) {
        PlayerScreen(
            channel = activeChannel,
            onBack = { viewModel.closePlayer() }
        )
    } else {
        HomeScreen(
            uiState = uiState,
            onCategorySelected = { category ->
                viewModel.selectCategory(category)
            },
            onChannelClick = { channel ->
                viewModel.playChannel(channel)
            }
        )
    }
}
