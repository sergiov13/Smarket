package server.application;

import java.util.Optional;

import server.controller.IServerExchange;
import server.controller.action.IServerAction;


/* IServerRouter defines the behaviour for routing URls to server actions.
 *
 */
public interface IServerRouter {
	 Optional<IServerAction> get(IServerExchange e);

}
