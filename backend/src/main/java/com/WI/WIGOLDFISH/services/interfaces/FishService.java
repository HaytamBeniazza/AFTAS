package com.WI.WIGOLDFISH.services.interfaces;

import com.WI.WIGOLDFISH.entities.fish.FishDtoReq;
import com.WI.WIGOLDFISH.entities.fish.FishDtoRes;
import com.WI.WIGOLDFISH.services.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FishService extends BaseService<FishDtoRes, FishDtoReq, String> {
    Page<FishDtoRes> findAllPaginated(Pageable pageable);
    void importAllFishSpecies();
    void createTestFish();
    void fetchImagesForAllFish();
}
