package com.example.baloonstd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuSelect extends BaseActivity {
    private int mapNum=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        EdgeToEdge.enable(this);
        setContentView(R.layout.menu_select);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void goToGame0(View v) {
        mapNum=0;
        Intent i = new Intent(MenuSelect.this, gameActivity.class);
        i.putExtra("mapNum", mapNum);
        startActivity(i);
    }
    public void goToGame1(View v) {
        mapNum=1;
        Intent i = new Intent(MenuSelect.this, gameActivity.class);
        i.putExtra("mapNum", mapNum);
        startActivity(i);
    }
    public void goToGame2(View v) {
        mapNum=2;
        Intent i = new Intent(MenuSelect.this, gameActivity.class);
        i.putExtra("mapNum", mapNum);
        startActivity(i);
    }
    public int getMapNum(){ return mapNum;}
}
