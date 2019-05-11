package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nic.RdAssetTrackingAndMonitoringSystem.Adapter.MyNodeViewFactory;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.texy.treeview.TreeNode;
import me.texy.treeview.TreeView;

public class AssetListScreen extends AppCompatActivity implements View.OnClickListener {



    private ViewGroup viewGroup;
    private TreeNode root;
    private TreeView treeView;
    private ArrayList<RoadListValue> ListValues;
    public dbData dbData = new dbData(this);
    private ImageView back_img;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.tree_activity_main);
        intializeUI();

        root = TreeNode.root();
        // buildTree();
        loadAssets();
    }

    private void intializeUI() {
        viewGroup = (RelativeLayout) findViewById(R.id.container);
//        setLightStatusBar(viewGroup);
        back_img = (ImageView) findViewById(R.id.back_img);
        back_img.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                onBackPress();
                break;

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_all:
                treeView.selectAll();
                break;
            case R.id.deselect_all:
                treeView.deselectAll();
                break;
            case R.id.expand_all:
                treeView.expandAll();
                break;
            case R.id.collapse_all:
                treeView.collapseAll();
                break;
            case R.id.expand_level:
                treeView.expandLevel(1);
                break;
            case R.id.collapse_level:
                treeView.collapseLevel(1);
                break;
            case R.id.show_select_node:
                Toast.makeText(getApplication(), getSelectedNodes(), Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getSelectedNodes() {
        StringBuilder stringBuilder = new StringBuilder("You have selected: ");
        List<TreeNode> selectedNodes = treeView.getSelectedNodes();
        for (int i = 0; i < selectedNodes.size(); i++) {
            if (i < 5) {
                stringBuilder.append(selectedNodes.get(i).getValue().toString() + ",");
            } else {
                stringBuilder.append("...and " + (selectedNodes.size() - 5) + " more.");
                break;
            }
        }
        return stringBuilder.toString();
    }


    public void loadAssets() {
        String road_id = getIntent().getStringExtra(AppConstant.KEY_ROAD_ID);
        String loc_grp = getIntent().getStringExtra(AppConstant.KEY_LOCATION_GROUP);
        JSONObject param = new JSONObject();
        try {
            param.put(AppConstant.KEY_ROAD_ID,road_id);
            param.put(AppConstant.KEY_LOCATION_GROUP,loc_grp);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new fetchAssettask().execute(param);
    }

    public class fetchAssettask extends AsyncTask<JSONObject, Void,
            ArrayList<RoadListValue>> {
        @Override
        protected ArrayList<RoadListValue> doInBackground(JSONObject... params) {
            dbData.open();
            ListValues = new ArrayList<>();
            ListValues = dbData.select_Asset(params[0],"secondLevel");
            return ListValues;
        }

        @Override
        protected void onPostExecute(ArrayList<RoadListValue> roadList) {
            super.onPostExecute(roadList);
            buildTree(roadList);
        }
    }

    private void buildTree(ArrayList<RoadListValue> listValues) {

        for (int i = 0; i < listValues.size(); i++) {
            String subgrpname = listValues.get(i).getSubgroupName();
            String col_label = listValues.get(i).getColLabel();
            String location_details = listValues.get(i).getLocationDetails();

            JSONArray locationJson = null,colJson = null;

            try {
                locationJson = new JSONArray(location_details);
                colJson = new JSONArray(col_label);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            TreeNode treeNode = new TreeNode(new String(subgrpname));
            treeNode.setLevel(0);
            for (int j = 0; j < locationJson.length(); j++) {
                JSONObject value1 = new JSONObject();
                JSONObject jsonObject =null;
                try {
                    jsonObject = locationJson.getJSONObject(j);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int l = 0; l < jsonObject.length(); l++)
                {
                    String value = "";
                    String id = "";
                    try {
                        int label_id = 0;
                        Iterator keys = jsonObject.keys();

                        while (keys.hasNext()) {
                            Object key = keys.next();
                            if(key.equals("id")) {
                                id = jsonObject.getString(String.valueOf(key));
                            }
                            String value2 = jsonObject.getString(String.valueOf(key)) ;
                            Log.d("value",":"+value2+" Label" +colJson.get(label_id));
                            value = value.concat(colJson.get(label_id)+" : " +value2+"\n" );
                            label_id++;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        value1.put("display",value);
                        value1.put("id",id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                TreeNode treeNode1 = null;
                try {
                    treeNode1 = new TreeNode(new JSONObject(String.valueOf(value1)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                treeNode1.setLevel(1);
                treeNode.addChild(treeNode1);

// for (int k = 0; k < 5; k++) {
// TreeNode treeNode2 = new TreeNode(new String("Grand Child " + "No." + k));
// treeNode2.setLevel(2);
// treeNode1.addChild(treeNode2);
// }
// treeNode.addChild(treeNode1);
            }
            root.addChild(treeNode);
        }

        treeView = new TreeView(root, this, new MyNodeViewFactory());
        view = treeView.getView();
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewGroup.addView(view);
    }

    private void setLightStatusBar(@NonNull View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            getWindow().setStatusBarColor(Color.parseColor("#EDE2E0"));
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }


}