/*
 * Copyright 2013 MS OpenTech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.msopentech.odatajclient.engine.data;

import static com.msopentech.odatajclient.engine.data.Deserializer.toEntry;

import com.msopentech.odatajclient.engine.data.atom.AtomEntry;
import com.msopentech.odatajclient.engine.data.json.JSONEntry;
import com.msopentech.odatajclient.engine.format.ODataPubFormat;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ODataEntitySetIterator implements Iterator<ODataEntity> {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ODataEntitySetIterator.class);

    private static final long serialVersionUID = 9039605899821494025L;

    private final InputStream stream;

    private final ODataPubFormat format;

    private EntryResource cached;

    private ODataEntitySet entitySet;

    private final ByteArrayOutputStream osFeed;

    private final String namespaces;

    private boolean available = true;

    public ODataEntitySetIterator(final InputStream stream, final ODataPubFormat format) {
        this.stream = stream;
        this.format = format;
        this.osFeed = new ByteArrayOutputStream();

        if (format == ODataPubFormat.ATOM) {
            namespaces = getAllElementAttributes(stream, "feed", osFeed);
        } else {
            namespaces = null;
            try {
                if (consume(stream, "\"value\":", osFeed, true) >= 0) {
                    int c = 0;
                    while (c != '[' && (c = stream.read()) >= 0) {
                        osFeed.write(c);
                    }
                }
            } catch (IOException e) {
                LOG.error("Error parsing feed", e);
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public boolean hasNext() {
        if (available && cached == null) {
            switch (format) {
                case ATOM:
                    cached = nextAtomEntryFromFeed(stream, osFeed, namespaces);
                    break;
                default:
                    cached = nextJsonEntryFromFeed(stream, osFeed);
            }

            if (cached == null) {
                available = false;
                entitySet = ODataReader.readEntitySet(new ByteArrayInputStream(osFeed.toByteArray()), format);
                close();
            }
        }

        return available;
    }

    @Override
    public ODataEntity next() {
        if (hasNext()) {
            ODataEntity res = ODataBinder.getODataEntity(cached);
            cached = null;
            return res;
        }

        throw new NoSuchElementException("No entity found");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Operation not supported");
    }

    public void close() {
        IOUtils.closeQuietly(stream);
        IOUtils.closeQuietly(osFeed);
    }

    public URI getNext() {
        if (entitySet == null) {
            throw new IllegalStateException("Iteration must be completed in order to retrieve the link for next page");
        }
        return entitySet.getNext();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

    private JSONEntry nextJsonEntryFromFeed(
            final InputStream input, final OutputStream osFeed) {
        final ByteArrayOutputStream entry = new ByteArrayOutputStream();

        JSONEntry entity = null;

        try {
            int c = 0;

            boolean foundNewOne = false;

            do {
                c = input.read();
                if (c == '{') {
                    entry.write(c);
                    c = -1;
                    foundNewOne = true;
                }
                if (c == ']') {
                    osFeed.write(c);
                    c = -1;
                }
            } while (c >= 0);

            if (foundNewOne) {
                int count = 1;
                c = 0;

                while (count > 0 && c >= 0) {
                    c = input.read();
                    if (c == '{') {
                        count++;
                    } else if (c == '}') {
                        count--;
                    }
                    entry.write(c);
                }

                if (c >= 0) {
                    entity = toEntry(new ByteArrayInputStream(entry.toByteArray()), JSONEntry.class);
                }
            } else {
                while ((c = input.read()) >= 0) {
                    osFeed.write(c);
                }
            }

        } catch (Exception e) {
            LOG.error("Error retrieving entities from EntitySet", e);
        }

        return entity;
    }

    /**
     * De-Serializes a stream into an OData entity set.
     *
     * @param input stream to de-serialize.
     * @param format de-serialize as AtomFeed or JSONFeed
     * @return de-serialized entity set.
     */
    private AtomEntry nextAtomEntryFromFeed(
            final InputStream input, final OutputStream osFeed, final String namespaces) {
        final ByteArrayOutputStream entry = new ByteArrayOutputStream();

        AtomEntry entity = null;

        try {
            if (consume(input, "<entry>", osFeed, false) >= 0) {
                entry.write("<entry ".getBytes("UTF-8"));
                entry.write(namespaces.getBytes("UTF-8"));
                entry.write(">".getBytes("UTF-8"));

                if (consume(input, "</entry>", entry, true) >= 0) {
                    entity = toEntry(new ByteArrayInputStream(entry.toByteArray()), AtomEntry.class);
                }
            }

        } catch (Exception e) {
            LOG.error("Error retrieving entities from EntitySet", e);
        }

        return entity;
    }

    private String getAllElementAttributes(final InputStream input, final String name, final OutputStream os) {
        final ByteArrayOutputStream attrs = new ByteArrayOutputStream();

        String res;

        try {
            byte[] attrsDeclaration = null;

            final String key = "<" + name + " ";
            if (consume(input, key, os, true) >= 0) {
                if (consume(input, ">", attrs, false) >= 0) {
                    attrsDeclaration = attrs.toByteArray();
                    os.write(attrsDeclaration);
                    os.write('>');
                }
            }

            if (attrsDeclaration == null) {
                res = StringUtils.EMPTY;
            } else {
                res = new String(attrsDeclaration, "UTF-8").trim();
            }

        } catch (Exception e) {
            LOG.error("Error retrieving entities from EntitySet", e);
            res = StringUtils.EMPTY;
        }

        return res.endsWith("/") ? res.substring(0, res.length() - 1) : res;
    }

    private int consume(
            final InputStream input, final String end, final OutputStream os, final boolean includeEndKey)
            throws IOException {

        char[] endKey = end.toCharArray();
        char[] endLowerKey = end.toLowerCase().toCharArray();
        char[] endUpperKey = end.toUpperCase().toCharArray();

        int pos = 0;

        int c = 0;

        while (pos < endKey.length && (c = input.read()) >= 0) {
            if (c == endLowerKey[pos] || c == endUpperKey[pos]) {
                pos++;
                if (includeEndKey && os != null) {
                    os.write(c);
                }
            } else if (pos > 0) {
                if (!includeEndKey && os != null) {
                    for (int i = 0; i < pos; i++) {
                        os.write(endKey[i]);
                    }
                }
                if (os != null) {
                    os.write(c);
                }
                pos = 0;
            } else {
                if (os != null) {
                    os.write(c);
                }
            }
        }

        return c;
    }
}
