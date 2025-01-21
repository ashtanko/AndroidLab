package dev.shtanko.androidlab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme

@Composable
fun FirstScreen(
    onNavigateToNextScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(15.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            stringResource(id = R.string.first_screen_description),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Button(onClick = {
            onNavigateToNextScreen()
        }) {
            Text(stringResource(id = R.string.navigate_to_second_screen))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FirstScreenPreview() {
    AndroidLabTheme {
        FirstScreen(onNavigateToNextScreen = {})
    }
}
