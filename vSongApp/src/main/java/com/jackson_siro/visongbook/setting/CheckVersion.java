package com.jackson_siro.visongbook.setting;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CheckVersion {

    public static void isOutdatedCheck(Context context){
        SharedPreferences prefget;
        SharedPreferences.Editor prefedit;

        String appVersion = null;
        String appUpdated = null;
        String appSize = null;
        String appFeatures = null;

        prefget = PreferenceManager.getDefaultSharedPreferences(context);
        prefedit = prefget.edit();
        try {
            Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=com.jackson_siro.visongbook&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
            if (document != null) {

                Elements vElements = document.getElementsContainingOwnText("Current Version");
                Elements dElements = document.getElementsContainingOwnText("Updated");
                Elements sElements = document.getElementsContainingOwnText("Size");
                Elements fElements = document.getElementsContainingOwnText("What&#39;s New");

                for (Element ele : vElements) {
                    if (ele.siblingElements() != null) {
                        Elements sibElemets = ele.siblingElements();
                        for (Element sibElemet : sibElemets) appVersion = sibElemet.text();
                    }
                }

                for (Element ele : dElements) {
                    if (ele.siblingElements() != null) {
                        Elements sibElemets = ele.siblingElements();
                        for (Element sibElemet : sibElemets) appUpdated = sibElemet.text();
                    }
                }

                for (Element ele : sElements) {
                    if (ele.siblingElements() != null) {
                        Elements sibElemets = ele.siblingElements();
                        for (Element sibElemet : sibElemets) appSize = sibElemet.text();
                    }
                }

                for (Element ele : fElements) {
                    if (ele.siblingElements() != null) {
                        Elements sibElemets = ele.siblingElements();
                        for (Element sibElemet : sibElemets) appFeatures = sibElemet.text();
                    }
                }

                prefedit.putString("app_version_current", appVersion).commit();
                prefedit.putString("app_version_updated", appUpdated).commit();
                prefedit.putString("app_version_size", appSize).commit();
                prefedit.putString("app_version_features", appFeatures).commit();
            }
        }catch (Exception e){

        }
    }
}
