package server.controller;

import java.io.IOException;

import server.controller.IServerExchange;
import server.controller.ServerExchange;
import server.controller.action.IServerAction;
import server.controller.action.ServerActionFactory;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/* ServerHandler implements the Mediator pattern to trigger different actions for different URLs.
 * It use a constructor injected action factory to instantiate the right action for the job and execute it.
 */
public class ServerHandler implements HttpHandler {

    // ServerAction factory
    private final ServerActionFactory actionFactory;

    public ServerHandler(ServerActionFactory actionFactory) {
        this.actionFactory = actionFactory;
    }

    // Implementation of HttpHandler
    public void handle(HttpExchange httpExchange) throws IOException {
        IServerExchange exchange = new ServerExchange(httpExchange);
        IServerAction action = actionFactory.getAction(exchange);
        action.execute(exchange);
    }
}
