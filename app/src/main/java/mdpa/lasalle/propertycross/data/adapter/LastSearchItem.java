package mdpa.lasalle.propertycross.data.adapter;

import android.support.annotation.NonNull;

import mdpa.lasalle.propertycross.ui.adapters.AdapterLastSearches;

public class LastSearchItem implements AdapterLastSearches.LastSearchItem<LastSearchItem.LastSearch> {

    public static class LastSearch implements AdapterLastSearches.LastSearchItem.LastSearch {
        private String lastSearch;

        public LastSearch(@NonNull String lastSearch) {
            this.lastSearch = lastSearch;
        }

        @NonNull @Override
        public String getLastSearch() {
            return lastSearch;
        }
    }

    private Object item;

    public LastSearchItem(@NonNull LastSearch lastSearch) {
        this.item = lastSearch;
    }

    @NonNull @Override
    public LastSearch getLastSearch() {
        return (LastSearch)item;
    }
}
