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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.json.JSONObject;

   
class Main{
    public static void main(String[] args){
        System.out.println("Starting API sample");
       
        Options options = new Options();
        options.addOption("f", "file", true, "Path to file, mandatory")
            .addOption("r", "remote", true, "Remote path, eg. /my-directory/, mandatory")
            .addOption("w", "weblink", false, "Create weblink")
            .addOption("e", "email", false, "When creating weblink, send email to account owner")
            .addOption("h", "help", false, "Print help");

        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try{
            cmd = parser.parse( options, args);
        }catch(Exception e){
            System.out.println("Parse exception: " + e.getMessage());
            return;
        }

        if(cmd.hasOption("h") || ! cmd.hasOption("f") || ! cmd.hasOption("r")){
            formatter.printHelp("FTPVault.io API sample", options);
            return;
        }

        ApiSession apiSession = new ApiSession(C.API_ACCESS_TOKEN, C.API_USERNAME, C.API_PASSWORD);
        apiSession.createSessionToken();

        if(apiSession.hasSessionToken()){
            System.out.println("Valid API Session: " + apiSession.getSessionToken());
        }else{
            System.out.println("Invalid session token");
            return;
        }

        System.out.println("Uploading " + cmd.getOptionValue("f") + " to " + cmd.getOptionValue("r"));

        FileUploader fileUploader = new FileUploader( cmd.getOptionValue("f"), cmd.getOptionValue("r"), new FileUploadProgress(), apiSession);
        JSONObject fileUploadJsonReponse = fileUploader.upload();
        System.out.println(fileUploadJsonReponse.toString(2));

        if(cmd.hasOption("w") && fileUploadJsonReponse.has("session") ){
            System.out.println("Creating weblink to " + cmd.getOptionValue("f"));

            int expires = fileUploadJsonReponse.getJSONObject("session").getInt("expiresSeconds");

            if(expires < 5){ 
                apiSession.createSessionToken();
            }

            boolean sendEmail = false;

            if(cmd.hasOption("e")){
                sendEmail = true;
            }

            File remoteFile = new File(cmd.getOptionValue("f"));
            String remotePath = cmd.getOptionValue("r") + remoteFile.getName();

            WeblinkCreator weblink = new WeblinkCreator(apiSession);
            JSONObject weblinkJsonReponse = weblink.createWeblink(remotePath, sendEmail);
            System.out.println(weblinkJsonReponse.toString(2));
        }
    }
}

