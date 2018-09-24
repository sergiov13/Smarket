package server.application;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import server.application.IServerRouter;
import server.application.ServerRouter;
import server.controller.ServerHandler;
import server.controller.action.IServerAction;
import server.controller.action.FilterAction;
import server.controller.action.CartListAction;
import server.controller.action.HomeAction;
import server.controller.action.ServerActionFactory;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;


public abstract class Context {

    private HttpServer server;

    private Map<String, IServerAction> routerConfiguration =
    	    new HashMap<String, IServerAction>()
    		{{
    	    	 put("(POST|GET)home.html", new HomeAction());
    	         put("(POST|GET)/list.html$", new FilterAction());
    	         put("POST/cartList.html$", new CartListAction());
            }};

    // Interface to supply context that subclasses must implement.
    protected interface ISupplyContext {
        HttpHandler getHandler(IServerRouter router);
        IServerRouter getRouter();
    }

    // Context subclasses need to supply specific context, ex. Run or Test
    protected abstract ISupplyContext supplyContext();


    // Start listening on a port
    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        IServerRouter router = new ServerRouter(routerConfiguration);
        HttpContext context = server.createContext("/", new ServerHandler(new ServerActionFactory(supplyContext().getRouter())));
        server.setExecutor(null);
        server.start();
    }

    // Stop the server
    public void stop(int delay) throws IOException {
        if (null != server) server.stop(delay);
    }
}
