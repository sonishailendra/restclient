import java.util.HashMap;
import java.util.Map;

import com.americanexpress.sis.rest.model.JSONRequest;
import com.americanexpress.sis.rest.model.JSONResponse;
import com.americanexpress.sis.rest.restclient.RestClient;

public class TestRestClient {

    public static void main(String[] args) throws Exception {
        String restURL = "http://localhost:8080/RESTfulExample/json/product/postproduct";
        
        Product p = new Product();
       
        RestClient<Product> rc = new RestClient<Product>(restURL);
        Product productResponse = rc.postRest( p, Product.class, getHeaders());
        System.out.println("Product Information :- " + ps.toString());
    }
    
    public static Map<String,String> getHeaders(){
        Map<String,String> headers = new HashMap<String, String>();
        headers.put("accountid", "testrest");
        return headers;
    }

}
class Product implements JSONResponse, JSONRequest{

    private String name = "testing";
    private int qty = 111;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "Product [name=" + name + ", qty=" + qty + "]";
    }
}
