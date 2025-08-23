package org.dive2025.qdeep.domain.store.service;

import org.dive2025.qdeep.common.exception.CustomException;
import org.dive2025.qdeep.common.exception.ErrorCode;
import org.dive2025.qdeep.domain.recommend.dto.response.RecommendationResponse;
import org.dive2025.qdeep.domain.store.dto.request.DeleteStoreRequest;
import org.dive2025.qdeep.domain.store.dto.request.SaveStoreRequest;
import org.dive2025.qdeep.domain.store.dto.request.StoredInformationRequest;
import org.dive2025.qdeep.domain.store.dto.response.DeleteStoreResponse;
import org.dive2025.qdeep.domain.store.dto.response.SavedStoreResponse;
import org.dive2025.qdeep.domain.store.dto.response.ShowStoreResponse;
import org.dive2025.qdeep.domain.store.entity.Store;
import org.dive2025.qdeep.domain.store.repository.StoreRepository;
import org.dive2025.qdeep.domain.user.entity.User;
import org.dive2025.qdeep.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

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


    public Store checkDuplicationSaving(Store store){

        if(storeRepository.existsByNameAndAddress(store.getName()
                ,store.getAddress())){

            return store;
        }
        return storeRepository.save(store);
    }

    @Transactional(readOnly = true)
    public ShowStoreResponse showStore(StoredInformationRequest storeInformationRequest){
        Store store = storeRepository.findById(storeInformationRequest.storeId())
                .orElseThrow(()->new CustomException(ErrorCode.STORE_NOT_FOUND));

        return new ShowStoreResponse(store.getName(),
                store.getAddress(),
                store.getHours(),
                store.getDescription());
    }

    @Transactional
    public ShowStoreResponse saveStore(SaveStoreRequest saveStoreRequest,String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        Store store = storeRepository.findById(saveStoreRequest.storeId())
                .orElseThrow(()->new CustomException(ErrorCode.STORE_NOT_FOUND));

        user.addStore(store);
        userRepository.save(user);

        return new ShowStoreResponse(store.getName(),
                store.getAddress(),
                store.getHours(),
                store.getDescription());


    }

    @Transactional
    public DeleteStoreResponse deleteSavedStore(DeleteStoreRequest deleteStoreRequest,String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));

        Store store = storeRepository.findById(deleteStoreRequest.storeId())
                .orElseThrow(()->new CustomException(ErrorCode.STORE_NOT_FOUND));

        user.removeStore(store);
        userRepository.save(user);

        return new DeleteStoreResponse(deleteStoreRequest.storeId());

    }

    public List<Store> findStoreByAddress(String addressPart){
        List<Store> stores = storeRepository.findByAddressContaining(addressPart);
        return stores;
    }



}
