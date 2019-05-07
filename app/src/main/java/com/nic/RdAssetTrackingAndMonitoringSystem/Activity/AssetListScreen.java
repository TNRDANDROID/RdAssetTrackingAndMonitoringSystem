package com.nic.RdAssetTrackingAndMonitoringSystem.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nic.RdAssetTrackingAndMonitoringSystem.Adapter.AssetListAdapter;
import com.nic.RdAssetTrackingAndMonitoringSystem.Adapter.MyNodeViewFactory;
import com.nic.RdAssetTrackingAndMonitoringSystem.Adapter.RoadListAdapter;
import com.nic.RdAssetTrackingAndMonitoringSystem.Constant.AppConstant;
import com.nic.RdAssetTrackingAndMonitoringSystem.DataBase.dbData;
import com.nic.RdAssetTrackingAndMonitoringSystem.Model.RoadListValue;
import com.nic.RdAssetTrackingAndMonitoringSystem.R;
import com.nic.RdAssetTrackingAndMonitoringSystem.Session.PrefManager;
import com.nic.RdAssetTrackingAndMonitoringSystem.Support.MyCustomTextView;

import java.util.ArrayList;
import java.util.List;

import me.texy.treeview.TreeNode;
import me.texy.treeview.TreeView;

public class AssetListScreen extends AppCompatActivity implements View.OnClickListener {



    private ViewGroup viewGroup;
    private TreeNode root;
    private TreeView treeView;
    private ArrayList<RoadListValue> ListValues;
    public dbData dbData = new dbData(this);
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        intializeUI();

        root = TreeNode.root();
      //  buildTree();
        loadAssets();
    }

    private void intializeUI() {
        viewGroup = (RelativeLayout) findViewById(R.id.container);
//        setLightStatusBar(viewGroup);

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
        String road_code = getIntent().getStringExtra(AppConstant.KEY_ROAD_ID);
        new fetchAssettask().execute(Integer.valueOf(road_code));
    }

    public class fetchAssettask extends AsyncTask<Integer, Void,
            ArrayList<RoadListValue>> {
        @Override
        protected ArrayList<RoadListValue> doInBackground(Integer... params) {
            dbData.open();
            ListValues = new ArrayList<>();
            ListValues = dbData.select_Asset(params[0]);
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
            String grpname = listValues.get(i).getGroupName();
            TreeNode treeNode = new TreeNode(new String(grpname));
            treeNode.setLevel(0);
            for (int j = 0; j < 10; j++) {
                TreeNode treeNode1 = new TreeNode(new String("Child " + "No." + j));
                treeNode1.setLevel(1);
                for (int k = 0; k < 5; k++) {
                    TreeNode treeNode2 = new TreeNode(new String("Grand Child " + "No." + k));
                    treeNode2.setLevel(2);
                    treeNode1.addChild(treeNode2);
                }
                treeNode.addChild(treeNode1);
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