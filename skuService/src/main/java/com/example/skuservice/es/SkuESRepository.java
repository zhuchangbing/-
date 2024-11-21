package com.example.skuservice.es;

import com.example.common.domain.entity.Sku;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * 针对于Sku的ES的操作
 */
@Repository
public interface SkuESRepository
        // <实体类类型, 主键类型>
        extends ElasticsearchRepository<Sku, Integer> {

}
