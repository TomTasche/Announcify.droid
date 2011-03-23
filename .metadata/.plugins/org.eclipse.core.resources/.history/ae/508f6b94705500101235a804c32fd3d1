package com.announcify.plugin.message.mms.util.pdu;

/*
 * Copyright (C) 2007-2008 Esmertec AG. Copyright (C) 2007-2008 The Android Open
 * Source Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.util.Log;

/**
 * Encoded-string-value = Text-string | Value-length Char-set Text-string
 */
public class EncodedStringValue implements Cloneable {

    private static final String TAG = "EncodedStringValue";

    /**
     * Concatenate an EncodedStringValue[] into a single String.
     */
    public static String concat(final EncodedStringValue[] addr) {
        final StringBuilder sb = new StringBuilder();
        final int maxIndex = addr.length - 1;
        for (int i = 0; i <= maxIndex; i++) {
            sb.append(addr[i].getString());
            if (i < maxIndex) {
                sb.append(";");
            }
        }

        return sb.toString();
    }

    public static EncodedStringValue copy(final EncodedStringValue value) {
        if (value == null) {
            return null;
        }

        return new EncodedStringValue(value.mCharacterSet, value.mData);
    }

    public static EncodedStringValue[] encodeStrings(final String[] array) {
        final int count = array.length;
        if (count > 0) {
            final EncodedStringValue[] encodedArray = new EncodedStringValue[count];
            for (int i = 0; i < count; i++) {
                encodedArray[i] = new EncodedStringValue(array[i]);
            }
            return encodedArray;
        }
        return null;
    }

    /**
     * Extract an EncodedStringValue[] from a given String.
     */
    public static EncodedStringValue[] extract(final String src) {
        final String[] values = src.split(";");

        final ArrayList<EncodedStringValue> list = new ArrayList<EncodedStringValue>();
        for (final String value : values) {
            if (value.length() > 0) {
                list.add(new EncodedStringValue(value));
            }
        }

        final int len = list.size();
        if (len > 0) {
            return list.toArray(new EncodedStringValue[len]);
        } else {
            return null;
        }
    }

    /**
     * The Char-set value.
     */
    private int mCharacterSet;

    /**
     * The Text-string value.
     */
    private byte[] mData;

    /**
     * Constructor.
     * 
     * @param data
     *            the Text-string value
     * @throws NullPointerException
     *             if Text-string value is null.
     */
    public EncodedStringValue(final byte[] data) {
        this(CharacterSets.DEFAULT_CHARSET, data);
    }

    /**
     * Constructor.
     * 
     * @param charset
     *            the Char-set value
     * @param data
     *            the Text-string value
     * @throws NullPointerException
     *             if Text-string value is null.
     */
    public EncodedStringValue(final int charset, final byte[] data) {
        // TODO: CharSet needs to be validated against MIBEnum.
        if (null == data) {
            throw new NullPointerException(
                    "EncodedStringValue: Text-string is null.");
        }

        mCharacterSet = charset;
        mData = new byte[data.length];
        System.arraycopy(data, 0, mData, 0, data.length);
    }

    public EncodedStringValue(final String data) {
        try {
            mData = data.getBytes(CharacterSets.DEFAULT_CHARSET_NAME);
            mCharacterSet = CharacterSets.DEFAULT_CHARSET;
        } catch (final UnsupportedEncodingException e) {
            Log.e(TAG, "Default encoding must be supported.", e);
        }
    }

    /**
     * Append to Text-string.
     * 
     * @param textString
     *            the textString to append
     * @throws NullPointerException
     *             if the text String is null or an IOException occured.
     */
    public void appendTextString(final byte[] textString) {
        if (null == textString) {
            throw new NullPointerException("Text-string is null.");
        }

        if (null == mData) {
            mData = new byte[textString.length];
            System.arraycopy(textString, 0, mData, 0, textString.length);
        } else {
            final ByteArrayOutputStream newTextString = new ByteArrayOutputStream();
            try {
                newTextString.write(mData);
                newTextString.write(textString);
            } catch (final IOException e) {
                e.printStackTrace();
                throw new NullPointerException(
                        "appendTextString: failed when write a new Text-string");
            }

            mData = newTextString.toByteArray();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        super.clone();
        final int len = mData.length;
        final byte[] dstBytes = new byte[len];
        System.arraycopy(mData, 0, dstBytes, 0, len);

        try {
            return new EncodedStringValue(mCharacterSet, dstBytes);
        } catch (final Exception e) {
            Log.e(TAG, "failed to clone an EncodedStringValue: " + this);
            e.printStackTrace();
            throw new CloneNotSupportedException(e.getMessage());
        }
    }

    /**
     * Get Char-set value.
     * 
     * @return the value
     */
    public int getCharacterSet() {
        return mCharacterSet;
    }

    /**
     * Convert this object to a {@link java.lang.String}. If the encoding of the
     * EncodedStringValue is null or unsupported, it will be treated as
     * iso-8859-1 encoding.
     * 
     * @return The decoded String.
     */
    public String getString() {
        if (CharacterSets.ANY_CHARSET == mCharacterSet) {
            return new String(mData); // system
        } else {
            try {
                final String name = CharacterSets.getMimeName(mCharacterSet);
                return new String(mData, name);
            } catch (final UnsupportedEncodingException e) {
                try {
                    return new String(mData, CharacterSets.MIMENAME_ISO_8859_1);
                } catch (final UnsupportedEncodingException _) {
                    return new String(mData); // system default encoding.
                }
            }
        }
    }

    /**
     * Get Text-string value.
     * 
     * @return the value
     */
    public byte[] getTextString() {
        final byte[] byteArray = new byte[mData.length];

        System.arraycopy(mData, 0, byteArray, 0, mData.length);
        return byteArray;
    }

    /**
     * Set Char-set value.
     * 
     * @param charset
     *            the Char-set value
     */
    public void setCharacterSet(final int charset) {
        // TODO: CharSet needs to be validated against MIBEnum.
        mCharacterSet = charset;
    }

    /**
     * Set Text-string value.
     * 
     * @param textString
     *            the Text-string value
     * @throws NullPointerException
     *             if Text-string value is null.
     */
    public void setTextString(final byte[] textString) {
        if (null == textString) {
            throw new NullPointerException(
                    "EncodedStringValue: Text-string is null.");
        }

        mData = new byte[textString.length];
        System.arraycopy(textString, 0, mData, 0, textString.length);
    }

    /**
     * Split this encoded string around matches of the given pattern.
     * 
     * @param pattern
     *            the delimiting pattern
     * @return the array of encoded strings computed by splitting this encoded
     *         string around matches of the given pattern
     */
    public EncodedStringValue[] split(final String pattern) {
        final String[] temp = getString().split(pattern);
        final EncodedStringValue[] ret = new EncodedStringValue[temp.length];
        for (int i = 0; i < ret.length; ++i) {
            try {
                ret[i] = new EncodedStringValue(mCharacterSet,
                        temp[i].getBytes());
            } catch (final NullPointerException _) {
                // Can't arrive here
                return null;
            }
        }
        return ret;
    }
}
