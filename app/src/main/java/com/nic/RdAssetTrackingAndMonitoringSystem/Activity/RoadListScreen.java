package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.nic.RdAssetTrackingAndMonitoringSystem.Adapter.RoadListAdapter;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;
import com.nic.RdAssetTrackingAndMonitoringSystem.Utils.Utils;

import java.util.ArrayList;



public class RoadListScreen extends AppCompatActivity implements View.OnClickListener{

    private ShimmerRecyclerView recyclerView;
    public dbData dbData = new dbData(this);
    private RoadListAdapter roadListAdapter;
    private ArrayList<RoadListValue> roadLists = new ArrayList<>();
    private ImageView back_img;
    private LinearLayout district_user_layout, block_user_layout;
    private MyCustomTextView district_tv, block_tv,title_tv;
    Handler myHandler = new Handler();
    private SearchView searchView;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.road_list_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intializeUI();
    }


    public void intializeUI() {
        prefManager = new PrefManager(this);
        district_user_layout = (LinearLayout) findViewById(R.id.district_user_layout);
        block_user_layout = (LinearLayout) findViewById(R.id.block_user_layout);
        district_tv = (MyCustomTextView) findViewById(R.id.district_tv);
        title_tv = (MyCustomTextView) findViewById(R.id.title_tv);
        block_tv = (MyCustomTextView) findViewById(R.id.block_tv);
        recyclerView = (ShimmerRecyclerView) findViewById(R.id.road_list);
        back_img = (ImageView) findViewById(R.id.back_img);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        back_img.setOnClickListener(this);
        block_user_layout.setAlpha(0);
        final Runnable block = new Runnable() {
            @Override
            public void run() {
                block_user_layout.setAlpha(1);
                block_user_layout.startAnimation(AnimationUtils.loadAnimation(RoadListScreen.this, R.anim.text_view_move));

            }
        };
        myHandler.postDelayed(block, 800);
        district_user_layout.setAlpha(0);
        final Runnable district = new Runnable() {
            @Override
            public void run() {
                district_user_layout.setAlpha(1);
                district_user_layout.startAnimation(AnimationUtils.loadAnimation(RoadListScreen.this, R.anim.text_view_move));

            }
        };
        myHandler.postDelayed(district, 1000);


        district_tv.setText(prefManager.getDistrictName());
        block_tv.setText(prefManager.getBlockName());
        loadVPR();
    }

    public void loadVPR() {
        String code = getIntent().getExtras().getString(AppConstant.KEY_ROAD_CATEGORY_CODE);
        if(code.equalsIgnoreCase("1")){
            title_tv.setText("PUR Road List");
        }else if(code.equalsIgnoreCase("2")){
            title_tv.setText("VPR Road List");
        }else if(code.equalsIgnoreCase("3")){
            title_tv.setText("Highway Road List");
        } else if(code.equalsIgnoreCase("4")){
            title_tv.setText("VPR/PUR Road List");
        }
        new fetchRoadtask().execute(code);
    }

    public class fetchRoadtask extends AsyncTask<String, Void,
            ArrayList<RoadListValue>> {
        @Override
        protected ArrayList<RoadListValue> doInBackground(String... params) {
            dbData.open();
            roadLists = dbData.getAll_Road(params[0]);
            ArrayList<RoadListValue> screenArrayList = new ArrayList<>();

            if (roadLists.size() > 0) {
                for (int i = 0; i < roadLists.size(); i++) {
                    RoadListValue card = new RoadListValue();
                    card.setRoadName(roadLists.get(i).getRoadName());
                    card.setRoadID(roadLists.get(i).getRoadID());
                    card.setRoadCode(roadLists.get(i).getRoadCode());
                    card.setRoadVillage(roadLists.get(i).getRoadVillage());
                    card.setRoadCategory(roadLists.get(i).getRoadCategory());
                    card.setRoadCategoryCode(roadLists.get(i).getRoadCategoryCode());
                    card.setState(roadLists.get(i).getState());
                    if (i%2 == 0) {
                       card.setType(RoadListValue.ItemType.ONE_ITEM);
                    } else {
                       card.setType(RoadListValue.ItemType.TWO_ITEM);
                    }
                    screenArrayList.add(card);
                }
            }
            return screenArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<RoadListValue> roadList) {
            super.onPostExecute(roadList);
            roadListAdapter = new RoadListAdapter(RoadListScreen.this,
                    roadList, dbData);
            recyclerView.setAdapter(roadListAdapter);
            recyclerView.showShimmerAdapter();
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadCards();
                }
            }, 3000);
        }

        private void loadCards() {

            recyclerView.hideShimmerAdapter();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                onBackPress();
                break;

        }
    }
    public void dashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void onBackPress() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                roadListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                roadListAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    public void onResume() {
        super.onResume();
//        Utils.hideSoftKeyboard(this);
    }
}
