/**
* Author   Carsten Peter Viggo Rasmussen / cpr@gnusys.dk
* Version  1.0
* Released 2019-10-11
*
* The FTPVault.io API Java sample shows how to use the  REST API for 
* uploading a file and creating a weblink (hyperlink) to the
* file when uploading has completed.
*
* This sample code uses a variety of external libraries to handle the 
* upload, parse commandline arguments etc. See gradle.build
*
*
* Permission is hereby granted, free of charge, to any person obtaining 
* a copy of this software and associated documentation files (the "Software"), 
* to deal in the Software without restriction, including without limitation 
* the rights to use, copy, modify, merge, publish, distribute, sublicense, 
* and/or sell copies of the Software, and to permit persons to whom the 
* Software is furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included 
* in all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS 
* OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
* IN THE SOFTWARE.
*/

package io.ftpvault.apisample;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CountingOutputStream extends FilterOutputStream {
    private ProgressListener listener;
    private long transferred;
    private long totalBytes;
 

    public CountingOutputStream(
      OutputStream out, ProgressListener listener, long totalBytes) {
        super(out);
        this.listener = listener;
        transferred = 0;
        this.totalBytes = totalBytes;
    }
 

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
        transferred += len;
        listener.progress(getCurrentProgress());
    }
 

    @Override
    public void write(int b) throws IOException {
        out.write(b);
        transferred++;
        listener.progress(getCurrentProgress());
    }
 

    private float getCurrentProgress() {
        return ((float) transferred / totalBytes) * 100;
    }
}