package com.example.brendon.proto;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends FragmentActivity {

    public static final int QUESTIONS_POSITION = 0;
    public static final int PROFILE_POSITION = 1;

    private String[] listItems;

    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    // Should this be an int?
    public int currentFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        switchFragment(currentFragment);
        listItems = new String[]{"Ask!", "Profile"};
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, listItems));
        DrawerItemClickListener listener = new DrawerItemClickListener();
        drawerList.setOnItemClickListener(listener);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle("Proto");
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("Menu");
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        //if (findViewById(R.id.content_frame) != null) {
        //    if (savedInstanceState != null)
        //        return;
        //
        //}
    }

    //public boolean onPrepareOptionsMenu(Menu menu) {
    //    boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
    //    menu.findItem()
    //}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
        // return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_edit_profile:
                startActivity(new Intent(this, EditProfileActivity.class));
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem editProfileItem = menu.findItem(R.id.action_edit_profile);
        if (currentFragment == PROFILE_POSITION) {
            editProfileItem.setVisible(true);
        } else {
            editProfileItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    public void switchFragment(int argFragment) {
        Fragment fragment = null;

        if (argFragment == PROFILE_POSITION) {
            fragment = new ProfileFragment();
        } else if (argFragment == QUESTIONS_POSITION) {
            fragment = new QuestionsFragment();
        }
        getFragmentManager().beginTransaction().
                replace(R.id.content_frame,fragment).commit();
        currentFragment = argFragment;
    }

    public class DrawerItemClickListener implements ListView.OnItemClickListener {


        public void onClick(AdapterView parent, View v, int position, long id) {
            selectItem(position);
        }

        public void selectItem(int position) {
            switchFragment(position);
            drawerLayout.closeDrawer(drawerList);
        }

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }


        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.profile_name:
                    break;
                default:
            }
        }

    }

}
