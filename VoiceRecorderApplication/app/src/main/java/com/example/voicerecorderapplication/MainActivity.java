package com.example.voicerecorderapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.widget.Toast;

import com.example.voicerecorderapplication.databinding.ActivityMainBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding tasarim;
    private boolean kayit=false;
    private MediaRecorder mediaRecorder=null;
    private String dosyaYolu="";
    private String dosya="";
    private String izinKayit=Manifest.permission.RECORD_AUDIO;
    private String izinDepolama=Manifest.permission.WRITE_EXTERNAL_STORAGE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tasarim= DataBindingUtil.setContentView(this,R.layout.activity_main);
        tasarim.setNesnem(this);
    }
    private void setDosyaYolu(){
        dosyaYolu=this.getExternalFilesDir("/").getAbsolutePath();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss",Locale.UK);
        Date date=new Date();
        dosya=simpleDateFormat.format(date)+".3gp";
    }
    public void butonKontrol() {
        Toast.makeText(this, "Buton Kontrol", Toast.LENGTH_LONG).show();
        if (izinKontrol()) {
            if (!kayit) {
                kayit=true;
                setDosyaYolu();
                baslat();
            }
            else{
                kayit=false;
                durdur();
            }
        }
        else {
            Toast.makeText(this, "İzin Vermelisiniz.", Toast.LENGTH_LONG).show();
            izinAl();
        }
    }
    public void baslat(){
           mediaRecorder=new MediaRecorder();
           mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
           mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
           mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
           mediaRecorder.setOutputFile(dosyaYolu+"/"+dosya);
           kayit=true;

           tasarim.imageButton.setImageResource(R.drawable.ic_baseline_mic_24);

           tasarim.chronometer1.setBase(SystemClock.elapsedRealtime());
           tasarim.chronometer1.start();
           try{
               mediaRecorder.prepare();
               mediaRecorder.start();
               Toast.makeText(getApplicationContext(),
                       "Kayıt Başlatıldı.",
                       Toast.LENGTH_SHORT).show();
           }
           catch (IOException e){
               e.printStackTrace();
           }
    }
    public void durdur(){
        tasarim.imageButton.setImageResource(R.drawable.ic_baseline_mic_off_24);
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder=null;
        kayit=false;
        tasarim.chronometer1.stop();
        Toast.makeText(getApplicationContext(),
                "Kayıt Durduruldu.",
                Toast.LENGTH_SHORT).show();
    }
    public boolean izinKontrol() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(),izinDepolama)==PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(getApplicationContext(),izinKayit)==PackageManager.PERMISSION_GRANTED&&
                Build.VERSION.SDK_INT >= 23)
                return true;
        return false;
    }
    private void izinAl() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{izinKayit,izinDepolama}, 1);
    }
}