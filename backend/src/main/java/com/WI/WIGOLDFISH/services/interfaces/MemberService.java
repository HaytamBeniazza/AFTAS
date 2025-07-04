package com.WI.WIGOLDFISH.services.interfaces;

import com.WI.WIGOLDFISH.entities.member.MemberDtoReq;
import com.WI.WIGOLDFISH.entities.member.MemberDtoRes;
import com.WI.WIGOLDFISH.enums.Role;
import com.WI.WIGOLDFISH.services.BaseService;

import java.util.List;
import java.util.UUID;

public interface MemberService extends BaseService<MemberDtoRes, MemberDtoReq, UUID> {
    List<MemberDtoRes> findPendingMembers();
    void approveMember(UUID id, Role role);
}
