package com.laundrygo.shorturl.service;

import com.laundrygo.shorturl.domain.Member;
import com.laundrygo.shorturl.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author laundrygo
 * @version 0.1.0
 * @since 2021/06/22 7:06 오후
 */
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
