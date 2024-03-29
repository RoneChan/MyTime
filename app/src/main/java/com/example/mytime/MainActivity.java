package com.example.mytime;

import android.content.Intent;
import android.os.Bundle;
import com.example.mytime.ui.AddNewTime.NewTime;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import static com.example.mytime.ui.AddNewTime.NewTime.TIME_OK;
import static com.example.mytime.ui.home.HomeFragment.times;


public class MainActivity extends AppCompatActivity {


    public final static int TIME_NEW_REQUEST_CODE = 201;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //顶部
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //右下角圆圈小图标
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewTime.class);
                startActivityForResult(intent, TIME_NEW_REQUEST_CODE);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_help, R.id.nav_about,
                R.id.nav_study, R.id.nav_birth, R.id.nav_work, R.id.nav_vocation, R.id.nav_self_def)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    //覆盖onSupportNavigateUp()以处理向上导航
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TIME_NEW_REQUEST_CODE && resultCode == TIME_OK) {
             MyTime time = new MyTime(data.getStringExtra("title"), data.getStringExtra("year")
                        , data.getStringExtra("month"), data.getStringExtra("day"), data.getStringExtra("remark")
                        , data.getStringExtra("tag"), data.getStringExtra("reset"),data.getStringExtra("image"));
                times.add(time);
        }
    }
}
