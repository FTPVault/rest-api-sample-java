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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

class WeblinkCreator {
    private final ApiSession apiSession;


    public WeblinkCreator(ApiSession apiSession){
        this.apiSession = apiSession;
    }


    public JSONObject createWeblink(String remotePath, boolean sendEmail){
        try{
            URL url = new URL ("https://code.ftpvault.io/api/json/weblink/" + apiSession.getSessionToken() + "/create/");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("comment", "Created with Java API sample");
            jsonRequest.put("path", remotePath);
            jsonRequest.put("send", sendEmail);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            byte[] input = jsonRequest.toString().getBytes("utf-8");
            outputStream.write(input, 0, input.length);           
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {
               result.append(line);
            }

            rd.close();
            
            JSONObject jsonResponse = new JSONObject(result.toString());
            return jsonResponse;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}