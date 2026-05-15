package com.example.praktam_2417051018

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.praktam_2417051018.data.model.Movie
import com.example.praktam_2417051018.data.repository.MovieRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//Warna tema
private val BgDark       = Color(0xFF0B101B)
private val CardDark     = Color(0xFF161D2F)
private val AccentRed    = Color(0xFFE53935)
private val AccentPurple = Color(0xFF5E50A1)
private val TextPrimary  = Color(0xFFFFFFFF)
private val TextSecond   = Color(0xFF9E9E9E)
private val LevelRingan  = Color(0xFF43A047)
private val LevelSedang  = Color(0xFFFB8C00)
private val LevelEkstrem = Color(0xFFE53935)

fun levelColor(level: String?) = when (level) {
    "ekstrem" -> LevelEkstrem
    "sedang"  -> LevelSedang
    "ringan"  -> LevelRingan
    else      -> Color.Gray
}

fun levelLabel(level: String?) = when (level) {
    "ekstrem" -> "Ekstrem"
    "sedang"  -> "Sedang"
    "ringan"  -> "Ringan"
    else      -> "?"
}

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

//Navigasi
@Composable
fun AppNavigation(navController: NavHostController) {
    val repository = remember { MovieRepository() }
    var moviesList by remember { mutableStateOf<List<Movie>>(emptyList()) }

    NavHost(navController = navController, startDestination = "quiz") {
        composable("quiz") {
            HorrorQuizScreen { level ->
                navController.navigate("home/$level") {
                    popUpTo("quiz") { inclusive = true }
                }
            }
        }
        composable("home/{level}") { back ->
            val level = back.arguments?.getString("level") ?: "semua"
            MovieScreen(navController, repository, level) { fetched ->
                moviesList = fetched
            }
        }
        composable("detail/{title}") { back ->
            val title = back.arguments?.getString("title")
            val movie = moviesList.find { it.title == title }
            if (movie != null) DetailScreen(movie, navController)
        }
    }
}


//  HORROR LEVEL TEST

data class QuizQuestion(val text: String, val options: List<Pair<String, Int>>)

val quizQuestions = listOf(
    QuizQuestion(
        "Kamu berani nonton horror sendirian di malam hari?",
        listOf("Santai aja" to 3, "Agak deg-degan" to 2, "Ogah banget" to 1)
    ),
    QuizQuestion(
        "Pernah mimpi buruk setelah nonton horror?",
        listOf("Tidak pernah" to 3, "Jarang sih" to 2, "Sering banget" to 1)
    ),
    QuizQuestion(
        "Pilih suasana horror favoritmu:",
        listOf("Gore & brutal" to 3, "Hantu & jump scare" to 2, "Psikologis & misterius" to 2)
    ),
    QuizQuestion(
        "Reaksimu saat ada adegan jump scare:",
        listOf("Biasa aja" to 3, "Kaget dikit" to 2, "Langsung tutup mata" to 1)
    ),
    QuizQuestion(
        "Seberapa sering kamu nonton film horror?",
        listOf("Hampir tiap minggu" to 3, "Sesekali aja" to 2, "Nyaris tidak pernah" to 1)
    )
)

@Composable
fun HorrorQuizScreen(onResult: (String) -> Unit) {
    var currentQ     by remember { mutableStateOf(0) }
    var totalScore   by remember { mutableStateOf(0) }
    var showResult   by remember { mutableStateOf(false) }
    var resultLevel  by remember { mutableStateOf("") }

    fun calcLevel(score: Int) = when {
        score >= 12 -> "ekstrem"
        score >= 8  -> "sedang"
        else        -> "ringan"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        if (!showResult) {
            val q = quizQuestions[currentQ]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))
                Text("Horror Level Test", fontSize = 26.sp, fontWeight = FontWeight.Black, color = AccentRed)
                Text(
                    "Temukan film yang cocok untuk nyali kamu!",
                    fontSize = 13.sp, color = TextSecond,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Spacer(Modifier.height(32.dp))

                // Progress
                Text("Pertanyaan ${currentQ + 1} dari ${quizQuestions.size}", color = TextSecond, fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { (currentQ + 1).toFloat() / quizQuestions.size },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = AccentRed,
                    trackColor = CardDark
                )
                Spacer(Modifier.height(40.dp))

                // Kartu pertanyaan
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardDark)
                ) {
                    Text(
                        text = q.text,
                        color = TextPrimary, fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp,
                        modifier = Modifier.padding(24.dp)
                    )
                }
                Spacer(Modifier.height(24.dp))

                // Pilihan jawaban
                q.options.forEach { (label, score) ->
                    Button(
                        onClick = {
                            val newScore = totalScore + score
                            if (currentQ < quizQuestions.size - 1) {
                                totalScore = newScore
                                currentQ++
                            } else {
                                resultLevel = calcLevel(newScore)
                                showResult = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp).height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CardDark, contentColor = TextPrimary)
                    ) {
                        Text(label, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        } else {
            //Halaman Hasil
            val emoji = when (resultLevel) { "ekstrem" -> "💀"; "sedang" -> "👻"; else -> "🙈" }
            val title = when (resultLevel) { "ekstrem" -> "PSYCHO HORROR"; "sedang" -> "BRAVE WATCHER"; else -> "HORROR ROOKIE" }
            val desc  = when (resultLevel) {
                "ekstrem" -> "Nyali kamu baja! Siap nonton film paling ekstrem."
                "sedang"  -> "Cukup berani! Film horror sedang cocok buat kamu."
                else      -> "Santai dulu dengan film horror ringan ya!"
            }
            val color = levelColor(resultLevel)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(emoji, fontSize = 72.sp)
                Spacer(Modifier.height(16.dp))
                Text("Level kamu:", color = TextSecond, fontSize = 16.sp)
                Text(title, color = color, fontSize = 32.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
                Text(desc, color = TextPrimary, fontSize = 16.sp, textAlign = TextAlign.Center, lineHeight = 24.sp)
                Spacer(Modifier.height(40.dp))

                Button(
                    onClick = { onResult(resultLevel) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = color)
                ) {
                    Text("Lihat Rekomendasi Film 🎬", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { currentQ = 0; totalScore = 0; showResult = false },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
                ) {
                    Text("Ulangi Quiz", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

//  LAYAR UTAMA
@Composable
fun MovieScreen(
    navController: NavHostController,
    repository: MovieRepository,
    userLevel: String,
    onMoviesLoaded: (List<Movie>) -> Unit
) {
    var moviesState by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading   by remember { mutableStateOf(true) }
    var isError     by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun fetchData() {
        scope.launch {
            isLoading = true; isError = false
            val result = repository.getMovies()
            if (result.isNotEmpty()) {
                moviesState = result; onMoviesLoaded(result)
            } else { isError = true }
            isLoading = false
        }
    }

    LaunchedEffect(Unit) { fetchData() }

    val recommended = moviesState.filter { it.level == userLevel }

    Box(modifier = Modifier.fillMaxSize().background(BgDark)) {
        when {
            isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = AccentRed)

            isError -> Column(
                modifier = Modifier.align(Alignment.Center).padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier.size(80.dp).background(AccentRed.copy(0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Filled.Warning, null, tint = AccentRed, modifier = Modifier.size(40.dp)) }
                Text("Tidak ada koneksi internet", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Text("Periksa koneksi internetmu lalu coba lagi.", color = TextSecond, fontSize = 14.sp, textAlign = TextAlign.Center)
                Button(onClick = { fetchData() }, colors = ButtonDefaults.buttonColors(containerColor = AccentRed), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Text("Coba Lagi", fontWeight = FontWeight.Bold)
                }
            }

            else -> LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 24.dp)) {

                // Header
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .background(Brush.verticalGradient(listOf(Color(0xFF1A0A0A), BgDark)))
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        Column {
                            Text("MoodFlix", fontSize = 28.sp, fontWeight = FontWeight.Black, color = AccentRed)
                            Text("Horror Collection", fontSize = 14.sp, color = TextSecond)
                        }
                    }
                }

                // Badge level user
                item {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Level nyali kamu: ", color = TextSecond, fontSize = 13.sp)
                        Surface(color = levelColor(userLevel).copy(0.2f), shape = RoundedCornerShape(8.dp)) {
                            Text(levelLabel(userLevel), color = levelColor(userLevel), fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                        }
                        Spacer(Modifier.weight(1f))
                        TextButton(onClick = { navController.navigate("quiz") }) {
                            Text("Quiz Ulang", color = AccentRed, fontSize = 12.sp)
                        }
                    }
                }

                // Rekomendasi untuk level kamu
                item {
                    Column {
                        Text("Rekomendasi untuk Kamu", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))
                        if (recommended.isEmpty()) {
                            Text("Belum ada film untuk level ini.", color = TextSecond, modifier = Modifier.padding(horizontal = 20.dp))
                        } else {
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(horizontal = 20.dp)) {
                                items(recommended) { movie ->
                                    MovieRowItem(movie) { navController.navigate("detail/${movie.title}") }
                                }
                            }
                        }
                    }
                }

                // Daftar semua film
                item {
                    Text("Daftar Film", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 8.dp))
                }

                items(moviesState) { movie ->
                    MovieCard(movie = movie, modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                        navController.navigate("detail/${movie.title}")
                    }
                }
            }
        }
    }
}

//Card Horizontal
@Composable
fun MovieRowItem(movie: Movie, onClick: () -> Unit) {
    Card(
        modifier = Modifier.width(130.dp).clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Box {
                AsyncImage(model = movie.imageUrl, contentDescription = movie.title, modifier = Modifier.fillMaxWidth().height(170.dp), contentScale = ContentScale.Crop)
                Box(modifier = Modifier.fillMaxWidth().height(60.dp).align(Alignment.BottomCenter).background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xCC000000)))))
                Surface(color = levelColor(movie.level).copy(0.9f), shape = RoundedCornerShape(6.dp), modifier = Modifier.align(Alignment.TopStart).padding(6.dp)) {
                    Text(levelLabel(movie.level), color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 5.dp, vertical = 3.dp))
                }
            }
            Text(movie.title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp))
            Text(movie.year, color = TextSecond, fontSize = 11.sp, modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp))
        }
    }
}

//Card Vertikal
@Composable
fun MovieCard(movie: Movie, modifier: Modifier = Modifier, onClick: () -> Unit) {
    var isFavorite by remember { mutableStateOf(false) }
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardDark),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            Box {
                AsyncImage(model = movie.imageUrl, contentDescription = movie.title, modifier = Modifier.fillMaxWidth().height(200.dp), contentScale = ContentScale.Crop)
                Box(modifier = Modifier.fillMaxWidth().height(100.dp).align(Alignment.BottomCenter).background(Brush.verticalGradient(listOf(Color.Transparent, CardDark))))
                Surface(color = AccentRed.copy(0.85f), shape = RoundedCornerShape(6.dp), modifier = Modifier.align(Alignment.TopStart).padding(10.dp)) {
                    Text(movie.genre, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
                Surface(color = levelColor(movie.level).copy(0.85f), shape = RoundedCornerShape(6.dp), modifier = Modifier.align(Alignment.TopEnd).padding(10.dp)) {
                    Text(levelLabel(movie.level), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(movie.title, color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(movie.year, color = TextSecond, fontSize = 13.sp)
                }
                IconButton(onClick = { isFavorite = !isFavorite }) {
                    Icon(if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, null, tint = if (isFavorite) AccentRed else TextSecond)
                }
            }
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 4.dp).padding(bottom = 14.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
            ) {
                Icon(Icons.Filled.PlayArrow, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Details", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

//  LAYAR DETAIL

@Composable
fun DetailScreen(movie: Movie, navController: NavHostController) {
    val scope             = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val context           = LocalContext.current
    var isFavorite        by remember { mutableStateOf(false) }
    var isWatching        by remember { mutableStateOf(false) }

    Scaffold(containerColor = BgDark, snackbarHost = { SnackbarHost(snackBarHostState) }) { pv ->
        Column(
            modifier = Modifier.fillMaxSize().padding(pv).verticalScroll(rememberScrollState())
        ) {
            // Poster
            Box(modifier = Modifier.fillMaxWidth().height(420.dp)) {
                AsyncImage(model = movie.imageUrl, contentDescription = movie.title, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                Box(modifier = Modifier.fillMaxWidth().height(220.dp).align(Alignment.BottomCenter).background(Brush.verticalGradient(listOf(Color.Transparent, BgDark))))

                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.TopStart).statusBarsPadding().padding(12.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Black.copy(0.5f), contentColor = TextPrimary)
                ) { Text("← Back") }

                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier.align(Alignment.TopEnd).statusBarsPadding().padding(12.dp).background(Color.Black.copy(0.5f), CircleShape)
                ) {
                    Icon(if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, null, tint = if (isFavorite) AccentRed else TextPrimary)
                }

                Column(modifier = Modifier.align(Alignment.BottomStart).padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Text(movie.title, color = TextPrimary, fontSize = 26.sp, fontWeight = FontWeight.Black)
                    Spacer(Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(color = AccentRed.copy(0.85f), shape = RoundedCornerShape(6.dp)) {
                            Text(movie.genre, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                        Surface(color = levelColor(movie.level).copy(0.85f), shape = RoundedCornerShape(6.dp)) {
                            Text(levelLabel(movie.level), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                        Surface(color = Color.White.copy(0.15f), shape = RoundedCornerShape(6.dp)) {
                            Text(movie.year, color = TextPrimary, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                }
            }

            // Konten bawah
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(12.dp))
                Text("Sinopsis", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(movie.description, color = TextSecond, fontSize = 14.sp, lineHeight = 22.sp)
                Spacer(Modifier.height(28.dp))

                // Tombol Watch Now
                Button(
                    onClick = {
                        val url = movie.youtubeUrl
                        if (!url.isNullOrEmpty()) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        } else {
                            scope.launch {
                                isWatching = true
                                delay(1500)
                                snackBarHostState.showSnackbar("▶ Now playing: ${movie.title}")
                                isWatching = false
                            }
                        }
                    },
                    enabled = !isWatching,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                ) {
                    if (isWatching) {
                        CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
                        Spacer(Modifier.width(10.dp))
                        Text("Memuat...", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Filled.PlayArrow, null, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (!movie.youtubeUrl.isNullOrEmpty()) "Watch Trailer di YouTube" else "Watch Now",
                            fontWeight = FontWeight.Bold, fontSize = 16.sp
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
                ) { Text("← Back", fontWeight = FontWeight.SemiBold, fontSize = 16.sp) }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}