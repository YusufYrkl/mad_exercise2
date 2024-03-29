package com.example.movieappmad24

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.movieappmad24.models.Movie
import com.example.movieappmad24.models.getMovies
import com.example.movieappmad24.ui.theme.MovieAppMAD24Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppMAD24Theme {
                MovieExplorerApp()
            }
        }
    }
}

@Composable
fun MovieExplorerApp() {
    Scaffold(
        topBar = { AppTopBar() },
        bottomBar = { NavigationBarComponent() }
    ) { paddingValues ->
        MovieListScreen(modifier = Modifier.padding(paddingValues))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar() {
    CenterAlignedTopAppBar(
        title = { Text("CinemaScope") }
    )
}

@Composable
fun NavigationBarComponent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButtonItem(icon = Icons.Filled.Home, label = "Home")
        IconButtonItem(icon = Icons.Filled.List, label = "Watchlist")
    }
}

@Composable
fun IconButtonItem(icon: ImageVector, label: String) {
    IconButton(onClick = { /* navigation logic */ }) {
        Icon(icon, contentDescription = label)
    }
}

@Composable
fun MovieListScreen(modifier: Modifier = Modifier) {
    val movies = getMovies()
    MovieListView(movies = movies, modifier = modifier)
}

@Composable
fun MovieListView(movies: List<Movie>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(movies) { movie ->
            MovieCard(movie = movie)
        }
    }
}

@Composable
fun MovieCard(movie: Movie) {
    var isExpanded by remember { mutableStateOf(false) }
    val toggleExpand: () -> Unit = { isExpanded = !isExpanded }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize()
    ) {
        MovieCardHeader(movie = movie, isExpanded = isExpanded, toggleExpand = toggleExpand)
        MovieCardDetails(movie = movie, isExpanded = isExpanded)
    }
}

@Composable
fun MovieCardHeader(movie: Movie, isExpanded: Boolean, toggleExpand: () -> Unit) {
    Card {
        Box {
            AsyncImage(
                model = movie.images.firstOrNull(),
                contentDescription = "Movie Poster",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Like",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                tint = Color.White
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = movie.title)
        IconButton(onClick = toggleExpand) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                contentDescription = "Expand"
            )
        }
    }
}

@Composable
fun MovieCardDetails(movie: Movie, isExpanded: Boolean) {
    AnimatedVisibility(visible = isExpanded) {
        Column {
            Text(text = "Director: ${movie.director}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Year: ${movie.year}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Genre: ${movie.genre}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Synopsis: ${movie.plot}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    MovieAppMAD24Theme {
        MovieExplorerApp()
    }
}
