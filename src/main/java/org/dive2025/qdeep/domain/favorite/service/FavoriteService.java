package org.dive2025.qdeep.domain.favorite.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.dive2025.qdeep.common.exception.CustomException;
import org.dive2025.qdeep.common.exception.ErrorCode;
import org.dive2025.qdeep.domain.favorite.dto.request.AddFavoriteRequest;
import org.dive2025.qdeep.domain.favorite.dto.request.DeleteFavoriteRequest;
import org.dive2025.qdeep.domain.favorite.dto.response.AddFavoriteResponse;
import org.dive2025.qdeep.domain.favorite.dto.response.DeleteFavoriteResponse;
import org.dive2025.qdeep.domain.favorite.entity.Favorite;
import org.dive2025.qdeep.domain.favorite.repository.FavoriteRepository;
import org.dive2025.qdeep.domain.store.entity.Store;
import org.dive2025.qdeep.domain.store.repository.StoreRepository;
import org.dive2025.qdeep.domain.user.entity.User;
import org.dive2025.qdeep.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreRepository storeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public AddFavoriteResponse addFavorite(AddFavoriteRequest addFavoriteRequest){


        User user = userRepository
                .getUserByfindByIdOnly(addFavoriteRequest.userId(),
                        entityManager);

        Store store = storeRepository
                .getStoreByIdOnly(addFavoriteRequest.storeId(),
                        entityManager);

        Favorite favorite = Favorite.builder()
                .user(user)
                .store(store)
                .addTime(LocalDateTime.now())
                .build();

        user.addFavorite(favorite);
        store.addFavorite(favorite);
        favoriteRepository.save(favorite);

        return new AddFavoriteResponse(user.getId(),
                store.getId());

    }

    @Transactional
    public DeleteFavoriteResponse deleteFavorite(DeleteFavoriteRequest deleteFavoriteRequest){


        Favorite favorite = favoriteRepository
                .getFavoriteByFindByUser(deleteFavoriteRequest.userId(),
                        deleteFavoriteRequest.storeId(),
                        entityManager);

        User user = userRepository
                .getUserByfindByIdOnly(deleteFavoriteRequest.userId(),entityManager);

        Store store = storeRepository
                .getStoreByIdOnly(deleteFavoriteRequest.storeId(),
                        entityManager);


        // oneToMany를 가지는 객체에서 컬렉션의 요소 제거해주기 -> 영속성 관리
        user.deleteFavorite(favorite);
        store.deleteFavorite(favorite);

        // 리포지토리에서 객체 삭제하기
        favoriteRepository.delete(favorite);

        return new DeleteFavoriteResponse(user.getUsername(),store.getName());
    }
}
