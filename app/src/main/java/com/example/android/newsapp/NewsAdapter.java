package com.example.android.newsapp;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<NewsArticle> {


    public NewsAdapter(Context context, List<NewsArticle> newsArticles) {
        super(context, 0, newsArticles);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        NewsArticle currentNewsArticle = getItem(position);

        TextView section = (TextView) listItemView.findViewById(R.id.section);
        section.setText(currentNewsArticle.getmSectionName());


        TextView title = (TextView) listItemView.findViewById(R.id.title);
        title.setText(currentNewsArticle.getmTitle());


        TextView date = (TextView) listItemView.findViewById(R.id.date);

        date.setText(currentNewsArticle.getmDate());

        TextView author = (TextView) listItemView.findViewById(R.id.author);

        author.setText(currentNewsArticle.getmAuthor());

        ImageView thumbnail = (ImageView) listItemView.findViewById(R.id.articleimage);
        thumbnail.setImageBitmap(currentNewsArticle.getmImageUrl());


        return listItemView;
    }

}
