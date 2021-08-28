/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.transaction.service;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

/**
 *
 * @author yohanr
 */
public class CustomDeployer {

    public static void main(String[] args) {
        //new Launcher().dispatch(args);
        //Vertx.vertx().deployVerticle("com.popsockets.clientservices.vertxtest4.MainVerticle");
        VertxOptions options = new VertxOptions();
        //options.setClustered(true);
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                res.result().deployVerticle("com.mycompany.transaction.manager.MainVerticle");
            } else {
                res.cause().printStackTrace();
            }
        });
    }

}
