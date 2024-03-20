package com.codewithash.redblogs;

import static com.codewithash.redblogs.SplashActivity.I_ID;


import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AdManager {
    // Static fields are shared between all instances.
    public static InterstitialAd ad;
    private Context ctx;

    public AdManager(Context ctx) {
        this.ctx = ctx;
        createAd();
    }

    public void createAd() {
        // Create an ad.
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(ctx,I_ID, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                       
                        ad = interstitialAd;

                        ad.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdClicked() {

                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                ad = null;

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                ad = null;
                            }

                            @Override
                            public void onAdImpression() {

                            }

                            @Override
                            public void onAdShowedFullScreenContent() {

                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        ad = null;
                    }
                });
    }

    public InterstitialAd getAd() {
        return ad;
    }
}