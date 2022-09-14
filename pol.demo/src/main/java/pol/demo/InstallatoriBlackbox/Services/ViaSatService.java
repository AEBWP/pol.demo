package pol.demo.InstallatoriBlackbox.Services;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pol.demo.InstallatoriBlackbox.Models.Request;
import pol.demo.InstallatoriBlackbox.Models.Response;

import java.io.IOException;


@Service
public class ViaSatService {


    public Response GetInstallatoriViasat(Request req) throws IOException {
        String endpoint = "http://www.soscall.it/installatori/RESTService/Installatori/FromPos";
        String body = "";
        endpoint += "?lat=" + "45.6523748";
        endpoint += "&lon=" + "13.7486426";
        endpoint += "&radius=" + "5";

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            //HTTP GET method
            HttpGet httpget = new HttpGet(endpoint);
            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
            };
            String responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
            System.out.println(responseBody);
            body = responseBody;
        }


        Response r = new Response();

        r.Body = body;

        return r;
    }
}
