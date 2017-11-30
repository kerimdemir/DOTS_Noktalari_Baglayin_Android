package com.example.kerim.noktakutu.Firebase_Database;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.kerim.noktakutu.NetworkChangeReceiver;
import com.example.kerim.noktakutu.OyunSecimActivity;
import com.example.kerim.noktakutu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class EnIyiOyuncular extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    ProgressDialog Dialog;

    private RecyclerView recyclerView;
    private InventorAdapter inventorAdapter;
    private List<InventorModel> inventorModels;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private FloatingActionButton fab;
    private CustomTabsIntent.Builder customTabsIntent;
    private NetworkChangeReceiver receiver;
    public static int COUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eniyioyuncu);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        inventorModels = new ArrayList<>();
        inventorAdapter = new InventorAdapter(this, inventorModels);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(inventorAdapter);

        fetchFirebaseDb();


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                customTabsIntent = new CustomTabsIntent.Builder();
                customTabsIntent.setToolbarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }


    @Override
    protected void onDestroy() { //Activity Kapatıldığı zaman receiver durduralacak.Uygulama arka plana alındığı zamanda receiver çalışmaya devam eder
        super.onDestroy();

        unregisterReceiver(receiver);//receiver durduruluyor


    }

    private void fetchFirebaseDb() {
        Dialog = new ProgressDialog(EnIyiOyuncular.this);
        Dialog = new ProgressDialog(EnIyiOyuncular.this);
        Dialog.setMessage("Lütfen Bekleyiniz");
        Dialog.setCancelable(false);
        Dialog.show();
        myRef = database.getReference();
        myRef.child("inventors").orderByChild("puan").limitToFirst(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                inventorModels.clear();
                Log.d(TAG, "Value is: " + dataSnapshot.getChildrenCount());
                COUNT = Integer.parseInt(dataSnapshot.getChildrenCount() + "");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    InventorModel inventorModel = data.getValue(InventorModel.class);
                    inventorModels.add(inventorModel);
                }
                inventorAdapter.notifyDataSetChanged();
                Dialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar kullaniciadi on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the kullaniciadi when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}
