package dev.shtanko.androidlab.core.designsystem.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EmptyPlaceholder(
    @DrawableRes icon: Int,
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .fillMaxSize()
                .padding(12.dp),
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.size(64.dp),
        )

        Spacer(Modifier.height(12.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true, name = "EmptyPlaceholder - No Data")
@Composable
private fun PreviewEmptyPlaceholderNoData() {
    EmptyPlaceholder(
        icon = android.R.drawable.ic_menu_report_image,
        text = "No data available",
    )
}

@Preview(showBackground = true, name = "EmptyPlaceholder - Error")
@Composable
private fun PreviewEmptyPlaceholderError() {
    EmptyPlaceholder(
        icon = android.R.drawable.ic_delete,
        text = "Something went wrong",
    )
}

@Preview(showBackground = true, name = "EmptyPlaceholder - No Internet")
@Composable
private fun PreviewEmptyPlaceholderNoInternet() {
    EmptyPlaceholder(
        icon = android.R.drawable.ic_dialog_alert,
        text = "No internet connection",
    )
}
