package com.mycompany.result.manager;

import com.yohan.ms.vertx.util.VerticleDeployHelper;
import com.yohan.mysql.db.DatabaseConfig;
import com.yohan.mysql.dbmanager.DBManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MainVerticle extends AbstractVerticle {

    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    /**
     *
     * @param promise
     */
    @Override
    public void start(Promise<Void> promise) {
        logger.debug("Starting MainVerticle.....");
        configureDb(config(), res -> {

            if (res.succeeded()) {
                deployVerticles(promise);
                logger.debug("Deploy verticles succeeded.....");
            } else {
                logger.debug(res.cause().getMessage());
            }

        });

    }

    private void deployVerticles(Promise<Void> promise) {
        DeploymentOptions options = new DeploymentOptions().setConfig(config());
        DeploymentOptions options2 = new DeploymentOptions().setMaxWorkerExecuteTime(50000).setConfig(config()).setWorker(true).setInstances(15);

        List<Future> list = new ArrayList<>();

        list.add(VerticleDeployHelper.deployHelper(
                vertx,
                options2,
                SubjectVerticle.class.getName()
        ));
        
        list.add(VerticleDeployHelper.deployHelper(
                vertx,
                options,
                RestVerticle.class.getName()
        ));
      
        CompositeFuture
                .all(list)
                .onComplete(
                        result -> {
                            if (result.succeeded()) {
                                logger.info("Verticle deployment successfull.");
                                promise.complete();
                            } else {
                                logger.error("Verticle deployment failed.");
                                promise.fail(result.cause());

                            }
                        }
                );
    }

    @Override
    public void stop() {
        logger.debug("Stop succeeded ");
    }

    private void configureDb(
            JsonObject config,
            Handler<AsyncResult<Void>> future
    ) {

        vertx.executeBlocking(
                executeBlockingFuture -> {
                    try {

                        String dbName = config.getString("db_name", "heroku_1daa753160a2e11");
                        String dbUrl = config.getString("db_url", "jdbc:mysql://bea0d9684fce15:20472493@eu-cdbr-west-01.cleardb.com/heroku_1daa753160a2e11?reconnect=true");
                        String dbUsername = config.getString("db_username", "bea0d9684fce15");
                        String dbPassword = config.getString("db_password", "20472493");
                        DatabaseConfig dbConfig = new DatabaseConfig();
                        dbConfig.setDb(dbName);
                        dbConfig.setPass(dbPassword);
                        dbConfig.setUrl(dbUrl);
                        dbConfig.setUser(dbUsername);

                        DBManager.getInstance().configure(dbConfig);
                        executeBlockingFuture.complete();
                        //db config end

                    } catch (Exception ex) {
                        logger.error("Database configuring exceptions.");
                        executeBlockingFuture.fail(ex);
                    }
                },
                res -> {
                    if (res.failed()) {
                        future.handle(Future.failedFuture(res.cause()));
                    } else {
                        future.handle(Future.succeededFuture());
                    }
                }
        );
    }
}
