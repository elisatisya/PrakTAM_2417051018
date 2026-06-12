package com.example.praktam_2417051018.data.model

object LocalMovieSource {
    val movies = listOf(
        // Action
        Movie(
            title = "Marvel's The Avengers",
            year = "2012",
            imageUrl = "https://image.tmdb.org/t/p/w500/RYMX2wc7H6Zr2461jGSJg7q4ebd.jpg",
            description = "Loki, saudara tiri Thor dari Asgard, bersekutu dengan alien untuk menginvasi Bumi. Nick Fury mengumpulkan para pahlawan terkuat untuk menghentikannya.",
            genre = "Action",
            youtubeUrl = "https://www.youtube.com/watch?v=eOrNdByGMv8",
            level = "ringan"
        ),
        Movie(
            title = "The Dark Knight",
            year = "2008",
            imageUrl = "https://image.tmdb.org/t/p/w500/qJ2tWw2mIMIvR0G41mFmG4n31nK.jpg",
            description = "Ketika ancaman yang dikenal sebagai Joker mengacaukan kota Gotham, Batman harus menerima salah satu tes psikologis dan fisik terbesar untuk melawan ketidakadilan.",
            genre = "Action",
            youtubeUrl = "https://www.youtube.com/watch?v=EXeTwQWrcwY",
            level = "sedang"
        ),
        Movie(
            title = "Mad Max: Fury Road",
            year = "2015",
            imageUrl = "https://image.tmdb.org/t/p/w500/8tZYBMwNzYA5dfv272S3875NuDV.jpg",
            description = "Di gurun pasca-apokaliptik, seorang wanita memberontak melawan penguasa tirani dalam pencarian tanah airnya dengan bantuan sekelompok tawanan wanita dan seorang pengembara bernama Max.",
            genre = "Action",
            youtubeUrl = "https://www.youtube.com/watch?v=hEJnMQG9ev8",
            level = "ekstrem"
        ),
        // Comedy
        Movie(
            title = "Jumanji: Welcome to the Jungle",
            year = "2017",
            imageUrl = "https://image.tmdb.org/t/p/w500/vgpbg7tq46aBzo78z169j974415.jpg",
            description = "Empat remaja menemukan konsol video game tua dan ditarik ke dalam latar hutan permainan, secara harfiah menjadi avatar dewasa yang mereka pilih.",
            genre = "Comedy",
            youtubeUrl = "https://www.youtube.com/watch?v=2QKg5SZ_35I",
            level = "ringan"
        ),
        Movie(
            title = "The Hangover",
            year = "2009",
            imageUrl = "https://image.tmdb.org/t/p/w500/wnekr59Gg9R22vJ5K4q360215ch.jpg",
            description = "Tiga pengiring pria terbangun dari pesta bujang di Las Vegas tanpa ingatan tentang malam sebelumnya dan menyadari calon pengantin pria telah hilang.",
            genre = "Comedy",
            youtubeUrl = "https://www.youtube.com/watch?v=tcdUhdOlz9M",
            level = "sedang"
        ),
        Movie(
            title = "Superbad",
            year = "2007",
            imageUrl = "https://image.tmdb.org/t/p/w500/ek8go1yGslM9126k861L51k62c2.jpg",
            description = "Dua sahabat sekolah menengah yang tidak populer merencanakan pesta kelulusan yang liar untuk memikat gadis-gadis, namun rencana mereka menjadi sangat kacau.",
            genre = "Comedy",
            youtubeUrl = "https://www.youtube.com/watch?v=2n55h2mRslE",
            level = "ekstrem"
        ),
        // Sci-Fi
        Movie(
            title = "Back to the Future",
            year = "1985",
            imageUrl = "https://image.tmdb.org/t/p/w500/fN5JBFh54j6mVRlq1z9a1u76gS0.jpg",
            description = "Marty McFly secara tidak sengaja terkirim kembali ke tahun 1955 menggunakan mobil DeLorean penemu waktu buatan sahabatnya, Dr. Emmett Brown.",
            genre = "Sci-Fi",
            youtubeUrl = "https://www.youtube.com/watch?v=qvsgGtIvCQs",
            level = "ringan"
        ),
        Movie(
            title = "Interstellar",
            year = "2014",
            imageUrl = "https://image.tmdb.org/t/p/w500/gEU2QvH353eGo3i8db26vNm9v21.jpg",
            description = "Sebuah tim penjelajah melakukan perjalanan melalui lubang cacing di luar angkasa dalam upaya untuk memastikan kelangsungan hidup umat manusia.",
            genre = "Sci-Fi",
            youtubeUrl = "https://www.youtube.com/watch?v=zSWdZAIGM3I",
            level = "sedang"
        ),
        Movie(
            title = "Inception",
            year = "2010",
            imageUrl = "https://image.tmdb.org/t/p/w500/o0j4eg0uTIocJSjoiGe4aar5G62.jpg",
            description = "Seorang pencuri yang mencuri rahasia perusahaan melalui penggunaan teknologi berbagi mimpi diberi tugas sebaliknya: menanamkan ide ke dalam pikiran seorang CEO.",
            genre = "Sci-Fi",
            youtubeUrl = "https://www.youtube.com/watch?v=YoHD9XEInc0",
            level = "ekstrem"
        ),
        // Romance
        Movie(
            title = "La La Land",
            year = "2016",
            imageUrl = "https://image.tmdb.org/t/p/w500/u7CHaV9o354t94Xg46a9Fv24u.jpg",
            description = "Seorang pianis jazz dan seorang aktris yang bercita-cita tinggi jatuh cinta sambil mengejar impian mereka di Los Angeles, menghadapi pilihan sulit antara cinta dan karir.",
            genre = "Romance",
            youtubeUrl = "https://www.youtube.com/watch?v=0pdqf4P9MB8",
            level = "ringan"
        ),
        Movie(
            title = "The Fault in Our Stars",
            year = "2014",
            imageUrl = "https://image.tmdb.org/t/p/w500/4n034w4L6V24Uq6iV4a71o46a9F.jpg",
            description = "Dua pasien kanker remaja memulai perjalanan untuk menemui seorang penulis terasing di Amsterdam dan belajar bagaimana menghargai hidup dan cinta.",
            genre = "Romance",
            youtubeUrl = "https://www.youtube.com/watch?v=9ItBvH5urOk",
            level = "sedang"
        ),
        Movie(
            title = "Titanic",
            year = "1997",
            imageUrl = "https://image.tmdb.org/t/p/w500/9xj7v4a65HGwPG85d26m1w13G.jpg",
            description = "Kisah cinta antara Jack dan Rose, dua sejoli dari kelas sosial berbeda, di atas kapal termegah yang menemui nasib tragis di pelayaran perdananya.",
            genre = "Romance",
            youtubeUrl = "https://www.youtube.com/watch?v=CHekzSiZhsY",
            level = "ekstrem"
        ),
        // Horror
        Movie(
            title = "Annabelle",
            year = "2014",
            imageUrl = "https://tse3.mm.bing.net/th/id/OIP.KtP8oTqUDadltnJLOh39pQHaK-?pid=Api&P=0&h=180",
            description = "Sebuah boneka antik menjadi wadah bagi roh jahat yang mengincar pasangan muda dan bayi mereka yang baru lahir.",
            genre = "Supernatural",
            youtubeUrl = "https://www.youtube.com/watch?v=aMOsjGxeQh4",
            level = "ringan"
        ),
        Movie(
            title = "The Conjuring",
            year = "2013",
            imageUrl = "https://tse4.mm.bing.net/th/id/OIP.bZssMYeq1sUG3eHh51rqfQHaLH?pid=Api&P=0&h=180",
            description = "Pasangan paranormal Ed dan Lorraine Warren menyelidiki sebuah keluarga yang diteror oleh kehadiran gelap di rumah pertanian terpencil mereka.",
            genre = "Supernatural",
            youtubeUrl = "https://youtu.be/u0jVO7TFS8w?si=ZmIaHvDVMd26mzCI",
            level = "sedang"
        ),
        Movie(
            title = "Insidious",
            year = "2010",
            imageUrl = "https://www.sonypictures.ca/sites/canada/files/2023-08/DP_7164177_InsidiousTheRedDoor_2000x3000-min.jpg",
            description = "Sebuah keluarga berjuang untuk menyelamatkan putra mereka yang terjebak dalam kondisi koma misterius dan menjadi sasaran roh jahat.",
            genre = "Supernatural",
            youtubeUrl = "https://www.youtube.com/watch?v=2WjHBxqj2Tk",
            level = "sedang"
        ),
        Movie(
            title = "The Nun",
            year = "2018",
            imageUrl = "https://i.pinimg.com/736x/92/2a/70/922a70e6a129644df8731ac3840789a9.jpg",
            description = "Seorang pastor dan seorang calon biarawati dikirim ke Rumania untuk menyelidiki kematian misterius seorang biarawati di sebuah biara terpencil.",
            genre = "Supernatural",
            youtubeUrl = "https://www.youtube.com/watch?v=pzD9zGcUNrw",
            level = "ekstrem"
        ),
        Movie(
            title = "IT",
            year = "2017",
            imageUrl = "https://i.pinimg.com/originals/7f/f7/fe/7ff7fe3da302ed431d0ef031d9223964.jpg",
            description = "Sekelompok anak-anak di kota Derry menghadapi badut menakutkan bernama Pennywise yang memangsa ketakutan terdalam mereka.",
            genre = "Psychological",
            youtubeUrl = "https://www.youtube.com/watch?v=FnCdOQsX5kc",
            level = "ekstrem"
        )
    )
}
