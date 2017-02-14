package com.cisco.blook;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by JiyoungPark on 2017. 2. 14..
 */
public class BookData {
/*
    String sale_price;
    String cover_s_url;
    String author;
    String title;
    String description;
    String list_price;
    */

    public int result;
    public String title;
    public int totalCount;
    public String description;
    public List<BookItem> item;
    public String lastBuildDate;
    public String link;
    public String generator;
}

