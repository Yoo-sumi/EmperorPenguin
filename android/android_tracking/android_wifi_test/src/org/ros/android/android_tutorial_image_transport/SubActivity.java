package org.ros.android.android_tutorial_image_transport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubActivity extends Activity {
    private ImageButton buttonBack;
    //view Objects
    private Button buttonAdd, buttonEdit;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        buttonAdd = (Button) findViewById(R.id.button_add);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //button onClick
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //scan option
                qrScan.setPrompt("Scanning...");
                qrScan.setOrientationLocked(false);
                qrScan.initiateScan();
            }
        });

        buttonBack = findViewById(R.id.button_back);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new RecyclerAdapter(new ArrayList());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }


    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
                Toast.makeText(SubActivity.this, "취소!", Toast.LENGTH_SHORT).show();
            } else {
                //qrcode 결과가 있으면
                Toast.makeText(SubActivity.this, "스캔완료!", Toast.LENGTH_SHORT).show();
                try {
                    //data를 json으로 변환
                    JSONObject obj = new JSONObject(result.getContents());

                    int id = obj.getInt("id");
                    String name = obj.getString("name");
                    int price = obj.getInt("price");
                    int amount;

                    Product old = adapter.getItemList(id);

                    if (old == null) { // 새 품목일 때
                        amount = 1;
                        Product p = new Product(id, name, price, amount, price * amount);
                        adapter.addItemList(p);
                    } else { // 해당 품목 추가 이력이 있을 때
                        amount = old.getAmount() + 1;
                        old.setAmount(amount);
                        old.setTotal(price * amount);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
