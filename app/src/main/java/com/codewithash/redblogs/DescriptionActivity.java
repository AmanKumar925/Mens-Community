package com.codewithash.redblogs;

import static com.codewithash.redblogs.SplashActivity.B_ID;

import android.speech.tts.TextToSpeech;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class DescriptionActivity extends AppCompatActivity {
    private Toolbar descriptionToolbar;
    private TextView descActivityTitle;
    private TextView descAvtivityDesc;
    private ImageView descActivityImage;
    private Button descActivityAudioBtn;
    private String blog_post_id;
    private FirebaseFirestore firebaseFirestore;
    AdView adView;
    private FirebaseAuth firebaseAuth;
    private TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        descriptionToolbar = findViewById(R.id.desc_toolbar);
        setSupportActionBar(descriptionToolbar);
        getSupportActionBar().setTitle("Mans Community");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        blog_post_id = getIntent().getStringExtra("blog_post_id");
        BannerAdsPlacement();
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    int lang = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }

        });
        descActivityAudioBtn = findViewById(R.id.desc_activity_audiobtn);
        descActivityAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = descAvtivityDesc.getText().toString();
                int speech = textToSpeech.speak(s,TextToSpeech.QUEUE_FLUSH,null);

            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null) {
            firebaseFirestore = FirebaseFirestore.getInstance();
            firebaseFirestore.collection("Posts").document(blog_post_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        String title = task.getResult().getString("title");
                        String desc = task.getResult().getString("desc");
                        String imageurl = task.getResult().getString("image_url");
                        setBlogPost(title,desc,imageurl);
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(DescriptionActivity.this,error,Toast.LENGTH_LONG).show();
                    }
                }
            });

        }


    }
    @Override
    protected void onStop()
    {
        super.onStop();

        if(textToSpeech != null){
            textToSpeech.shutdown();
        }
    }
    private void BannerAdsPlacement() {

        adView = new AdView(this);
        adView.setAdUnitId(B_ID); // Replace with your AdMob ad unit ID
        adView.setAdSize(AdSize.BANNER);
        LinearLayout adContainer = findViewById(R.id.bannerAdContainer);
        adContainer.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void setBlogPost(String title, String desc, String imageurl) {
        descActivityTitle = findViewById(R.id.desc_activity_title);
        descActivityImage = findViewById(R.id.desc_activity_image);
        descAvtivityDesc = findViewById(R.id.desc_activity_desc);
        descActivityTitle.setText(title);
        descAvtivityDesc.setText(desc);
        RequestOptions placeholderOption = new RequestOptions();
        placeholderOption.placeholder(R.drawable.default_image);
        Glide.with(DescriptionActivity.this).applyDefaultRequestOptions(placeholderOption).load(imageurl).into(descActivityImage);
    }
}
