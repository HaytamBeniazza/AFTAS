package com.WI.WIGOLDFISH.entities.ranking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingIdDto {
    private Long member_id;
    private String competition_id;
}
