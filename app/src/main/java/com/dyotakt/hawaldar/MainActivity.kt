package com.dyotakt.hawaldar

import android.R.color
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dyotakt.hawaldar.ui.theme.HawaldarTheme
import java.util.Calendar


class MainActivity : ComponentActivity() {

    private val s by viewModels<MainViewModel>()

    private var seconds by mutableIntStateOf(0)


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HawaldarTheme {
                // A surface container using the 'background' color from the theme
                var countOfItems = remember {
                    mutableStateOf(10)
                }

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 0.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            MediumTopAppBar(
                                title = {
                                    Text(
                                        text = "HAWALDAR",
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold
                                    )},
                                actions = {

                                }
                            )

                        }
                    ) { innerPadding ->
                        val myList by s.myList.observeAsState(listOf())

                        Column {
                            MainScreen(
                                countOfItems,
                                myList,
                                innerPadding,
                                s,
                                seconds,
                                applicationContext
                            )
                            fun immediateExecutor(runnable: () -> Unit) {
                                runnable()
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MainScreen(
    countOfItems: MutableState<Int>,
    listOfItems: List<items>?,
    innerPadding: PaddingValues,
    s: MainViewModel,
    seconds: Int?,
    context: Context?
) {
    val calender: Calendar = Calendar.getInstance();
//    var secsIn30 by remember{mutableStateOf((calender.get(Calendar.SECOND))%30)}
    val scrollState = rememberScrollState()

    Column (modifier = Modifier
        .padding(innerPadding)
        .verticalScroll(scrollState)){
        AuthView(listOfItems = listOfItems, s, context)
    }
}




@Composable
fun AuthView (listOfItems: List<items>?, s: MainViewModel, context: Context?) {
    val myList by s.myList.observeAsState(listOf())
    val seconds = s.tickerFlow().collectAsState(initial = 30)
    var clickedItem by rememberSaveable { mutableStateOf<Int?>(null) }
    myList.forEach{items ->
        Box(modifier = Modifier
            .shadow(elevation = 0.dp, shape = RoundedCornerShape(30.dp))
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .fillMaxWidth()
            .height(height = 100.dp)
            .clip(shape = RoundedCornerShape(18.dp))
            .background(ConvertToColor(items.backgroundColor))
            .clickable {clickedItem = items.otp}) {
            Row(modifier = Modifier.padding(10.dp)) {
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    var otp by remember{mutableStateOf(items.otp)}
                    Text(
                        text = "${items.name}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = getFontColorDynamic(items.backgroundColor)
                    )
                    Text(
                        text = threeSpaceThree(String.format("%06d",items.otp)),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = getFontColorDynamic(items.backgroundColor)
                    )
                }
                Icon(Icons.Default.CheckCircle, contentDescription = "Icon for Auth item", modifier = Modifier
                    .wrapContentSize(unbounded = true)
                    .size(130.dp)
                    .offset(x = 10.dp, y = 10.dp)
                    .align(Alignment.CenterVertically)
                    .alpha(0.2f),
                    tint = getFontColorDynamic(items.backgroundColor)
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(modifier = Modifier.align(Alignment.TopEnd),text = seconds.value.toString(), fontSize = 30.sp, fontWeight = FontWeight.Bold, color = getFontColorDynamicExtraAlpha(items.backgroundColor))
                }
                if(clickedItem!=null) {
                    Toast.makeText(context, clickedItem.toString(), Toast.LENGTH_LONG).show()
                    clickedItem = null
                }
            }

        }
    }
}

fun ConvertToColor(backgroundColor: String): Color {
    val red: Int = Integer.parseInt(backgroundColor.take(3))
    val green: Int = Integer.parseInt(backgroundColor.takeLast(6).take(3))
    val blue: Int = Integer.parseInt(backgroundColor.takeLast(3))
    return Color(red, green, blue)
}

fun getFontColorDynamic(backgroundColor: String): Color {
    val red: Int = Integer.parseInt(backgroundColor.take(3))
    val green: Int = Integer.parseInt(backgroundColor.takeLast(6).take(3))
    val blue: Int = Integer.parseInt(backgroundColor.takeLast(3))
    val averageColor: Int = ((red+green+blue))/3
    return if(averageColor>127) {
        Color(0,0,0,100)
    } else {
        Color(255,255,255,100)
    }
}

fun getFontColorDynamicExtraAlpha(backgroundColor: String): Color {
    val red: Int = Integer.parseInt(backgroundColor.take(3))
    val green: Int = Integer.parseInt(backgroundColor.takeLast(6).take(3))
    val blue: Int = Integer.parseInt(backgroundColor.takeLast(3))
    val averageColor: Int = ((red+green+blue))/3
    return if(averageColor>127) {
        Color(0,0,0,30)
    } else {
        Color(255,255,255,30)
    }
}

fun threeSpaceThree(originalString: String): String {
    return (originalString[0] + "" + originalString[1] + "" + originalString[2] + " " + originalString[3] + "" + originalString[4] + "" + originalString[5])
}