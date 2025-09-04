package com.example.ass1.ui.screen.home

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ass1.R
import com.example.ass1.model.Event
import com.example.ass1.model.Module
import com.example.ass1.model.Pet
import com.example.ass1.ui.component.SetSystemNavColor
import com.example.ass1.ui.component.base64ToBitmap
import com.example.ass1.ui.screen.community.CommunityViewModel
import com.example.ass1.ui.screen.pet.PetViewModel
import com.example.ass1.ui.screen.profile.ProfileViewModel
import com.example.ass1.ui.theme.poppinsFontFamily

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(
    onNavigateToReport: () -> Unit,
    onNavigateToDonation: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToPet: () -> Unit,
    onNavigateToBooking: () -> Unit,
    onNavigateToAboutUs: () -> Unit,
    onNavigateToCommunityPost: () -> Unit,
    onNavigateToPetDetails: () -> Unit,
    communityViewModel: CommunityViewModel,
    petViewModel: PetViewModel,
    profileViewModel: ProfileViewModel
) {

    profileViewModel.loadUserProfile()
    val communityList = communityViewModel.eventsList.value
    val petList = petViewModel.petsList.value
    val userName = profileViewModel.currentUser.value.userName
    val userGender = profileViewModel.currentUser.value.userGender

    SetSystemNavColor(0xFF4A7ABF, 0xFF4e84cc)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFF4e84cc))
    ) {
        WelcomeSection(userName,userGender)

        Spacer(modifier = Modifier.height(8.dp))

        //Home Page Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 28.dp, horizontal = 8.dp)
            ) {
                ModulesSection(
                    onNavigateToReport = onNavigateToReport,
                    onNavigateToDonation = onNavigateToDonation,
                    onNavigateToCommunity = onNavigateToCommunity,
                    onNavigateToPet = onNavigateToPet,
                    onNavigateToBooking = onNavigateToBooking,
                    onNavigateToAboutUs = onNavigateToAboutUs
                )

                Spacer(modifier = Modifier.height(20.dp))

                CommunitiesSection(
                    communityList,
                    onNavigateToCommunity = onNavigateToCommunity,
                    onNavigateToCommunityPost = onNavigateToCommunityPost,
                    communityViewModel = communityViewModel
                )

                Spacer(modifier = Modifier.height(20.dp))

                PetListSection(
                    petList,
                    onNavigateToPet = onNavigateToPet,
                    onNavigateToPetDetails = onNavigateToPetDetails,
                    petViewModel = petViewModel
                )
            }
        }
    }
}

@Composable
fun WelcomeSection(
    name: String,
    gender: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        if(gender == 0) {
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
        } else if (gender == 1) {
            Image(
                painter = painterResource(R.drawable.male_profile_picture),
                contentDescription = "User Profile Picture",
                modifier = Modifier
                    .size(72.dp)
                    .shadow(elevation = 4.dp, shape = CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(2.dp)
            )
        }else if (gender == 2) {
            Image(
                painter = painterResource(R.drawable.female_profile_picture),
                contentDescription = "User Profile Picture",
                modifier = Modifier
                    .size(72.dp)
                    .shadow(elevation = 4.dp, shape = CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(2.dp)
            )
        }else {
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
        }

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

            Text(
                text = name,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = poppinsFontFamily
                )
            )
        }
    }
}

@Composable
fun ModulesSection(
    onNavigateToReport: () -> Unit,
    onNavigateToDonation: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToPet: () -> Unit,
    onNavigateToBooking: () -> Unit,
    onNavigateToAboutUs: () -> Unit
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
        GridModules(modules = modules)
    }
}

@Composable
fun GridModules(
    modules: List<Module>
) {
    val rows = modules.chunked(3)

    Column {
        rows.forEach { rowModules ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowModules.forEach { module ->
                    ModuleItem(module = module)
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ModuleItem(
    module: Module
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(90.dp)
            .clickable { module.onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
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
                        .size(36.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = module.title,
                    style = TextStyle(
                        fontSize = 12.sp,
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

@Composable
fun CommunitiesSection(
    communityList: List<Event>,
    onNavigateToCommunity: () -> Unit,
    onNavigateToCommunityPost: () -> Unit,
    communityViewModel: CommunityViewModel
) {
    val communities = communityList

    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.pet_community),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontFamily = poppinsFontFamily
                )
            )

            Text(
                text = stringResource(R.string.see_more),
                style = TextStyle(
                    color = Color.Blue,
                    fontSize = 12.sp,
                    fontFamily = poppinsFontFamily,
                    fontStyle = FontStyle.Italic
                ),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onNavigateToCommunity() }
            )

        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(communities) { community ->
                CommunityCard(
                    community = community,
                    communityViewModel = communityViewModel,
                    onNavigateToCommunityPost = { onNavigateToCommunityPost() }
                )
            }
        }
    }
}

@Composable
fun CommunityCard(
    community: Event,
    communityViewModel: CommunityViewModel,
    onNavigateToCommunityPost: () -> Unit
) {
    Box(
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Card(
            modifier = Modifier
                .width(240.dp)
                .height(176.dp)
                .clickable {
                    communityViewModel.updateSelectedEvent(community.eventId)
                    onNavigateToCommunityPost()
                },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box {
                Column {
                    val decodedBitmap = base64ToBitmap(community.imageUrl)
                    decodedBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = community.eventTitle,
                            modifier = Modifier
                                .width(240.dp)
                                .height(100.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    //Title and short description
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = community.eventTitle,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = community.eventDetails,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = poppinsFontFamily,
                                color = Color.Black
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Clip
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PetListSection(
    petList: List<Pet>,
    onNavigateToPet: () -> Unit,
    onNavigateToPetDetails: () -> Unit,
    petViewModel: PetViewModel
) {
    val pets = petList

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pet List",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFontFamily
                )
            )

            Text(
                text = stringResource(R.string.see_more),
                style = TextStyle(
                    color = Color.Blue,
                    fontSize = 12.sp,
                    fontFamily = poppinsFontFamily,
                    fontStyle = FontStyle.Italic
                ),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onNavigateToPet() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pets) { pet ->
                PetCard(
                    pet = pet,
                    petViewModel = petViewModel,
                    onNavigateToPetDetails = { onNavigateToPetDetails() }
                )
            }
        }
    }
}

@Composable
fun PetCard(
    pet: Pet,
    petViewModel: PetViewModel,
    onNavigateToPetDetails: () -> Unit
) {
    Box(
        modifier = Modifier.padding(4.dp)
    ) {
        Card(
            modifier = Modifier
                .width(180.dp)
                .height(192.dp)
                .clickable {
                    petViewModel.updateSelectedPet(pet.petId)
                    onNavigateToPetDetails()
                },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box {
                Column {

                    val decodedBitmap = base64ToBitmap(pet.image1)
                    decodedBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = pet.petName,
                            modifier = Modifier
                                .width(240.dp)
                                .height(132.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    //Title and short description
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = pet.petName,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = poppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        )

                        Text(
                            text = pet.breed,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = poppinsFontFamily,
                                color = Color.Black
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Clip
                        )
                    }
                }
            }
        }
    }
}