package com.example.sunbr.blookfront;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_FROM_GALLERY = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton findFromG = (ImageButton) findViewById(R.id.findFromG);
        findFromG.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);

                intent.putExtra("crop", "true");
                intent.putExtra("outputX", 400);
                intent.putExtra("outputY", 250);

                try {
                    intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, PICK_FROM_GALLERY);

                } catch (ActivityNotFoundException e) {
                    // Do nothing for now
                }

            }

        });
        ImageButton takeP = (ImageButton) findViewById(R.id.takeP);
        final Activity activity = this;
        takeP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();


            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this,"Cancelled", Toast.LENGTH_SHORT).show();
            }else{
                //result.getContents() 이거가 -> isbn 번호입니다!!
                Log.d("MainActivity", "Scanned");
                Toast.makeText(this,"Scanned: " + result.getContents(), Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
/*        Bundle extras;
        try {
            Toast.makeText(getApplicationContext(), "들어간다~", Toast.LENGTH_SHORT).show();
            extras = data.getExtras();
            Bitmap photo = extras.getParcelable("data");
            switch (requestCode) {
                case PICK_FROM_GALLERY:
                    Intent intent = new Intent(MainActivity.this,FindFromGall.class);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    if(photo != null) photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    intent.putExtra("image",byteArray);
                    startActivity(intent);
                break;
            }
//            dir = saveImage(photo);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }*/
    }

}
