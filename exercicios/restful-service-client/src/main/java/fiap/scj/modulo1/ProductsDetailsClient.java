package fiap.scj.modulo1;

import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import fiap.scj.modulo1.interfaces.client.ProductDetailsResourceClient;
import lombok.Data;

public class ProductsDetailsClient {

    public static final ProductDetailsClientBuilder builder = new ProductDetailsClientBuilder();

    @Data
    public static class ProductDetailsClientBuilder {

        public static String DEFAULT_URL = "http://localhost:8080";
        private String url = DEFAULT_URL;

        ProductDetailsClientBuilder() {
        }

        public ProductDetailsClientBuilder setUrl(String url) {
            this.url = url;
            return this;
        }

        public ProductDetailsResourceClient build() {
            return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.FULL)
                .target(ProductDetailsResourceClient.class, this.url);
        }

    }

}
