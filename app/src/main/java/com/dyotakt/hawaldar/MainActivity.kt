package com.dyotakt.hawaldar

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
//import androidx.compose.foundation.gestures.ModifierLocalScrollableContainerProvider.value
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dyotakt.hawaldar.ui.theme.HawaldarTheme

private val cl: ColorLogics = ColorLogics()
private val tl: TextLogics = TextLogics()
val dataMan: DataMan = DataMan
var context: Context? = null

class MainActivity : ComponentActivity() {

    private val s by viewModels<MainViewModel>()

    private var seconds by mutableIntStateOf(0)


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        dataMan.init(this)
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
//                    color = MaterialTheme.colorScheme.background
//                    color = Color.Black
                ) {
                    Scaffold(
                        topBar = {
                            val showDialog =  remember { mutableStateOf(false) }
                            val dynamicAppName = remember { mutableStateOf("HAWALDAR") }
                            if(showDialog.value) {
                                ButtonDialog(setShowDialog = {
                                    showDialog.value = it
                                },s)
                            }
                            MediumTopAppBar(
                                title = {
                                    Text(
                                        text = dynamicAppName.value,
                                        fontSize = 30.sp,
//                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal=20.dp)
                                    )},
                                actions = {
                                    Button(onClick = {
                                        showDialog.value = true
                                    }) {
                                        Icon(Icons.Default.AddCircle, contentDescription = "Add")
                                    }
//                                    Icon(
//                                        Icons.Default.Add,
//                                        contentDescription = "Add",
//                                        modifier = Modifier
//                                            .height(40.dp)
//                                            .width(40.dp)
//                                            .padding(50.dp)
//                                            .clickable { showDialog.value = true })
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.background,
//                                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
//                                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
//                                    actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                                ),
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
    myList?.forEach{items ->
        Box(modifier = Modifier
            .shadow(elevation = 0.dp, shape = RoundedCornerShape(30.dp))
            .padding(horizontal = 30.dp, vertical = 15.dp)
            .fillMaxWidth()
            .height(height = 100.dp)
            .clip(shape = RoundedCornerShape(18.dp))
            .background(cl.ConvertToColor(items.backgroundColor))
            .clickable { clickedItem = items.otp }) {
            Row(modifier = Modifier.padding(10.dp)) {
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    var otp by remember{mutableStateOf(items.otp)}
                    Text(
                        text = "${items.name}",
                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
                        color = cl.getFontColorDynamic(items.backgroundColor)
                    )
                    Text(
                        text = tl.threeSpaceThree(String.format("%06d",items.otp)),
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        color = cl.getFontColorDynamic(items.backgroundColor)
                    )
                }
                Icon(Icons.Default.CheckCircle, contentDescription = "Icon for Auth item", modifier = Modifier
                    .wrapContentSize(unbounded = true)
                    .size(120.dp)
                    .offset(x = 10.dp, y = 10.dp)
                    .align(Alignment.CenterVertically)
                    .alpha(0.2f),
                    tint = cl.getFontColorDynamic(items.backgroundColor)
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(modifier = Modifier.align(Alignment.TopEnd),text = seconds.value.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = cl.getFontColorDynamicExtraAlpha(items.backgroundColor))
                }
                if(clickedItem!=null) {
                    Toast.makeText(context, clickedItem.toString(), Toast.LENGTH_LONG).show()
                    clickedItem = null
                }
            }

        }
    }
}

@Composable
fun ButtonDialog(setShowDialog: (Boolean) -> Unit, s: MainViewModel) {
    val txtFieldError = remember { mutableStateOf("") }
    val txtField = remember { mutableStateOf("") }
    val keyField = remember { mutableStateOf("") }

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface (
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth(0.9f)
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
                    Spacer(modifier = Modifier.height(20.dp))
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        placeholder = {Text("Enter Label")},
                        value = txtField.value,
                        onValueChange = {
                            txtField.value = it
                        })
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        placeholder = {Text("Enter Key")},
                        value = keyField.value,
                        onValueChange = {
                            keyField.value = it
                        })
                    Spacer(modifier = Modifier.height(20.dp))
                    Row( modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add from Camera",
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)
                                .clickable { } //Logic to be added
                        )
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Enter",
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)
                                .clickable {
                                    dataMan.saveString(txtField.value,keyField.value)
                                    s.fetchAndPopulateData()
                                    setShowDialog(false)
                                }
                        )
                    }
                }
            }
        }
    }
}