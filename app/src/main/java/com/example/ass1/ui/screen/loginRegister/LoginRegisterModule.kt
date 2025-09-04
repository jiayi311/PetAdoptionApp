package com.example.ass1.ui.screen.loginRegister

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.ass1.R
import com.example.ass1.ui.theme.comicSansFontFamily
import com.example.ass1.ui.theme.moreSugarFontFamily
import com.example.ass1.ui.theme.poppinsFontFamily


@Composable
fun LoginRegisterModule(
    content: @Composable () -> Unit
) {

    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            content()
        }
    }
}

@Composable
fun LoginSignUpBackground(modifier : Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.login_signup_background),
        contentDescription = "Background With Paws",
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4e84cc)),
        contentScale = ContentScale.FillBounds
    )
}

@Composable
fun FureverFriendsTitle(modifier : Modifier = Modifier) {

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 36.dp),
    ) {
        Text(
            text = "Furever",
            style = TextStyle(
                fontSize = 48.sp,
                fontFamily = moreSugarFontFamily,
                color = Color(0xFF0c4485),
            ),
            lineHeight = 20.sp
        )
        Text(
            text = "Friends",
            style = TextStyle(
                fontSize = 48.sp,
                fontFamily = moreSugarFontFamily,
                color = Color(0xFF0c4485)
            ),
            lineHeight = 20.sp
        )
    }
}

@Composable
fun ImageOnLoginRegisterPage(image: Painter, modifier : Modifier = Modifier){
    //Dog Image
    Box(
        modifier = Modifier
            .zIndex(1f)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = image,
            contentDescription = "Dog Image",
            modifier = modifier
        )
    }
}

@Composable
fun Subtitle(string: String, modifier: Modifier = Modifier){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = string,
            style = TextStyle(
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            ),
        )

        Image(
            painter = painterResource(R.drawable.white_paw),
            contentDescription = "White Paw",
            modifier = Modifier
                .size(48.dp)
                .fillMaxHeight(),)

    }
}

@Composable
fun PasswordRequirement(requirement: String, modifier: Modifier = Modifier, color: Color){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = " • " + requirement,
            style = TextStyle(
                color = color,
                fontSize = 16.sp,
                fontFamily = comicSansFontFamily
            )
        )
    }
}
