package com.lco.flashlight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button button;
    ImageView img;
    private static final int CAMERA_REQUEST=50;
    private boolean flashlightstatus=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
        img=findViewById(R.id.img);
        final boolean hascameraflash=getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        boolean isenable= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED;
        button.setEnabled(!isenable);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST);

            }

        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hascameraflash) {
                    if (flashlightstatus) {
                        flashLightOff();
                    } else {
                        flashLightOn();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No Flash Is Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void flashLightOn(){
        CameraManager cameraManager=(CameraManager)getSystemService(CAMERA_SERVICE);
        try{
            String cameraId=cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId,true);
            flashlightstatus=true;
            img.setImageResource(R.drawable.onn);
        }catch (CameraAccessException e){

        }
    }
    private void flashLightOff(){
        CameraManager cameraManager=(CameraManager)getSystemService(CAMERA_SERVICE);
        try{
            String cameraId=cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId,false);
            flashlightstatus=false;
            img.setImageResource(R.drawable.offf);
        }catch (CameraAccessException e){

        }

    }
    public void onRequestPermissionResult(int reuestCode, @NonNull String[] permissions,@NonNull int[] grantResults){
        switch (reuestCode){
            case CAMERA_REQUEST:
            if(grantResults.length>0 && grantResults[0]==
            PackageManager.PERMISSION_GRANTED){
                button.setEnabled(false);
                button.setText("Camera Enabled");
                img.setEnabled(true);
            }
            else{

                Toast.makeText(MainActivity.this,"Permission Denied"+"For camera",Toast.LENGTH_SHORT).show();
                img.setEnabled(false);
            }
            break;

        }
    }

}

