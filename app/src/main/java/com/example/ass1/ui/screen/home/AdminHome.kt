package com.example.ass1.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ass1.R
import com.example.ass1.model.Module
import com.example.ass1.ui.component.SetSystemNavColor
import com.example.ass1.ui.theme.poppinsFontFamily

@Composable
fun AdminHomeScreen(
    onSignOut: () -> Unit,
    onNavigateToReport: () -> Unit,
    onNavigateToDonation: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToPet: () -> Unit,
    onNavigateToBooking: () -> Unit,
    onNavigateToAboutUs: () -> Unit,
) {
    SetSystemNavColor(0xFF4A7ABF, 0xFF4A7ABF)

    // Get current configuration
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Determine if it's a tablet (using a common threshold of 600dp)
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    // Determine if it's in landscape mode
    val isLandscape = screenWidth > screenHeight

    if (isTablet) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xff4e8acc))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                WelcomeSection("Admin", onSignOut, isTablet)

                Spacer(modifier = Modifier.height(8.dp))

                //Home Page Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
                        .background(Color.White)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 28.dp, horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))

                        HomePageDogImage(isTablet)

                        Spacer(modifier = Modifier.height(36.dp))

                        HomeTitle(isTablet)

                        Spacer(modifier = Modifier.height(44.dp))

                        AdminModulesSection(
                            isTablet, isLandscape,
                            onNavigateToReport = { onNavigateToReport() },
                            onNavigateToDonation = { onNavigateToDonation() },
                            onNavigateToCommunity = { onNavigateToCommunity() },
                            onNavigateToPet = { onNavigateToPet() },
                            onNavigateToBooking = { onNavigateToBooking() },
                            onNavigateToAboutUs = { onNavigateToAboutUs() }
                        )

                        Spacer(modifier = Modifier.weight(1f))

                    }
                }


            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xff4e8acc))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                WelcomeSection("Admin", onSignOut, isTablet)

                Spacer(modifier = Modifier.height(8.dp))

                //Home Page Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
                        .background(Color.White)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 28.dp, horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        HomePageDogImage(isTablet)

                        Spacer(modifier = Modifier.height(20.dp))

                        HomeTitle(isTablet)

                        Spacer(modifier = Modifier.height(28.dp))

                        AdminModulesSection(
                            isTablet, isLandscape,
                            onNavigateToReport = { onNavigateToReport() },
                            onNavigateToDonation = { onNavigateToDonation() },
                            onNavigateToCommunity = { onNavigateToCommunity() },
                            onNavigateToPet = { onNavigateToPet() },
                            onNavigateToBooking = { onNavigateToBooking() },
                            onNavigateToAboutUs = { onNavigateToAboutUs() }
                        )

                        Spacer(modifier = Modifier.weight(1f))

                    }
                }
            }
        }
    }
}

@Composable
fun HomePageDogImage(
    isTablet: Boolean
) {
    if (isTablet) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.home_page_dog),
                contentDescription = "Admin Home Page Dog",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(152.dp) //ori: 120
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.home_page_dog),
                contentDescription = "Admin Home Page Dog",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(120.dp)
            )
        }
    }
}

//Same with user home page, but the name is just writing "Admin
@Composable
fun WelcomeSection(
    name: String,
    onSignOut: () -> Unit,
    isTablet: Boolean
) {
    if (isTablet) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.unknown_profile_picture),
                contentDescription = "User Profile Picture",
                modifier = Modifier
                    .size(84.dp) //ori: 72
                    .shadow(elevation = 4.dp, shape = CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(2.dp)
            )

            //Welcome text and user name
            Column(
                modifier = Modifier
                    .padding(start = 24.dp)
            ) {
                Text(
                    text = "Welcome!",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 28.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 24.sp,
                            fontFamily = poppinsFontFamily
                        )
                    )

                    Text(
                        text = "Sign Out",
                        style = TextStyle(
                            color = Color.White,
                            fontFamily = poppinsFontFamily,
                            fontSize = 20.sp,
                        ),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .clickable{ onSignOut() }
                    )
                }
            }
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.unknown_profile_picture),
                contentDescription = "User Profile Picture",
                modifier = Modifier
                    .size(72.dp)
                    .shadow(elevation = 4.dp, shape = CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(2.dp)
            )

            //Welcome text and user name
            Column(
                modifier = Modifier
                    .padding(start = 20.dp)
            ) {
                Text(
                    text = "Welcome!",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 24.sp,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = name,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp,
                            fontFamily = poppinsFontFamily
                        )
                    )

                    Text(
                        text = "Sign Out",
                        style = TextStyle(
                            color = Color.White,
                            fontFamily = poppinsFontFamily,
                            fontSize = 16.sp,
                        ),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .clickable{ onSignOut() }
                    )
                }
            }
        }
    }
}

@Composable
fun HomeTitle(
    isTablet: Boolean
) {
    if (isTablet) {
        Text(
            text = "Choose a module to continue!",
            style = TextStyle(
                color = Color(0xff0e2e6b),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        Text(
            text = "Choose a module to continue!",
            style = TextStyle(
                color = Color(0xff0e2e6b),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun AdminModulesSection(
    isTablet: Boolean,
    isLandscape: Boolean,
    onNavigateToReport: () -> Unit,
    onNavigateToDonation: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToPet: () -> Unit,
    onNavigateToBooking: () -> Unit,
    onNavigateToAboutUs: () -> Unit,
) {
    val modules = listOf(
        Module(Icons.AutoMirrored.Filled.ListAlt, "Pet List", onClick = {onNavigateToPet()}),
        Module(Icons.Default.Groups, "Community", onClick = {onNavigateToCommunity()}),
        Module(Icons.Default.EditCalendar, "Booking", onClick = {onNavigateToBooking()}),
        Module(Icons.Default.ReportProblem, "Report",onClick = {onNavigateToReport()}),
        Module(Icons.Default.VolunteerActivism, "Donation",onClick = {onNavigateToDonation()}),
        Module(Icons.Default.Info, "About Us",onClick = {onNavigateToAboutUs()})
    )

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        AdminGridModules(modules = modules, isTablet,isLandscape )
    }
}

@Composable
fun AdminGridModules(
    modules: List<Module>,
    isTablet: Boolean,
    isLandscape: Boolean
) {
    if (isTablet) {
        if (isLandscape) {
            val rows = modules.chunked(3)

            Column {
                rows.forEach { rowModules ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowModules.forEach { module ->
                            AdminModuleItem(module = module, isTablet)
                        }
                    }
                    Spacer(modifier = Modifier.height(52.dp))
                }
            }
        } else {
            val rows = modules.chunked(2)

            Column {
                rows.forEach { rowModules ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowModules.forEach { module ->
                            AdminModuleItem(module = module, isTablet)
                        }
                    }
                    Spacer(modifier = Modifier.height(52.dp))
                }
            }
        }
    } else {
        if(isLandscape) {
            val rows = modules.chunked(3)

            Column {
                rows.forEach { rowModules ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowModules.forEach { module ->
                            AdminModuleItem(module = module, isTablet)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        } else {
            val rows = modules.chunked(2)

            Column {
                rows.forEach { rowModules ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowModules.forEach { module ->
                            AdminModuleItem(module = module, isTablet)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun AdminModuleItem(
    module: Module,
    isTablet: Boolean
) {
    if (isTablet) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(172.dp)
                .clickable { module.onClick() }
        ) {
            Box(
                modifier = Modifier
                    .size(172.dp) //ori: 120
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xff0e2e6b)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = module.icon,
                        contentDescription = module.title,
                        tint = Color.White,
                        modifier = Modifier
                            .size(52.dp) //ori: 40
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = module.title,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            fontFamily = poppinsFontFamily
                        ),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(120.dp)
                .clickable { module.onClick() }
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xff0e2e6b)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = module.icon,
                        contentDescription = module.title,
                        tint = Color.White,
                        modifier = Modifier
                            .size(40.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = module.title,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            fontFamily = poppinsFontFamily
                        ),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
