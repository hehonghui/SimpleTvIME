package com.simple.simpleremoteinputmethod.httpd;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by andrei on 7/30/15.
 */
public class LocalServer extends NanoHTTPD {
    private final static int PORT = 10086;

    private Context mAppContext;

    public LocalServer(Context context) {
        super(PORT);
        mAppContext = context ;
    }

    @Override
    public Response serve(IHTTPSession session) {
        Log.e("", "@@@ local server : " + session.getUri() + ", params : " + session.getParms().toString());

        String path = session.getUri() ;

        if ( path.endsWith("html")
                || path.endsWith("js")
                || path.endsWith("css")
                || path.endsWith("png")
                || path.endsWith("ico")) {
            try {
                String fileName = path.substring(1, path.length()) ;
                InputStream inputStream = mAppContext.getAssets().open(fileName) ;
                return newFixedLengthResponse(Response.Status.OK, getMimeType(path), inputStream, (long)inputStream.available()) ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String msg = "<html><body><h1>Hello server</h1>\n";
        msg += "<p>We serve " + session.getUri() + " !</p>";
        return newFixedLengthResponse( msg + "</body></html>\n" );
    }

    private static String getMimeType(String path) {
        if ( path.contains(".") ) {
            String type = path.split("\\.")[1];
            switch (type) {
                case "html":
                    return NanoHTTPD.MIME_HTML ;
                case "js":
                    return NanoHTTPD.MIME_JS;
                case "css":
                    return NanoHTTPD.MIME_CSS ;
                case "png":
                    return NanoHTTPD.MIME_PNG ;
                case "ico":
                    return NanoHTTPD.MIME_ICO ;
            }
        }
        return NanoHTTPD.MIME_PLAINTEXT ;
    }

    public int getPort() {
        return PORT;
    }
}
