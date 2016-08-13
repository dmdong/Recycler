package com.manhdong.recycler;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ReadRss";

    SwipeRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        refresh.setOnRefreshListener(refreshLayout());
        try {
            new MyAsyncTask().execute(new URL("http://vnexpress.net/rss/tin-moi-nhat.rss"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        new ServletAsyncTask().execute(new Pair<Context, String>(this, "Saphiro"));



    }

    private SwipeRefreshLayout.OnRefreshListener refreshLayout() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);

// Xử lý sao nữa??
            }
        };

    }


    class MyAsyncTask extends AsyncTask<URL, Void, List<News>>{
        @Override
        protected List<News> doInBackground(URL... params) {
            URLConnection urlConnection = null;
            //StringBuilder total = new StringBuilder();
            List<News> newsList = new ArrayList<>();
            try {
                urlConnection = params[0].openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                int respCode = httpURLConnection.getResponseCode();
                if(respCode == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = httpURLConnection.getInputStream();
                    //parse xml
                     newsList = parseRSSVnExpress(inputStream);

                    //test parse
                    for(News news : newsList)
                        Log.i(TAG, "doInBackground: " + news.toString());


                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO: xuat gia tri tra ve len textView
            return newsList;
        }

        @Override
        protected void onPostExecute(List<News> newsList) {
            super.onPostExecute(newsList);
            initRecyclerView(newsList);
        }
    };

    void initRecyclerView (final List<News> newsList){

        RecyclerView myRecycler = (RecyclerView) findViewById(R.id.myRecycler);
        NewsAdapter adapter = new NewsAdapter(this, R.layout.item_cardview, newsList);
        myRecycler.setAdapter(adapter);
        myRecycler.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
//        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        //listener lắng nghe item bên trong viewholder
//        myRecycler.setRecyclerListener(new RecyclerView.RecyclerListener() {
//            @Override
//            public void onViewRecycled(RecyclerView.ViewHolder holder) {
//                String touch = newsList.get(holder.getLayoutPosition()).getTitle();
//                Toast.makeText(getApplicationContext(), "Item clicked"+touch , Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    public List<News> parseRSSVnExpress(InputStream inputStream){
        List<News> newsList = new ArrayList<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            if(document != null) {
                NodeList items = document.getElementsByTagName("item");
                if(items != null) {

                    for (int i = 0; i < items.getLength(); i++) {
                        News news = new News();
                        Node item = items.item(i);
                        NodeList children = item.getChildNodes();
                        for (int j = 0; j < children.getLength(); j++) {
                            Node node = children.item(j);
                            String content = node.getTextContent();
                            switch (node.getNodeName()){
                                case News.TITLE:
                                    news.setTitle(content);
                                    break;
                                case News.PUB_DATE:
                                    news.setPubDate(content);
                                    break;
                                case News.DESCRIPTION:
//                                    news.setTitle(content);
//                                    news.setDescription(content);
                                    int src = content.indexOf("src=")+5;
                                    int lastsrc = content.indexOf("\" ></a>");
                                    int txt = content.indexOf("</br>")+5;
//                                    String last = lastsrc+"";
//                                    Log.i("lastsrc", last+img);
                                    String desc = content.substring(txt);
                                    String img = content.substring(src,lastsrc);
//                                  Log.i("DESC", desc);

                                    news.setDescription(desc);
                                    news.setImageLink(img);
//                                    String temp[] = content.split("</br>");
//                                    //temp[0] chua image, temp[1] chua description
//                                    news.setDescription(temp[1]);
//                                    news.setImageLink(temp[0].split("src=\"")[1].split("\"")[0].trim());

                                    break;
                                case News.LINK:
                                    news.setLink(content);
                                    break;
                            }
                        }

                        newsList.add(news);
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newsList;
    }
}
