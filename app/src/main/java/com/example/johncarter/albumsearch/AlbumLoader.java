package com.example.johncarter.albumsearch;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by john carter on 2/12/2017.
 */

public class AlbumLoader extends AsyncTaskLoader<ArrayList<album>> {

    private String mUrl;
    private int requestCode;

    public AlbumLoader(Context context, String mUrl, int requestCode) {
        super(context);
        this.mUrl = mUrl;
        this.requestCode = requestCode;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<album> loadInBackground() {
        if(mUrl == null)
            return null;

        return QueryUtils.fetchAlbumData(mUrl,requestCode);
    }
}
