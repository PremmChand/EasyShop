package com.example.easyshop.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.HorizontalPagerIndicator

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BannerView(modifier: Modifier=Modifier){

    var bannerList by remember{
        mutableStateOf<List<String>>(emptyList())
    }
    LaunchedEffect(Unit) {
        Firebase.firestore.collection("banners")
            .document("banners")
            .get().addOnCompleteListener(){
                bannerList = it.result.get("urls") as List<String>
            }
    }


    /*Column(
        modifier=modifier
    ) {
        val pagerState = rememberPagerState(0) {
            bannerList.size
        }
        HorizontalPager(state=pagerState, pageSpacing = 24.dp){
            AsyncImage( bannerList.get(it), contentDescription = "banner image",
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(10.dp)))

        }
        Spacer(modifier = Modifier.height(10.dp))
        DotsIndicator(
            dotCount = bannerList.size,
            type=ShiftIndicatorTpe(DotGraphic(
                color = MaterialTheme.colors.primary
            )),
            pagerState =pagerState
        )
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            activeColor = MaterialTheme.colors.primary
        )

    }*/

    val pagerState = rememberPagerState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            count = bannerList.size,
            state = pagerState
        ) { page ->
            AsyncImage(
                model = bannerList[page],
                contentDescription = "Banner Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = MaterialTheme.colors.primary,
            modifier = Modifier
                //.fillMaxWidth()
                .padding(16.dp)
        )
    }

}