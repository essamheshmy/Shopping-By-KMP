package presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircleImage(
    image: String,
    modifier: Modifier,
    onClick: (() -> Unit?)? = null,
    width: Dp = 1.5.dp,
    color: Color = Color.White
) {
    Card(
        modifier = modifier,
        onClick = {
            if (onClick != null) {
                onClick()
            }
        },
        border = if (width == 0.dp) null else BorderStroke(width = width, color = color),
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Image(
            rememberCustomImagePainter(image),
            null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
