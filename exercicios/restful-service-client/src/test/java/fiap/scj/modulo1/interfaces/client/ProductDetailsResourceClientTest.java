package fiap.scj.modulo1.interfaces.client;

import feign.Response;
import fiap.scj.modulo1.ProductsClient;
import fiap.scj.modulo1.ProductsDetailsClient;
import fiap.scj.modulo1.domain.Product;
import fiap.scj.modulo1.domain.ProductDetails;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDetailsResourceClientTest {

    private ProductDetailsResourceClient details;
    static private ProductDetails mockDetails = new ProductDetails(99l, "Grampeador", "Grampeia até 20 folhas");
    private ProductResourceClient products;
    static private Product mockProduct = new Product(99l, "Grampeador", "Grampeia até 20 folhas", 19.90d);


    @Before
    public void setup() {

        details = ProductsDetailsClient.builder.build();
        products = ProductsClient.builder.build();

        Response response = products.create(mockProduct);
        String location = (String) response.headers().get("Location").toArray()[0];
        String[] aux = location.split("/");
        Long id = Long.valueOf(aux[aux.length - 1]);
        mockProduct.setId(id);
    }

    @Test
    public void should_1_create_with_success() throws Exception {
        Response response = details.create(mockProduct.getId(), mockDetails);
        String location = (String) response.headers().get("Location").toArray()[0];
        String[] aux = location.split("/");
        Long id = Long.valueOf(aux[aux.length - 1]);
        mockDetails.setId(id);
        assertNotNull(id);
    }


    @Test
    public void should_2_delete_with_success() throws Exception {
        Response response = details.delete(mockDetails.getId());
        assertEquals(202, response.status());
    }

    @After
    public void destroy() {
        products.delete(mockProduct.getId());
    }

}
