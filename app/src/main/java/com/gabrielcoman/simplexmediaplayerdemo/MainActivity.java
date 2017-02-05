package com.gabrielcoman.simplexmediaplayerdemo;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gabrielcoman.simplexmediaplayer.Simplex;
import com.gabrielcoman.simplexmediaplayer.SimplexEvent;

import tv.superawesome.lib.sanetwork.file.SAFileDownloader;
import tv.superawesome.lib.sanetwork.file.SAFileDownloaderInterface;

public class MainActivity extends AppCompatActivity {

    private static final String myPlayerTag = "Taggy";
    private Simplex myPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentManager manager = getFragmentManager();
        if (manager.findFragmentByTag(myPlayerTag) == null) {
            myPlayer = new Simplex();

            SAFileDownloader.getInstance().downloadFileFrom(MainActivity.this, "https://s3-eu-west-1.amazonaws.com/sb-ads-video-transcoded/UAICy6n2MiSfyxmPoPjV4sqWPVXTRjVi.mp4", new SAFileDownloaderInterface() {
                @Override
                public void saDidDownloadFile(boolean success, final String filePath) {

                    myPlayer.setListener(new Simplex.SimplexInterface() {
                        @Override
                        public void didReceiveEvent(SimplexEvent event) {
                            if (event == SimplexEvent.Prepared) {
                                try {
                                    myPlayer.play(filePath);
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            }
                        }
                    });

                    manager.beginTransaction().add(R.id.PlayerHolder, myPlayer, myPlayerTag).commit();
                }
            });

        }
        else {
            myPlayer = (Simplex) manager.findFragmentByTag(myPlayerTag);
        }

    }
}
