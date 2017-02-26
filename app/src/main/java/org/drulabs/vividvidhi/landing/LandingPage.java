package org.drulabs.vividvidhi.landing;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.drulabs.vividvidhi.R;
import org.drulabs.vividvidhi.add.AddPicture;
import org.drulabs.vividvidhi.poem.PoemsFragment;
import org.drulabs.vividvidhi.ui.NotificationToast;
import org.drulabs.vividvidhi.utils.Store;
import org.drulabs.vividvidhi.utils.Utility;

public class LandingPage extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int PICS_FRAGMENT = 1;
    private static final int POEMS_FRAGMENT = 2;

    private static final long SWIPE_REFRESH_DELAY_MILLIS = 2500;


    // Is the current user admin ???
    private boolean isAdmin = false;

    private int currentlyVisibleFragment;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Bundle savedInstanceState;

    // Keeping a reference of all potential fragments, that may or may not be present here
    PicsFragment picsFragment;
    PoemsFragment poemsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        this.savedInstanceState = savedInstanceState;

        if (savedInstanceState == null) {
            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pics_swipe_layout);
            swipeRefreshLayout.setOnRefreshListener(this);

            picsFragment = PicsFragment.newInstance("Pics", "none");
            picsFragment.setRetainInstance(true);

            currentlyVisibleFragment = PICS_FRAGMENT;
            loadFragment(this.savedInstanceState, picsFragment, false);

            isAdmin = Store.getInstance(this).isAdmin();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!fragment.isDetached()) {
//            fragment.reset();
//        }
    }

    private void loadFragment(Bundle savedInstanceState, Fragment fragment, boolean
            addToBackStack) {
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_holder) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            //if (savedInstanceState != null) {
            //    return;
            //}

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            fragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            if (addToBackStack) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_holder, fragment).addToBackStack(null).commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_holder, fragment).commit();
            }
        }
    }

    private void removeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (!isAdmin) {
            // inflate regular menu
            inflater.inflate(R.menu.menu_landing, menu);
        } else {
            // Inflate admin menu
            inflater.inflate(R.menu.menu_landing_admin, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.only_videos:
            case R.id.only_audio:
                NotificationToast.showToast(this, getString(R.string.feature_coming_soon));
                return true;
            case R.id.only_picture:

                if (currentlyVisibleFragment == PICS_FRAGMENT) {
                    if (picsFragment != null) {
                        picsFragment.reset();
                    }
                    return true;
                }

                if (picsFragment == null) {
                    picsFragment = PicsFragment.newInstance("Pics", "none");
                    picsFragment.setRetainInstance(true);
                }

                currentlyVisibleFragment = PICS_FRAGMENT;
                loadFragment(this.savedInstanceState, picsFragment, false);
                return true;
            case R.id.only_poems:

                if (currentlyVisibleFragment == POEMS_FRAGMENT) {
                    if (poemsFragment != null) {
                        poemsFragment.reset();
                    }
                    return true;
                }

                if (poemsFragment == null) {
                    poemsFragment = PoemsFragment.newInstance("Daddy Ji", "Vidhi", true);
                    poemsFragment.setRetainInstance(true);
                }

                currentlyVisibleFragment = POEMS_FRAGMENT;
                loadFragment(savedInstanceState, poemsFragment, true);
                return true;
            case R.id.bug_report_menu:

                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String version = pInfo.versionName;

                    Utility.composeEmail(LandingPage.this, new String[]{"kaushal.devil009@gmail" +
                            ".com"}, "Vivid Vidhi (v" + version + ") Bug report from "
                            + Store.getInstance(this).getMyName() + "(" + Store.getInstance(this)
                            .getMyKey() + ")");
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.add_stuff:
                navigateToAddPicActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToAddPicActivity() {
        Intent addPicIntent = new Intent(this, AddPicture.class);
        startActivity(addPicIntent);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, SWIPE_REFRESH_DELAY_MILLIS);

        switch (currentlyVisibleFragment) {
            case PICS_FRAGMENT:
                if (picsFragment != null) {
                    picsFragment.reset();
                }
                break;
            case POEMS_FRAGMENT:
                if (poemsFragment != null) {
                    poemsFragment.reset();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}