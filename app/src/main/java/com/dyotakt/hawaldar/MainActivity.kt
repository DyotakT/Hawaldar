package com.dyotakt.hawaldar

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxScopeInstance.align
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dyotakt.hawaldar.ui.theme.HawaldarTheme
import kotlinx.coroutines.delay

private val cl: ColorLogics = ColorLogics()
private val tl: TextLogics = TextLogics()
val dataMan: DataMan = DataMan
var context: Context? = null

class MainActivity : ComponentActivity() {

    private val s by viewModels<MainViewModel>()
    private var seconds by mutableIntStateOf(0)
//    val clipboardManager: clipboardManager


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
                val editMode = remember { mutableStateOf(false) }


                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 0.dp),
                ) {
                    Scaffold(
                        topBar = {
                            val dynamicAppName = remember { mutableStateOf("HAWALDAR") }
                            val darkThemeEnabled = isSystemInDarkTheme()
                            MediumTopAppBar(
                                title = {
                                    Row(modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = dynamicAppName.value,
                                            fontSize = 30.sp,
                                            modifier = Modifier
                                                .padding(horizontal = 20.dp)
                                                .clickable {
                                                    editMode.value = !editMode.value
                                                    if (editMode.value) {
                                                        dynamicAppName.value = "EDIT MODE"
                                                    } else {
                                                        dynamicAppName.value = "HAWALDAR"
                                                    }
                                                }
                                        )
                                        Spacer(Modifier.width(15.dp))
                                    }
                                },
                                actions = {    },
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
                                applicationContext,
                                editMode
                            )
//                            fun immediateExecutor(runnable: () -> Unit) {
//                                runnable()
//                            }
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
    context: Context?,
    editMode: MutableState<Boolean>,
    ){
    val scrollState = rememberScrollState()
    Column (modifier = Modifier
        .padding(innerPadding)
        .verticalScroll(scrollState)){
        listOfItems?.forEach {items ->
            if(!editMode.value) AuthView(items, s, context)
            else {
                ChangeableAuthView(items, s, context)
            }
        }
        if(editMode.value) AddNewItem(s)
    }
}

@Composable
fun AddNewItem(s: MainViewModel) {
    val showDialog = remember { mutableStateOf(false) }
    Box(modifier = Modifier
        .shadow(elevation = 0.dp, shape = RoundedCornerShape(30.dp))
        .padding(horizontal = 30.dp, vertical = 15.dp)
        .fillMaxWidth()
        .height(height = 100.dp)
        .clip(shape = RoundedCornerShape(18.dp))
        .background(Color(128, 128, 128, 55))
        .clickable { showDialog.value = true }
    ) {
        Icon(Icons.Default.AddCircle, contentDescription = "Icon for Auth item", modifier = Modifier
            .wrapContentSize(unbounded = true)
            .size(40.dp)
            .align(Alignment.Center)
            .alpha(0.2f)
        )
        if (showDialog.value) {
            ButtonDialog(setShowDialog = {
                showDialog.value = it
            }, s)
        }
    }
}



@Composable
fun AuthView (currentItem: items, s: MainViewModel, context: Context?) {
    val seconds = s.tickerFlow().collectAsState(initial = 30)
    var clickedItem by rememberSaveable { mutableStateOf<items?>(null) }
    Box(modifier = Modifier
        .shadow(elevation = 0.dp, shape = RoundedCornerShape(30.dp))
        .padding(horizontal = 30.dp, vertical = 15.dp)
        .fillMaxWidth()
        .height(height = 100.dp)
        .clip(shape = RoundedCornerShape(18.dp))
        .background(cl.ConvertToColor(currentItem.backgroundColor))
        .clickable {
            clickedItem = currentItem
        }) {
        Row(modifier = Modifier.padding(10.dp)) {
            Column(modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically)) {
                var otp by remember{mutableStateOf(currentItem.otp)}
                if(currentItem == clickedItem) {
                    Text(
                        text = "Copied!",
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        color = cl.getFontColorDynamicExtraAlpha(currentItem.backgroundColor)
                    )
                    LaunchedEffect(this) {
                        //Copy logic to be placed here
                        delay(1000)
                        clickedItem = null
                    }
                } else {
                    Text(
                        text = currentItem.name,
                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
                        color = cl.getFontColorDynamic(currentItem.backgroundColor)
                    )
                    Text(
                        text = tl.threeSpaceThree(String.format("%06d", currentItem.otp)),
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        color = cl.getFontColorDynamic(currentItem.backgroundColor)
                    )
                }
            }
            Icon(Icons.Default.CheckCircle, contentDescription = "Icon for Auth item", modifier = Modifier
                .wrapContentSize(unbounded = true)
                .size(120.dp)
                .offset(x = 10.dp, y = 10.dp)
                .align(Alignment.CenterVertically)
                .alpha(0.2f),
                tint = cl.getFontColorDynamic(currentItem.backgroundColor)
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(modifier = Modifier.align(Alignment.TopEnd),
                    text = seconds.value.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = cl.getFontColorDynamicExtraAlpha(currentItem.backgroundColor)
                )
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeableAuthView(currentItem: items, s: MainViewModel, context: Context?) {
    val seconds = s.tickerFlow().collectAsState(initial = 30)
    var clickedItem by rememberSaveable { mutableStateOf<items?>(null) }
    val showMore = remember { mutableStateOf(false) }
    val boxHeight = remember { mutableStateOf(140.dp) }
    val darkMode = isSystemInDarkTheme() //Need to go over this again later
    var dropDownDynamicIcon = Icons.Default.KeyboardArrowDown //Need to go over this again later
    Box(
        modifier = Modifier
            .padding(horizontal = 30.dp, vertical = 15.dp)
            .animateContentSize()
            .fillMaxWidth()
            .height(boxHeight.value)
            .clip(shape = RoundedCornerShape(18.dp))
            .background(cl.getDarkerShade(currentItem.backgroundColor))
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Box(modifier = Modifier
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(18.dp))
                .fillMaxWidth()
                .height(height = 100.dp)
                .clip(shape = RoundedCornerShape(18.dp))
                .background(cl.ConvertToColor(currentItem.backgroundColor))
                .clickable {
                    clickedItem = currentItem
                }) {
                Row(modifier = Modifier.padding(10.dp)) {
                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .align(Alignment.CenterVertically)
                    ) {
//                    var otp by remember{mutableStateOf(currentItem.otp)}
                        if (currentItem == clickedItem) {
                            Text(
                                text = "Copied!",
                                fontSize = 35.sp,
                                fontWeight = FontWeight.Bold,
                                color = cl.getFontColorDynamicExtraAlpha(currentItem.backgroundColor)
                            )
                            LaunchedEffect(this) {
                                //Copy logic to be placed here
                                delay(1000)
                                clickedItem = null
                            }
                        } else {
                            Text(
                                text = currentItem.name,
                                fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
                                color = cl.getFontColorDynamic(currentItem.backgroundColor)
                            )
                            Text(
                                text = tl.threeSpaceThree(String.format("%06d", currentItem.otp)),
                                fontSize = 35.sp,
                                fontWeight = FontWeight.Bold,
                                color = cl.getFontColorDynamic(currentItem.backgroundColor)
                            )
                        }
                    }
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Icon for Auth item",
                        modifier = Modifier
                            .wrapContentSize(unbounded = true)
                            .size(120.dp)
                            .offset(x = 10.dp, y = 10.dp)
                            .align(Alignment.CenterVertically)
                            .alpha(0.2f),
                        tint = cl.getFontColorDynamic(currentItem.backgroundColor)
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            modifier = Modifier.align(Alignment.TopEnd),
                            text = seconds.value.toString(),
                            fontSize = 20.sp, fontWeight =
                            FontWeight.Bold,
                            color = cl.getFontColorDynamicExtraAlpha(currentItem.backgroundColor)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Spacer(Modifier)
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "More",
                    modifier = Modifier
                        .clickable {
                            showMore.value = !showMore.value
                            if (showMore.value) boxHeight.value = 470.dp else boxHeight.value =
                                140.dp
                        }
                        .size(40.dp),
                    tint = cl.ConvertToColor(currentItem.backgroundColor),

                    )
                Spacer(Modifier)
            }
            if (showMore.value) {
                Box(modifier = Modifier) {
                    Column(modifier = Modifier.padding(start = 25.dp, end = 25.dp, bottom = 25.dp)) {
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = cl.getFontColorDynamic(currentItem.backgroundColor),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(100.dp),
                            //                    color = Color(120,20,20),
                            placeholder = { Text("Enter Key") },
                            value = currentItem.name,
                            onValueChange = {
                                currentItem.name = it
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = cl.getFontColorDynamic(currentItem.backgroundColor),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(100.dp),
                            //                    color = Color(120,20,20),
                            placeholder = { Text("Enter Key") },
                            value = currentItem.data,
                            onValueChange = {
                                currentItem.data = it
                            }
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Icon for Auth item",
                                modifier = Modifier
                                    .wrapContentSize(unbounded = true)
                                    .size(80.dp)
                                    .align(Alignment.CenterVertically)
                                    .alpha(0.2f),
                                tint = cl.getFontColorDynamic(currentItem.backgroundColor)
                            )
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "Icons for color pallete",
                                modifier = Modifier
                                    .wrapContentSize(unbounded = true)
                                    .size(80.dp)
                                    .align(Alignment.CenterVertically),
                                tint = cl.ConvertToColor(currentItem.backgroundColor)
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Icon(
                                Icons.Default.Delete,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable {  }, //Deletion logic to be written here
                                contentDescription = "Delete Icon for Auth item",
                                tint = Color(255, 20, 20,128)
                            )
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Save Icon for Auth item",
                                modifier = Modifier.size(40.dp),
                                tint = Color(20,255,20, 128)
                            )
                        }
                    }
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
//            color = MaterialTheme.colorScheme.background,
            color = cl.getDifferentShade(MaterialTheme.colorScheme.background),
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
                                fontSize = 22.sp,
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
                                .clickable {
                                    Toast
                                        .makeText(context, "Coming soon..", Toast.LENGTH_SHORT)
                                        .show() //To be added
                                }
                        )
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Enter",
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)
                                .clickable {
                                    dataMan.saveString(txtField.value, keyField.value)
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