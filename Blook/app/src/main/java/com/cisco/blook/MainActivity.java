package com.cisco.blook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ImageButton imageButton_camera;
    private Button button_getInfo;
    private AsyncHttpClient asyncHttpClient;
    private BookData bookData;
    private String ibmStr;
    private final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
    }

    protected void initView() {
        button_getInfo = (Button) findViewById(R.id.button_getInfo);
        imageButton_camera = (ImageButton) findViewById(R.id.imageButton_camera);
        asyncHttpClient = new AsyncHttpClient();
    }

    protected void initListener() {
        imageButton_camera.setOnClickListener(new View.OnClickListener() {
            @Override
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
        button_getInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBookInfo();
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
                ibmStr = result.getContents();
                getBookInfo();
                //result.getContents() 이거가 -> isbn 번호입니다!!
                Log.d("MainActivity", "Scanned");
                //Toast.makeText(this,"Scanned: " + result.getContents(), Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }


    private void getBookInfo() {
        asyncHttpClient.get(getString(R.string.daum_book_api_url) + getString(R.string.daum_book_api_key)
                + "&q=" + ibmStr + "&output=json", new BaseJsonHttpResponseHandler<JSONObject>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, JSONObject response) {

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

                intent.putExtra("cover_s_url", bookData.item.get(0).cover_s_url);
                intent.putExtra("title", bookData.item.get(0).title);
                intent.putExtra("author", bookData.item.get(0).author);
                intent.putExtra("author_t", bookData.item.get(0).author_t);
                intent.putExtra("list_price", bookData.item.get(0).list_price);
                intent.putExtra("sale_yn", bookData.item.get(0).sale_yn);
                intent.putExtra("sale_price", bookData.item.get(0).sale_price);
                intent.putExtra("description", bookData.item.get(0).description);
                intent.putExtra("link", bookData.item.get(0).link);
                //액티비티 시작!
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, JSONObject errorResponse) {

            }

            @Override
            protected JSONObject parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                Gson gson = new Gson();

                try {
                    JSONObject json = new JSONObject(rawJsonData);
                    // jsonString을 JSON객체로 만들고

                    json = json.getJSONObject("channel");
                    // channel 안에 있는 JSON 객체를 가져옵니다! => 다음 api 예시 참고

                    bookData = gson.fromJson(json.toString(), BookData.class);


                    //Log.e("HERE!!!!!", bookData.item.get(0).cover_s_url);

                    // 그 후 json을 문자열 형태로 받아와서 GSON을 이용해 shopResult에 매핑해줍니다.

                    //customAdapter.setSource((ArrayList<ShopItem>) shopResult.item);
                    // 그 결과에서 item를 어댑터를 통해서 리스트뷰의 source로 지정해줍니다.

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        });
    }
}