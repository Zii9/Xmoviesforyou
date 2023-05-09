package com.xmoviesforyou
  
 import com.lagradost.cloudstream3.* 
 import org.jsoup.Jsoup 
  
 class Xmoviesforyourovider : MainAPI() { // all providers must be an instance of MainAPI 
     override var mainUrl = "https://xmoviesforyou.com/" 
     override var name = "xmoviesforyou" 
     override val supportedTypes = setOf(TvType.NSFW) 
  
     override var lang = "vi" 
  
     // enable this when your provider has a main page 
     override val hasMainPage = true 
  
     // this function gets called when you search for something 
     override suspend fun search(query: String): List<SearchResponse> { 
         return listOf<SearchResponse>() 
     } 
  
     override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse? { 
         val html = app.get(mainUrl).text 
         val doc = Jsoup.parse(html) 
         val listHomePageList = arrayListOf<HomePageList>() 
         doc.select(".block").forEach { 
             val name = it.select(".caption").text().trim() 
             val urlMore = fixUrl(it.select(".see-more").attr("href")) 
             val listMovie = it.select(".list-film .item").map { 
                 val title = it.select("p").last()!!.text() 
                 val href = fixUrl(it.selectFirst("a")!!.attr("href")) 
                 val year = 0 
                 val image = it.selectFirst("img")!!.attr("src") 
                 MovieSearchResponse( 
                     title, 
                     href, 
                     this.name, 
                     TvType.NSFW, 
                     image, 
                     year, 
                     posterHeaders = mapOf("referer" to mainUrl) 
                 ) 
             } 
             if (listMovie.isNotEmpty()) 
                 listHomePageList.add(HomePageList(name, listMovie )) 
         } 
  
         return HomePageResponse(listHomePageList) 
     } 
 }
