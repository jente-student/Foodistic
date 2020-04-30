package com.example.foodify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.foodify.ShoppingList.ListCollectionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Main activity in which the fragment will be replaced for each scene
 * @author Tim-Lukas Blom
 */
public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private BottomNavigationView mBottomNav;
    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // setting up navigation controller
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);



        //Navigation bar setup
        mBottomNav = findViewById(R.id.bottom_nav);
        NavigationUI.setupWithNavController(mBottomNav, mNavController);

        //App bar setup

        //AppBarConfiguration appBarConfiguration =
          //      new AppBarConfiguration.Builder(navController.getGraph()).build();

        mDrawerLayout = findViewById(R.id.drawer_layout);

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.shopFragment, R.id.pointFragment, R.id.listCollectionFragment, R.id.profileFragment).build();


        mToolbar = findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.toolbarmenuitems);
        setSupportActionBar(mToolbar);
       // ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayShowHomeEnabled(false);
        //NavigationUI.setupWithNavController(
         //       mToolbar, mNavController, appBarConfiguration);



        // Add destination listener
        mNavController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.profileFragment){
                    mToolbar.setVisibility(View.GONE);
                    mBottomNav.setVisibility(View.GONE);

                }
                else{
                    mToolbar.setVisibility(View.VISIBLE);
                    mBottomNav.setVisibility(View.VISIBLE);
                }
            }
        });


        Intent i = getIntent();
        int start_tab = i.getIntExtra("TabToStart", 1);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch (start_tab){
            case 1:
                fragmentTransaction.replace(R.id.nav_host_fragment, new ShopFragment());
                fragmentTransaction.commitNow();
                mBottomNav.setSelectedItemId(R.id.shopFragment);
                break;
            case 2:
                fragmentTransaction.replace(R.id.nav_host_fragment, new PointFragment());
                fragmentTransaction.commitNow();
                mBottomNav.setSelectedItemId(R.id.pointFragment);
                break;
            case 3:
                fragmentTransaction.replace(R.id.nav_host_fragment, new ListCollectionFragment());
                fragmentTransaction.commitNow();
                mBottomNav.setSelectedItemId(R.id.listCollectionFragment);
                break;
            case 4:
                fragmentTransaction.replace(R.id.nav_host_fragment, new ProfileFragment());
                fragmentTransaction.commitNow();
                mBottomNav.setSelectedItemId(R.id.profileFragment);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add the shopping basket to the actionbar
        getMenuInflater().inflate(R.menu.toolbarmenuitems, menu);
        setCount(this, "10", menu); //TODO automate basket count
        return true;
    }


    /**
     * method that gets called when an item in the actionbar is pressed
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shoppingBasket:
                // User chose the "Settings" item, show the app settings UI...
                //TODO open shoppingbasket when tapped
                String toastText = "Open Shopping basket";
                Toast toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
                toast.show();
                mDrawerLayout.openDrawer(GravityCompat.END);
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Set the count on top of the shopping basket
     * @param context
     * @param count count to be set
     * @param menu menu in which the count gets changed
     */
    public void setCount(Context context, String count, Menu menu) {

        MenuItem menuItem = menu.findItem(R.id.shoppingBasket);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        BasketCount badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_item_count);
        if (reuse != null && reuse instanceof BasketCount) {
            badge = (BasketCount) reuse;
        } else {
            badge = new BasketCount(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_item_count, badge);
    }

}
