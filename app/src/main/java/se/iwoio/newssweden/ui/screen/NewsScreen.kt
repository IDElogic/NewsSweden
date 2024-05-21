package se.iwoio.newssweden.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import se.iwoio.newssweden.R
import se.iwoio.newssweden.data.Article
import se.iwoio.newssweden.data.NewsResult

@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.pic32),
            contentDescription = "image",
            contentScale = ContentScale.Crop
        )
        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 0.dp),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .background(Color.Transparent)
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(.7f)
                            .padding(20.dp),
                        onClick = {
                            newsViewModel.getNews()
                        })
                    {
                        Text(
                            text = "News from Hungary",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(10.dp),
                        )
                    }
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(width = 1.dp, color = Color.White),
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 4.dp)
                            .width(90.dp)
                            .height(90.dp)
                            .clickable {},
                    ) {
                        val imageModifier = Modifier
                            .fillMaxWidth(1f)
                            .blur(0.dp)

                        Image(
                            painter = painterResource(id = R.drawable.news),
                            contentDescription = "releasers",
                            modifier = imageModifier,
                            contentScale = ContentScale.Fit,
                        )
                    }
                }


                when (newsViewModel.newsUiState) {
                    is NewsUiState.Init -> {}
                    is NewsUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .align(Alignment.Center),
                            )
                        }
                    }

                    is NewsUiState.Success -> ResultScreen((newsViewModel.newsUiState as NewsUiState.Success).news)
                    is NewsUiState.Error -> Text(text = "Error: ${(newsViewModel.newsUiState as NewsUiState.Error).errorMsg}")
                }
            }
        }}
}

@Composable
fun ResultScreenString(news: String) {
    Text(text = news)
}

@Composable
fun ResultScreen(newsResult: NewsResult) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(newsResult.articles!!) {
            NewsCard(it!!)
        }
    }
}



@Composable
fun NewsCard (
    article: Article
) {
    val uriHandler = LocalUriHandler.current

    val annotatedString = buildAnnotatedString {
       //append(article.url)
        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline, color = Color.Blue)) {
            append("Link")
            addStringAnnotation(
                tag = "URL",
                annotation = article.url!!,
                start = length - article.url.length,
                end = length
            )
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = article.author ?:"",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = article.publishedAt ?: "",
                fontSize = 12.sp
            )
            Text(
                text = article.title ?: ""
            )
            ClickableText(
                text = annotatedString,
                onClick = { offset ->
                    annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                        .firstOrNull()?.let { annotation ->
                            uriHandler.openUri(annotation.item)
                        }
                }
            )
        }
    }
}


