package com.WI.WIGOLDFISH.services.impl;

import com.WI.WIGOLDFISH.entities.member.DBUser;
import com.WI.WIGOLDFISH.entities.member.MemberDtoReq;
import com.WI.WIGOLDFISH.entities.member.MemberDtoRes;
import com.WI.WIGOLDFISH.exceptions.ResourceNotFound;
import com.WI.WIGOLDFISH.repositories.MemberRepository;
import com.WI.WIGOLDFISH.services.interfaces.MemberService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    @Override
    public MemberDtoReq save(MemberDtoReq dtoMini) {
        DBUser member = modelMapper.map(dtoMini, DBUser.class);
        member = memberRepository.save(member);
        return modelMapper.map(member, MemberDtoReq.class);
    }

    @Override
    public MemberDtoReq update(MemberDtoReq dtoMini, UUID aLong) {
        DBUser user = memberRepository.findById(aLong).orElseThrow(() -> new ResourceNotFound("Member not found"));
        user.setRole(dtoMini.getRole());
        user = memberRepository.save(user);
        return modelMapper.map(user, MemberDtoReq.class);
    }

    @Override
    public Boolean delete(UUID aLong) {
        memberRepository.findById(aLong).orElseThrow(() -> new ResourceNotFound("Member not found"));
        memberRepository.deleteById(aLong);
        if (memberRepository.findById(aLong).isPresent()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public MemberDtoRes findOne(UUID aLong) {
        DBUser member = memberRepository.findById(aLong).orElseThrow(() -> new ResourceNotFound("Member not found"));
        return modelMapper.map(member, MemberDtoRes.class);
    }

    @Override
    public List<MemberDtoRes> findAll() {
        return memberRepository.findAll().stream().map(member -> modelMapper.map(member, MemberDtoRes.class)).toList();
    }
}
