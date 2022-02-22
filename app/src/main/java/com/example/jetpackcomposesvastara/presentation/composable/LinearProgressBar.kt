package com.example.jetpackcomposesvastara.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcomposesvastara.R
import com.google.android.material.progressindicator.LinearProgressIndicator


@Composable
fun LinearProgressBar(progress: Float,
                      progressText: String,
                      progressColor: Color,
                      bgColor: Color,
                      textColor: Color,
                      text: String,
                      isLinearProgressIndicatorVisible: Boolean)
{
    Column {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,) {
            TextCustom(
                text = text,
                modifier = Modifier
                    .padding(start = 6.dp),
                textColor =textColor ,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp)
            TextCustom(
                text = progressText,
                modifier = Modifier
                    .padding(end = 6.dp),
                textColor =textColor ,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp)
        }
        if(isLinearProgressIndicatorVisible)
            LinearProgressIndicator(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(2.dp))
                    .width(4.dp),
                progress = progress,
                color = progressColor,
                backgroundColor = bgColor
            )
    }

}

@Composable
fun TextCustom(
    text: String,
    modifier: Modifier,
    textColor: Color,
    fontWeight: FontWeight?,
    fontSize: TextUnit
)
{
    Text(
        text = text,
        modifier = modifier,
        color = textColor,
        fontWeight = fontWeight,
        fontSize = fontSize
    )
}

@Preview()
@Composable
fun PreviewLinearProgressbar() {
    LinearProgressBar(
        progress = (6f / 10),
        progressText = "6/10",
        progressColor = colorResource(id = R.color.steps_progress),
        bgColor = colorResource(id = R.color.progress_bg),
        textColor = colorResource(id = R.color.steps_progress),
        text = "Steps",
        true
    )
}