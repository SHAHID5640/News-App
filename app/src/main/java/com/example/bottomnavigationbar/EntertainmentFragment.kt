package com.example.bottomnavigationbar

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.bottomnavigationbar.R


class EntertainmentFragment : Fragment(),NewsItemClicked {
    private lateinit var mAdapter: NewsListAdapter
    private lateinit var v: View
    private lateinit var recyclerView: RecyclerView


    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_entertainment, null)
        recyclerView = v.findViewById(R.id.recyclerViewofEntertainment)
        recyclerView.layoutManager = LinearLayoutManager(context)
        fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter

        return v
    }

    private fun fetchData() {

        val url =
            "https://gnews.io/api/v4/top-headlines?&token=bd813f1bf3d55923cb997dd8bb6fc752&category=entertainment&lang=hi&breaking-news&country-in"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                val newsJSONArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until newsJSONArray.length()) {
                    val newsJsonObject = newsJSONArray.getJSONObject(i)
                    val author=newsJsonObject.getJSONObject("source")
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("image"),
                        newsJsonObject.getString("publishedAt"),
                        author.getString("name")
                    )
                    newsArray.add(news)
                }
                mAdapter.updatedNews(newsArray)
            },
            {

            }
        )
        context?.let { MySingleton.getInstance(it).addToRequestQueue(jsonObjectRequest) }
    }

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        context?.let {
            customTabsIntent.launchUrl(it, Uri.parse(item.url))
        }


    }
}