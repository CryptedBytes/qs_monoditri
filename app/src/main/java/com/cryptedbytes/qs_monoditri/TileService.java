package com.cryptedbytes.qs_monoditri;

import static com.cryptedbytes.qs_monoditri.AudioMode.MONO;
import static com.cryptedbytes.qs_monoditri.AudioMode.STEREO;

import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class TileService extends android.service.quicksettings.TileService {
    AudioMode currentAudioMode;


    public TileService() {
        super();



        Log.println(6,"qsmonoditri", "constructor");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Log.d("TileService Events", "onStartListening");
        updateTileStatus();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();
        //Toast.makeText(this, "Tile clicked!!", Toast.LENGTH_SHORT).show();
       // Log.d("TileTest","Tile click event");

        //updateTileStatus();

        Log.d("qs_monoditri", "Output of shell command: " + executeShellForResult("settings get system master_mono",true));

        Log.d("qs_monoditri", executeShellForResult("settings get system master_mono", true));


        if(currentAudioMode == MONO){
            setAudioMode(STEREO);
            updateTileStatus();
            Log.d("qs_monoditri","setAudioMode(STEREO)");
        }
        else{
            setAudioMode(MONO);
            updateTileStatus();
            Log.d("qs_monoditri","setAudioMode(MONO)");
        }
    }


    public void updateTileStatus(){
        if(executeShellForResult("settings get system master_mono", true).contains("0")){
            Log.d("qs_monoditri", "stereo");
            currentAudioMode = STEREO;
            Tile tile = getQsTile();
            tile.setState(Tile.STATE_INACTIVE);
            tile.setLabel("Monoditri-Stereo");
            tile.updateTile();
        }
        else {
            Log.d("qs_monoditri", "mono");
            currentAudioMode = MONO;
            Tile tile = getQsTile();
            tile.setState(Tile.STATE_ACTIVE);
            tile.setLabel("Monoditri-Mono");
            tile.updateTile();
        }
    }

    public void updateTileStatus(boolean override, int overrideState){
        if(override){
            Tile tile = getQsTile();
            switch (overrideState){
                case Tile.STATE_UNAVAILABLE:
                    tile.setState(Tile.STATE_UNAVAILABLE);
                    tile.updateTile();
                    break;
                case Tile.STATE_INACTIVE:
                    tile.setState(Tile.STATE_INACTIVE);
                    tile.updateTile();
                    break;
                case Tile.STATE_ACTIVE:
                    tile.setState(Tile.STATE_ACTIVE);
                    tile.updateTile();
                    break;
            }
        }
    }


    public void setAudioMode(AudioMode targetaudiomode){
        if(targetaudiomode == MONO){
            executeShellForResult("settings put system master_mono 1", true);
        }
        else{
            executeShellForResult("settings put system master_mono 0", true);
        }
    }

    public static String executeShellForResult(String command, Boolean useRootShell) {
        String line="",output="";
        try
        {
            Process p;
            if(useRootShell) p = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
            else p = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});

            BufferedReader b=new BufferedReader(new InputStreamReader(p.getInputStream()));
            while((line=b.readLine())!=null){output+=line+"\r\n";}
        }catch(Exception e){return "error";}
        return output;

    }


}
