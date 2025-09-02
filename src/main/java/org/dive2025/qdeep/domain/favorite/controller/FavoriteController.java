package org.dive2025.qdeep.domain.favorite.controller;

import org.dive2025.qdeep.domain.favorite.dto.request.AddFavoriteRequest;
import org.dive2025.qdeep.domain.favorite.dto.request.DeleteFavoriteRequest;
import org.dive2025.qdeep.domain.favorite.dto.response.DeleteFavoriteResponse;
import org.dive2025.qdeep.domain.favorite.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    // 찜하기 기능
    @PostMapping("/add")
    public ResponseEntity<?> addFavorite(@RequestBody AddFavoriteRequest addFavoriteRequest){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(favoriteService.addFavorite(addFavoriteRequest));

    }

    // 찜 삭제 기능
    @DeleteMapping("/delete")
    public ResponseEntity<DeleteFavoriteResponse> deleteFavorite(@RequestBody DeleteFavoriteRequest deleteFavoriteRequest){

        return ResponseEntity.status(HttpStatus.OK)
                .body(favoriteService.deleteFavorite(deleteFavoriteRequest));
    }


}
