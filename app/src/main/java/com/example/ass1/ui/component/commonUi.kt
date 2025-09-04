package com.example.ass1.ui.component

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandCircleDown
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ass1.R
import com.example.ass1.model.TabItem
import com.example.ass1.ui.theme.poppinsFontFamily
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.Timestamp
import java.io.ByteArrayOutputStream
import java.util.Calendar

@Composable
fun SetSystemNavColor(navColor: Long, wifiColor: Long) {
    val systemUiController = rememberSystemUiController()
    val navBarColor = Color(color = navColor)
    val wifiBarColor = Color(color = wifiColor)

    SideEffect {
        systemUiController.setNavigationBarColor(
            color = navBarColor,
            darkIcons = false
        )

        systemUiController.setStatusBarColor(
            color = wifiBarColor,
            darkIcons = true
        )
    }

}

@Composable
fun SubmitReportDialog(
    onReportAgain: () -> Unit,
    onClickCloseDialog: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title:String,
    text:String,
    confirmButtonText:String
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = ""
            )},
        onDismissRequest = {},
        title = { Text(text = title) },
        text = { Text(text = text) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onClickCloseDialog) {
                Text(text = stringResource(R.string.close))
            }
        },
        confirmButton = {
            TextButton(onClick = onReportAgain) {
                Text(text = confirmButtonText)
            }
        }
    )
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MyTopBar(
//    title:String,
//    onNavigateBack: () -> Unit,
//    modifier:Modifier,
//    isTablet: Boolean,
//    isLandscape: Boolean
//) {
//    if (isTablet) {
//        TopAppBar(
//            title = {
//                Box(
//                    modifier = Modifier.fillMaxWidth(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = title,
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold,
//                        fontFamily = poppinsFontFamily,
//                        fontSize = 36.sp
//                    )
//                }
//            },
//
//            navigationIcon = {
//                IconButton(onClick = { onNavigateBack() }) {
//                    Icon(
//                        imageVector = Icons.Default.ExpandCircleDown,
//                        contentDescription = "Back",
//                        tint = Color.White,
//                        modifier = Modifier
//                            .size(36.dp)
//                            .rotate(90f)
//                    )
//                }
//            },
//
//            actions = {
//                IconButton(onClick = { /* Handle menu */ }) {
//                    Icon(
//                        imageVector = Icons.Rounded.Menu,
//                        contentDescription = "Menu",
//                        tint = Color.Transparent,
//                        modifier = Modifier.size(44.dp)
//                    )
//                }
//            },
//
//            modifier = modifier
//        )
//    } else {
//        TopAppBar(
//            title = {
//                Box(
//                    modifier = Modifier.fillMaxWidth(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = title,
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold,
//                        fontFamily = poppinsFontFamily,
//                        fontSize = 28.sp
//                    )
//                }
//            },
//
//            navigationIcon = {
//                IconButton(onClick = { onNavigateBack() }) {
//                    Icon(
//                        imageVector = Icons.Default.ExpandCircleDown,
//                        contentDescription = "Back",
//                        tint = Color.White,
//                        modifier = Modifier
//                            .size(28.dp)
//                            .rotate(90f)
//                    )
//                }
//            },
//
//            actions = {
//                IconButton(onClick = { /* Handle menu */ }) {
//                    Icon(
//                        imageVector = Icons.Rounded.Menu,
//                        contentDescription = "Menu",
//                        tint = Color.Transparent,
//                        modifier = Modifier.size(36.dp)
//                    )
//                }
//            },
//
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = Color(0xFF4A7ABF)
//            ),
//
//            modifier = modifier
//        )
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    title:String,
    onNavigateBack: () -> Unit,
    modifier:Modifier,
    isTablet: Boolean,
    isLandscape: Boolean
) {
    if (isTablet) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily,
                        fontSize = 36.sp
                    )
                }
            },

            navigationIcon = {
                IconButton(onClick = { onNavigateBack() }) {
                    Icon(
                        imageVector = Icons.Default.ExpandCircleDown,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .size(36.dp)
                            .rotate(90f)
                    )
                }
            },

            actions = {
                IconButton(onClick = { /* Handle menu */ }) {
                    Icon(
                        imageVector = Icons.Rounded.Menu,
                        contentDescription = "Menu",
                        tint = Color.Transparent,
                        modifier = Modifier.size(44.dp)
                    )
                }
            },

            modifier = modifier
        )
    } else {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsFontFamily,
                        fontSize = 28.sp
                    )
                }
            },

            navigationIcon = {
                IconButton(onClick = { onNavigateBack() }) {
                    Icon(
                        imageVector = Icons.Default.ExpandCircleDown,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .rotate(90f)
                    )
                }
            },

            actions = {
                IconButton(onClick = { /* Handle menu */ }) {
                    Icon(
                        imageVector = Icons.Rounded.Menu,
                        contentDescription = "Menu",
                        tint = Color.Transparent,
                        modifier = Modifier.size(36.dp)
                    )
                }
            },

            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF4A7ABF)
            ),

            modifier = modifier
        )
    }
}

@Composable
fun TabBar(
    tabs: List<TabItem>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    isTablet: Boolean,
    isLandscape: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF4A7ABF))
    ) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                title = tab.title,
                isSelected = selectedTab == index,
                isTablet,
                isLandscape,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .clickable(onClick = {
                        onTabSelected(index)
                        tab.command()
                    })
                    .background(
                        if (selectedTab == index) Color.White else Color(0xFF4A7ABF)
                    )
                    .padding(
                        vertical = 12.dp,
                        // horizontal = if (tabs.size <= 2) 28.dp else if (tabs.size <= 4) 16.dp else 12.dp
                        horizontal = if (tabs.size <= 2) 28.dp else if (tabs.size <= 4) 8.dp else 12.dp
                    ),
            )
        }
    }
}

@Composable
fun Tab(
    title: String,
    isSelected: Boolean,
    isTablet: Boolean,
    isLandscape: Boolean,
    modifier: Modifier = Modifier
) {
    if (isTablet) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,

            ) {
            Text(
                text = title,
                color = if (isSelected) Color.Black else Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                maxLines = 1,
            )
        }
    } else {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,

            ) {
            Text(
                text = title,
                color = if (isSelected) Color.Black else Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                maxLines = 1,
            )
        }
    }
}

@Composable
fun AdminBottomBar(
    onClickHome: () -> Unit,
    onClickCommunity: () -> Unit,
    selectedItem: Int
    //when call this func, pass a int to indicate which icon is selected / click by user
    //if none of them, then pass integer 0
    //when user select pet community pass integer 2, home pass int 1
) {
    BottomAppBar(
        containerColor = Color(0xFF4A7ABF),
        contentColor = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavItem(
                icon = Icons.Rounded.Home,
                contentDescription = "Home",
                iconTitle = "Home",
                isSelected = selectedItem == 1,
                onClick = { onClickHome() }
            )

            BottomNavItem(
                icon = Icons.Rounded.Groups,
                contentDescription = "Community",
                iconTitle = "Community",
                isSelected = selectedItem == 2,
                onClick = { onClickCommunity() }
            )
        }
    }
}

@Composable
fun MyBottomBar(
    onClickHome: () -> Unit,
    onClickCommunity: () -> Unit,
    onClickProfile: () -> Unit,
    selectedItem: Int
    //when call this func, pass a int to indicate which icon is selected / click by user
    //if none of them, then pass integer 0
    //when user select pet community pass integer 1, home pass int 2, profile pass int 3
) {
    BottomAppBar(
        containerColor = Color(0xFF4A7ABF),
        contentColor = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavItem(
                icon = Icons.Rounded.Groups,
                contentDescription = "Community",
                iconTitle = "Community",
                isSelected = selectedItem == 1,
                onClick = { onClickCommunity() }
            )
            BottomNavItem(
                icon = Icons.Rounded.Home,
                contentDescription = "Home",
                iconTitle = "Home",
                isSelected = selectedItem == 2,
                onClick = { onClickHome() }
            )
            BottomNavItem(
                icon = Icons.Rounded.Person,
                contentDescription = "Profile",
                iconTitle = "Profile",
                isSelected = selectedItem == 3,
                onClick = { onClickProfile() }
            )
        }
    }
}

@Composable
fun SideBar(
    currentScreen: String,
    onPetListClick: () -> Unit,
    onCommunityClick: () -> Unit,
    onBookingListClick: () -> Unit,
    onReportClick: () -> Unit,
    onDonationClick: () -> Unit,
    onAboutUsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onBackHome: () -> Unit,
    isTablet: Boolean,
    isLandscape: Boolean
) {
    if (isTablet) {

        Column(
            modifier = Modifier
                .background(Color(0xFF2261b5))
                .fillMaxHeight()
                .fillMaxWidth(0.25f)
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Logo
            Image(
                painter = painterResource(R.drawable.furever_friends_logo),
                contentDescription = "Furever Friends Logo",
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .size(160.dp)
                    .padding(vertical = 16.dp)
            )

            // Navigation Items
            SideNavItem(
                text = "Home",
                isSelected = currentScreen == "Home",
                onClick = onBackHome,
                isTablet,
                isLandscape
            )

            SideNavItem(
                text = "Pet List",
                isSelected = currentScreen == "Pet List",
                onClick = onPetListClick,
                isTablet,
                isLandscape
            )

            SideNavItem(
                text = "Community",
                isSelected = currentScreen == "Community",
                onClick = onCommunityClick,
                isTablet,
                isLandscape
            )

            SideNavItem(
                text = "Booking",
                isSelected = currentScreen == "Booking",
                onClick = onBookingListClick,
                isTablet,
                isLandscape
            )

            SideNavItem(
                text = "Report",
                isSelected = currentScreen == "Report",
                onClick = onReportClick,
                isTablet,
                isLandscape
            )

            SideNavItem(
                text = "Donation",
                isSelected = currentScreen == "Donation",
                onClick = onDonationClick,
                isTablet,
                isLandscape
            )

            SideNavItem(
                text = "About Us",
                isSelected = currentScreen == "About Us",
                onClick = onAboutUsClick,
                isTablet,
                isLandscape
            )

            SideNavItem(
                text = "Profile",
                isSelected = currentScreen == "Profile",
                onClick = onProfileClick,
                isTablet,
                isLandscape
            )
        }

    } else {
        if(isLandscape) {
            Column(
                modifier = Modifier
                    .background(Color(0xFF2261b5))
                    .fillMaxHeight()
                    .fillMaxWidth(0.2f)
                    .padding(vertical = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Logo
                Image(
                    painter = painterResource(R.drawable.furever_friends_logo),
                    contentDescription = "Furever Friends Logo",
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .size(100.dp)
                        .padding(vertical = 16.dp)
                )

                // Navigation Items
                SideNavItem(
                    text = "Home",
                    isSelected = currentScreen == "Home",
                    onClick = onBackHome,
                    isTablet,
                    isLandscape
                )

                SideNavItem(
                    text = "Pet List",
                    isSelected = currentScreen == "Pet List",
                    onClick = onPetListClick,
                    isTablet,
                    isLandscape
                )

                SideNavItem(
                    text = "Community",
                    isSelected = currentScreen == "Community",
                    onClick = onCommunityClick,
                    isTablet,
                    isLandscape
                )

                SideNavItem(
                    text = "Booking",
                    isSelected = currentScreen == "Booking",
                    onClick = onBookingListClick,
                    isTablet,
                    isLandscape
                )

                SideNavItem(
                    text = "Report",
                    isSelected = currentScreen == "Report",
                    onClick = onReportClick,
                    isTablet,
                    isLandscape
                )

                SideNavItem(
                    text = "Donation",
                    isSelected = currentScreen == "Donation",
                    onClick = onDonationClick,
                    isTablet,
                    isLandscape
                )

                SideNavItem(
                    text = "About Us",
                    isSelected = currentScreen == "About Us",
                    onClick = onAboutUsClick,
                    isTablet,
                    isLandscape
                )

                SideNavItem(
                    text = "Profile",
                    isSelected = currentScreen == "Profile",
                    onClick = onProfileClick,
                    isTablet,
                    isLandscape
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .background(Color(0xFF2261b5))
                    .fillMaxHeight()
                    .fillMaxWidth(0.25f)
                    .padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Logo
                Image(
                    painter = painterResource(R.drawable.furever_friends_logo),
                    contentDescription = "Furever Friends Logo",
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .size(160.dp)
                        .padding(vertical = 16.dp)
                )

                // Navigation Items
                SideNavItem(
                    text = "Home",
                    isSelected = currentScreen == "Home",
                    onClick = onBackHome,
                    isTablet,
                    isLandscape
                )

                SideNavItem(
                    text = "Pet List",
                    isSelected = currentScreen == "Pet List",
                    onClick = onPetListClick,
                    isTablet,
                    isLandscape
                )

                SideNavItem(
                    text = "Community",
                    isSelected = currentScreen == "Community",
                    onClick = onCommunityClick,
                    isTablet,
                    isLandscape
                )

                SideNavItem(
                    text = "Booking",
                    isSelected = currentScreen == "Booking",
                    onClick = onBookingListClick,
                    isTablet,
                    isLandscape
                )

                SideNavItem(
                    text = "Report",
                    isSelected = currentScreen == "Report",
                    onClick = onReportClick,
                    isTablet,
                    isLandscape
                )

                SideNavItem(
                    text = "Donation",
                    isSelected = currentScreen == "Donation",
                    onClick = onDonationClick,
                    isTablet,
                    isLandscape
                )

                SideNavItem(
                    text = "About Us",
                    isSelected = currentScreen == "About Us",
                    onClick = onAboutUsClick,
                    isTablet,
                    isLandscape
                )

                SideNavItem(
                    text = "Profile",
                    isSelected = currentScreen == "Profile",
                    onClick = onProfileClick,
                    isTablet,
                    isLandscape
                )
            }
        }
    }
}

@Composable
fun SideNavItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isTablet: Boolean,
    isLandscape: Boolean
) {
    if(isTablet) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(vertical = 8.dp)
                .height(52.dp)
                .background(if (isSelected) Color.White else Color.Transparent),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                fontSize = 24.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color(0xff2261b5) else Color.White.copy(alpha = 0.7f),
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    } else {
        if(isLandscape) {
            Row(
                modifier = Modifier
                    .clickable(onClick = onClick)
                    .padding(vertical = 4.dp)
                    .height(24.dp)
                    .background(if (isSelected) Color.White else Color.Transparent),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color(0xff2261b5) else Color.White.copy(alpha = 0.7f),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .clickable(onClick = onClick)
                    .padding(vertical = 8.dp)
                    .height(40.dp)
                    .background(if (isSelected) Color.White else Color.Transparent),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color(0xff2261b5) else Color.White.copy(alpha = 0.7f),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    contentDescription: String,
    iconTitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(60.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .align(Alignment.TopCenter)

        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(36.dp)
            )
        }
        Text(
            text = iconTitle,
            fontSize = 12.sp,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .wrapContentSize(align = Alignment.Center)
        )
    }
}

/**
 * Converts a Bitmap to a Base64 encoded string
 *
 * @param bitmap The bitmap to convert
 * @param compressFormat The format to compress the image (default: JPEG)
 * @param quality The quality of the compression (0-100, default: 100)
 * @return Base64 encoded string of the bitmap
 */
fun bitmapToBase64(
    bitmap: Bitmap,
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(compressFormat, quality, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

/**
 * Converts a Base64 encoded string back to a Bitmap
 *
 * @param base64String The Base64 string to convert
 * @return Bitmap created from the Base64 string
 */
fun base64ToBitmap(base64String: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Creates a Firebase Timestamp for a specific date and time
 *
 * @param hour Hour in 24-hour format (0-23)
 * @param minute Minute (0-59)
 * @param day Day of month (1-31)
 * @param month Month (1-12)
 * @param year Year (e.g., 2025)
 * @return Firebase Timestamp object
 */
fun createTimestamp(hour: Int, minute: Int = 0, day: Int, month: Int, year: Int): Timestamp {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month - 1) // Calendar months are 0-based (0 = January)
    calendar.set(Calendar.DAY_OF_MONTH, day)
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    val date = calendar.time
    return Timestamp(date)
}