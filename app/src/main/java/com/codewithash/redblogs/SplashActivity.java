package com.codewithash.redblogs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.analytics.FirebaseAnalytics;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {
    public static String A_ID,B_ID,I_ID,R_ID,B_ID_S = null;
    private static final String BLOGGER_POST_URL = "https://rsakhiji.blogspot.com/";
    private static final long SPLASH_DISPLAY_LENGTH = 4000;

    FirebaseAnalytics firebaseAnaly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE);
        }

        setContentView(R.layout.activity_splash);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        if (isNetworkAvailable())
        {
            new FetchPostContentTask().execute();

        }else {
            B_ID="ca-app-pub-6651078789096656/5050900757";
            B_ID_S="ca-app-pub-6651078789096656/5050900757";
            I_ID="ca-app-pub-6651078789096656/2561302936";
            R_ID="ca-app-pub-3940256099942544/5224354917";
        }





        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ImageView imageView = findViewById(R.id.spsh);
                imageView.setVisibility(View.GONE);
                ImageView imageviewsq = findViewById(R.id.spshq);
                imageviewsq.setVisibility(View.VISIBLE);
            }
        },3000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }
        },8000);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        return false;
    }
    private class FetchPostContentTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // Fetch post content from the Blogger post URL
                Document doc = Jsoup.connect(BLOGGER_POST_URL).get();

                // Extract IDs from the post content
                Elements paragraphs = doc.select("p");
                for (Element paragraph : paragraphs) {
                    String text = paragraph.text();
                    if (text.contains("App ID")) {
                        String appId = text.split("App ID: ")[1];
                        A_ID = appId;
                        Log.d("App ID Units", appId);
                    } else if (text.contains("Banner ID")) {
                        String bannerId = text.split("Banner ID: ")[1];
//                        BannerAds();
                        B_ID = bannerId;
                        Log.d("Banner ID Units", bannerId);
                    }else if (text.contains("Shabadban ID")) {
                        String bannerIdsh = text.split("Shabadban ID: ")[1];
                        B_ID_S = bannerIdsh;
                        Log.d("Shabadban ID", bannerIdsh);
                    } else if (text.contains("Interstitial ID")) {
                        String interstitialId = text.split("Interstitial ID: ")[1];
                        I_ID = interstitialId;
                        Log.d("Interstitial ID Units", interstitialId);
                    } else if (text.contains("Reward ID")) {
                        String rewardId = text.split("Reward ID: ")[1];
                        R_ID=rewardId;
                        Log.d("Reward ID Units", rewardId);
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            setAdMobAppId(A_ID);
            AdManager adManager = new AdManager(SplashActivity.this);
            adManager.createAd();


            super.onPostExecute(unused);
        }
    }
    private void setAdMobAppId(String appId) {
        try {
            // Use reflection to set the AdMob App ID dynamically
            Class<?> clazz = Class.forName("com.google.android.gms.ads.MobileAds");
            clazz.getDeclaredMethod("initialize", android.content.Context.class, String.class)
                    .invoke(null, this, appId);
        } catch (Exception e) {
            // Log error or handle it accordingly
            e.printStackTrace();
        }
    }
}