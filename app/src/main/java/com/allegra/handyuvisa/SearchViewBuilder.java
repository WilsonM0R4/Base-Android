package com.allegra.handyuvisa;

import android.app.SearchManager;
import android.content.Context;
import android.view.Menu;
import android.widget.SearchView;

/**
 * Created by lisachui on 7/17/15.
 */
public class SearchViewBuilder {

    public static void build(Context context, Menu menu) {
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
       /* ComponentName searchActivityComponentName = new ComponentName(context, SearchActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(searchActivityComponentName));
        searchView.setIconifiedByDefault(false);*/ // Do not iconify the widget; expand it by default
    }
}
