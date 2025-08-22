package org.dive2025.qdeep.domain.store.service;

import org.dive2025.qdeep.domain.recommend.dto.response.RecommendationResponse;
import org.dive2025.qdeep.domain.recommend.dto.response.RecommendationSaveResponse;
import org.dive2025.qdeep.domain.recommend.service.GptService;
import org.dive2025.qdeep.domain.store.dto.response.SavedStoreResponse;
import org.dive2025.qdeep.domain.store.entity.Store;
import org.dive2025.qdeep.domain.store.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Transactional
    public List<SavedStoreResponse> saveAllResponsesBatch(List<RecommendationResponse> responses) {
        return responses.stream()
                .map(response -> {
                    Store store = Store.builder()
                            .name(response.name())
                            .address(response.address())
                            .hours(response.hours())
                            .description(response.description())
                            .latitude(response.latitude())
                            .longtitude(response.longitude())
                            .build();
                    checkDuplicationSaving(store); // 이미 등록된건지 체크하는 메소드

                    // DTO로 반환
                    return new SavedStoreResponse(
                            store.getName(),
                            store.getAddress(),
                            store.getHours(),
                            store.getDescription()
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Store checkDuplicationSaving(Store store){

        if(storeRepository.existsByNameAndAddress(store.getName()
                ,store.getAddress())){

            return store;
        }
        return storeRepository.save(store);
    }

}
