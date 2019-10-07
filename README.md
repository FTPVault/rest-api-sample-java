# FTPVault.io REST API Java sample code

This project shows how to upload a file to the https://ftpvault.io/ cloud storage using our REST API in a small Java application.
The code is not intentet to be production ready, but serve as a guide on how to get started.

Out of the box, this sample code will :

- Upload a (large) file
- Create a weblink (hyperlink) to the uploaded file
- Send an email to the account owner with the weblink

Remember to update access token, username and password in ```C.java```

```java
package io.ftpvault.apisample;

class C {
    public static final String API_ACCESS_TOKEN = "YOUR_ACCESSTOKEN";
    public static final String API_USERNAME = "YOUR_API_USER";
    public static final String API_PASSWORD = "YOUR_API_PASSWORD";
}
```

Visit https://ftpvault.io/api/ to create API username and password, you must be logged in.

Example run:
java -jar ftpvault-apisample.jar -w -e -f my-file-to-upload.txt

The full API documentation is available at https://ftpvault.io/support/api-rest-home/
