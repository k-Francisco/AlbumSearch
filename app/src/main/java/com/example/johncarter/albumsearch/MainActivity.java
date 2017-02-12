package com.example.johncarter.albumsearch;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<album>>{

    private static final String REQUEST_URL = "http://ws.audioscrobbler.com/2.0/?" ;

    private static final int ALBUM_LOADER_ID1 = 1;
    private static final int ALBUM_LOADER_ID2 = 2;
    private int searchBy = 1;
    private TextView mState;
    private TextView mSearchBy;
    private EditText mSearch;
    private RecyclerView recyclerView;
    private ProgressBar mLoading;
    private String search = "";
    private AlbumAdapter albumRecycler;
    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLoading = (ProgressBar) findViewById(R.id.pbLoading);
        mState = (TextView) findViewById(R.id.tvEmpty);
        mState.setText(R.string.no_albums);
        mSearchBy = (TextView) findViewById(R.id.tvSearchBy);
        mSearch = (EditText) findViewById(R.id.etSearch);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){

        }
        else{
            mLoading.setVisibility(View.GONE);
            mState.setText(R.string.no_internet);
        }

        mSearch.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    setSearch(mSearch.getText().toString());
                    performSearch();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.clear){
            mSearch.setText("");
        }
        if(id == R.id.artistName){
            mSearchBy.setText(R.string.search_by_artist);
            searchBy = 1;
        }
        if(id == R.id.albumName){
            mSearchBy.setText(R.string.search_by_album);
            searchBy = 2;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<ArrayList<album>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        if(id == ALBUM_LOADER_ID1) {
            uriBuilder.appendQueryParameter("method", "artist.gettopalbums");
            uriBuilder.appendQueryParameter("artist", getSearch());
            uriBuilder.appendQueryParameter("api_key", "1e6651a3bde8951b018d03e7337ceb0a");
            uriBuilder.appendQueryParameter("limit", "50");
            uriBuilder.appendQueryParameter("format", "json");
        }
        else if (id == ALBUM_LOADER_ID2){
            uriBuilder.appendQueryParameter("method", "album.search");
            uriBuilder.appendQueryParameter("album", getSearch());
            uriBuilder.appendQueryParameter("api_key", "1e6651a3bde8951b018d03e7337ceb0a");
            uriBuilder.appendQueryParameter("limit", "50");
            uriBuilder.appendQueryParameter("format", "json");
        }


        return new AlbumLoader(this,uriBuilder.toString(),id);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<album>> loader, ArrayList<album> data) {

        recyclerView.setVisibility(View.VISIBLE);
        albumRecycler = new AlbumAdapter(this,data);
        if(data != null && !data.isEmpty()){
            mState.setVisibility(View.GONE);
            mLoading.setVisibility(View.GONE);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(albumRecycler);
        }
        else{
            recyclerView.setVisibility(View.GONE);
            mLoading.setVisibility(View.GONE);
            mState.setVisibility(View.VISIBLE);
        }
        getLoaderManager().destroyLoader(searchBy);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<album>> loader) {

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void performSearch(){
        mState.setVisibility(View.GONE);
        mLoading.setVisibility(View.VISIBLE);
        loaderManager = getLoaderManager();
        loaderManager.initLoader(searchBy, null, this);
        hideSoftKeyboard(MainActivity.this);

    }

    public String getSearch() {
        return search;
    }
    public void setSearch(String search) {
        this.search = search;
    }
}
