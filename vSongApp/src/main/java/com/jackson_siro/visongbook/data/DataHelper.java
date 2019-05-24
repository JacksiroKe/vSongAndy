package com.jackson_siro.visongbook.data;

import android.content.Context;
import android.widget.Filter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jackson_siro.visongbook.models.SearchModel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataHelper {

    private static final String COLORS_FILE_NAME = "colors.json";

    private static List<SearchWrapper> sSearchWrappers = new ArrayList<>();

    private static List<SearchModel> sSearchSuggestions =
            new ArrayList<>(Arrays.asList(
                    new SearchModel("green", 1),
                    new SearchModel("blue", 2),
                    new SearchModel("pink", 3),
                    new SearchModel("purple", 4),
                    new SearchModel("brown", 5),
                    new SearchModel("gray", 6),
                    new SearchModel("Granny Smith Apple", 7),
                    new SearchModel("Indigo", 8),
                    new SearchModel("Periwinkle", 9),
                    new SearchModel("Mahogany", 10),
                    new SearchModel("Maize", 11),
                    new SearchModel("Mahogany", 12),
                    new SearchModel("Outer Space", 13),
                    new SearchModel("Melon", 14),
                    new SearchModel("Yellow", 15),
                    new SearchModel("Orange", 16),
                    new SearchModel("Red", 17),
                    new SearchModel("Orchid", 18)));

    public interface OnFindSearchesListener {
        void onResults(List<SearchWrapper> results);
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<SearchModel> results);
    }

    public static List<SearchModel> getHistory(Context context, int count) {

        List<SearchModel> suggestionList = new ArrayList<>();
        SearchModel SearchModel;
        for (int i = 0; i < sSearchSuggestions.size(); i++) {
            SearchModel = sSearchSuggestions.get(i);
            SearchModel.setIsHistory(true);
            suggestionList.add(SearchModel);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    public static void resetSuggestionsHistory() {
        for (SearchModel SearchModel : sSearchSuggestions) {
            SearchModel.setIsHistory(false);
        }
    }

    public static void findSuggestions(Context context, String query, final int limit,
                                       final long simulatedDelay, final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DataHelper.resetSuggestionsHistory();
                List<SearchModel> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {

                    for (SearchModel suggestion : sSearchSuggestions) {
                        if (suggestion.getBody().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(suggestion);
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }
                }

                FilterResults results = new FilterResults();
                Collections.sort(suggestionList, new Comparator<SearchModel>() {
                    @Override
                    public int compare(SearchModel lhs, SearchModel rhs) {
                        return lhs.getIsHistory() ? -1 : 0;
                    }
                });
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (listener != null) {
                    listener.onResults((List<SearchModel>) results.values);
                }
            }
        }.filter(query);

    }


    public static void findColors(Context context, String query, final OnFindSearchesListener listener) {
        initSearchWrapperList(context);

        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {


                List<SearchWrapper> suggestionList = new ArrayList<>();

                if (!(constraint == null || constraint.length() == 0)) {

                    for (SearchWrapper color : sSearchWrappers) {
                        if (color.getName().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(color);
                        }
                    }

                }

                FilterResults results = new FilterResults();
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<SearchWrapper>) results.values);
                }
            }
        }.filter(query);

    }

    private static void initSearchWrapperList(Context context) {

        if (sSearchWrappers.isEmpty()) {
            String jsonString = loadJson(context);
            sSearchWrappers = deserializeColors(jsonString);
        }
    }

    private static String loadJson(Context context) {

        String jsonString;

        try {
            InputStream is = context.getAssets().open(COLORS_FILE_NAME);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return jsonString;
    }

    private static List<SearchWrapper> deserializeColors(String jsonString) {

        Gson gson = new Gson();

        Type collectionType = new TypeToken<List<SearchWrapper>>() {
        }.getType();
        return gson.fromJson(jsonString, collectionType);
    }

}