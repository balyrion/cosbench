/** 
 
Copyright 2013 Intel Corporation, All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 
*/ 

package com.intel.cosbench.client.http;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.*;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.*;

/**
 * This class encapsulates basic HTTP client related functions which are
 * necessary for REST based storage system.
 * 
 * @author ywang19, qzheng7
 * 
 */
public class HttpClientUtil {

    /**
     * Creates a default HTTP client with a given timeout setting.<br />
     * Note that this client is <strong>NOT Thread-Safe</strong> and can only be
     * used by a single thread.
     * 
     * @param timeout
     *            the timeout in seconds that will be honored by this client
     * @return a new HTTP client
     */
    public static HttpClient createHttpClient(int timeout) {
        SchemeRegistry registry = initSchemeRegistry();
        ClientConnectionManager cm = new SingleClientConnManager(registry);
        HttpParams params = createDefaultHttpParams(timeout);
        return new DefaultHttpClient(cm, params);
    }

    private static SchemeRegistry initSchemeRegistry() {
        SchemeRegistry registry = new SchemeRegistry();
        /* HTTP */
        SchemeSocketFactory plain = PlainSocketFactory.getSocketFactory();
        Scheme http = new Scheme("http", 80, plain);
        registry.register(http);
        /* HTTPS */
        SchemeSocketFactory ssl = SSLSocketFactory.getSocketFactory();
        Scheme https = new Scheme("https", 443, ssl);
        registry.register(https);
        return registry;
    }

    private static HttpParams createDefaultHttpParams(int timeout) {
        HttpParams params = new BasicHttpParams();
        /* default HTTP parameters */
        DefaultHttpClient.setDefaultHttpParams(params);
        /* connection/socket timeouts */
        HttpConnectionParams.setSoTimeout(params, timeout);
        HttpConnectionParams.setConnectionTimeout(params, timeout);
        /* user agent */
        HttpProtocolParams.setUserAgent(params, "cosbench/2.0");
        return params;
    }

    /**
     * Releases the resources held by the given HTTP client.<br />
     * Note that no further connections can be made upon a disposed HTTP client.
     * 
     * @param client
     *            the HTTP client to be disposed.
     */
    public static void disposeHttpClient(HttpClient client) {
        ClientConnectionManager manager = client.getConnectionManager();
        manager.shutdown();
    }

    public static HttpGet makeHttpGet(String url) {
    	return new HttpGet(url);
    }

    public static HttpPut makeHttpPut(String url) {
        return new HttpPut(url);
    }

    public static HttpHead makeHttpHead(String url) {
        return new HttpHead(url);
    }

    public static HttpPost makeHttpPost(String url) {
        return new HttpPost(url);
    }

    public static HttpDelete makeHttpDelete(String url) {
        return new HttpDelete(url);
    }

    public static String encodeURL(String str) {
        URLCodec codec = new URLCodec();
        try {
            return codec.encode(str).replaceAll("\\+", "%20");
        } catch (EncoderException ee) {
            return str;
        }
    }

}
