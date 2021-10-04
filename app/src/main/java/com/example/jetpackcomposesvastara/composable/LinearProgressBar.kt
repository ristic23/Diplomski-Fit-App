package com.example.jetpackcomposesvastara.composable

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposesvastara.R
import org.w3c.dom.Text


@Composable
fun LinearProgressBar(maxValue: Int, progressValue: Int,
                      progressColor: Color, bgColor: Color,
                      textColor: Color, text: String)
{
    Column (modifier = Modifier.background(Color.White)){
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,) {
            TextCustom(
                text = text,
                modifier = Modifier
                    .padding(start = 6.dp),
                textColor =textColor ,
                fontWeight = FontWeight.Bold)
            TextCustom(
                text = "$progressValue/$maxValue",
                modifier = Modifier
                    .padding(end = 6.dp),
                textColor =textColor ,
                fontWeight = FontWeight.Bold)
        }
        LinearProgressIndicator(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(2.dp))
                .width(4.dp),
            progress = (progressValue / maxValue.toFloat()),
            color = progressColor,
            backgroundColor = bgColor
        )
    }

}

@Composable
fun TextCustom(text: String, modifier: Modifier, textColor: Color, fontWeight: FontWeight?)
{
    Text(
        text = text,
        modifier = modifier,
        color = textColor,
        fontWeight = fontWeight
    )
}

@Preview()
@Composable
fun PreviewLinearProgressbar() {
    LinearProgressBar(
        maxValue = 10,
        progressValue = 6,
        progressColor = colorResource(id = R.color.steps_progress),
        bgColor = colorResource(id = R.color.progress_bg),
        textColor = colorResource(id = R.color.progress_text_color),
        text = "Steps"
    )
}