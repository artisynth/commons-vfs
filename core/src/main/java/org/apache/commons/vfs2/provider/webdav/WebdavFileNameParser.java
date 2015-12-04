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
package org.apache.commons.vfs2.provider.webdav;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.provider.FileNameParser;
import org.apache.commons.vfs2.provider.VfsComponentContext;
import org.apache.commons.vfs2.provider.http.HttpFileNameParser;

/**
 * Implementation for webdav, set default port to 80
 * @since 2.0
 */
public class WebdavFileNameParser extends HttpFileNameParser
{
    private static final WebdavFileNameParser INSTANCE = new WebdavFileNameParser();

    public WebdavFileNameParser()
    {
        super();
    }

    public static FileNameParser getInstance()
    {
        return INSTANCE;
    }
    
	@Override
	public FileName parseUri(final VfsComponentContext context, FileName base, final String filename) throws FileSystemException {
		
		return super.parseUri(context, base, filename);
		//      XXX IS THIS NEEDED?
		//		// FTP URI are generic URI (as per RFC 2396)
		//		final StringBuilder name = new StringBuilder();
		//
		//		// Extract the scheme and authority parts
		//		final Authority auth = extractToPath(filename, name);
		//
		//		// Extract the queryString
		//		String queryString = UriParser.extractQueryString(name);
		//
		//		// Decode and normalise the file name
		//		UriParser.canonicalizePath(name, 0, name.length(), this);
		//		UriParser.fixSeparators(name);
		//		FileType fileType = UriParser.normalisePath(name);
		//		final String path = name.toString();
		//
		//		return new URLFileName(auth.getScheme(), auth.getHostName(), auth.getPort(), getDefaultPort(), auth.getUserName(), auth.getPassword(), path, fileType,
		//						queryString);
	}
}