package org.api.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.api.bo.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.api.utils.CustomJSONArray;
import org.api.utils.JSONAble;
import org.api.utils.Security;
import org.api.utils.TechniqueException;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON + ";charset=UTF-8")
public abstract class ApiService {

    protected User mRequester;

    @AroundInvoke
    private Object intercept(InvocationContext ctx) {
        long start = new Date().getTime();
        try {
            if (ctx.getParameters() != null && ctx.getParameters().length > 0
                    && ctx.getParameters()[0] instanceof HttpHeaders) {
                HttpHeaders lHeaders = (HttpHeaders) ctx.getParameters()[0];
                String lToken = lHeaders.getRequestHeaders().getFirst("authToken");

                /*if (lToken != null) {
                    try {
                        mRequester = mBasicUserManager.getUserFromExistingAuthToken(lToken);
                    } catch (TechniqueException ex) {
                        return internalError(ex);
                    }
                }*/
            }
            return ctx.proceed();
        } catch (Exception ex) {
            ex.printStackTrace();
            return internalError(new TechniqueException());
        } finally {
            traceAppel(ctx, new Date().getTime() - start);
        }
    }

    private void traceAppel(InvocationContext ctx, long length) {

        System.out.println(ctx.getMethod().getName() + " [ " + length + " ms ] " + 0 + " sql query(s). ");
    }

    protected void writeFile(byte[] content, String filename, boolean pOverrideIfNeeded)
            throws IOException, TechniqueException {
        File file = new File(filename);
        if (file.exists()) {
            if (pOverrideIfNeeded) {
                file.delete();
            } else {
                throw new TechniqueException("Impossible d'uploader le fichier");
            }
        }
        file.createNewFile();
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(content);
        fop.flush();
        fop.close();
    }

    /**
     * Download the file given in parameters in the client browser
     *
     * @param pFile
     * @return
     */
    protected Response wrapResponse(File pFile) {
        try {
            return wrapResponse(new JSONObject().put("path", Security.crypt(pFile.getAbsolutePath())));
        } catch (JSONException e) {
            return wrapResponse();
        }
    }

    /**
     *
     * @return an error internal server error Response
     */
    protected Response internalError() {
        return wrapResponse(new TechniqueException(), Response.Status.INTERNAL_SERVER_ERROR);
    }


    protected Response internalError(TechniqueException pException) {
        return wrapResponse(pException, Response.Status.INTERNAL_SERVER_ERROR);
    }

    /**
     *
     * @param pException
     *            the exception thrown during the call
     * @param pStatusCode
     *            the status code of the error thrown
     * @param pException
     *            the exception thrown during the call
     */
    protected Response wrapResponse(TechniqueException pException, Status pStatusCode) {
        JSONObject responseJSON = new JSONObject();
        try {
            responseJSON.put("message", pException.getMessage());
            responseJSON.put("errorType", "technique");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        pException.printStackTrace();
        return getNoCacheResponseBuilder(pStatusCode).entity(responseJSON.toString()).build();
    }

    /**
     *
     * @return send a success response with the statuts code 200
     */
    protected Response wrapResponse() {
        return getNoCacheResponseBuilder(Response.Status.OK).build();
    }

    /**
     *
     * @param pResponseObject
     * @return the JSONObject given in parameter
     */
    protected Response wrapResponse(JSONObject pResponseObject) {
        return getNoCacheResponseBuilder(Response.Status.OK).entity(pResponseObject.toString()).build();
    }

    /**
     *
     * @param pResponseObject
     * @return the JSONObject given in parameter
     */
    protected <T extends JSONAble> Response wrapResponse(T pResponseObject) {
        try {
            return getNoCacheResponseBuilder(Response.Status.OK).entity(pResponseObject.toJSON().toString()).build();
        } catch (TechniqueException ex) {
            return internalError(ex);
        }
    }

    /**
     *
     * @param pObjects
     * @return an array containing the objects in the given List of JSONObjects
     */
    protected Response wrapResponse(List<JSONObject> pObjects) {
        return getNoCacheResponseBuilder(Response.Status.OK).entity(new JSONArray(pObjects).toString()).build();
    }

    /**
     *
     * @param pObjects
     * @return an array containing the objects in the given List of JSONAble
     *         objects
     */
    protected Response wrapJSONAbleResponse(List<? extends JSONAble> pObjects) {
        try {
            return getNoCacheResponseBuilder(Response.Status.OK).entity(new CustomJSONArray(pObjects).toString())
                    .build();
        } catch (TechniqueException ex) {
            return internalError(ex);
        }
    }

    protected <T extends JSONAble> Response wrapResponse(Map<? extends JSONAble, List<T>> pObjects) {
        JSONObject lMapJSON = new JSONObject();
        try {
            for (Map.Entry<? extends JSONAble, List<T>> entry : pObjects.entrySet()) {
                if (!lMapJSON.has(entry.getKey().getUniqueId())) {
                    lMapJSON.put(entry.getKey().getUniqueId(), new JSONObject());
                    ((JSONObject) lMapJSON.get(entry.getKey().getUniqueId())).put("key", entry.getKey().toJSON());
                    ((JSONObject) lMapJSON.get(entry.getKey().getUniqueId())).put("values", new JSONArray());
                }
                for (JSONAble lJSONAble : entry.getValue()) {
                    ((JSONArray) ((JSONObject) lMapJSON.get(entry.getKey().getUniqueId())).get("values"))
                            .put(lJSONAble.toJSON());
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
            return internalError();
        } catch (TechniqueException ex) {
            return internalError(ex);
        }
        return wrapResponse(lMapJSON);
    }

    protected Response wrapResponse(ResponseSupplierWrapper pSupplier) {
        try {
            return getNoCacheResponseBuilder(Response.Status.OK).entity(pSupplier.get()).build();
        } catch (final TechniqueException ex) {
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("message", ex.getMessage());
            JsonObject jsonObj = jsonObjBuilder.build();
            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonObj.toString()).build();
        }
    }

    protected Response wrapResponse(WrapEmptySupplerResponse pSupplier) {
        try {
            pSupplier.get();
            return getNoCacheResponseBuilder(Response.Status.OK).build();
        } catch (final TechniqueException ex) {
            JsonObjectBuilder jsonObjBuilder = Json.createObjectBuilder();
            jsonObjBuilder.add("message", ex.getMessage());
            JsonObject jsonObj = jsonObjBuilder.build();
            return getNoCacheResponseBuilder(Response.Status.UNAUTHORIZED).entity(jsonObj.toString()).build();
        }
    }

    protected Response.ResponseBuilder getNoCacheResponseBuilder(Response.Status status) {
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        cc.setMaxAge(-1);
        cc.setMustRevalidate(true);
        return Response.status(status).cacheControl(cc);
    }

}

