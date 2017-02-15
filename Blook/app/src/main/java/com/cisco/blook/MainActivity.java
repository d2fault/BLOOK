package com.cisco.blook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class MainActivity extends AppCompatActivity {

    private GlideDrawableImageViewTarget imageViewTarget_camera, imageViewTarget_history;
    private ImageView imageView_camera, imageView_history;
    private AsyncHttpClient asyncHttpClient;
    private BookData bookData;
    private String ibmStr;
    private final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initModel();
        initView();
        initListener();
    }

    protected void initModel() {
        asyncHttpClient = new AsyncHttpClient();
    }

    protected void initView() {
        imageView_camera = (ImageView) findViewById(R.id.imageView_camera);
        imageView_history = (ImageView) findViewById(R.id.imageView_history);

        imageViewTarget_camera = new GlideDrawableImageViewTarget(imageView_camera);
        Glide.with(this).load(R.raw.buttnum222).into(imageViewTarget_camera);

        imageViewTarget_history = new GlideDrawableImageViewTarget(imageView_history);
        Glide.with(this).load(R.mipmap.buttnum1).into(imageViewTarget_history);


        final Activity activity = this;
        imageView_camera.setOnClickListener(new View.OnClickListener() {
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

    protected void initListener() {
        imageView_camera.setOnClickListener(new View.OnClickListener() {
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
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this,"Cancelled", Toast.LENGTH_SHORT).show();
            }else{
                ibmStr = result.getContents();

                if(result.getFormatName().startsWith("EAN_13") && ibmStr.startsWith("978")) {
                    getBookInfo();
                }else{
                    Toast.makeText(this,"ISBN을 인식하지 못했습니다", Toast.LENGTH_SHORT).show();
                }

                // result.getContents() 이거가 -> isbn 번호입니다!!
                // Log.d("MainActivity", "Scanned");
                // Toast.makeText(this,"Scanned: " + result.getContents(), Toast.LENGTH_SHORT).show();
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

                JSONObject json = new JSONObject();
                try{
                    json.put("coversurl", bookData.item.get(0).cover_s_url);
                    json.put("title", bookData.item.get(0).title);
                    json.put("author", bookData.item.get(0).author);
                    json.put("authort", bookData.item.get(0).author_t);
                    json.put("listprice", bookData.item.get(0).list_price);
                    json.put("saleyn", bookData.item.get(0).sale_yn);
                    json.put("saleprice", bookData.item.get(0).sale_price);
                    json.put("description", bookData.item.get(0).description);
                    json.put("link", bookData.item.get(0).link);

                }catch(JSONException e) {
                    e.printStackTrace();
                }

                ByteArrayEntity entity = new ByteArrayEntity(json.toString().getBytes(StandardCharsets.UTF_8));

                asyncHttpClient.post(getApplicationContext(), "http://52.78.5.187:5005/api/books", entity, "application/json", new TextHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.e("go to server completed.", "");
                        Toast.makeText(getApplicationContext(), "성공적으로 서버에 저장하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.e("server inincompleted.", "");
                        Toast.makeText(getApplicationContext(), "서버와의 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }

                });

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