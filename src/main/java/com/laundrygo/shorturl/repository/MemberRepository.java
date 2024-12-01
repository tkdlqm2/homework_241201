package com.laundrygo.shorturl.repository;

import com.laundrygo.shorturl.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author laundrygo
 * @version 0.1.0
 * @since 2021/06/22 7:06 오후
 */
@Mapper
public interface MemberRepository {
    List<Member> findAll();
}
