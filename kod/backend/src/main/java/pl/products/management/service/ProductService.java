package pl.products.management.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.products.management.model.entity.ProductCategoryEntity;
import pl.products.management.model.entity.ProductEntity;
import pl.products.management.repository.ProductRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<ProductEntity> findPage(Pageable pageable){

        pageable = handlePagination(pageable);

        return productRepository.findAll(pageable);
    }

    public Page<ProductEntity> findPageByCategory(Pageable pageable, String categoryName){

        pageable = handlePagination(pageable);

        return productRepository.findAllByProductCategory_NameIgnoreCase(pageable, categoryName);
    }

    private Pageable handlePagination(Pageable pageable){

        int page = DEFAULT_PAGE;
        int pageSize = DEFAULT_PAGE_SIZE;

        if(pageable != null){

            pageSize = handleInputPageSize(pageable.getPageSize());
            page = handleInputPage(pageable.getPageNumber());
        }

        return PageRequest.of(page, pageSize);
    }

    private int handleInputPageSize(int pageSize){

        if(pageSize <= 0){

            return DEFAULT_PAGE_SIZE;
        }

        if(pageSize > MAX_PAGE_SIZE){

            return MAX_PAGE_SIZE;
        }

        return pageSize;
    }

    private int handleInputPage(int page){

        if(page <= 0){

            return DEFAULT_PAGE;
        }

        return page;
    }

    public ProductEntity getById(UUID id) throws EntityNotFoundException{

        return productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Nie odnaleziono produktu o id " + id));
    }

    @Transactional
    public ProductEntity create(ProductEntity toCreateData) throws EntityExistsException, EntityNotFoundException{

        String name = toCreateData.getName();

        if(productRepository.existsByNameIgnoreCase(name)){

            throw new EntityExistsException("Istnieje już produkt o nazwie " + name);
        }

        updateCategory(toCreateData, toCreateData.getProductCategory());

        return productRepository.save(toCreateData);
    }

    @Transactional
    public ProductEntity updateById(UUID id, ProductEntity newData)
        throws EntityNotFoundException, EntityExistsException
    {
        ProductEntity foundProduct = getById(id);

        updateName(foundProduct, newData.getName());
        updateCategory(foundProduct, newData.getProductCategory());
        foundProduct.setPrice(newData.getPrice());
        foundProduct.setDescription(newData.getDescription());

        return foundProduct;
    }

    private void updateName(ProductEntity foundProduct, String newName) throws EntityExistsException{

        String oldName = foundProduct.getName();

        if(oldName.equals(newName)){
            return;
        }

        if(productRepository.existsByNameIgnoreCase(newName)){

            throw new EntityExistsException("Istnieje już produkt o nazwie " + newName);
        }

        foundProduct.setName(newName);
    }

    private void updateCategory(ProductEntity foundProduct, ProductCategoryEntity newCategory) throws EntityExistsException{

        ProductCategoryEntity productCategory = null;

        if(newCategory != null){

            productCategory = productCategoryService.getById(newCategory.getId());
        }

        foundProduct.setProductCategory(productCategory);
    }

    @Transactional
    public void deleteById(UUID id) throws EntityNotFoundException{

        if(!productRepository.existsById(id)){

            throw new EntityNotFoundException("Nie odnaleziono produktu o id " + id);
        }

        productRepository.deleteById(id);
    }
}
