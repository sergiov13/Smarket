package server.controller.action;

import server.controller.IServerExchange;

import java.io.IOException;

/* IServerAction interface implements Strategy pattern. Classes implementing IServerAction are
 * used as controllers for different URI request made to our server.
 */
public interface IServerAction {
    void execute(IServerExchange exchange) throws IOException;
}
