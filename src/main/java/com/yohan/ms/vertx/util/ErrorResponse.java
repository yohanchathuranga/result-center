
package com.yohan.ms.vertx.util;

import com.yohan.exceptions.CustomException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

/**
 *
 * @author yohanr
 */
public class ErrorResponse {
    // helper method dealing with failure
    
    public static void errorResponse(RoutingContext ctx, CustomException ex) {

        JsonObject o = new JsonObject();
        o.put("code", ex.getErrorNo());
        o.put("error", ex.getError());
        o.put("type", ex.getErrorType());
        o.put("description", ex.getErrorMsg());

        JsonObject ret = new JsonObject();
        ret.put("fault", o);

        ctx.response().setStatusCode(400)
                .putHeader("content-type", "application/json")
                .end(ret.encodePrettily());

    }
    
    public static void errorResponse(RoutingContext ctx, CustomException ex, int statusCode) {

        JsonObject o = new JsonObject();
        o.put("code", ex.getErrorNo());
        o.put("error", ex.getError());
        o.put("type", ex.getErrorType());
        o.put("description", ex.getErrorMsg());

        JsonObject ret = new JsonObject();
        ret.put("fault", o);

        ctx.response().setStatusCode(statusCode)
                .putHeader("content-type", "application/json")
                .end(ret.encodePrettily());

    }
    
    public static void errorResponse(RoutingContext ctx, JsonObject fault, int statusCode) {

        JsonObject ret = new JsonObject();
        ret.put("fault", fault);

        ctx.response().setStatusCode(statusCode)
                .putHeader("content-type", "application/json")
                .end(ret.encodePrettily());

    }
    

    public static JsonObject getFaultResponseContent(CustomException ex) {

        JsonObject o = new JsonObject();
        o.put("code", ex.getErrorNo());
        o.put("error", ex.getError());
        o.put("type", ex.getErrorType());
        o.put("description", ex.getErrorMsg());

        JsonObject ret = new JsonObject();
        ret.put("fault", o);

        return ret;
    }


    public static void internalError(RoutingContext context, Throwable ex) {
        
        CustomException exx = new CustomException(500, "Internal Error", "InternalError", "Internal Error");
        
        context.response().setStatusCode(500)
                .putHeader("content-type", "application/json")
                .end(getFaultResponseContent(exx).encodePrettily());
    }

    public static void badGateway( RoutingContext context, Throwable ex) {
        
        CustomException exx = new CustomException(404, "Bad Gateway", "badGateway", "Bad Gateway");
        
        //ex.printStackTrace();
        context.response()
                .setStatusCode(502)
                .putHeader("content-type", "application/json")
                .end(getFaultResponseContent(exx).encodePrettily());
    }


}
