package fiap.scj.modulo1.interfaces.client;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;
import fiap.scj.modulo1.domain.Product;
import fiap.scj.modulo1.domain.ProductDetails;

import java.util.List;

@Headers({"Content-Type: application/json", "Accept: application/json"})
public interface ProductDetailsResourceClient {

    @RequestLine("GET /products/{productId}/details")
    List<ProductDetails> search(@Param("productId") Long productId);

    @RequestLine("POST /products/{productId}/details")
    Response create(@Param("productId") Long productId, ProductDetails productDetails);

    @RequestLine("GET /products/details/{id}")
    ProductDetails retrieve(@Param("id") Long id);

    @RequestLine("PUT /products/{productId}/details/{id}")
    ProductDetails update(@Param("productId") Long productId, @Param("id") Long id, ProductDetails productDetails);

    @RequestLine("DELETE /products/details/{id}")
    Response delete(@Param("id") Long id);

}
