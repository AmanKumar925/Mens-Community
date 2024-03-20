package com.codewithash.redblogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

public class AppRatingManager {
    private static final String PREFS_NAME = "AppRatingPrefs";
    private static final String KEY_RATED = "Rated";

    public static void promptForRating(final Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (!prefs.getBoolean(KEY_RATED, false)) {
            // The user hasn't rated the app yet, show the rating prompt
            showRatingDialog(context);
        }
    }

    private static void showRatingDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Rate Our App");
        builder.setMessage("Enjoying our app? Please take a moment to rate it!");
        builder.setPositiveButton("Rate Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Open app store for rating
                openPlayStoreForRating(context);

                // Mark as rated
                markAsRated(context);
            }
        });
        builder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // User clicked "Not Now", do nothing
            }
        });


        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void openPlayStoreForRating(Context context) {
        // Replace "com.example.yourapp" with your app's package name
        String appPackageName = "com.dreamfactory.radhasoamiji";

        try {
            // Open the app's page on the Play Store
            context.startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW,
                    android.net.Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            // If the Play Store app is not available, open the Play Store website
            context.startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW,
                    android.net.Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private static void markAsRated(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_RATED, true);
        editor.apply();
    }
}
