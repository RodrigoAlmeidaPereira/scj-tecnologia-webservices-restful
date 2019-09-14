package fiap.scj.modulo1.domain.repository;

import fiap.scj.modulo1.domain.Product;
import fiap.scj.modulo1.domain.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetails, Long> {
    List<ProductDetails> findAllByProductId(Long productId);
}
