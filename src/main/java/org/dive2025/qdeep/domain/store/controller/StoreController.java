package org.dive2025.qdeep.domain.store.controller;


import org.dive2025.qdeep.common.security.auth.UserDetailsImpl;
import org.dive2025.qdeep.domain.store.dto.request.DeleteStoreRequest;
import org.dive2025.qdeep.domain.store.dto.request.SaveStoreRequest;
import org.dive2025.qdeep.domain.store.dto.request.StoredInformationRequest;
import org.dive2025.qdeep.domain.store.dto.response.DeleteStoreResponse;
import org.dive2025.qdeep.domain.store.dto.response.ShowListByAddressResponse;
import org.dive2025.qdeep.domain.store.dto.response.ShowStoreResponse;
import org.dive2025.qdeep.domain.store.repository.StoreRepository;
import org.dive2025.qdeep.domain.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private StoreService storeService;

    // 가게를 찜하는 메소드
    @PostMapping("/save")
    public ResponseEntity<?> saveStore(@RequestBody SaveStoreRequest saveStoreRequest,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails){

        return ResponseEntity.status(HttpStatus.OK)
                .body(storeService.saveStore(saveStoreRequest,
                        userDetails.getUsername()));

    }

    // 찜한 가게를 삭제하는 메소드
    @DeleteMapping("/delete")
    public ResponseEntity<DeleteStoreResponse> deleteSavedStore(@RequestBody DeleteStoreRequest deleteStoreRequest,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails){

        return ResponseEntity.status(HttpStatus.OK)
                .body(storeService.deleteSavedStore(deleteStoreRequest,
                        userDetails.getUsername()));
    }

    // 가게 상세 페이지
    @GetMapping("/load")
    public ResponseEntity<ShowStoreResponse> showStore(@RequestBody StoredInformationRequest storedInformationRequest){

        return ResponseEntity.status(HttpStatus.OK)
                .body(storeService
                        .showStore(storedInformationRequest));

    }

    // 가게 리스트
    @GetMapping("/list")
    public ResponseEntity<ShowListByAddressResponse> showListByAddressPart(@RequestParam(name="addressPart") String addressPart){
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ShowListByAddressResponse(storeService
                        .findStoreByAddress(addressPart)));
    }

}
