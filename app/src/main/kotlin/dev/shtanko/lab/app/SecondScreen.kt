package dev.shtanko.lab.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shtanko.androidlab.core.designsystem.theme.AndroidLabTheme

@Composable
fun SecondScreen(
    onNavigateToNextScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDetail by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(15.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            stringResource(id = R.string.second_screen_description),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = {
                showDetail = !showDetail
            },
        ) {
            Text(stringResource(id = R.string.button_show_details))
        }
        if (showDetail) {
            Text(
                stringResource(id = R.string.second_screen_detail_text),
            )
        }
        Button(onClick = {
            onNavigateToNextScreen()
        }) {
            Text(stringResource(id = R.string.navigate_to_third_screen))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SecondScreenPreview() {
    AndroidLabTheme {
        SecondScreen(onNavigateToNextScreen = {})
    }
}
