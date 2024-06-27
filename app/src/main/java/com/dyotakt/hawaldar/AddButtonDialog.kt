package com.dyotakt.hawaldar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

class AddButtonDialog(setShowDialog: () -> Unit) {

    @Composable
    fun ButtonDialog(setShowDialog: (Boolean)->Unit) {
        Dialog(onDismissRequest = { setShowDialog(false) }) {
            Surface (
                shape = RoundedCornerShape(18.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth(0.8f)
            ){
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp) )
                    {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Add", modifier = Modifier
                                    .align(Alignment.CenterVertically),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Default,
                                    fontWeight = FontWeight.Bold
                                )

                            )
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close the add dialog",
                                modifier = Modifier
                                    .height(15.dp)
                                    .width(15.dp)
                                    .clickable { setShowDialog(false) }
                            )
                        }
                    }
                }
            }
        }
    }
}