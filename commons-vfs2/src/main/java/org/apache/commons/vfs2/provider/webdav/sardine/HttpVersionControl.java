/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.commons.vfs2.provider.webdav.sardine;

import java.net.URI;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * Simple class for making WebDAV <code>VERSION-CONTROL</code> requests.
 *
 */
public class HttpVersionControl extends HttpRequestBase
{
    public static final String METHOD_NAME = "VERSION-CONTROL";

    public HttpVersionControl(URI sourceUrl)
    {
        this.setURI(sourceUrl);
    }

    public HttpVersionControl(String sourceUrl) {
        this(URI.create(sourceUrl));
    }

    @Override
    public String getMethod()
    {
        return METHOD_NAME;
    }
}