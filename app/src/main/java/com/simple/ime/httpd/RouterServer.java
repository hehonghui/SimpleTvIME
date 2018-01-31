package com.simple.ime.httpd;

import android.content.Context;
import android.util.Log;

import com.simple.ime.services.RemoteInputEvent;
import com.simple.ime.services.RemoteKeyEvent;
import com.simple.ime.utils.Utils;

import java.io.IOException;
import java.io.InputStream;

import de.greenrobot.event.EventBus;

/**
 * Created by andrei on 7/30/15.
 */
public class RouterServer extends NanoHTTPD {
    private final static int PORT = 10086;

    protected Context mAppContext;

    public RouterServer(Context context) {
        super(PORT);
        mAppContext = context ;
    }

    @Override
    public Response serve(IHTTPSession session) {
        Log.e("", "@@@ local server : " + session.getUri() + ", params : " + session.getParms().toString());

        String path = session.getUri() ;

        if ( path.equalsIgnoreCase("/") ) {
            path = "/index.html" ;
        }

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

        if ( path.endsWith("key") ) {
            final String keyCode = session.getParms().get("keycode") ;
            EventBus.getDefault().post(RemoteKeyEvent.create(Integer.parseInt(keyCode)));
            return newFixedLengthJsonResponse( "{}" );
        }

        if ( path.endsWith("text") ) {
            final String text = session.getParms().get("text");
            EventBus.getDefault().post(RemoteInputEvent.create(text));
            return newFixedLengthJsonResponse( "{}" );
        }
        return newFixedLengthJsonResponse( "{\"err_msg\":\"Unknown event for : " + session.getUri() + "!!!\"}" );
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


    public String getLocalAddress() {
        return "http://" + Utils.getIpAddress(mAppContext) + ":" + PORT ;
    }
}
