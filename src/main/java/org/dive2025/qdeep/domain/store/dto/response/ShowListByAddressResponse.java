package org.dive2025.qdeep.domain.store.dto.response;

import org.dive2025.qdeep.domain.store.entity.Store;

import java.util.List;

public record ShowListByAddressResponse(List<Store> storeList) {
}
