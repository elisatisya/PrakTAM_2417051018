package com.example.praktam_2417051018

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.praktam_2417051018.model.Movie
import com.example.praktam_2417051018.model.MovieSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HorrorScreen()
        }
    }
}

@Composable
fun HorrorScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B101B))
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "MoodFlix - Horror",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ROW 1
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MovieItem(MovieSource.movieList[0], Modifier.weight(1f))
            MovieItem(MovieSource.movieList[1], Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ROW 2
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MovieItem(MovieSource.movieList[2], Modifier.weight(1f))
            MovieItem(MovieSource.movieList[3], Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ROW 3
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MovieItem(MovieSource.movieList[4], Modifier.weight(1f))
            MovieItem(MovieSource.movieList[5], Modifier.weight(1f))
        }
    }
}

@Composable
fun MovieItem(movie: Movie, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {

        Image(
            painter = painterResource(id = movie.imageRes),
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = movie.title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Text(
            text = movie.year,
            color = Color.Gray,
            fontSize = 13.sp
        )
    }
}