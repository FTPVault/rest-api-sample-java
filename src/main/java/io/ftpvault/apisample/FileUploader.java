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

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

class FileUploader{
    private final File uploadFile;
    private final String remotePath;
    private final FileUploadProgress uploaderProgress;
    private final ApiSession apiSession;


    public FileUploader(String uploadFile, String remotePath, FileUploadProgress uploaderProgress, ApiSession apiSession){
        this.uploadFile = new File(uploadFile);
        this.remotePath = remotePath;
        this.uploaderProgress = uploaderProgress;
        this.apiSession = apiSession;
    }


    public JSONObject upload(){
        if(!uploadFile.exists()){
            return null;
        }

        try{
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("https://code.ftpvault.io/api/json/data/" + apiSession.getSessionToken() + "/createxl/");
        
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("data", uploadFile, ContentType.APPLICATION_OCTET_STREAM, uploadFile.getName());
            builder.addTextBody("path", remotePath);
            HttpEntity multipart = builder.build();
            
            FileUploadProgress fileUploadProgress = new FileUploadProgress();
            httpPost.setEntity(new ProgressEntityWrapper(multipart, fileUploadProgress));
        
            CloseableHttpResponse response = client.execute(httpPost);
            
            String responseString = new BasicResponseHandler().handleResponse(response);
            JSONObject jsonResponse = new JSONObject(responseString);

            client.close();

            return jsonResponse;
        }catch(Exception ex){
            ex.printStackTrace();
            uploaderProgress.progress(-1);
            return null;
        }
    }
}
