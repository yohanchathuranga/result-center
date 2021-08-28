package com.mycompany.result.manager;

import com.yohan.exceptions.ServerFailedException;
import com.yohan.ms.vertx.util.ResponseBuilderUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.docker.DockerLinksServiceImporter;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class RestVerticle extends AbstractVerticle {

    private final Logger logger = LogManager.getLogger(this.getClass().getName());
    ServiceDiscovery discovery;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        Router router = Router.router(vertx);
        //cors handler start
        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add("x-requested-with");
        allowedHeaders.add("Access-Control-Allow-Origin");
        allowedHeaders.add("origin");
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("accept");
        allowedHeaders.add("X-PINGARUNER");
        allowedHeaders.add("Authorization");

        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.OPTIONS);
        allowedMethods.add(HttpMethod.DELETE);
        allowedMethods.add(HttpMethod.PATCH);
        allowedMethods.add(HttpMethod.PUT);
        router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));
        //cors handler end

        router.route().handler(BodyHandler.create());

        router.route("/*").handler(StaticHandler.create());
        router.get("/health").handler(this::handleHealth);

        SubjectHandler subjectHandler = new SubjectHandler(vertx, router);
        router.mountSubRouter(subjectHandler.getMountPoint(), subjectHandler.getSubRouter());

        
        String discoveryAnounceAddress = config().getString("discovery.address", "printstation-services");
        String discoveryName = config().getString("discovery.name", "printstation");
        //init service discovery instance
        discovery = ServiceDiscovery.create(vertx,
                new ServiceDiscoveryOptions()
                        .setAnnounceAddress(discoveryAnounceAddress)
                        .setName(discoveryName)
                        .setBackendConfiguration(config()))
                .registerServiceImporter(new DockerLinksServiceImporter(), new JsonObject());

        String serviceHost = config().getString("service.host", "localhost");
        int servicePort = config().getInteger("service.port", 9888);

        vertx.createHttpServer().requestHandler(router).listen(servicePort, http -> {
            if (http.succeeded()) {
                startPromise.complete();
                String serviceName = "transaction";
                String serviceRoot = "/";
                String apiName = "transaction";
                publishHttpEndpoint(serviceHost, servicePort, serviceName, serviceRoot, apiName);
            } else {
                startPromise.fail(http.cause());
            }
        });

    }

    private void publishHttpEndpoint(String serviceHost, int servicePort, String serviceName, String serviceRoot, String apiName) {
        //discovery record
        Record record = HttpEndpoint.createRecord(serviceName, serviceHost, servicePort, serviceRoot, new JsonObject().put("api.name", apiName));

        //publish the service
        discovery.publish(record, ar -> {
            if (ar.succeeded()) {
                //   LOGGER.info("\"" + record.getName() + "\" successfully published!");
                //Record publishedRecord = ar.result();
            } else {
                // publication failed
                // LOGGER.error("publishing micro service api failed. name: " + apiName);
            }

            discovery.close();
        });

    }

    private void handleHealth(RoutingContext ctx) {
        try {
            JsonObject o = new JsonObject();
            o.put("time_stamp", Instant.now().getEpochSecond());
            HttpServerResponse response = ctx.response();
            response.putHeader("content-type", "application/json").end(o.encodePrettily());

        } catch (Exception e) {
            ServerFailedException ex = new ServerFailedException(e.getMessage());
            ctx.response().setStatusCode(400)
                    .putHeader("content-type", "application/json")
                    .end(ResponseBuilderUtil.getFaultResponseContent(ex).encode());
        }
    }

}
