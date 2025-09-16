package org.dive2025.qdeep.domain.store.dto.response;

public record SavedStoreResponse (String name,
                                 String address,
                                 String hours,
                                 String description,
                                  double latitude,
                                  double longtitude){
}
