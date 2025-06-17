package com.WI.WIGOLDFISH.entities.fish;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FishDtoReq {
        @NotNull
      private  String  name;
        @NotNull
      private double averageWeight;
      private String imageUrl;
        @NotNull
      private Long level_id;
}
