/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.vfs2.provider.http;

import java.security.KeyStore;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemConfigBuilder;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.UserAuthenticator;
import org.apache.http.cookie.Cookie;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

/**
 * Configuration options for HTTP.
 */
public class HttpFileSystemConfigBuilder extends FileSystemConfigBuilder {
    protected static final String KEY_FOLLOW_REDIRECT = "followRedirect";

    protected static final String KEY_CIRCULAR_REDIRECTS_ALLOWED = "http.circularRedirect";

    protected static final String KEY_USER_AGENT = "userAgent";

    private static final HttpFileSystemConfigBuilder BUILDER = new HttpFileSystemConfigBuilder();

    private static final int DEFAULT_MAX_HOST_CONNECTIONS = 5;

    private static final int DEFAULT_MAX_CONNECTIONS = 50;

    private static final int DEFAULT_CONNECTION_TIMEOUT = 0;

    private static final int DEFAULT_SO_TIMEOUT = 0;

    private static final boolean DEFAULT_FOLLOW_REDIRECT = true;

    private static final boolean DEFAULT_CIRCULAR_REDIRECTS_ALLOWED = false;

    private static final String DEFAULT_USER_AGENT = "Jakarta-Commons-VFS";

    private static final String KEY_PREEMPTIVE_AUTHENTICATION = "preemptiveAuth";

    /**
     * Creates new config builder.
     *
     * @param prefix String for properties of this file system.
     * @since 2.0
     */
    protected HttpFileSystemConfigBuilder(final String prefix) {
        super(prefix);
    }

    private HttpFileSystemConfigBuilder() {
        super("http.");
    }

    /**
     * Gets the singleton builder.
     *
     * @return the singleton builder.
     */
    public static HttpFileSystemConfigBuilder getInstance() {
        return BUILDER;
    }

    /**
     * Sets the charset used for url encoding.<br>
     *
     * @param opts The FileSystem options.
     * @param chaset the chaset
     */
    public void setUrlCharset(final FileSystemOptions opts, final String chaset) {
        setParam(opts, "urlCharset", chaset);
    }

    /**
     * Sets the charset used for url encoding.<br>
     *
     * @param opts The FileSystem options.
     * @return the chaset
     */
    public String getUrlCharset(final FileSystemOptions opts) {
        return getString(opts, "urlCharset");
    }

    /**
     * Sets the proxy to use for http connection.<br>
     * You have to set the ProxyPort too if you would like to have the proxy really used.
     *
     * @param opts The FileSystem options.
     * @param proxyHost the host
     * @see #setProxyPort
     */
    public void setProxyHost(final FileSystemOptions opts, final String proxyHost) {
        setParam(opts, "proxyHost", proxyHost);
    }

    /**
     * Sets the proxy-port to use for http connection. You have to set the ProxyHost too if you would like to have the
     * proxy really used.
     *
     * @param opts The FileSystem options.
     * @param proxyPort the port
     * @see #setProxyHost
     */
    public void setProxyPort(final FileSystemOptions opts, final int proxyPort) {
        setParam(opts, "proxyPort", Integer.valueOf(proxyPort));
    }

    /**
     * Gets the proxy to use for http connection. You have to set the ProxyPort too if you would like to have the proxy
     * really used.
     *
     * @param opts The FileSystem options.
     * @return proxyHost
     * @see #setProxyPort
     */
    public String getProxyHost(final FileSystemOptions opts) {
        return getString(opts, "proxyHost");
    }

    /**
     * Gets the proxy-port to use for http the connection. You have to set the ProxyHost too if you would like to have
     * the proxy really used.
     *
     * @param opts The FileSystem options.
     * @return proxyPort: the port number or 0 if it is not set
     * @see #setProxyHost
     */
    public int getProxyPort(final FileSystemOptions opts) {
        return getInteger(opts, "proxyPort", 0);
    }

    /**
     * Sets the proxy authenticator where the system should get the credentials from.
     *
     * @param opts The FileSystem options.
     * @param authenticator The UserAuthenticator.
     */
    public void setProxyAuthenticator(final FileSystemOptions opts, final UserAuthenticator authenticator) {
        setParam(opts, "proxyAuthenticator", authenticator);
    }

    /**
     * Gets the proxy authenticator where the system should get the credentials from.
     *
     * @param opts The FileSystem options.
     * @return The UserAuthenticator.
     */
    public UserAuthenticator getProxyAuthenticator(final FileSystemOptions opts) {
        return (UserAuthenticator) getParam(opts, "proxyAuthenticator");
    }

    /**
     * The cookies to add to the request.
     *
     * @param opts The FileSystem options.
     * @param cookies An array of Cookies.
     */
    public void setCookies(final FileSystemOptions opts, final Cookie[] cookies) {
        setParam(opts, "cookies", cookies);
    }

    /**
     * Sets whether to follow redirects for the connection.
     *
     * @param opts The FileSystem options.
     * @param redirect {@code true} to follow redirects, {@code false} not to.
     * @see #setFollowRedirect
     * @since 2.1
     */
    public void setFollowRedirect(final FileSystemOptions opts, final boolean redirect) {
        setParam(opts, KEY_FOLLOW_REDIRECT, redirect);
    }

    /**
     * Gets the cookies to add to the request.
     *
     * @param opts The FileSystem options.
     * @return the Cookie array.
     */
    public Cookie[] getCookies(final FileSystemOptions opts) {
        return (Cookie[]) getParam(opts, "cookies");
    }

    /**
     * Gets whether to follow redirects for the connection.
     *
     * @param opts The FileSystem options.
     * @return {@code true} to follow redirects, {@code false} not to.
     * @see #setFollowRedirect
     * @since 2.1
     */
    public boolean getFollowRedirect(final FileSystemOptions opts) {
        return getBoolean(opts, KEY_FOLLOW_REDIRECT, DEFAULT_FOLLOW_REDIRECT);
    }

    /**
     * Sets whether to follow circular redirects for the connection.
     *
     * @param opts
     *            The FileSystem options.
     * @param set {@code true} to follow circular, {@code false} not to.
    
     */
    public void getCircularRedirectsAllowed( final FileSystemOptions opts, boolean set ) {
        setParam( opts, KEY_CIRCULAR_REDIRECTS_ALLOWED, set );
    }

    /**
     * Gets whether to follow circular redirects for the connection.
     *
     * @param opts
     *            The FileSystem options.
     * @return {@code true} to follow redirects, {@code false} not to.
    
     */
    public boolean getCircularRedirectsAllowed( final FileSystemOptions opts ) {
        return getBoolean( opts, KEY_CIRCULAR_REDIRECTS_ALLOWED, DEFAULT_CIRCULAR_REDIRECTS_ALLOWED );
    }

    /**
     * Sets the maximum number of connections allowed.
     * @param opts The FileSystem options.
     * @param maxTotalConnections The maximum number of connections.
     * @since 2.0
     */
    public void setMaxTotalConnections(final FileSystemOptions opts, final int maxTotalConnections) {
        setParam(opts, HttpConnectionParams.MAX_TOTAL_CONNECTIONS, Integer.valueOf(maxTotalConnections));
    }

    /**
     * Gets the maximum number of connections allowed.
     *
     * @param opts The FileSystemOptions.
     * @return The maximum number of connections allowed.
     * @since 2.0
     */
    public int getMaxTotalConnections(final FileSystemOptions opts) {
        return getInteger(opts, HttpConnectionParams.MAX_TOTAL_CONNECTIONS, DEFAULT_MAX_CONNECTIONS);
    }

    /**
     * Sets the maximum number of connections allowed to any host.
     *
     * @param opts The FileSystem options.
     * @param maxHostConnections The maximum number of connections to a host.
     * @since 2.0
     */
    public void setMaxConnectionsPerHost(final FileSystemOptions opts, final int maxHostConnections) {
        setParam(opts, HttpConnectionParams.MAX_HOST_CONNECTIONS, Integer.valueOf(maxHostConnections));
    }

    /**
     * Gets the maximum number of connections allowed per host.
     *
     * @param opts The FileSystemOptions.
     * @return The maximum number of connections allowed per host.
     * @since 2.0
     */
    public int getMaxConnectionsPerHost(final FileSystemOptions opts) {
        return getInteger(opts, HttpConnectionParams.MAX_HOST_CONNECTIONS, DEFAULT_MAX_HOST_CONNECTIONS);
    }

    /**
     * Determines if the FileSystemOptions indicate that preemptive authentication is requested.
     *
     * @param opts The FileSystemOptions.
     * @return true if preemptiveAuth is requested.
     * @since 2.0
     */
    public boolean isPreemptiveAuth(final FileSystemOptions opts) {
        return getBoolean(opts, KEY_PREEMPTIVE_AUTHENTICATION, Boolean.FALSE).booleanValue();
    }

    /**
     * Sets the given value for preemptive HTTP authentication (using BASIC) on the given FileSystemOptions object.
     * Defaults to false if not set. It may be appropriate to set to true in cases when the resulting chattiness of the
     * conversation outweighs any architectural desire to use a stronger authentication scheme than basic/preemptive.
     *
     * @param opts The FileSystemOptions.
     * @param preemptiveAuth the desired setting; true=enabled and false=disabled.
     */
    public void setPreemptiveAuth(final FileSystemOptions opts, final boolean preemptiveAuth) {
        setParam(opts, KEY_PREEMPTIVE_AUTHENTICATION, Boolean.valueOf(preemptiveAuth));
    }

    /**
     * The connection timeout.
     *
     * @param opts The FileSystem options.
     * @param connectionTimeout The connection timeout.
     * @since 2.1
     */
    public void setConnectionTimeout(final FileSystemOptions opts, final int connectionTimeout) {
        setParam(opts, HttpConnectionParams.CONNECTION_TIMEOUT, Integer.valueOf(connectionTimeout));
    }

    /**
     * Gets the connection timeout.
     *
     * @param opts The FileSystem options.
     * @return The connection timeout.
     * @since 2.1
     */
    public int getConnectionTimeout(final FileSystemOptions opts) {
        return getInteger(opts, HttpConnectionParams.CONNECTION_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT);
    }

    /**
     * The socket timeout.
     *
     * @param opts The FileSystem options.
     * @param soTimeout socket timeout.
     * @since 2.1
     */
    public void setSoTimeout(final FileSystemOptions opts, final int soTimeout) {
        setParam(opts, HttpConnectionParams.SO_TIMEOUT, Integer.valueOf(soTimeout));
    }

    /**
     * Gets the socket timeout.
     *
     * @param opts The FileSystemOptions.
     * @return The socket timeout.
     * @since 2.1
     */
    public int getSoTimeout(final FileSystemOptions opts) {
        return getInteger(opts, HttpConnectionParams.SO_TIMEOUT, DEFAULT_SO_TIMEOUT);
    }

    /**
     * Sets the user agent to attach to the outgoing http methods
     *
     * @param opts the file system options to modify
     * @param userAgent User Agent String
     */
    public void setUserAgent(final FileSystemOptions opts, final String userAgent) {
        setParam(opts, "userAgent", userAgent);
    }

    /**
     * Gets the user agent string
     *
     * @param opts the file system options to modify
     * @return User provided User-Agent string, otherwise default of: Commons-VFS
     */
    public String getUserAgent(final FileSystemOptions opts) {
        final String userAgent = (String) getParam(opts, KEY_USER_AGENT);
        return userAgent != null ? userAgent : DEFAULT_USER_AGENT;
    }
    
    /**
     * Allows setting of a custom trust strategy, allowing to accept custom SSL
     * certificates
     * 
     * @param opts file system options
     * @param ts custom trust strategy
     */
    public void setTrustStrategies(final FileSystemOptions opts, final TrustStrategy[] ts) {
        // add copy of strategies
        setParam(opts, "trustStrategies", Arrays.copyOf(ts, ts.length));
    }
    
    /**
     * Return custom trust-strategies (if defined)
     * 
     * @param opts file system options
     * @return User-provided trust strategy, or null if not defined
     */
    public TrustStrategy[] getTrustStrategies(final FileSystemOptions opts) {
        return (TrustStrategy[])getParam(opts, "trustStrategies");
    }

    /**
     * Allows setting of a custom trust strategy, allowing to accept custom SSL
     * certificates
     * 
     * @param opts file system options
     * @param ts custom trust strategy
     */
    public void addTrustStrategy(final FileSystemOptions opts, final TrustStrategy ts) {
        // add copy of strategies
        TrustStrategy[] strategies = getTrustStrategies(opts);
        if (strategies == null) {
            strategies = new TrustStrategy[]{ts};
        } else {
            strategies = Arrays.copyOf(strategies, strategies.length+1);
            strategies[strategies.length-1] = ts;
        }
        setParam(opts, "trustStrategies", strategies);
    }

    /**
     * Allows setting of a custom key stores, allowing to accept custom SSL
     * certificates
     * 
     * @param opts file system options
     * @param ks custom key stores
     */
    public void setKeyStores(final FileSystemOptions opts, final KeyStore[] ks) {
        // add copy of strategies
        setParam(opts, "keyStores", Arrays.copyOf(ks, ks.length));
    }
    
    /**
     * Return custom key stores (if defined)
     * 
     * @param opts file system options
     * @return User-provided key store, or null if not defined
     */
    public KeyStore[] getKeyStores(final FileSystemOptions opts) {
        return (KeyStore[])getParam(opts, "keyStores");
    }

    /**
     * Allows setting of a a custom key store, allowing to accept custom SSL
     * certificates
     * 
     * @param opts file system options
     * @param ks custom key store
     */
    public void addKeyStore(final FileSystemOptions opts, final KeyStore ks) {
        // add copy of strategies
        KeyStore[] keystores = getKeyStores(opts);
        if (keystores == null) {
            keystores = new KeyStore[]{ks};
        } else {
            keystores = Arrays.copyOf(keystores, keystores.length+1);
            keystores[keystores.length-1] = ks;
        }
        setParam(opts, "keyStores", keystores);
    }
    
    /**
     * Retrieves the custom HostnameVerifier for SSL connections
     * 
     * @param opts file system options
     * @return the provided HostnameVerifier, or null for default
     */
    public HostnameVerifier getSSLHostnameVerifier(final FileSystemOptions opts) {
        return (HostnameVerifier)getParam(opts, "sslHostnameVerifier");
    }
    
    /**
     * Sets a custom HostnameVerifier for SSL connections
     * 
     * @param opts file system options
     * @param hv hostname verifier, or null to use the default
     */
    public void setSSLHostnameVerifier(final FileSystemOptions opts, final HostnameVerifier hv) {
        setParam(opts, "sslHostnameVerifier", hv);
    }
    
    /**
     * Attempts to determine the set of supported SSL protocols
     * @return supported protocols, if available, null otherwise
     */
    public String[] getSupportedSSLProtocols() {
        try {
            SSLContextBuilder contextBuilder = new SSLContextBuilder();
            SSLContext context = contextBuilder.build();
            SSLSocketFactory sf = context.getSocketFactory();
            SSLSocket socket = (SSLSocket) sf.createSocket();

            return socket.getSupportedProtocols();
        } catch(Exception e){}
        return null;
    }
    
    /**
     * Attempts to determine the set of default enabled SSL protocols
     * @return default protocols, if available, null otherwise
     */
    public String[] getDefaultSSLProtocols() {
        try {
            SSLContextBuilder contextBuilder = new SSLContextBuilder();
            SSLContext context = contextBuilder.build();
            SSLSocketFactory sf = context.getSocketFactory();
            SSLSocket socket = (SSLSocket) sf.createSocket();

            return socket.getEnabledProtocols();
        } catch(Exception e){}
        return null;
    }
    
    /**
     * Get the custom set of enabled SSL protocols
     * 
     * @param opts file system options
     * @return enabled protocols (null for system defaults)
     */
    public String[] getEnabledSSLProtocols(final FileSystemOptions opts) {
        return (String[])getParam(opts, "sslProtocols");        
    }
    
    /**
     * Enable a set of SSL protocols
     * 
     * @param opts file system options
     * @param protocols enabled protocols (null for system defaults)
     */
    public void setEnabledSSLProtocols(final FileSystemOptions opts, String[] protocols) {
        // add copy of strategies
        setParam(opts, "sslProtocols", Arrays.copyOf(protocols, protocols.length));
    }
    
    /**
     * Attempts to determine the set of supported SSL cipher suites
     * @return supported suites, if available, null otherwise
     */
    public String[] getSupportedSSLCipherSuites() {
        try {
            SSLContextBuilder contextBuilder = new SSLContextBuilder();
            SSLContext context = contextBuilder.build();
            SSLSocketFactory sf = context.getSocketFactory();
            SSLSocket socket = (SSLSocket) sf.createSocket();

            return socket.getSupportedCipherSuites();
        } catch(Exception e){}
        return null;
    }
    
    /**
     * Attempts to determine the set of default enabled SSL cipher suites
     * @return default suites, if available, null otherwise
     */
    public String[] getDefaultSSLCipherSuites() {
        try {
            SSLContextBuilder contextBuilder = new SSLContextBuilder();
            SSLContext context = contextBuilder.build();
            SSLSocketFactory sf = context.getSocketFactory();
            SSLSocket socket = (SSLSocket) sf.createSocket();

            return socket.getEnabledCipherSuites();
        } catch(Exception e){}
        return null;
    }
    
    /**
     * Enable a set of SSL cipher suites
     * 
     * @param opts file system options
     * @param ciphers enabled ciphers (null for system defaults)
     */
    public void setEnabledSSLCipherSuites(final FileSystemOptions opts, String[] ciphers) {
        setParam(opts, "sslCipherSuites", Arrays.copyOf(ciphers, ciphers.length));
    }
    
    /**
     * Get the custom set of enabled SSL cipher suites
     * 
     * @param opts file system options
     * @return enabled ciphers (null for system defaults)
     */
    public String[] getEnabledSSLCipherSuites(final FileSystemOptions opts) {
        return (String[])getParam(opts, "sslCipherSuites");        
    }

    @Override
    protected Class<? extends FileSystem> getConfigClass() {
        return HttpFileSystem.class;
    }
}
