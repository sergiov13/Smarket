package server.application;

import server.controller.IServerExchange;
import server.controller.action.IServerAction;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/* ServerRouter encapsulates the logic behind mapping a URL to a server action.
 *
 */
public class ServerRouter implements IServerRouter {

    private final Map<String, IServerAction> router;

    ServerRouter(Map<String, IServerAction> router) {
        this.router = router;
    }


    // Router logic to build the key to map for an action
    private String setRouterKey(IServerExchange exchange) {
        return exchange.getRequestMethod() + exchange.getRequestURI().getPath();
    }


	@Override
	public Optional<IServerAction> get(IServerExchange exchange) {

        for (Map.Entry<String, IServerAction> e: router.entrySet()) {
            if (Pattern.matches(e.getKey(), setRouterKey(exchange)))
                return Optional.of(e.getValue());
        }

        return Optional.empty();
    }
	
}
