package fiap.scj.modulo1.interfaces;

import fiap.scj.modulo1.application.ProductDetailsService;
import fiap.scj.modulo1.application.ProductService;
import fiap.scj.modulo1.domain.Product;
import fiap.scj.modulo1.domain.ProductDetails;
import fiap.scj.modulo1.infrastructure.ProductServiceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static fiap.scj.modulo1.infrastructure.ProductServiceException.*;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
@Slf4j
public class ProductDetailsResource {

    private final ProductService productService;
    private final ProductDetailsService service;


    @GetMapping(path = "{productId}/details")
    @ResponseStatus(code = HttpStatus.OK)
    public List<ProductDetails> search(@PathVariable Long productId) {
        log.info("Processing search request");
        try {

            return service.search(productId);
        } catch (ProductServiceException e) {
            log.error("Error processing search request", e);
            throw exceptionHandler(e);
        }
    }


    @PostMapping("{productId}/details")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Void> create(
            @PathVariable Long productId,
            @RequestBody ProductDetails productDetails) throws ProductServiceException {
        log.info("Processing create request");
        try {
            Product product = productService.retrieve(productId);

            productDetails.setProduct(product);
            ProductDetails result = service.create(productDetails);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                    "/{id}").buildAndExpand(result.getId()).toUri();
            return ResponseEntity.created(location).build();
        } catch (ProductServiceException e) {
            log.error("Error processing create request", e);
            throw exceptionHandler(e);
        }
    }


    @GetMapping("/details/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ProductDetails retrieve(@PathVariable Long id) throws ProductServiceException {
        log.info("Processing retrieve request");
        try {
            return service.retrieve(id);
        } catch (ProductServiceException e) {
            log.error("Error processing retrieve request", e);
            throw exceptionHandler(e);
        }
    }


    @PostMapping("{productId}/details/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ProductDetails update(
            @PathVariable Long id,
            @PathVariable Long productId,
            @RequestBody ProductDetails productDetails) throws ProductServiceException {
        log.info("Processing update request");
        try {
            productDetails.setProduct(productService.retrieve(productId));
            return service.update(id, productDetails);
        } catch (ProductServiceException e) {
            log.error("Error processing update request", e);
            throw exceptionHandler(e);
        }
    }


    @DeleteMapping("/details/{id}")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void delete(@PathVariable Long id) throws ProductServiceException {
        log.info("Processing delete request");
        try {
            service.delete(id);
        } catch (ProductServiceException e) {
            log.error("Error processing delete request", e);
            throw exceptionHandler(e);
        }
    }

    private ResponseStatusException exceptionHandler(ProductServiceException e) {
        if (e.getOperation() == null || e.getOperation().isEmpty()) {
            return new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (SEARCH_OPERATION_ERROR.equals(e.getOperation())
                || CREATE_OPERATION_ERROR.equals(e.getOperation())
                || RETRIEVE_OPERATION_ERROR.equals(e.getOperation())
                || UPDATE_OPERATION_ERROR.equals(e.getOperation())
                || DELETE_OPERATION_ERROR.equals(e.getOperation())) {
            return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (PRODUCT_NOT_FOUND_ERROR.equals(e.getOperation())) {
            return new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }


}
