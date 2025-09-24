package org.dive2025.qdeep.domain.favorite.dto.response;

import org.dive2025.qdeep.domain.favorite.entity.Favorite;
import org.dive2025.qdeep.domain.store.entity.Store;

import java.util.List;

public record ShowFavoriteResponse(List<Store> favoriteList) {
}
