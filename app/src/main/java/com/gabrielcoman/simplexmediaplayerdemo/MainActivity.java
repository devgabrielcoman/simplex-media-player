package com.gabrielcoman.simplexmediaplayerdemo;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gabrielcoman.simplexmediaplayer.Simplex;
import com.gabrielcoman.simplexmediaplayer.aux.style.SimplexStyle;

import tv.superawesome.lib.sanetwork.file.SAFileDownloader;
import tv.superawesome.lib.sanetwork.file.SAFileDownloaderInterface;

public class MainActivity extends AppCompatActivity {

    private static final String myPlayerTag = "Taggy";
    private Simplex myPlayer;

    private static final String url1 = "https://s3-eu-west-1.amazonaws.com/sb-ads-video-transcoded/UAICy6n2MiSfyxmPoPjV4sqWPVXTRjVi.mp4";
    private static final String url2 = "https://ads.superawesome.tv/v2/demo_images/video.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentManager manager = getFragmentManager();
        if (manager.findFragmentByTag(myPlayerTag) == null) {

            myPlayer = new Simplex();
            myPlayer.shouldAutostart();
            myPlayer.setStyle(SimplexStyle.greenStyle());
//            myPlayer.hideController();
            manager.beginTransaction()
                    .add(R.id.PlayerHolder, myPlayer, myPlayerTag)
                    .commitAllowingStateLoss();
        }
        else {
            myPlayer = (Simplex) manager.findFragmentByTag(myPlayerTag);
        }

    }

    public void gotoOther (View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://hotnews.ro")));
    }

    public void destroyVideo (View view) {
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction()
                .remove(myPlayer)
                .commitAllowingStateLoss();
    }

    public void playMedia1 (View view) {
        myPlayer.setMedia(url1);
    }

    public void playMedia2 (View view) {
        SAFileDownloader.getInstance().downloadFileFrom(MainActivity.this, url2, new SAFileDownloaderInterface() {
            @Override
            public void saDidDownloadFile(boolean b, String s) {
                myPlayer.setMedia(s);
            }
        });
    }
}
