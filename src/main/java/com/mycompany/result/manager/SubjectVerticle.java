package com.mycompany.result.manager;

import com.google.gson.Gson;
import com.mycompany.result.base.SubjectManager;
import com.mycompany.result.dataobject.DOCountRequest;
import com.mycompany.result.dataobject.DOListCountResult;
import com.mycompany.result.dataobject.DOListRequest;
import com.mycompany.result.dataobject.DOSubject;
import com.yohan.exceptions.CustomException;
import com.yohan.exceptions.InvalidInputException;
import com.yohan.ms.vertx.util.ErrorResponse;
import com.yohan.mysql.dbmanager.DBManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author yohanr
 */
public class SubjectVerticle extends AbstractVerticle {

    final String EB_ADDRESS = "mycompany.subject.subjects";
    SubjectManager subjectManager;
    Gson gson = new Gson();
    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        subjectManager = new SubjectManager(DBManager.getInstance().getFactory());

        vertx.eventBus().consumer(EB_ADDRESS, (message) -> {

            Object result = null;
            try {
                JsonObject req = (JsonObject) message.body();
                String method = req.getString("method");
                logger.debug("method  ---> " + method);
                logger.debug("request ---> " + gson.toJson(req));
                switch (method) {
                    case "subject.create":
                        result = subjectCreate(req.getJsonObject("data"));
                        break;
                    case "subject.update":
                        result = subjectUpdate(req.getJsonObject("data"));
                        break;
                    case "subject.get":
                        result = subjectGet(req.getJsonObject("data"));
                        break;
                    case "subject.delete":
                        result = subjectDelete(req.getJsonObject("data"));
                        break;
                    case "subject.list":
                        result = subjectList(req.getJsonObject("data"));
                        break;
                    case "subject.count":
                        result = subjectCount(req.getJsonObject("data"));
                        break;
                    case "subject.gpa":
                        result = subjectGpa(req.getJsonObject("data"));
                        break;
                    default:
                        throw new InvalidInputException("Invalid result");
                }

                JsonObject rest = new JsonObject()
                        .put("success", true)
                        .put("data", result);
                message.reply(rest);

            } catch (CustomException ex) {
                logger.error(ex.getMessage());
                result = ErrorResponse.getFaultResponseContent(ex)
                        .put("success", false);
                message.reply(result);
            }
        });

    }

    private JsonObject subjectCreate(JsonObject request) throws CustomException {
        DOSubject subjectCreateRequest = gson.fromJson(request.encode(), DOSubject.class);
        DOSubject result = subjectManager.create(subjectCreateRequest);
        return new JsonObject(Json.encode(result));

    }

    private JsonObject subjectUpdate(JsonObject request) throws CustomException {
        DOSubject subjectUpdateRequest = gson.fromJson(request.encode(), DOSubject.class);
        DOSubject result = subjectManager.update(subjectUpdateRequest);
        return new JsonObject(Json.encode(result));

    }

    private JsonObject subjectGet(JsonObject request) throws CustomException {
        DOSubject result = subjectManager.get(request.getString("id"));
        return new JsonObject(Json.encode(result));
    }

    private JsonObject subjectDelete(JsonObject request) throws CustomException {
        boolean result = subjectManager.delete(request.getString("id"));
        JsonObject resultObj = new JsonObject();
        resultObj.put("isDeleted", result);
        return new JsonObject(Json.encode(resultObj));
    }

    private JsonObject subjectCount(JsonObject request) throws CustomException {
        DOCountRequest countRequest = gson.fromJson(request.encode(), DOCountRequest.class);
        DOListCountResult result = subjectManager.count(countRequest.getFilterData());
        return new JsonObject(Json.encode(result));
    }

    private JsonArray subjectList(JsonObject request) throws CustomException {
        DOListRequest listRequest = gson.fromJson(request.encode(), DOListRequest.class);
        ArrayList<DOSubject> result = subjectManager.list(listRequest.getFilterData(), listRequest.getOrderFields(), listRequest.isDescending(), listRequest.getPage(), listRequest.getLimit());
        return new JsonArray(gson.toJson(result));
    }
    
    private JsonObject subjectGpa(JsonObject request) throws CustomException {
        double result = subjectManager.gpa();
        JsonObject resultObj = new JsonObject();
        resultObj.put("GPA", result);
        return new JsonObject(Json.encode(resultObj));
    }


}
