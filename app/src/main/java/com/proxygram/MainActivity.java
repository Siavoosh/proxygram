package com.proxygram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.proxygram.model.Proxy;
import com.proxygram.model.User;

import java.util.List;

import co.ronash.pushe.Pushe;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TELE_X_PACKAGE = "org.thunderdog.challegram";
    private static final String TELE_PACKAGE = "org.telegram.messenger";

    TextView serverMessage, name, row_id;
    WebApiClient webApiClient = new WebApiClient();
    SwipeRefreshLayout swip;
    RecyclerView proxies;
    User user;
    Button changeName;

    @SuppressLint("HardwareIds")
    String android_id, pushe_id;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Pushe.initialize(this, true);
        pushe_id = Pushe.getPusheId(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        serverMessage = findViewById(R.id.tv_serverMessage);
        swip = findViewById(R.id.swip);
        proxies = findViewById(R.id.rvProxies);

        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        serverMessage.setText("");

        getInfo(android_id);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SupportActivity.class).putExtra(SupportActivity.USER_KEY, user));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        name = navigationView.getHeaderView(0).findViewById(R.id.nav_tv_name);
        row_id = navigationView.getHeaderView(0).findViewById(R.id.nav_tv_row_id);
        changeName = navigationView.getHeaderView(0).findViewById(R.id.nav_changeName);

        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeNameDialog();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInfo(android_id);
                swip.setRefreshing(false);
            }
        });

    }

    private void showChangeNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.alert_signup_layout, null, false);
        builder.setView(view);
        builder.setCancelable(true);
        Button done = view.findViewById(R.id.alert_signup_send);
        TextView text = view.findViewById(R.id.alert_signup_text);
        final EditText name = view.findViewById(R.id.alert_signup_name);
        text.setText(Utils.createIndentedText("لطفا نام جدید خود را وارد کنید", 5, 10));
        final AlertDialog dialog = builder.create();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().trim().isEmpty())
                    Toast.makeText(MainActivity.this, "نام نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                else
                    webApiClient.changeName(android_id, name.getText().toString().trim(), MainActivity.this,
                            new WebApiClient.callback<String>() {
                                @Override
                                public void onResponse(boolean ok, String response) {
                                    dialog.dismiss();
                                    if (ok) {
                                        showMessageDialog("تغییر نام", response);
                                        getUserInfo();
                                    }
                                }

                                @Override
                                public void onFailure(String reason) {
                                    dialog.dismiss();
                                    showMessageDialog("تغییر نام", reason);
                                }
                            });
            }
        });
        dialog.show();
    }

    private void getInfo(String android_id) {
        swip.setRefreshing(true);
        webApiClient.getInfo(android_id, pushe_id, this, new WebApiClient.callback<String>() {
            @Override
            public void onResponse(boolean ok, String response) {
                swip.setRefreshing(false);
                if (ok)
                    showSignupDialog(response);
                else
                    getUserInfo();
            }

            @Override
            public void onFailure(String reason) {
                swip.setRefreshing(false);
                showMessageDialog("خطا ی سرور", reason);
            }
        });
    }

    private void getUserInfo() {
        webApiClient.getUser(android_id, this, new WebApiClient.callback<User>() {
            @Override
            public void onResponse(boolean ok, User response) {
                user = response;
                serverMessage.setText(user.getExpire());
                serverMessage.setTextColor(Color.parseColor(user.getColor()));
                name.setText(user.getName());
                row_id.setText("کد شما : " + user.getRow_id());
                loadProxyList();
            }

            @Override
            public void onFailure(String reason) {
                showMessageDialog("خطای نرم افزار", reason);
            }
        });
    }

    private void loadProxyList() {
        swip.setRefreshing(true);
        webApiClient.getProxies(android_id, this, new WebApiClient.callback<List<Proxy>>() {
            @Override
            public void onResponse(boolean ok, List<Proxy> response) {
                if (ok) {
                    Toast.makeText(MainActivity.this, "list size " + response.size(), Toast.LENGTH_SHORT).show();
                    ProxyAdapter adapter = new ProxyAdapter(response, new ProxyAdapter.onOpenClicked() {
                        @Override
                        public void onClick(Proxy proxy) {
                            showProxyDialog(proxy);
                        }
                    });
                    LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                    proxies.setAdapter(adapter);
                    proxies.setLayoutManager(layoutManager);
                    adapter.notifyDataSetChanged();
                }
                swip.setRefreshing(false);
            }

            @Override
            public void onFailure(String reason) {
                showMessageDialog("خطا در دریافت اطلاعات", reason);
                swip.setRefreshing(false);
            }
        });
    }

    private void showProxyDialog(final Proxy proxy) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.alert_proxy_layout, null, false);
        builder.setView(view);
        builder.setCancelable(true);

        Button telegram = view.findViewById(R.id.proxy_dialog_telegram);
        Button telegramX = view.findViewById(R.id.proxy_dialog_telegramX);
        Button telegramOther = view.findViewById(R.id.proxy_dialog_telegramOther);

        AlertDialog dialog = builder.create();

        if (isInstall(this, TELE_PACKAGE))
            telegram.setVisibility(View.VISIBLE);
        else
            telegram.setVisibility(View.GONE);

        if (isInstall(this, TELE_X_PACKAGE))
            telegramX.setVisibility(View.VISIBLE);
        else
            telegramX.setVisibility(View.GONE);

        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink(TELE_PACKAGE, proxy.getLink());
            }
        });
        telegramX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink(TELE_X_PACKAGE, proxy.getLink());
            }
        });
        telegramOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLink("other", proxy.getLink());
            }
        });

        dialog.show();
    }

    public void openLink(String packageName, String link) {
        if (packageName.equals(TELE_PACKAGE) || packageName.equals(TELE_X_PACKAGE)) {
            if (isInstall(this, packageName)) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(link));
                i.setPackage(packageName);
                startActivity(i);
            } else
                Toast.makeText(this, "نرم افزار مورد نظر نصب نمی باشد", Toast.LENGTH_SHORT).show();
        } else {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(link));
            startActivity(Intent.createChooser(i, "برنامه ای برای اجرای پروکسی انتخاب کنید"));
        }

    }

    public boolean isInstall(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void showSignupDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.alert_signup_layout, null, false);
        builder.setView(view);
        builder.setCancelable(false);
        Button done = view.findViewById(R.id.alert_signup_send);
        TextView text = view.findViewById(R.id.alert_signup_text);
        final EditText name = view.findViewById(R.id.alert_signup_name);
        text.setText(Utils.createIndentedText(message, 5, 10));
        final AlertDialog dialog = builder.create();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().trim().isEmpty())
                    Toast.makeText(MainActivity.this, "نام نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                else
                    webApiClient.sendInfo(android_id, DeviceName.getDeviceName(), name.getText().toString().trim(), MainActivity.this,
                            new WebApiClient.callback<User>() {
                                @Override
                                public void onResponse(boolean ok, User response) {
                                    dialog.dismiss();
                                    if (ok) {
                                        user = response;
                                        serverMessage.setText(user.getExpire());
                                        serverMessage.setTextColor(Color.parseColor(user.getColor()));
                                        getUserInfo();
                                    }
                                }

                                @Override
                                public void onFailure(String reason) {
                                    dialog.dismiss();
                                    showMessageDialog("خطا", reason);
                                }
                            });
            }
        });
        dialog.show();
    }

    private void showMessageDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.alert_message_layout, null, false);
        builder.setView(view);
        builder.setCancelable(false);
        Button done = view.findViewById(R.id.alert_message_done);
        TextView tit = view.findViewById(R.id.alert_message_title),
                text = view.findViewById(R.id.alert_message_text);
        tit.setText(title);
        text.setText(Utils.createIndentedText(message, 5, 10));
        final AlertDialog dialog = builder.create();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_support:
                startActivity(new Intent(MainActivity.this, SupportActivity.class).putExtra(SupportActivity.USER_KEY, user));
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
