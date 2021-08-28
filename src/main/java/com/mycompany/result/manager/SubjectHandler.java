/*
 * To change this license header, choose Subject Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.result.manager;



import com.mycompany.result.util.EventBusMessageUtil;
import com.yohan.ms.vertx.util.ErrorResponse;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author yohanr
 */
public class SubjectHandler {

    Vertx vertx;
    Router router;
    Router subRouter;
    String mountPoint = "/subjects";
    final String EB_ADDRESS = "mycompany.subject.subjects";
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    public SubjectHandler(Vertx vertx, Router router) {
        this.vertx = vertx;
        this.router = router;

        subRouter = Router.router(vertx);
        subRouter.route().handler(BodyHandler.create());
        subRouter.post("/").handler(this::handleSubjectCreate);
        subRouter.put("/").handler(this::handleSubjectUpdate);
        subRouter.get("/:id").handler(this::handleSubjectGet);
        subRouter.delete("/:id").handler(this::handleSubjectDelete);
        subRouter.post("/list").handler(this::handleSubjectList);
        subRouter.post("/count").handler(this::handleSubjectCount);
        subRouter.post("/gpa").handler(this::handleSubjectGpa);
    }

    public String getMountPoint() {
        return mountPoint;
    }

    public Router getSubRouter() {
        return subRouter;
    }

    private void handleSubjectCreate(RoutingContext ctx) {
        
        HttpServerResponse response = ctx.response();
        JsonObject reqObj = ctx.getBodyAsJson();
        String type = reqObj.getString("type");
        JsonObject msg = EventBusMessageUtil.buildMessage("subject.create", reqObj);
        vertx.eventBus().request(EB_ADDRESS, msg, res -> {
            if (res.succeeded()) {
                JsonObject msgResult = (JsonObject) res.result().body();
                    if (msgResult.getBoolean("success")) {
                        response.putHeader("content-type", "application/json").end(msgResult.getJsonObject("data").encodePrettily());
                    } else {
                        ErrorResponse.errorResponse(ctx, msgResult.getJsonObject("fault"), 400);
                    }
            } else {
                ErrorResponse.internalError(ctx, res.cause());
            }
        });

    }

    private void handleSubjectUpdate(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();
        JsonObject reqObj = ctx.getBodyAsJson();
        JsonObject msg = EventBusMessageUtil.buildMessage("subject.update", reqObj);
        vertx.eventBus().request(EB_ADDRESS, msg, res -> {
            if (res.succeeded()) {
                JsonObject msgResult = (JsonObject) res.result().body();
                if (msgResult.getBoolean("success")) {
                    response.putHeader("content-type", "application/json").end(msgResult.getJsonObject("data").encodePrettily());
                } else {
                    ErrorResponse.errorResponse(ctx, msgResult.getJsonObject("fault"), 400);
                }
            } else {
                ErrorResponse.internalError(ctx, res.cause());
            }
        });

    }

    private void handleSubjectGet(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();
        String id = ctx.request().getParam("id");
        JsonObject reqObj = new JsonObject();
        reqObj.put("id", id);
        JsonObject msg = EventBusMessageUtil.buildMessage("subject.get", reqObj);
        vertx.eventBus().request(EB_ADDRESS, msg, res -> {
            if (res.succeeded()) {
                JsonObject msgResult = (JsonObject) res.result().body();
                if (msgResult.getBoolean("success")) {
                    response.putHeader("content-type", "application/json").end(msgResult.getJsonObject("data").encodePrettily());
                } else {
                    ErrorResponse.errorResponse(ctx, msgResult.getJsonObject("fault"), 400);
                }
            } else {
                ErrorResponse.internalError(ctx, res.cause());
            }
        });
    }

    private void handleSubjectDelete(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();
        String id = ctx.request().getParam("id");
        JsonObject reqObj = new JsonObject();
        reqObj.put("id", id);
        JsonObject msg = EventBusMessageUtil.buildMessage("subject.delete", reqObj);
        vertx.eventBus().request(EB_ADDRESS, msg, res -> {
            if (res.succeeded()) {
                JsonObject msgResult = (JsonObject) res.result().body();
                if (msgResult.getBoolean("success")) {
                    response.putHeader("content-type", "application/json").end(msgResult.getJsonObject("data").encodePrettily());
                } else {
                    ErrorResponse.errorResponse(ctx, msgResult.getJsonObject("fault"), 400);
                }
            } else {
                ErrorResponse.internalError(ctx, res.cause());
            }
        });
    }

    private void handleSubjectList(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();
        JsonObject reqObj = ctx.getBodyAsJson();
        JsonObject msg = EventBusMessageUtil.buildMessage("subject.list", reqObj);
        vertx.eventBus().request(EB_ADDRESS, msg, res -> {
            if (res.succeeded()) {
                JsonObject msgResult = (JsonObject) res.result().body();
                if (msgResult.getBoolean("success")) {
                    response.putHeader("content-type", "application/json").end(msgResult.getJsonArray("data").encodePrettily());
                } else {
                    ErrorResponse.errorResponse(ctx, msgResult.getJsonObject("fault"), 400);
                }
            } else {
                ErrorResponse.internalError(ctx, res.cause());
            }
        });
    }

    private void handleSubjectCount(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();
        JsonObject reqObj = ctx.getBodyAsJson();
        JsonObject msg = EventBusMessageUtil.buildMessage("subject.count", reqObj);
        vertx.eventBus().request(EB_ADDRESS, msg, res -> {
            if (res.succeeded()) {
                JsonObject msgResult = (JsonObject) res.result().body();
                if (msgResult.getBoolean("success")) {
                    response.putHeader("content-type", "application/json").end(msgResult.getJsonObject("data").encodePrettily());
                } else {
                    ErrorResponse.errorResponse(ctx, msgResult.getJsonObject("fault"), 400);
                }
            } else {
                ErrorResponse.internalError(ctx, res.cause());
            }
        });
    }
    private void handleSubjectGpa(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();
        JsonObject reqObj = new JsonObject();
        JsonObject msg = EventBusMessageUtil.buildMessage("subject.gpa", reqObj);
        vertx.eventBus().request(EB_ADDRESS, msg, res -> {
            if (res.succeeded()) {
                JsonObject msgResult = (JsonObject) res.result().body();
                if (msgResult.getBoolean("success")) {
                    response.putHeader("content-type", "application/json").end(msgResult.getJsonObject("data").encodePrettily());
                } else {
                    ErrorResponse.errorResponse(ctx, msgResult.getJsonObject("fault"), 400);
                }
            } else {
                ErrorResponse.internalError(ctx, res.cause());
            }
        });
    }

}
