package fiap.scj.modulo1.application;

import fiap.scj.modulo1.domain.ProductDetails;
import fiap.scj.modulo1.domain.repository.ProductDetailRepository;
import fiap.scj.modulo1.infrastructure.ProductServiceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static fiap.scj.modulo1.infrastructure.ProductServiceException.*;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class ProductDetailsServiceImpl implements ProductDetailsService {

    private final ProductDetailRepository repository;


    @Override
    public List<ProductDetails> search(Long productId) throws ProductServiceException {
        log.info("Searching detais for product={}", productId);
        try {

            if (Objects.isNull(productId)) {
                log.debug("No keyword specified, listing all products");
                throw new ProductServiceException(INVALID_PARAMETER_ERROR, null);
            }

            return repository.findAllByProductId(productId);
        } catch (Exception e) {
            log.error("Error searching product", e);
            throw new ProductServiceException(SEARCH_OPERATION_ERROR, e);
        }
    }

    @Override
    public ProductDetails create(ProductDetails entity) throws ProductServiceException {
        log.info("Creating product details ({})", entity);
        try {
            if (Objects.isNull(entity)) {
                log.error("Invalid product details");
                throw new ProductServiceException(INVALID_PARAMETER_ERROR, null);
            }
            return repository.save(entity);
        } catch (Exception e) {
            log.error("Error creating product details", e);
            throw new ProductServiceException(CREATE_OPERATION_ERROR, e);
        }
    }

    @Override
    public ProductDetails retrieve(Long id) throws ProductServiceException {
        log.info("Retrieving product for id={}", id);
        try {
            if (Objects.isNull(id)) {
                log.error("Invalid id");
                throw new ProductServiceException(INVALID_PARAMETER_ERROR, null);
            }
            return repository.findById(id)
                    .orElseThrow(() -> new ProductServiceException(RETRIEVE_OPERATION_ERROR, null));
        } catch (Exception e) {
            log.error("Error creating product", e);
            throw new ProductServiceException(RETRIEVE_OPERATION_ERROR, e);
        }
    }

    @Override
    public ProductDetails update(Long id, ProductDetails entity) throws ProductServiceException {
        log.info("Updating product ({}) for id={}", entity, id);
        try {
            if (Objects.isNull(id) || Objects.isNull(entity)) {
                log.error("Invalid details id or product");
                throw new ProductServiceException(INVALID_PARAMETER_ERROR, null);
            }
            if (!repository.existsById(id)) {
                log.debug("Product details not found for id={}", id);
                throw new ProductServiceException(PRODUCT_NOT_FOUND_ERROR, null);
            }
            return  repository.save(entity);
        } catch (Exception e) {
            log.error("Error creating product details", e);
            throw new ProductServiceException(RETRIEVE_OPERATION_ERROR, e);
        }
    }

    @Override
    public void delete(Long id) throws ProductServiceException {
        log.info("Deleting product details for id={}", id);
        try {
            if (Objects.isNull(id)) {
                log.error("Invalid id or product details");
                throw new ProductServiceException(INVALID_PARAMETER_ERROR, null);
            }
            if (!repository.existsById(id)) {
                log.debug("Product details not found for id={}", id);
                throw new ProductServiceException(PRODUCT_NOT_FOUND_ERROR, null);
            }
            repository.delete(this.retrieve(id));
        } catch (Exception e) {
            log.error("Error creating product details", e);
            throw new ProductServiceException(DELETE_OPERATION_ERROR, e);
        }
    }
}
