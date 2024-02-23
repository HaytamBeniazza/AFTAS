package com.WI.WIGOLDFISH.entities.ranking;

import com.WI.WIGOLDFISH.entities.member.MemberDtoReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingDtoForCompetition {
    private  int rank;
    private int score;
    private MemberDtoReq member;
}
