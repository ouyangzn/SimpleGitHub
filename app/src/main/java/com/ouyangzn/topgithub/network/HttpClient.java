/*
 * Copyright (c) 2016.  ouyangzn   <ouyangzn@163.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ouyangzn.topgithub.network;

import com.ouyangzn.topgithub.BuildConfig;
import com.ouyangzn.topgithub.utils.Log;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 匿名 SSL HttpClient
 */
public class HttpClient {
  public static OkHttpClient getDefaultHttpClient() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.connectTimeout(30, TimeUnit.SECONDS);
    builder.writeTimeout(30, TimeUnit.SECONDS);
    builder.readTimeout(30, TimeUnit.SECONDS);
    if (BuildConfig.LOG_DEBUG) {
      //HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      //logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
      //builder.interceptors().add(logging);
      builder.interceptors().add(new Interceptor() {
        @Override public Response intercept(Chain chain) throws IOException {
          Request request = chain.request();
          Response response = chain.proceed(request);
          return response.newBuilder()
              .body(new DebugResponseBody(request, response.body()))
              .build();
        }
      });
    }
    try {
      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
          new java.security.SecureRandom());
      builder.sslSocketFactory(sc.getSocketFactory());
      builder.hostnameVerifier(new TrustAnyHostnameVerifier());
    } catch (KeyManagementException ignored) {
    } catch (NoSuchAlgorithmException ignored) {
    }
    return builder.build();
  }

  private static class TrustAnyTrustManager implements X509TrustManager {
    public void checkClientTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
      return new X509Certificate[] {};
    }
  }

  private static class TrustAnyHostnameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
      return true;
    }
  }

  /**
   * 打印请求内容
   */
  private static class DebugResponseBody extends ResponseBody {
    private static final String TAG = DebugResponseBody.class.getSimpleName();
    private final Request request;
    private final ResponseBody responseBody;
    private BufferedSource bufferedSource;
    private Buffer buffer = new Buffer();

    DebugResponseBody(Request request, ResponseBody responseBody) {
      this.request = request;
      this.responseBody = responseBody;
      Log.d(TAG, "Http start: " + request.toString());
      Log.d(TAG, "Http headers: " + request.headers());
    }

    @Override public MediaType contentType() {
      return responseBody.contentType();
    }

    @Override public long contentLength() {
      return responseBody.contentLength();
    }

    @Override public BufferedSource source() {
      if (bufferedSource == null) {
        bufferedSource = Okio.buffer(source(responseBody.source()));
      }
      return bufferedSource;
    }

    @Override public void close() {
      Log.d(TAG,
          "Http end: [" + request.url().toString() + "]" + " Data: " + buffer.readByteString()
              .utf8());
      buffer.close();
      super.close();
    }

    private Source source(Source source) {
      return new ForwardingSource(source) {
        @Override public long read(Buffer sink, long byteCount) throws IOException {
          long bytesRead;
          if ((bytesRead = super.read(sink, byteCount)) > 0) {
            sink.copyTo(buffer, 0, bytesRead);
          }
          return bytesRead;
        }
      };
    }
  }
}
