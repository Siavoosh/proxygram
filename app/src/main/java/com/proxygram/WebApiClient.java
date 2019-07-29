package com.proxygram;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.proxygram.model.Message;
import com.proxygram.model.Proxy;
import com.proxygram.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebApiClient {

    private static final String BASE_URL = "http://vippanel.cf/telegram/";
    private Gson gson = new Gson();
    private RequestQueue queue;

    public void getMessages(final String row_id, Context context, final callback<List<Message>> callback) {
        String url = BASE_URL + "getMessages.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("ok")) {
                        Type type = new TypeToken<List<Message>>() {
                        }.getType();
                        List<Message> messages = gson.fromJson(object.getJSONArray("data").toString(), type);
                        callback.onResponse(true, messages);
                    } else {
                        callback.onFailure(object.getString("msg"));
                    }

                } catch (JSONException e) {
                    callback.onFailure("خطا در تجزیه اطلاعات دریافتی");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure("اختلال در برقراری ارتباط با سرور");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("device_id", row_id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 1000));
        getQueue(context).add(request);
    }

    public void sendMessage(final String row_id, final String text, final String photo, Context context, final callback<String> callback) {
        String url = BASE_URL + "sendMessage.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("ok")) {
                        callback.onResponse(true, object.getString("msg"));
                    } else {
                        callback.onFailure(object.getString("msg"));
                    }

                } catch (JSONException e) {
                    callback.onFailure("خطا در تجزیه اطلاعات دریافتی");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure("اختلال در برقراری ارتباط با سرور");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("device_id", row_id);
                params.put("text", text);
                params.put("photo", photo);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 1000));
        getQueue(context).add(request);
    }

    public void sendMessage(final String row_id, final String text, Context context, final callback<String> callback) {
        String url = BASE_URL + "sendMessage.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("ok")) {
                        callback.onResponse(true, object.getString("msg"));
                    } else {
                        callback.onFailure(object.getString("msg"));
                    }

                } catch (JSONException e) {
                    callback.onFailure("خطا در تجزیه اطلاعات دریافتی");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure("اختلال در برقراری ارتباط با سرور");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("device_id", row_id);
                params.put("text", text);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 1000));
        getQueue(context).add(request);
    }

    public void getProxies(final String board_id, Context context, final callback<List<Proxy>> callback) {
        String url = BASE_URL + "getProxies.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("ok")) {
                        Type type = new TypeToken<List<Proxy>>() {
                        }.getType();
                        List<Proxy> list = gson.fromJson(object.getJSONArray("data").toString(), type);
                        callback.onResponse(true, list);
                    } else {
                        callback.onFailure(object.getString("msg"));
                    }

                } catch (JSONException e) {
                    callback.onFailure("خطا در تجزیه اطلاعات دریافتی");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure("اختلال در برقراری ارتباط با سرور");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("device_id", board_id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 1000));
        getQueue(context).add(request);
    }

    public void changeName(final String board_id, final String newname, Context context, final callback<String> callback) {
        String url = BASE_URL + "changeName.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("ok")) {
                        callback.onResponse(true, object.getString("msg"));
                    } else {
                        callback.onFailure(object.getString("msg"));
                    }

                } catch (JSONException e) {
                    callback.onFailure("خطا در تجزیه اطلاعات دریافتی");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure("اختلال در برقراری ارتباط با سرور");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("device_id", board_id);
                params.put("newname", newname);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 1000));
        getQueue(context).add(request);
    }

    public void sendInfo(final String board_id, final String model, final String name, Context context, final callback<User> callback) {
        String url = BASE_URL + "sendUserInfo.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("ok")) {
                        User user = gson.fromJson(object.getJSONObject("data").toString(), User.class);
                        callback.onResponse(true, user);
                    } else {
                        callback.onFailure(object.getString("msg"));
                    }

                } catch (JSONException e) {
                    callback.onFailure("خطا در تجزیه اطلاعات دریافتی");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure("اختلال در برقراری ارتباط با سرور");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("device_id", board_id);
                params.put("name", name);
                params.put("model", model);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 1000));
        getQueue(context).add(request);
    }

    public void getUser(final String board_id, Context context, final callback<User> callback) {
        String url = BASE_URL + "getUserInfo.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("ok")) {
                        if (object.getBoolean("new"))
                            callback.onFailure("ابتدا باید پروفایل خود را تکمیل کنید");
                        else
                            callback.onResponse(true, gson.fromJson(object.getJSONObject("data").toString(), User.class));
                    } else {
                        callback.onFailure(object.getString("msg"));
                    }

                } catch (JSONException e) {
                    callback.onFailure("خطا در تجزیه اطلاعات دریافتی");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure("اختلال در برقراری ارتباط با سرور");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("device_id", board_id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 1000));
        getQueue(context).add(request);
    }

    public void getInfo(final String board_id,final String push_id, Context context, final callback<String> callback) {
        String url = BASE_URL + "getUserInfo.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("ok")) {
                        callback.onResponse(object.getBoolean("new"), object.getString("msg"));
                    } else {
                        callback.onFailure(object.getString("msg"));
                    }

                } catch (JSONException e) {
                    callback.onFailure("خطا در تجزیه اطلاعات دریافتی");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure("اختلال در برقراری ارتباط با سرور");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("device_id", board_id);
                params.put("push_id", push_id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(10000, 5, 1000));
        getQueue(context).add(request);
    }

    private RequestQueue getQueue(Context context) {
        if (this.queue == null)
            this.queue = Volley.newRequestQueue(context);
        return this.queue;
    }

    public interface callback<T> {
        void onResponse(boolean ok, T response);

        void onFailure(String reason);
    }
}
