package com.example.android.newsapp;

import android.graphics.Bitmap;

public class NewsArticle {

    private String mTitle;

    private String mSectionName;

    private String mDate;

    private String mAuthor;

    private String mUrl;

    private Bitmap mImageUrl;

    public NewsArticle(String title, String sectionName, String date, String author, String url, Bitmap ImageUrl) {
        mTitle = title;
        mSectionName = sectionName;
        mDate = date;
        mUrl = url;
        mImageUrl = ImageUrl;
        mAuthor = author;

    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSectionName() {
        return mSectionName;
    }


    public String getmDate() {
        return mDate;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmUrl() {
        return mUrl;
    }

    public Bitmap getmImageUrl() {
        return mImageUrl;
    }


}


