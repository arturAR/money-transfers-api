package server.handlers;

import spark.Filter;

public class ContentHandler {
    public Filter getContentHandler() {
        return (request, response) -> {
            response.header("Content-Encoding", "gzip");
            response.type("application/json");
        };
    }
}
