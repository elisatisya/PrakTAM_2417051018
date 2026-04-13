package com.example.praktam_2417051018

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.*
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.praktam_2417051018.model.Movie
import com.example.praktam_2417051018.model.MovieSource

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            AppNavigation(navController)
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            MovieScreen(navController)
        }

        composable("detail/{title}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title")
            val movie = MovieSource.movieList.find { it.title == title }

            if (movie != null) {
                DetailScreen(movie, navController)
            }
        }
    }
}

@Composable
fun MovieScreen(navController: NavHostController) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B101B)),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item {
            Text(
                "MoodFlix - Horror",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Red
            )
        }

        item {
            Text(
                "Rekomendasi",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(MovieSource.movieList) { movie ->
                    MovieRowItem(movie, navController)
                }
            }
        }

        item {
            Text(
                "Daftar Film",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        items(MovieSource.movieList) { movie ->
            MovieCard(movie, navController)
        }
    }
}

@Composable
fun MovieRowItem(movie: Movie, navController: NavHostController) {

    Card(
        modifier = Modifier
            .width(140.dp)
            .clickable {
                navController.navigate("detail/${movie.title}")
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column {
            Image(
                painter = painterResource(movie.imageRes),
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                movie.title,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun MovieCard(movie: Movie, navController: NavHostController) {

    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("detail/${movie.title}")
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {

        Column {

            Image(
                painter = painterResource(movie.imageRes),
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    movie.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = { isFavorite = !isFavorite }) {
                    Icon(
                        if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }

            Text(
                movie.year,
                color = Color.Gray
            )

            Button(
                onClick = { navController.navigate("detail/${movie.title}") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Details")
            }
        }
    }
}

@Composable
fun DetailScreen(movie: Movie, navController: NavHostController) {

    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0B101B))
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            Image(
                painter = painterResource(movie.imageRes),
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                movie.title,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                movie.year,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = movie.description,
                color = Color.White,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    scope.launch {
                        isLoading = true
                        delay(2000)
                        snackbarHostState.showSnackbar("Now playing ${movie.title}")
                        isLoading = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Loading...")
                    }
                } else {
                    Text("Watch Now")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back")
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}