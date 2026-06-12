package com.example.praktam_2417051018

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.praktam_2417051018.data.model.Movie
import com.example.praktam_2417051018.data.repository.MovieRepository
import com.example.praktam_2417051018.data.repository.FavoritesManager
import com.example.praktam_2417051018.data.repository.UserManager
import com.example.praktam_2417051018.data.repository.HistoryManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//Warna tema
private val BgDark       = Color(0xFF0B101B)
private val CardDark     = Color(0xFF161D2F)
private val AccentRed    = Color(0xFFE63946)
private val TextPrimary  = Color(0xFFFFFFFF)
private val TextSecond   = Color(0xFF9E9E9E)
private val LevelRingan  = Color(0xFF06D6A0)
private val LevelSedang  = Color(0xFFFFB703)
private val LevelEkstrem = Color(0xFFE63946)

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

@Composable
fun moodFlixTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AccentRed,
    unfocusedBorderColor = CardDark,
    focusedLabelColor = AccentRed,
    unfocusedLabelColor = TextSecond,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedContainerColor = CardDark,
    unfocusedContainerColor = CardDark,
    errorTextColor = TextPrimary,
    errorContainerColor = CardDark,
    errorLabelColor = TextSecond,
    errorLeadingIconColor = AccentRed,
    errorBorderColor = AccentRed
)

@Composable
fun MoodFlixDialog(
    show: Boolean,
    message: String,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🎬 ", fontSize = 22.sp)
                    Text("MoodFlix", color = AccentRed, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            },
            text = {
                Text(message, color = TextPrimary, fontSize = 14.sp)
            },
            confirmButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("OK", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = CardDark,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FavoritesManager.init(this)
        UserManager.init(this)
        HistoryManager.init(this)
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

    LaunchedEffect(Unit) {
        moviesList = repository.getAllMovies()
    }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showNavbar = currentRoute in listOf("genre_selection", "favorites", "profile")

    Scaffold(
        bottomBar = { MoodFlixBottomBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(bottom = if (showNavbar) innerPadding.calculateBottomPadding() else 0.dp)
        ) {
            composable("splash") {
                SplashScreen(navController)
            }
            composable("login") {
                LoginScreen(navController)
            }
            composable("register") {
                RegisterScreen(navController)
            }
            composable("genre_selection") {
                GenreSelectionScreen(navController)
            }
            composable("profile") {
                ProfileScreen(navController)
            }
            composable("quiz/{genre}") { back ->
                val genre = back.arguments?.getString("genre") ?: "Horror"
                QuizScreen(
                    genre = genre,
                    onBack = { navController.popBackStack() }
                ) { level ->
                    FavoritesManager.saveLevelForGenre(genre, level)
                    navController.navigate("home/$genre/$level") {
                        popUpTo("quiz/$genre") { inclusive = true }
                    }
                }
            }
            composable("quiz") {
                QuizScreen(
                    genre = "Horror",
                    onBack = { navController.popBackStack() }
                ) { level ->
                    FavoritesManager.saveLevelForGenre("Horror", level)
                    navController.navigate("home/Horror/$level") {
                        popUpTo("quiz") { inclusive = true }
                    }
                }
            }
            composable("home/{genre}/{level}") { back ->
                val genre = back.arguments?.getString("genre") ?: "Horror"
                val level = back.arguments?.getString("level") ?: "semua"
                MovieScreen(navController, repository, genre, level) { fetched ->
                    moviesList = (moviesList + fetched).distinctBy { it.title }
                }
            }
            composable("home/{level}") { back ->
                val level = back.arguments?.getString("level") ?: "semua"
                MovieScreen(navController, repository, "Horror", level) { fetched ->
                    moviesList = (moviesList + fetched).distinctBy { it.title }
                }
            }
            composable("detail/{title}") { back ->
                val title = back.arguments?.getString("title")
                val movie = moviesList.find { it.title == title }
                if (movie != null) DetailScreen(movie, navController)
            }
            composable("favorites") {
                FavoritesScreen(navController, repository)
            }
        }
    }
}


//  LEVEL TEST

data class QuizQuestion(val text: String, val options: List<Pair<String, Int>>)

val quizQuestionsMap = mapOf(
    "Horror" to listOf(
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
            listOf("Gore & brutal" to 3, "Hantu & jump scare" to 2, "Psikologis & misterius" to 1)
        ),
        QuizQuestion(
            "Reaksimu saat ada adegan jump scare:",
            listOf("Biasa aja" to 3, "Kaget dikit" to 2, "Langsung tutup mata" to 1)
        ),
        QuizQuestion(
            "Seberapa sering kamu nonton film horror?",
            listOf("Hampir tiap minggu" to 3, "Sesekali aja" to 2, "Nyaris tidak pernah" to 1)
        )
    ),
    "Action" to listOf(
        QuizQuestion(
            "Pilih jenis adegan aksi yang paling kamu sukai:",
            listOf("Ledakan dahsyat & kejar-kejaran ekstrem" to 3, "Pertarungan bela diri & baku tembak" to 2, "Aksi superhero ringan & petualangan" to 1)
        ),
        QuizQuestion(
            "Reaksi kamu terhadap adegan brutal/darah di film aksi?",
            listOf("Keren, memicu adrenalin!" to 3, "Biasa saja selama ceritanya bagus" to 2, "Kurang suka, lebih baik yang minim kekerasan" to 1)
        ),
        QuizQuestion(
            "Seberapa cepat tempo cerita aksi yang kamu sukai?",
            listOf("Sangat cepat & tegang dari awal sampai akhir" to 3, "Sedang, seimbang antara cerita dan pertarungan" to 2, "Santai, banyak dibumbui humor atau drama" to 1)
        ),
        QuizQuestion(
            "Pilih tipe karakter pahlawan aksi favoritmu:",
            listOf("Anti-hero kejam yang bertindak kasar" to 3, "Polisi atau agen rahasia cerdas penuh taktik" to 2, "Pahlawan ramah yang humoris & santai" to 1)
        ),
        QuizQuestion(
            "Seberapa sering kamu menonton film aksi menegangkan?",
            listOf("Sangat sering untuk memacu adrenalin" to 3, "Sesekali untuk hiburan akhir pekan" to 2, "Jarang sekali, hanya jika diajak teman" to 1)
        )
    ),
    "Comedy" to listOf(
        QuizQuestion(
            "Humor jenis apa yang paling bisa membuatmu tertawa lepas?",
            listOf("Humor dewasa, absurd & kacau balau" to 3, "Komedi situasi (sitcom) & sindiran cerdas" to 2, "Humor slapstick ringan & ramah keluarga" to 1)
        ),
        QuizQuestion(
            "Pandanganmu tentang lelucon konyol atau sedikit kasar?",
            listOf("Lucu banget, tidak perlu dibawa serius" to 3, "Boleh saja selama tidak keterlaluan" to 2, "Kurang suka, lebih suka lelucon cerdas & sopan" to 1)
        ),
        QuizQuestion(
            "Karakter komedi mana yang paling kamu sukai?",
            listOf("Karakter kacau yang selalu memicu masalah" to 3, "Karakter sarkas dengan celotehan tajam" to 2, "Karakter polos & konyol yang menggemaskan" to 1)
        ),
        QuizQuestion(
            "Pilih latar tempat komedi yang paling menarik:",
            listOf("Pesta liar di Las Vegas yang berujung petaka" to 3, "Kehidupan sekolah atau kantor sehari-hari" to 2, "Dunia fantasi atau petualangan game konyol" to 1)
        ),
        QuizQuestion(
            "Seberapa sering kamu menonton komedi untuk melepas stres?",
            listOf("Hampir setiap hari untuk hiburan rutin" to 3, "Sesekali saat merasa penat" to 2, "Jarang sekali, lebih suka genre serius" to 1)
        )
    ),
    "Sci-Fi" to listOf(
        QuizQuestion(
            "Konsep sains fiksi mana yang paling membuatmu penasaran?",
            listOf("Dunia mimpi berlapis & manipulasi pikiran" to 3, "Perjalanan waktu & relativitas ruang angkasa" to 2, "Teknologi masa depan & robot pintar yang lucu" to 1)
        ),
        QuizQuestion(
            "Seberapa rumit jalan cerita Sci-Fi yang kamu sukai?",
            listOf("Sangat rumit, butuh teori sains & mikir keras" to 3, "Sedang, menarik tapi masih mudah dipahami" to 2, "Sederhana, lebih fokus ke petualangan seru" to 1)
        ),
        QuizQuestion(
            "Bagaimana tanggapanmu tentang teori fisika berat di film?",
            listOf("Sangat keren, membuat film terasa realistis" to 3, "Cukup menarik asal tidak membosankan" to 2, "Pusing, lebih suka petualangan luar angkasa biasa" to 1)
        ),
        QuizQuestion(
            "Pilih elemen Sci-Fi favoritmu:",
            listOf("Lubang hitam, dimensi lain & paradoks waktu" to 3, "Alien, penjelajahan galaksi & luar angkasa" to 2, "Gadget canggih, hologram & kota masa depan" to 1)
        ),
        QuizQuestion(
            "Apakah kamu suka membaca teori penjelasan setelah menonton?",
            listOf("Selalu, sangat seru membaca berbagai teori" to 3, "Sesekali jika endingnya sangat menggantung" to 2, "Tidak pernah, tonton untuk hiburan instan saja" to 1)
        )
    ),
    "Romance" to listOf(
        QuizQuestion(
            "Alur cerita cinta seperti apa yang paling menyentuh hatimu?",
            listOf("Kisah cinta tragis yang menguras air mata" to 3, "Perjuangan cinta menghadapi rintangan hidup" to 2, "Kisah cinta manis, musikal, penuh mimpi indah" to 1)
        ),
        QuizQuestion(
            "Seberapa emosional kamu saat menonton film romantis?",
            listOf("Sangat emosional, bisa menangis berhari-hari" to 3, "Sedih secukupnya, larut dalam suasana" to 2, "Biasa saja, hanya tersenyum menikmati ceritanya" to 1)
        ),
        QuizQuestion(
            "Pilih konflik romantis yang paling kamu sukai:",
            listOf("Penyakit mematikan atau perpisahan abadi" to 3, "Perbedaan kasta/status sosial yang rumit" to 2, "Pengejaran mimpi & karir yang saling bertolak belakang" to 1)
        ),
        QuizQuestion(
            "Bagaimana akhir cerita (ending) romantis terbaik bagimu?",
            listOf("Sad ending membekas & tak terlupakan" to 3, "Open ending yang memicu diskusi rasa" to 2, "Happy ending wajib hukumnya!" to 1)
        ),
        QuizQuestion(
            "Seberapa sering kamu menonton film tentang cinta?",
            listOf("Sangat sering, penyuka drama romantis sejati" to 3, "Sesekali saat butuh tontonan santai" to 2, "Hampir tidak pernah, lebih menyukai genre lain" to 1)
        )
    )
)

data class QuizResultData(val emoji: String, val title: String, val description: String)

fun getQuizResult(genre: String, level: String): QuizResultData {
    return when (genre.lowercase()) {
        "action" -> when (level) {
            "ekstrem" -> QuizResultData("🔥", "ADRENALINE JUNKIE", "Kamu penyuka tantangan ekstrem! Siap untuk aksi brutal tanpa henti.")
            "sedang"  -> QuizResultData("💥", "TACTICAL SPECTATOR", "Kamu menyukai taktik cerdas & aksi keren dengan jalan cerita solid.")
            else      -> QuizResultData("🛡️", "COZY ADVENTURER", "Kamu lebih menyukai petualangan seru yang ringan dan menghibur.")
        }
        "comedy" -> when (level) {
            "ekstrem" -> QuizResultData("🤪", "LAUGHTER MASTER", "Lelucon gila & absurd adalah makanan sehari-harimu. Siap tertawa lepas!")
            "sedang"  -> QuizResultData("😆", "SITCOM ENTHUSIAST", "Sindiran cerdas & humor situasi yang menggelitik sangat cocok untukmu.")
            else      -> QuizResultData("😊", "FAMILY SMILEY", "Kamu menyukai humor ringan & komedi keluarga yang menghangatkan hati.")
        }
        "sci-fi" -> when (level) {
            "ekstrem" -> QuizResultData("🌀", "DIMENSIONAL TRAVELLER", "Otakmu siap memecahkan paradoks & teori ruang-waktu paling rumit.")
            "sedang"  -> QuizResultData("🚀", "SPACE EXPLORER", "Kamu menyukai penjelajahan galaksi & konsep masa depan yang futuristik.")
            else      -> QuizResultData("🤖", "TECH HOBBYIST", "Gadget canggih & robot pintar ramah sudah cukup memuaskan imajinasimu.")
        }
        "romance" -> when (level) {
            "ekstrem" -> QuizResultData("💔", "HEARTBREAK SURVIVOR", "Kamu siap dihujani emosi terdalam & air mata dari kisah cinta tragis.")
            "sedang"  -> QuizResultData("💖", "ROMANCE SEEKER", "Kisah perjuangan cinta yang tulus & penuh pengorbanan sangat cocok untukmu.")
            else      -> QuizResultData("✨", "DREAMY LOVER", "Kamu menyukai kisah manis penuh bunga, musikal, dan akhir bahagia.")
        }
        else -> when (level) {
            "ekstrem" -> QuizResultData("💀", "PSYCHO HORROR", "Nyali kamu baja! Siap menonton film horror paling ekstrem.")
            "sedang"  -> QuizResultData("👻", "BRAVE WATCHER", "Cukup berani! Film horror tingkat sedang sangat cocok buat kamu.")
            else      -> QuizResultData("🙈", "HORROR ROOKIE", "Santai dulu dengan film horror ringan yang tidak terlalu meneror!")
        }
    }
}

@Composable
fun QuizScreen(genre: String, onBack: () -> Unit, onResult: (String) -> Unit) {
    val questions = quizQuestionsMap[genre] ?: quizQuestionsMap["Horror"]!!
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
            val q = questions.getOrNull(currentQ) ?: questions.first()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(40.dp)
                            .background(CardDark, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("${genre} Level Test", fontSize = 24.sp, fontWeight = FontWeight.Black, color = AccentRed)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    when (genre.lowercase()) {
                        "action" -> "Seberapa tangguh kamu menghadapi aksi menegangkan?"
                        "comedy" -> "Temukan selera humormu dan tertawalah lepas!"
                        "sci-fi" -> "Uji seberapa jauh imajinasi teknologi dan sainsmu!"
                        "romance" -> "Seberapa dalam kamu ingin menyelami kisah cinta?"
                        else -> "Temukan film yang cocok untuk nyali kamu!"
                    },
                    fontSize = 13.sp, color = TextSecond,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))

                // Progress
                Text("Pertanyaan ${currentQ + 1} dari ${questions.size}", color = TextSecond, fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { (currentQ + 1).toFloat() / questions.size },
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
                            if (currentQ < questions.size - 1) {
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
            // Halaman Hasil dengan Tampilan Keren dan Konsisten (Theme Serasi)
            val resultData = getQuizResult(genre, resultLevel)
            val color = levelColor(resultLevel)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .background(color.copy(0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(resultData.emoji, fontSize = 72.sp)
                }
                Spacer(Modifier.height(24.dp))
                Text("Kategori Kamu:", color = TextSecond, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text(resultData.title, color = color, fontSize = 32.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
                Text(resultData.description, color = TextPrimary, fontSize = 16.sp, textAlign = TextAlign.Center, lineHeight = 24.sp)
                Spacer(Modifier.height(40.dp))

                Button(
                    onClick = { onResult(resultLevel) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = color)
                ) {
                    Text("Lihat Rekomendasi Film 🎬", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if (resultLevel == "sedang") Color.Black else Color.White)
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
    genre: String,
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
            val result = repository.getMoviesByGenre(genre)
            if (result.isNotEmpty()) {
                moviesState = result; onMoviesLoaded(result)
            } else { isError = true }
            isLoading = false
        }
    }

    LaunchedEffect(genre) { fetchData() }

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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(CardDark, CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = TextPrimary
                                )
                            }
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text("MoodFlix", fontSize = 28.sp, fontWeight = FontWeight.Black, color = AccentRed)
                                Text("$genre Collection", fontSize = 14.sp, color = TextSecond)
                            }
                        }
                    }
                }

                // Badge level user
                item {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val labelText = if (genre.equals("Horror", ignoreCase = true)) "Level nyali kamu: " else "Level rekomendasi: "
                        Text(labelText, color = TextSecond, fontSize = 13.sp)
                        Surface(color = levelColor(userLevel).copy(0.2f), shape = RoundedCornerShape(8.dp)) {
                            Text(levelLabel(userLevel), color = levelColor(userLevel), fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                        }
                        Spacer(Modifier.weight(1f))
                        TextButton(onClick = { navController.navigate("quiz/$genre") }) {
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
    val isFavorite = FavoritesManager.isFavorite(movie.title)
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
                if (movie.level != null) {
                    Surface(color = levelColor(movie.level).copy(0.85f), shape = RoundedCornerShape(6.dp), modifier = Modifier.align(Alignment.TopEnd).padding(10.dp)) {
                        Text(levelLabel(movie.level), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(movie.title, color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(movie.year, color = TextSecond, fontSize = 13.sp)
                }
                IconButton(onClick = { FavoritesManager.toggleFavorite(movie.title) }) {
                    Icon(if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, null, tint = if (isFavorite) AccentRed else TextSecond)
                }
            }
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 4.dp).padding(bottom = 14.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
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
    val context           = LocalContext.current
    val scope             = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val isFavorite        = FavoritesManager.isFavorite(movie.title)
    var isWatching        by remember { mutableStateOf(false) }
    var isPlaying         by remember { mutableStateOf(false) }

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
                    onClick = { FavoritesManager.toggleFavorite(movie.title) },
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
                        if (movie.level != null) {
                            Surface(color = levelColor(movie.level).copy(0.85f), shape = RoundedCornerShape(6.dp)) {
                                Text(levelLabel(movie.level), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                            }
                        }
                        Surface(color = Color.White.copy(0.15f), shape = RoundedCornerShape(6.dp)) {
                            Text(movie.year, color = TextPrimary, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                }
            }

            // Konten bawah
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // Embedded YouTube Player
                if (!movie.youtubeUrl.isNullOrEmpty()) {
                    Spacer(Modifier.height(16.dp))
                    Text("Official Trailer", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(10.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, Brush.horizontalGradient(listOf(AccentRed, Color(0xFFFF8A8A)))),
                        colors = CardDefaults.cardColors(containerColor = Color.Black),
                        elevation = CardDefaults.cardElevation(12.dp)
                    ) {
                        if (isPlaying) {
                            YouTubePlayer(
                                youtubeUrl = movie.youtubeUrl,
                                autoplay = true,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {
                                        HistoryManager.addWatched(movie.title)
                                        isPlaying = true
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = movie.imageUrl,
                                    contentDescription = "Trailer Thumbnail",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.5f))
                                )
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .background(AccentRed, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.PlayArrow,
                                        contentDescription = "Play",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                Text("Sinopsis", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(movie.description, color = TextSecond, fontSize = 14.sp, lineHeight = 22.sp)
                Spacer(Modifier.height(28.dp))

                // Tombol Watch Now
                Button(
                    onClick = {
                        HistoryManager.addWatched(movie.title)
                        scope.launch {
                            isWatching = true
                            snackBarHostState.showSnackbar("▶ Memutar film: ${movie.title}")
                            delay(1000)
                            isWatching = false
                            isPlaying = true
                        }
                    },
                    enabled = !isWatching,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentRed,
                        contentColor = Color.White,
                        disabledContainerColor = AccentRed.copy(alpha = 0.5f),
                        disabledContentColor = Color.White
                    )
                ) {
                    if (isWatching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.PlayArrow, null, modifier = Modifier.size(22.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Tonton Sekarang", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
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

// Embedded YouTube Player helper components
@Composable
fun YouTubePlayer(youtubeUrl: String, autoplay: Boolean = false, modifier: Modifier = Modifier) {
    val videoId = remember(youtubeUrl) { extractYoutubeVideoId(youtubeUrl) }
    if (videoId != null) {
        val autoplayParam = if (autoplay) "1" else "0"
        val html = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                <style>
                    html, body { margin: 0; padding: 0; background-color: black; width: 100%; height: 100%; overflow: hidden; }
                    .video-container { position: relative; width: 100%; height: 100%; }
                    .video-container iframe { position: absolute; top: 0; left: 0; width: 100%; height: 100%; border: 0; }
                </style>
            </head>
            <body>
                <div class="video-container">
                    <iframe src="https://www.youtube.com/embed/$videoId?autoplay=$autoplayParam&playsinline=1&rel=0&enablejsapi=1&origin=https://www.youtube.com" allow="autoplay; encrypted-media; picture-in-picture" allowfullscreen></iframe>
                </div>
            </body>
            </html>
        """.trimIndent()
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    layoutParams = android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    
                    // Set hardcoded desktop user agent to bypass WebView playback blocking
                    settings.userAgentString = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36"

                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.mediaPlaybackRequiresUserGesture = false
                    settings.useWideViewPort = true
                    settings.loadWithOverviewMode = true
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, request: android.webkit.WebResourceRequest?): Boolean {
                            return false
                        }
                    }
                    webChromeClient = WebChromeClient()
                    loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "UTF-8", null)
                }
            },
            modifier = modifier
        )
    } else {
        Box(
            modifier = modifier.background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text("Trailer tidak tersedia", color = Color.White)
        }
    }
}

fun extractYoutubeVideoId(url: String): String? {
    return try {
        val cleanUrl = url.trim()
        when {
            cleanUrl.contains("youtu.be/") -> {
                val id = cleanUrl.substringAfter("youtu.be/").substringBefore("?").substringBefore("/")
                if (id.isNotEmpty()) id else null
            }
            cleanUrl.contains("embed/") -> {
                val id = cleanUrl.substringAfter("embed/").substringBefore("?").substringBefore("/")
                if (id.isNotEmpty()) id else null
            }
            cleanUrl.contains("shorts/") -> {
                val id = cleanUrl.substringAfter("shorts/").substringBefore("?").substringBefore("/")
                if (id.isNotEmpty()) id else null
            }
            cleanUrl.contains("v=") -> {
                val id = cleanUrl.substringAfter("v=").substringBefore("&").substringBefore("?")
                if (id.isNotEmpty()) id else null
            }
            else -> {
                val regex = Regex("^([a-zA-Z0-9_-]{11})$")
                if (regex.matches(cleanUrl)) cleanUrl else null
            }
        }
    } catch (e: Exception) {
        null
    }
}

// ==================== SCREEN SPLASH SCREEN ====================
@Composable
fun SplashScreen(navController: NavHostController) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "alpha"
    )
    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.8f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "scale"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2000)
        val nextDestination = if (UserManager.isLoggedIn) "genre_selection" else "login"
        navController.navigate(nextDestination) {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(300.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(AccentRed.copy(alpha = 0.15f), Color.Transparent)
                    )
                )
        )
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.graphicsLayer(
                alpha = alphaAnim,
                scaleX = scaleAnim,
                scaleY = scaleAnim
            )
        ) {
            Text("🎬", fontSize = 80.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "MoodFlix",
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                color = AccentRed
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Temukan Film Sesuai Mood Kamu",
                fontSize = 14.sp,
                color = TextSecond,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// ==================== GENRE SELECTION SCREEN ====================
data class GenreItem(
    val name: String,
    val description: String,
    val emoji: String,
    val gradient: Brush
)

@Composable
fun GenreSelectionScreen(navController: NavHostController) {
    val genres = listOf(
        GenreItem("Horror", "Uji nyali & hadapi ketakutanmu", "👻", Brush.verticalGradient(listOf(Color(0xFF4A0E17), Color(0xFF1F0307)))),
        GenreItem("Action", "Adrenalin & pertarungan seru", "💥", Brush.verticalGradient(listOf(Color(0xFF5E2E0B), Color(0xFF2E1202)))),
        GenreItem("Comedy", "Tawa ceria & komedi jenaka", "😂", Brush.verticalGradient(listOf(Color(0xFF5C520E), Color(0xFF2C2502)))),
        GenreItem("Sci-Fi", "Masa depan, teknologi & ruang angkasa", "🚀", Brush.verticalGradient(listOf(Color(0xFF0F4A4C), Color(0xFF032224)))),
        GenreItem("Romance", "Kisah cinta manis & penuh emosi", "💖", Brush.verticalGradient(listOf(Color(0xFF5A0E3E), Color(0xFF2A031B))))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MoodFlix",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = AccentRed
                    )
                    Text(
                        text = "Pilih Genre Film",
                        fontSize = 15.sp,
                        color = TextSecond
                    )
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = { navController.navigate("favorites") },
                        modifier = Modifier
                            .size(48.dp)
                            .background(CardDark, RoundedCornerShape(12.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Favorites List",
                            tint = AccentRed
                        )
                    }

                    IconButton(
                        onClick = { navController.navigate("profile") },
                        modifier = Modifier
                            .size(48.dp)
                            .background(CardDark, RoundedCornerShape(12.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Profile Screen",
                            tint = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(genres) { genre ->
                    GenreCard(genre = genre) {
                        navController.navigate("quiz/${genre.name}")
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun GenreCard(genre: GenreItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(genre.gradient)
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = genre.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = genre.description,
                        fontSize = 12.sp,
                        color = TextPrimary.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = genre.emoji,
                    fontSize = 44.sp
                )
            }
        }
    }
}



// ==================== FAVORITES SCREEN ====================
@Composable
fun FavoritesScreen(
    navController: NavHostController,
    repository: MovieRepository
) {
    var favoriteMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(FavoritesManager.favoriteTitles.size) {
        isLoading = true
        val all = repository.getAllMovies()
        favoriteMovies = all.filter { FavoritesManager.isFavorite(it.title) }.distinctBy { it.title }
        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = AccentRed
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Brush.verticalGradient(listOf(Color(0xFF1E0A0A), BgDark)))
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .size(40.dp)
                                .background(CardDark, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "Favorites",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                color = AccentRed
                            )
                            Text(
                                text = "Film yang kamu simpan",
                                fontSize = 13.sp,
                                color = TextSecond
                            )
                        }
                    }
                }

                if (favoriteMovies.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 100.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text("❤️", fontSize = 60.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Belum ada film favorit",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Ketuk ikon hati pada film untuk menambahkannya ke sini.",
                                color = TextSecond,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 40.dp)
                            )
                        }
                    }
                } else {
                    items(favoriteMovies) { movie ->
                        MovieCard(
                            movie = movie,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        ) {
                            navController.navigate("detail/${movie.title}")
                        }
                    }
                }
            }
        }
    }
}

// ==================== SCREEN: LOGIN ====================
@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        // Glowing Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(AccentRed.copy(alpha = 0.15f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Logo / Icon
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = CardDark),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.size(90.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎬", fontSize = 48.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "MoodFlix",
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                color = AccentRed
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Masuk untuk mencari film sesuai mood kamu",
                fontSize = 13.sp,
                color = TextSecond,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Username input
            // Username input
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = it.trim().isEmpty()
                },
                label = { Text("Username") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = AccentRed) },
                isError = usernameError,
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = moodFlixTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
            if (usernameError) {
                Text(
                    text = "Username tidak boleh kosong",
                    color = AccentRed,
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.Start).padding(start = 8.dp, top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Email input
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = AccentRed) },
                isError = emailError,
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = moodFlixTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError) {
                Text(
                    text = "Masukkan alamat email yang valid",
                    color = AccentRed,
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.Start).padding(start = 8.dp, top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = AccentRed) },
                singleLine = true,
                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                shape = RoundedCornerShape(16.dp),
                colors = moodFlixTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Login button
            Button(
                onClick = {
                    val inputUser = username.trim()
                    val inputEmail = email.trim()
                    
                    val isUserEmpty = inputUser.isEmpty()
                    val isEmailInvalid = !android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()
                    
                    usernameError = isUserEmpty
                    emailError = isEmailInvalid

                    if (!isUserEmpty && !isEmailInvalid) {
                        val regUser = UserManager.registeredUsername
                        val regEmail = UserManager.registeredEmail
                        val regPass = UserManager.registeredPassword
                        
                        val authenticated = if (regUser.isNotEmpty()) {
                            // Check registered credentials
                            (inputUser.equals(regUser, ignoreCase = true) || inputEmail.equals(regEmail, ignoreCase = true)) && password == regPass
                        } else {
                            // Fallback default user for testing
                            inputUser.equals("user", ignoreCase = true) && password == "pass"
                        }
                        
                        if (authenticated) {
                            UserManager.username = if (regUser.isNotEmpty()) regUser else "user"
                            UserManager.email = if (regEmail.isNotEmpty()) regEmail else "user@moodflix.com"
                            UserManager.isLoggedIn = true
                            
                            navController.navigate("genre_selection") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            val errorMsg = if (regUser.isNotEmpty()) {
                                "Username/Email atau Password salah!"
                            } else {
                                "Akun belum terdaftar. Silakan daftar terlebih dahulu atau gunakan user/pass."
                            }
                            dialogMessage = errorMsg
                            showDialog = true
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = "Masuk",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Register screen navigation link
            TextButton(onClick = { navController.navigate("register") }) {
                Text("Belum punya akun? Daftar di sini", color = AccentRed, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    MoodFlixDialog(show = showDialog, message = dialogMessage) {
        showDialog = false
    }
}

// ==================== SCREEN: PROFILE ====================
@Composable
fun ProfileScreen(navController: NavHostController) {
    val context = LocalContext.current
    var bioText by remember { mutableStateOf(UserManager.bio) }
    var isEditingBio by remember { mutableStateOf(false) }
    var profilePicPath by remember { mutableStateOf(UserManager.profilePicturePath) }

    var showUrlDialog by remember { mutableStateOf(false) }
    var tempUrlText by remember { mutableStateOf(profilePicPath) }
    
    val repository = remember { MovieRepository() }
    var watchedMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    
    LaunchedEffect(HistoryManager.historyTitles.size) {
        val all = repository.getAllMovies()
        watchedMovies = HistoryManager.historyTitles.mapNotNull { title ->
            all.find { it.title == title }
        }.reversed()
    }
    
    // Fetch stats
    val favoritesCount = FavoritesManager.favoriteTitles.size
    val genres = listOf("Horror", "Action", "Comedy", "Sci-Fi", "Romance")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(CardDark, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Profile Saya",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = AccentRed
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Avatar & Info
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(CardDark)
                        .clickable {
                            tempUrlText = profilePicPath
                            showUrlDialog = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    val isUrl = profilePicPath.startsWith("http://") || profilePicPath.startsWith("https://")
                    val hasLocalFile = profilePicPath.isNotEmpty() && !isUrl && java.io.File(profilePicPath).exists()
                    if (profilePicPath.isNotEmpty() && (isUrl || hasLocalFile)) {
                        AsyncImage(
                            model = if (isUrl) profilePicPath else java.io.File(profilePicPath),
                            contentDescription = "Profile Photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Brush.linearGradient(listOf(AccentRed, Color(0xFFFF8A8A)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = getInitials(UserManager.username),
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                    
                    // Transparent Edit Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.35f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit Photo",
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = UserManager.username,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = UserManager.email,
                    fontSize = 13.sp,
                    color = TextSecond
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Bio Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tentang Saya (Bio)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        IconButton(
                            onClick = {
                                if (isEditingBio) {
                                    UserManager.bio = bioText
                                }
                                isEditingBio = !isEditingBio
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (isEditingBio) Icons.Filled.Check else Icons.Filled.Edit,
                                contentDescription = if (isEditingBio) "Save" else "Edit",
                                tint = AccentRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    if (isEditingBio) {
                        OutlinedTextField(
                            value = bioText,
                            onValueChange = { bioText = it },
                            placeholder = { Text("Tulis sesuatu tentang dirimu...", color = TextSecond) },
                            textStyle = LocalTextStyle.current.copy(color = TextPrimary),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AccentRed,
                                unfocusedBorderColor = BgDark,
                                focusedContainerColor = BgDark,
                                unfocusedContainerColor = BgDark,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    } else {
                        Text(
                            text = if (bioText.trim().isEmpty()) "Belum ada bio." else bioText,
                            fontSize = 13.sp,
                            color = TextSecond,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Stats Section
            Text(
                text = "Statistik Aktivitas",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            // Favorite Stats Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("❤️", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Film Favorit", color = TextSecond, fontSize = 11.sp)
                        Text("$favoritesCount Film Tersimpan", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quiz Results Stats Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Level Hasil Quiz per Genre",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    genres.forEach { genre ->
                        val savedLvl = FavoritesManager.getSavedLevelForGenre(genre)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = genre, color = TextPrimary, fontSize = 13.sp)
                            if (savedLvl != null) {
                                Surface(
                                    color = levelColor(savedLvl).copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = levelLabel(savedLvl),
                                        color = levelColor(savedLvl),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            } else {
                                Text(text = "Belum tes", color = TextSecond, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            
            Text(
                text = "History Nonton",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 10.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CardDark)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    if (watchedMovies.isEmpty()) {
                        Text(
                            text = "Belum ada film yang ditonton.",
                            color = TextSecond,
                            fontSize = 13.sp
                        )
                    } else {
                        watchedMovies.forEach { movie ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .clickable {
                                        navController.navigate("detail/${movie.title}")
                                    },
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = movie.imageUrl,
                                    contentDescription = movie.title,
                                    modifier = Modifier
                                        .size(40.dp, 60.dp)
                                        .clip(RoundedCornerShape(6.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = movie.title,
                                        color = TextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    val labelText = if (movie.level != null) "${movie.genre} • ${levelLabel(movie.level)}" else movie.genre
                                    Text(
                                        text = labelText,
                                        color = TextSecond,
                                        fontSize = 12.sp
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = "Replay",
                                    tint = AccentRed,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        TextButton(
                            onClick = {
                                HistoryManager.clearHistory()
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Hapus Semua History", color = AccentRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logout Button
            Button(
                onClick = {
                    UserManager.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentRed.copy(0.12f)),
                border = BorderStroke(1.dp, AccentRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = null,
                    tint = AccentRed
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Keluar Akun",
                    color = AccentRed,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showUrlDialog) {
        AlertDialog(
            onDismissRequest = { showUrlDialog = false },
            title = {
                Text(
                    text = "Ubah Foto Profil",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Masukkan URL/link foto profil baru kamu:",
                        color = TextSecond,
                        fontSize = 13.sp
                    )
                    OutlinedTextField(
                        value = tempUrlText,
                        onValueChange = { tempUrlText = it },
                        placeholder = { Text("https://example.com/image.jpg", color = TextSecond) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentRed,
                            unfocusedBorderColor = CardDark,
                            focusedContainerColor = BgDark,
                            unfocusedContainerColor = BgDark,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        UserManager.profilePicturePath = tempUrlText.trim()
                        profilePicPath = tempUrlText.trim()
                        showUrlDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Simpan", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showUrlDialog = false }) {
                    Text("Batal", color = TextSecond)
                }
            },
            containerColor = CardDark,
            shape = RoundedCornerShape(18.dp)
        )
    }
}

fun getInitials(name: String): String {
    if (name.isEmpty()) return "?"
    val parts = name.trim().split("\\s+".toRegex())
    return if (parts.size > 1) {
        (parts[0].take(1) + parts[1].take(1)).uppercase()
    } else {
        parts[0].take(2).uppercase()
    }
}

// ==================== SCREEN: REGISTER ====================
@Composable
fun RegisterScreen(navController: NavHostController) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var usernameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var registrationSuccess by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDark)
    ) {
        // Glowing Background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(AccentRed.copy(alpha = 0.15f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Logo / Icon
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = CardDark),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.size(90.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🎬", fontSize = 48.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Daftar Akun",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = AccentRed
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Buat akun baru untuk mulai menjelajah",
                fontSize = 13.sp,
                color = TextSecond,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Username input
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = it.trim().isEmpty()
                },
                label = { Text("Username") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = AccentRed) },
                isError = usernameError,
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = moodFlixTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
            if (usernameError) {
                Text(
                    text = "Username tidak boleh kosong",
                    color = AccentRed,
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.Start).padding(start = 8.dp, top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Email input
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
                },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = AccentRed) },
                isError = emailError,
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = moodFlixTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError) {
                Text(
                    text = "Masukkan alamat email yang valid",
                    color = AccentRed,
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.Start).padding(start = 8.dp, top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Password Input
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = it.length < 6
                },
                label = { Text("Password (Min. 6 Karakter)") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = AccentRed) },
                isError = passwordError,
                singleLine = true,
                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                shape = RoundedCornerShape(16.dp),
                colors = moodFlixTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
            if (passwordError) {
                Text(
                    text = "Password minimal 6 karakter",
                    color = AccentRed,
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.Start).padding(start = 8.dp, top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Confirm Password Input
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = it != password
                },
                label = { Text("Konfirmasi Password") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = AccentRed) },
                isError = confirmPasswordError,
                singleLine = true,
                visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                shape = RoundedCornerShape(16.dp),
                colors = moodFlixTextFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
            if (confirmPasswordError) {
                Text(
                    text = "Password tidak cocok",
                    color = AccentRed,
                    fontSize = 11.sp,
                    modifier = Modifier.align(Alignment.Start).padding(start = 8.dp, top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Register button
            Button(
                onClick = {
                    val isUserEmpty = username.trim().isEmpty()
                    val isEmailInvalid = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    val isPassInvalid = password.length < 6
                    val isConfirmInvalid = confirmPassword != password

                    usernameError = isUserEmpty
                    emailError = isEmailInvalid
                    passwordError = isPassInvalid
                    confirmPasswordError = isConfirmInvalid

                    if (!isUserEmpty && !isEmailInvalid && !isPassInvalid && !isConfirmInvalid) {
                        UserManager.registeredUsername = username.trim()
                        UserManager.registeredEmail = email.trim()
                        UserManager.registeredPassword = password
                        
                        dialogMessage = "Registrasi Berhasil! Silakan masuk."
                        registrationSuccess = true
                        showDialog = true
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = "Daftar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Back to login link
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Sudah punya akun? Masuk di sini", color = AccentRed, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    MoodFlixDialog(show = showDialog, message = dialogMessage) {
        showDialog = false
        if (registrationSuccess) {
            navController.popBackStack()
        }
    }
}

// ==================== COMPOSABLE: BOTTOM NAVIGATION BAR ====================
@Composable
fun MoodFlixBottomBar(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showNavbar = currentRoute in listOf("genre_selection", "favorites", "profile")
    if (!showNavbar) return

    NavigationBar(
        containerColor = CardDark,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 11.sp) },
            selected = currentRoute == "genre_selection",
            onClick = {
                if (currentRoute != "genre_selection") {
                    navController.navigate("genre_selection") {
                        popUpTo("genre_selection") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = AccentRed,
                unselectedIconColor = TextSecond,
                unselectedTextColor = TextSecond,
                indicatorColor = AccentRed
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, contentDescription = "Favorites") },
            label = { Text("Favorites", fontSize = 11.sp) },
            selected = currentRoute == "favorites",
            onClick = {
                if (currentRoute != "favorites") {
                    navController.navigate("favorites") {
                        popUpTo("genre_selection") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = AccentRed,
                unselectedIconColor = TextSecond,
                unselectedTextColor = TextSecond,
                indicatorColor = AccentRed
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Profile") },
            label = { Text("Profile", fontSize = 11.sp) },
            selected = currentRoute == "profile",
            onClick = {
                if (currentRoute != "profile") {
                    navController.navigate("profile") {
                        popUpTo("genre_selection") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = AccentRed,
                unselectedIconColor = TextSecond,
                unselectedTextColor = TextSecond,
                indicatorColor = AccentRed
            )
        )
    }
}

// ==================== HELPER: PERSIST PROFILE PIC ====================
fun saveProfilePicture(context: android.content.Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = java.io.File(context.filesDir, "profile_avatar.jpg")
        val outputStream = java.io.FileOutputStream(file)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}