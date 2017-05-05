package realtimeauctions;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.vertx.core.Vertx;

@SpringBootApplication
public class AuctionApplication {
	
	@Autowired
	private AuctionServiceVerticle auctionServiceVerticle;
	
	public static void main(String[] args) {
		SpringApplication.run(AuctionApplication.class);
	}

	@PostConstruct
	public void deployServerVerticle() {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(auctionServiceVerticle);
	}
}

