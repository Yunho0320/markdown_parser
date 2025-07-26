package markdown.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class MarkdownServerProcess {

    private final static String resourceDir = System.getProperty("resource.dir");

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        // Handler for serving static files (Vue build output)
        ResourceHandler staticHandler = new ResourceHandler();
        staticHandler.setResourceBase(resourceDir);
        staticHandler.setDirectoriesListed(false);
        staticHandler.setWelcomeFiles(new String[]{resourceDir + "/index.html"});

        // Handler for API endpoints
        ServletContextHandler apiHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        apiHandler.setContextPath("/api");
        apiHandler.addServlet(MainHandler.class, "/main");
        apiHandler.addServlet(UploadHandler.class, "/*");

        // API needs to be added first
        HandlerList handlers = new HandlerList();
        handlers.addHandler(apiHandler);
        handlers.addHandler(staticHandler);

        server.setHandler(handlers);
        server.start();
        server.join();
    }
}
