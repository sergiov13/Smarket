package server.application;

import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpHandler;
import server.controller.ServerHandler;
import server.controller.action.IServerAction;
import server.controller.action.CartListAction;
import server.controller.action.FilterAction;
import server.controller.action.HomeAction;
import server.controller.action.ServerActionFactory;

/* RunContext is the dependency injection container for Production.
 * It manages the creation of an specific http handler and its dependencies.
 */
public class RunContext extends Context {

    // Router configuration for the server
    // Maps a URI path and method with a concrete action controller
    protected HashMap<String, IServerAction> router = new HashMap<String, IServerAction>() {{
    	 put("(POST|GET)index.html", new HomeAction());
         put("(POST|GET)/list.html$", new FilterAction());
         put("POST/cartList.html$", new CartListAction());
    }};


    @Override
    public ISupplyContext supplyContext() {
        return new ISupplyContext() {
            @Override
            public HttpHandler getHandler(IServerRouter router) {
                return new ServerHandler(new ServerActionFactory(router));
            }

            @Override
            public IServerRouter getRouter() {
                return new ServerRouter(router);
            }
        };
    }
}