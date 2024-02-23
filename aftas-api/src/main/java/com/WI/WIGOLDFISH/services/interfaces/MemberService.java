package com.WI.WIGOLDFISH.services.interfaces;

import com.WI.WIGOLDFISH.entities.member.MemberDtoReq;
import com.WI.WIGOLDFISH.entities.member.MemberDtoRes;
import com.WI.WIGOLDFISH.services.BaseService;

import java.util.UUID;

public interface MemberService extends BaseService<MemberDtoRes, MemberDtoReq, UUID> {
}
