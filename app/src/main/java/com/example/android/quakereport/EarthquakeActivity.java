/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class EarthquakeActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=20";
    private EarthquakeAdapter mAdapter;
    // Constant value for the earthquake loader ID.
    // Only comes in play if using multiple loaders
    private static final int EARTHQUAKE_LOADER_ID = 1;
    public static final String TAG = "EarthquakeActivity";
    // TextView that is displayed when the list is empty
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: initialized");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.emptyView);
        earthquakeListView.setEmptyView(mEmptyStateTextView);


        // Set OnItemClickListener and add an Intent to go to the URL of the specific
        // quake website on USGS website
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake currentEarthquake = mAdapter.getItem(position);
                Intent quakeWebsiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentEarthquake.getEarthquakeUrl()));
                startActivity(quakeWebsiteIntent);

            }
        });

        /**
         * Tests network or wifi connection. If there is connection, the Loader will proceed as
         * planned, if it is not connected, it will enter "no internet connection" in empty
         * text view and set visibility of progress bar to gone.
         **/
        ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            // Initialize the loader. Pass in the int ID constant defined above and pass in null
            // for the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface.
            Log.i(TAG, "onCreate: initloader initialized");
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }else{
            mEmptyStateTextView.setText(R.string.no_internet);
            View progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);

        }

    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        Log.i("onCreateLoader", "onCreateLoader: initialized");
        return new EarthquakeLoader(this, USGS_REQUEST_URL);

    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {

        // Method defined below to set text if there is no internet connection
//        isOnline();

        // This will add a progress bar to show the app is working if the internet connection
        // is slow or if other factors are causing the app to take additional time to load
        // the data
        View progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // Set text for the empty view if there is no data to display
        mEmptyStateTextView.setText(R.string.no_earthquakes);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();
        Log.i(TAG, "onLoadFinished: initialized");

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        mAdapter.clear();
        Log.i(TAG, "onLoaderReset: initialized");
    }

    public static class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

        // Tag for log messages
        private final String LOG_TAG = EarthquakeLoader.class.getName();

        // Query URL
        private String mUrl;

        public EarthquakeLoader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
            Log.i(TAG, "onStartLoading: initialized");
        }

        @Override
        public List<Earthquake> loadInBackground() {
            if (mUrl == null) {
                Log.i(TAG, "loadInBackground: initialized");
                return null;
            }

            List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
            return earthquakes;
        }

    }




}
