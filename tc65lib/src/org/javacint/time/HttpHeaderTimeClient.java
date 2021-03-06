package org.javacint.time;

import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 * Simple HTTP Header time client.
 *
 * Gets time (GMT) from the "Date" HTTP header by performing a request.
 */
public class HttpHeaderTimeClient implements TimeClient {

    private final String url;
    private final String method;

    /**
     * Constructor.
     *
     * @param url URL to get the time from.
     */
    public HttpHeaderTimeClient(String url) {
        this.url = url;
        method = HttpConnection.HEAD;
    }

    public HttpHeaderTimeClient(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public long getTime() throws IOException {
        HttpConnection conn = (HttpConnection) Connector.open(url);
        try {
            conn.setRequestMethod(method);
            long time = conn.getDate();
            if (time != 0) {
                return time / 1000;
            } else {
                throw new RuntimeException("Server returned no valid date in http header");
            }
        } finally {
            conn.close();
        }
    }
}