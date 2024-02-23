package com.WI.WIGOLDFISH.services.impl;

import com.WI.WIGOLDFISH.entities.competition.Competition;
import com.WI.WIGOLDFISH.entities.fish.Fish;
import com.WI.WIGOLDFISH.entities.hunting.Hunting;
import com.WI.WIGOLDFISH.entities.hunting.HuntingDtoReq;
import com.WI.WIGOLDFISH.entities.hunting.HuntingDtoRes;
import com.WI.WIGOLDFISH.entities.member.DBUser;
import com.WI.WIGOLDFISH.exceptions.ResourceNotFound;
import com.WI.WIGOLDFISH.ids.RankingId;
import com.WI.WIGOLDFISH.repositories.*;
import com.WI.WIGOLDFISH.services.interfaces.HuntingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HuntingServiceImpl implements HuntingService {
    private final CompetitionRepository competitionRepository;
    private final FishRepository fishRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final HuntingRepository huntingRepository;
    private  final RankingRepository rankingRepository;

    @Override
    public HuntingDtoReq save(HuntingDtoReq dtoMini) {
        Competition competition = competitionRepository.findById(dtoMini.getCompetition_id())
                .orElseThrow(() -> new ResourceNotFound("Competition not found"));
        Fish fish = fishRepository.findById(dtoMini.getFish_id())
                .orElseThrow(() -> new ResourceNotFound("Fish not found"));
        DBUser member = memberRepository.findById(dtoMini.getMember_id())
                .orElseThrow(() -> new ResourceNotFound("Member not found"));
        Optional<Hunting> optionalHunting = huntingRepository.findByMemberAndCompetitionAndFish(member, competition, fish);

        return optionalHunting.map(hunting -> updateExistingHunting(hunting, dtoMini, competition, fish, member)).orElseGet(() -> createNewHunting(dtoMini, competition, fish, member));
    }

    public HuntingDtoReq updateExistingHunting(Hunting hunting, HuntingDtoReq dtoMini,
                                               Competition competition, Fish fish, DBUser member) {
        hunting.setNumberOfFish(hunting.getNumberOfFish() + dtoMini.getNumberOfFish());
        hunting = huntingRepository.save(hunting);

        HuntingDtoReq huntingDtoReq = modelMapper.map(hunting, HuntingDtoReq.class);
        huntingDtoReq.setCompetition_id(competition.getCode());
        huntingDtoReq.setFish_id(fish.getName());
        huntingDtoReq.setMember_id(member.getId());

        updateRanking(huntingDtoReq, competition, fish, member);

        return huntingDtoReq;
    }

    public HuntingDtoReq createNewHunting(HuntingDtoReq dtoMini, Competition competition,
                                           Fish fish, DBUser member) {
        Hunting hunting = modelMapper.map(dtoMini, Hunting.class);
        hunting.setCompetition(new Competition(dtoMini.getCompetition_id()));
        hunting.setFish(new Fish(dtoMini.getFish_id()));
        hunting.setMember(new DBUser(dtoMini.getMember_id()));
        hunting = huntingRepository.save(hunting);
        HuntingDtoReq huntingDtoReq = modelMapper.map(hunting, HuntingDtoReq.class);
        huntingDtoReq.setCompetition_id(hunting.getCompetition().getCode());
        huntingDtoReq.setFish_id(hunting.getFish().getName());
        huntingDtoReq.setMember_id(hunting.getMember().getId());
        updateRanking(huntingDtoReq, competition, fish, member);
        return huntingDtoReq;
    }

    private void updateRanking(HuntingDtoReq huntingDtoReq, Competition competition, Fish fish, DBUser member) {
        RankingId rankingId = new RankingId();
        rankingId.setCompetition(competition);
        rankingId.setMember(member);
        rankingRepository.findById(rankingId).ifPresent(ranking -> {
            ranking.setScore(ranking.getScore() + (huntingDtoReq.getNumberOfFish() * fish.getLevel().getPoints()));
            rankingRepository.save(ranking);
        });
    }

    @Override
    public HuntingDtoReq update(HuntingDtoReq dtoMini, Long aLong) {
        competitionRepository.findById(dtoMini.getCompetition_id()).orElseThrow(() -> new ResourceNotFound("Competition not found"));
        fishRepository.findById(dtoMini.getFish_id()).orElseThrow(() -> new ResourceNotFound("Fish not found"));
        memberRepository.findById(dtoMini.getMember_id()).orElseThrow(() -> new ResourceNotFound("Member not found"));
        Hunting hunting = modelMapper.map(dtoMini, Hunting.class);
        hunting.setCompetition(new Competition(dtoMini.getCompetition_id()));
        hunting.setFish(new Fish(dtoMini.getFish_id()));
        hunting.setMember(new DBUser(dtoMini.getMember_id()));
        hunting.setId(aLong);
        hunting = huntingRepository.save(hunting);
        return modelMapper.map(hunting, HuntingDtoReq.class);
    }

    @Override
    public Boolean delete(Long aLong) {
        huntingRepository.findById(aLong).orElseThrow(() -> new ResourceNotFound("Hunting not found"));
        huntingRepository.deleteById(aLong);
        return huntingRepository.findById(aLong).isEmpty();
    }

    @Override
    public HuntingDtoRes findOne(Long aLong) {
        Hunting hunting = huntingRepository.findById(aLong).orElseThrow(() -> new ResourceNotFound("Hunting not found"));
        return modelMapper.map(hunting, HuntingDtoRes.class);
    }

    @Override
    public List<HuntingDtoRes> findAll() {
        return huntingRepository.findAll().stream().map(hunting -> modelMapper.map(hunting, HuntingDtoRes.class)).toList();
    }
}
