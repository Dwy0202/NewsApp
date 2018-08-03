package com.example.android.newsapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class NetworkUtility {

    private static final String LOG_TAG = NetworkUtility.class.getSimpleName();


    private NetworkUtility() {

    }


    public static List<NewsArticle> fetchNewsData(String requestUrl) {

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with http request", e);

            e.printStackTrace();
        }
        List<NewsArticle> newsArticles = extractFeatureFromJson(jsonResponse);

        return newsArticles;
    }


    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            Log.e(LOG_TAG, "Connection Made");


            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
                Log.e(LOG_TAG, "Response 200");
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON response.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        Log.e(LOG_TAG, "JSON Response Returned");
        return jsonResponse;
    }


    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<NewsArticle> extractFeatureFromJson(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<NewsArticle> newsArticles = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(newsJSON);


            JSONObject resultsObject = baseJsonResponse.getJSONObject("response");

            JSONArray resultsArray = resultsObject.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject currentArticle = resultsArray.getJSONObject(i);


                String sectionName = currentArticle.getString("sectionName");

                String webDate = formatDate(currentArticle.getString("webPublicationDate"));

                String webTitle = currentArticle.getString("webTitle");

                String articleUrl = currentArticle.getString("webUrl");

                JSONObject fields = currentArticle.getJSONObject("fields");

                String thumbnail = fields.optString("thumbnail");

                JSONArray tags = currentArticle.getJSONArray("tags");

                JSONObject tagsObject = tags.optJSONObject(0);

                String author = getAuthor(tagsObject);


                newsArticles.add(new NewsArticle(webTitle, sectionName, webDate, author, articleUrl, bitMap(thumbnail)));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsArticles;
    }

    private static Bitmap bitMap(String thumbnailUrl) {
        Bitmap bitMap = null;
        try {
            InputStream input = new URL(thumbnailUrl).openStream();
            bitMap = BitmapFactory.decodeStream(input);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitMap;
    }


    private static String getAuthor(JSONObject authorObject) {
        if (authorObject == null) {
            return null;
        }
        String firstName = authorObject.optString("firstName");

        String lastName = authorObject.optString("lastName");

        String completeName = ("By: " + firstName + " " + lastName);

        return completeName;
    }

    private static String formatDate(String dateObject) {

        String year = dateObject.substring(0, 4);
        String month = dateObject.substring(5, 7);
        String day = dateObject.substring(8, 10);

        String formattedDate = (month + "-" + day + "-" + year);


        return formattedDate;
    }
}
