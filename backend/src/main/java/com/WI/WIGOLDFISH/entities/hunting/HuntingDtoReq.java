package com.WI.WIGOLDFISH.entities.hunting;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HuntingDtoReq {
     private  Long id;
     @NotNull
     private int numberOfFish;
     @NotNull
     private String fish_id;
     @NotNull
     private UUID member_id;
     @NotNull
     private String competition_id;
}
