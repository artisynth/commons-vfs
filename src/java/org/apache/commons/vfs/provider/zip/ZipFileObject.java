/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 */
package org.apache.commons.vfs.provider.zip;

import java.io.InputStream;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.AbstractFileObject;

/**
 * A file in a Zip file system.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision: 1.3 $ $Date: 2002/07/05 04:08:19 $
 */
public class ZipFileObject
    extends AbstractFileObject
    implements FileObject
{
    private final HashSet children = new HashSet();
    protected final ZipFile file;
    protected ZipEntry entry;
    private FileType type;

    public ZipFileObject( FileName name,
                          ZipEntry entry,
                          ZipFile zipFile,
                          ZipFileSystem fs )
    {
        super( name, fs );
        setZipEntry( entry );
        file = zipFile;
        if ( file == null )
        {
            type = null;
        }
    }

    /**
     * Sets the details for this file object.
     */
    protected void setZipEntry( final ZipEntry entry )
    {
        if ( this.entry != null )
        {
            return;
        }

        if ((entry == null) || ( entry.isDirectory() ))
        {
            type = FileType.FOLDER;
        }
        else
        {
            type = FileType.FILE;
        }

        this.entry = entry;
    }

    /**
     * Attaches a child
     */
    public void attachChild( FileName childName )
    {
        children.add( childName.getBaseName() );
    }

    /**
     * Returns true if this file is read-only.
     */
    public boolean isWriteable()
    {
        return false;
    }

    /**
     * Returns the file's type.
     */
    protected FileType doGetType()
    {
        return type;
    }

    /**
     * Lists the children of the file.
     */
    protected String[] doListChildren()
    {
        return (String[])children.toArray( new String[ children.size() ] );
    }

    /**
     * Returns the size of the file content (in bytes).  Is only called if
     * {@link #doGetType} returns {@link FileType#FILE}.
     */
    protected long doGetContentSize()
    {
        return entry.getSize();
    }

    /**
     * Creates an input stream to read the file content from.  Is only called
     * if  {@link #doGetType} returns {@link FileType#FILE}.  The input stream
     * returned by this method is guaranteed to be closed before this
     * method is called again.
     */
    protected InputStream doGetInputStream() throws Exception
    {
        return file.getInputStream( entry );
    }
}