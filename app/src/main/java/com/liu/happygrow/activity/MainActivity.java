package com.liu.happygrow.activity;

import android.animation.Animator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.liu.happygrow.R;
import com.liu.happygrow.colorUi.util.ColorUiUtil;
import com.liu.happygrow.colorUi.util.SharedPreferencesMgr;
import com.liu.happygrow.fragment.AndroidFragment;
import com.liu.happygrow.fragment.BaseListFragment;
import com.liu.happygrow.fragment.VelfareFragment;
import com.liu.happygrow.view.xlistview.CircleTransform;
import com.liu.happygrow.view.xlistview.XListView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout fl_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= 21) {
            // 允许使用transitions
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            // 设置一个exit transition
            getWindow().setExitTransition(new Explode());
        }
        super.onCreate(savedInstanceState);



        if (SharedPreferencesMgr.getInt("theme", 0) == 1) {
            SharedPreferencesMgr.setInt("theme", 0);
            setTheme(R.style.theme_1);
        } else {
            SharedPreferencesMgr.setInt("theme", 1);
            setTheme(R.style.theme_2);
        }
        setContentView(R.layout.activity_main);
//        EventBus.getDefault().register(this);
        initUI();
        // 设置默认的Fragment
        setDefaultFragment();
    }

    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        BaseListFragment afm = BaseListFragment.newInstance("Android");
        transaction.replace(R.id.fl_content, afm);
        transaction.commit();
    }


    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ImageView imageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        Picasso.with(this).load(R.drawable.meizis).transform(new CircleTransform()).into(imageView);


        fl_content = (FrameLayout) findViewById(R.id.fl_content);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (id == R.id.nav_android) {
            ft.replace(R.id.fl_content, BaseListFragment.newInstance("Android"));
        } else if (id == R.id.nav_ios) {
            ft.replace(R.id.fl_content, BaseListFragment.newInstance("iOS"));
        } else if (id == R.id.nav_video) {
            ft.replace(R.id.fl_content, BaseListFragment.newInstance("休息视频"));
        } else if (id == R.id.nav_velfare) {
            ft.replace(R.id.fl_content, VelfareFragment.newInstance("福利"));
        } else if (id == R.id.nav_resource) {
            ft.replace(R.id.fl_content, BaseListFragment.newInstance("拓展资源"));
        } else if (id == R.id.nav_web) {
            ft.replace(R.id.fl_content, BaseListFragment.newInstance("前端"));
        } else if (id == R.id.nav_recommend) {
            ft.replace(R.id.fl_content, BaseListFragment.newInstance("all"));
        } else if (id == R.id.nav_app) {
            ft.replace(R.id.fl_content, BaseListFragment.newInstance("Android"));
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_change) {
            changeTheme();
        }

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    private void changeTheme() {
        if (SharedPreferencesMgr.getInt("theme", 0) == 1) {
            SharedPreferencesMgr.setInt("theme", 0);
            setTheme(R.style.theme_1);
        } else {
            SharedPreferencesMgr.setInt("theme", 1);
            setTheme(R.style.theme_2);
        }
        final View rootView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= 14) {
            rootView.setDrawingCacheEnabled(true);
            rootView.buildDrawingCache(true);
            final Bitmap localBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
            rootView.setDrawingCacheEnabled(false);
            if (null != localBitmap && rootView instanceof ViewGroup) {
                final View localView2 = new View(getApplicationContext());
                localView2.setBackgroundDrawable(new BitmapDrawable(getResources(), localBitmap));
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                ((ViewGroup) rootView).addView(localView2, params);
                localView2.animate().alpha(0).setDuration(400).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        ColorUiUtil.changeTheme(rootView, getTheme());
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ((ViewGroup) rootView).removeView(localView2);
                        localBitmap.recycle();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
            }
        } else {
            ColorUiUtil.changeTheme(rootView, getTheme());
        }
    }
}
