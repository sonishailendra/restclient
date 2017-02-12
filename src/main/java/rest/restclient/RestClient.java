package rest.restclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import rest.exception.APIException;
import rest.model.JSONPojo;
import rest.model.JSONRequest;
import rest.model.JSONResponse;

/**
 * This class responsibility to call REST API and Response based on T.
 * 
 * @author Shailendra Soni
 *
 * @param <T> :- extends JSONResponse
 */
public class RestClient<T extends JSONResponse> {

    private String restURL;
    
    private int timeOut = 0; // in second

    public RestClient(String restURL) {
        this.restURL = restURL;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * Rest GET implementation
     * 
     * @param clazz
     * @param headers
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     * @throws APIException
     */
    public T getRest(Class<T> clazz, Map<String, String> headers)
            throws InvalidKeyException, NoSuchAlgorithmException, URISyntaxException, ClientProtocolException, IOException, APIException {

        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(this.restURL);
            setTimeOut(httpGet);
            setHeadder(httpGet, HttpGet.METHOD_NAME, headers);

            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK || httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                String result = getResponseInString(httpResponse);
                System.out.println("Response :- " + result);
                JSONConvertor<T> jsonConvertor1 = new JSONConvertor<T>();
                T t = jsonConvertor1.jsonToPojo(result, clazz);
                return t;
            } else {
                StringBuilder message = new StringBuilder("Status Code :- ");
                message.append(httpResponse.getStatusLine().getStatusCode());
                message.append(" Message :- ");
                message.append(getResponseInString(httpResponse));
                throw new APIException(message.toString());
            }
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }

        }

    }

    /**
     * This method server POST method of REST API.
     * 
     * @param requestObject
     *            - Convert POJO to JSON :- JSONRequest
     * @param class1
     * @param headers
     *            :- Map<String,String>
     * @return T :- Response JSON to POJO
     * @throws IOException
     * @throws APIException
     * @throws URISyntaxException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public T postRest(JSONRequest requestObject, Class<T> clazz, Map<String, String> headers)
            throws IOException, APIException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();

            JSONConvertor<JSONPojo> jsonConvertor = new JSONConvertor<JSONPojo>();
            StringEntity jsonRequest = new StringEntity(jsonConvertor.pojoToJson(requestObject));

            HttpPost httpPost = new HttpPost(this.restURL);
            setTimeOut(httpPost);
            setHeadder(httpPost, HttpPost.METHOD_NAME, headers);
            httpPost.setEntity(jsonRequest);

            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK || httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                String result = getResponseInString(httpResponse);
                JSONConvertor<T> jsonConvertor1 = new JSONConvertor<T>();
                T t = jsonConvertor1.jsonToPojo(result, clazz);
                return t;
            } else {
                StringBuilder message = new StringBuilder("Status Code :- ");
                message.append(httpResponse.getStatusLine().getStatusCode());
                message.append(" Message :- ");
                message.append(getResponseInString(httpResponse));
                throw new APIException(message.toString());
            }
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }

        }
    }

    private void setHeadder(HttpRequestBase httpRequest, String httpVerb, Map<String, String> headers)
            throws InvalidKeyException, NoSuchAlgorithmException, MalformedURLException, URISyntaxException {

        httpRequest.addHeader("content-type", ContentType.APPLICATION_JSON.getMimeType());
        if (headers != null && !headers.isEmpty()) {
            for (String keyHeader : headers.keySet()) {
                httpRequest.addHeader(keyHeader, headers.get(keyHeader));
            }
        }
    }

    private void setTimeOut(HttpRequestBase httpRequest) {
        if (this.timeOut > 0) {
            httpRequest.setConfig(getConfigurationForTimeOut());
        }
    }

    private String getResponseInString(CloseableHttpResponse httpResponse) throws IOException {
        HttpEntity entity = httpResponse.getEntity();
        String result = EntityUtils.toString(entity);
        return result;
    }

    private RequestConfig getConfigurationForTimeOut() {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(this.timeOut * 1000).setConnectionRequestTimeout(this.timeOut * 1000).setSocketTimeout(this.timeOut * 1000)
                .build();
        return config;
    }
}
