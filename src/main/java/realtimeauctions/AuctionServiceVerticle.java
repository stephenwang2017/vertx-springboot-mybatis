package realtimeauctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ErrorHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeEventType;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

@Component
public class AuctionServiceVerticle extends AbstractVerticle {
	
	@Autowired
	private MyService service;

    private static final Logger logger = LoggerFactory.getLogger(AuctionServiceVerticle.class);

    @Override
    public void start() {
        Router router = Router.router(vertx);

        router.route("/eventbus/*").handler(eventBusHandler());
        router.mountSubRouter("/api", auctionApiRouter());
        router.route().failureHandler(errorHandler());
        router.route().handler(staticHandler());
        vertx.createHttpServer().requestHandler(router::accept).listen(8088);
    }

    private SockJSHandler eventBusHandler() {
        BridgeOptions options = new BridgeOptions()
            .addOutboundPermitted(new PermittedOptions().setAddressRegex("auction\\.[0-9]+"));
        return SockJSHandler.create(vertx).bridge(options, event -> {
            if (event.type() == BridgeEventType.SOCKET_CREATED) {
                logger.info("A socket was created");
            }
            event.complete(true);
        });
    }
    
    private Router auctionApiRouter() {
        AuctionRepository repository = new AuctionRepository(vertx.sharedData());
        AuctionValidator validator = new AuctionValidator(repository);
        AuctionHandler handler = new AuctionHandler(repository, validator);

        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.route().consumes("application/json");
        router.route().produces("application/json");

        router.route("/auctions/:id").handler(handler::initAuctionInSharedData);
        router.get("/auctions/:id").handler(handler::handleGetAuction);
        router.patch("/auctions/:id").handler(handler::handleChangeAuctionPrice);
        
        router.get("/getTime").handler(this::getTime);

        return router;
    }

    private ErrorHandler errorHandler() {
        return ErrorHandler.create(true);
    }

    private StaticHandler staticHandler() {
        return StaticHandler.create("auction")
            .setCachingEnabled(false);
    }
    
    private void getTime(RoutingContext routingContext) {
        routingContext.response()
            .putHeader("content-type", "text/html; charset=utf-8")
            .end(service.getCurrentDataTime());
    }
}