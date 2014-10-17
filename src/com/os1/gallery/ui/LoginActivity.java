package com.os1.gallery.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.os1.camera.GalleryPicker;
import com.os1.camera.LogUtils;
import com.os1.gallery.R;
import android.app.Activity;
import android.os.Bundle;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    /**
     * The default email to populate the email field with.
     */
    public static final String EXTRA_EMAIL = "com.os1.camera.extra.EMAIL";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // Values for email and password at the time of the login attempt.
    private String mEmail;
    private String mPassword;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private View mLoginStatusView;
    private TextView mLoginStatusMessageView;
    private TextView mLoginRegister;
    private TextView mLogin_password;
    private View mUserIcon;

    private String TAG = LogUtils.makeLogTag(LoginActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mUserIcon = findViewById(R.id.user_icon);
        mUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoHomeActivity();
            }
        });

        mLoginRegister = (TextView) findViewById(R.id.login_register);
        mLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mLogin_password = (TextView) findViewById(R.id.login_password);
        mLogin_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PasswordRecovery.class);
                startActivity(intent);
            }
        });

        // Set up the login form.
        mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
        mEmailView = (EditText) findViewById(R.id.email);
        mEmailView.setText(mEmail);


        mPasswordView = (EditText) findViewById(R.id.password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusView = findViewById(R.id.pending_status);
        mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {


        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        mEmail = mEmailView.getText().toString();
        mPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (mPassword.length() < 4) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!mEmail.contains("@")) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
            showProgress(true);
            mAuthTask = new UserLoginTask();
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void gotoHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, GalleryPicker.class);
        startActivity(intent);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {
        /**
         * 服务器
         */
        //服务器的url地址
        private static final String host = "os1.ifancc.com";
        private static final String path = "api/user";
        //创建一个httpClient连接
        private HttpClient httpClient;
        //创建一个HttpResponse用于存放相应的数据
        private HttpResponse response;
        //创建一个HttpPost请求
        private HttpGet  httpGet;
        //创建一个httpEntity用于存放请求的实体数据
        private HttpEntity entity;
        private HttpParams params;

        protected Integer doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            httpClient = new DefaultHttpClient();
            int loginStatus = 0;
            try {
                Uri uri = new Uri.Builder().scheme("http")
                        .authority(host)
                        .encodedPath(path)
                        .appendQueryParameter("logineName", mEmail)
                        .appendQueryParameter("password", mPassword)
                        .build();
                //Log.d(TAG, "builded uri:"+uri.toString());
                //设置请求的路径
                httpGet = new HttpGet(uri.toString());
                //创建一个用户，用于向服务端发送数据时，存放的实体

                //设置请求体

                //执行请求获取响应
                response = httpClient.execute(httpGet);
                //如果响应的状态码为200时，表示请求响应成功
                Log.d(TAG, "post request");
                while (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    //获取响应的实体数据
                    entity = response.getEntity();
                    StringBuilder sb = new StringBuilder();
                    //通过reader读取实体对象包含的数据
                    BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
                    //循环读取实体里面的数据
                    String s;
                    while ((s = reader.readLine()) != null) {
                        sb.append(s);
                    }
                    //创建一个JSONObject对象存放从服务端获取到的JSONObject数据
                    JSONObject datas = new JSONObject(sb.toString());
                    //创建一个boolean变量用于存放服务端的处理结果状态
                    loginStatus = datas.getInt("status");
                    System.out.println("Campus" + datas.toString());
                    Log.d(TAG, "the status is " + loginStatus);


                }
                if (loginStatus == 0) {
                    Log.d(TAG, response.getStatusLine().toString());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    StringBuilder sb = new StringBuilder();
                    String s;
                    Log.d(TAG, "html body dumped");
                    while ((s = reader.readLine()) != null) {
                        sb.append(s);
                    }
                    Log.v(TAG, sb.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return loginStatus;
        }

        @Override
        protected void onPostExecute(final Integer status) {
            mAuthTask = null;
            showProgress(false);

            if (status == 0) {
                gotoHomeActivity();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

