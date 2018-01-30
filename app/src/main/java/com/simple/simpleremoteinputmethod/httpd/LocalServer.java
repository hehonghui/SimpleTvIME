package com.simple.simpleremoteinputmethod.httpd;

/**
 * Created by andrei on 7/30/15.
 */
public class LocalServer extends NanoHTTPD {
    private final static int PORT = 10086;

    public LocalServer() {
        super(PORT);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        msg += "<p>We serve " + session.getUri() + " !</p>";
        return newFixedLengthResponse( msg + "</body></html>\n" );
    }

    public int getPort() {
        return PORT;
    }
}
