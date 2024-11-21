package com.example.adminservice.mapper;

import com.example.common.domain.entity.Admin;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminMapper {
    @Select({
            "select * from tb_admin where email = #{email}"
    })
    Admin selectAdminByEmail(String email);

    @Insert({
            "insert into tb_admin",
            "(email,phone,nick_name,name,password,salt)",
            "values",
            "(#{email},#{phone},#{nickName},#{name},#{password},#{salt})"
    })
        /*@Options(useGeneratedKeys = true, keyColumn = "id",keyProperty = "id")*/
    int insert(Admin admin);

    /* 不能直接使用动态SQL 必须使用<script>把SQL包裹起来*/
    @Update({
            "<script>",
            "update tb_admin",
            "<set>",
            "<if test='password!=null and password.length>0'>password=#{password},</if>",
            "<if test='email!=null and email.length>0'>email=#{email},</if>",
            "<if test='phone!=null and phone.length>0'>phone=#{phone},</if>",
            "<if test='name!=null and name.length>0'>name=#{name},</if>",
            "<if test='password!=null and password.length>0'>password=#{password},</if>",
            "<if test='nickName!=null and nickName.length>0'>nick_name=#{nickName},</if>",
            "<if test='isBanned!=null'>is_banned=#{isBanned},</if>",
            "</set>",
            "where id = #{id}",
            "</script>",
    })
    int update(Admin admin);

    @Select({
            "select * from tb_admin",
            "where id = #{id}",
    })
    Admin selectAdminById(Integer id);

    @Select({
            "<script>",
            "select * from tb_admin",
            "<where>",
            "<if test='name!=null and name.length>0'>and name like concat('%', #{name}, '%')</if>",
            "<if test='email!=null and email.length>0'>and email like concat('%', #{email}, '%')</if>",
            "<if test='phone!=null and phone.length>0'>and phone like concat('%', #{phone}, '%')</if>",
            "<if test='nickName!=null and nickName.length>0'>and nick_name like concat('%', #{nickName}, '%')</if>",
            "<if test='isSuper!=null'>and is_super = #{isSuper}</if>",
            "<if test='isBanned!=null'>and is_banned = #{isBanned}</if>",
            "</where>",
            "order by create_time",
            "</script>",
    })
    List<Admin> select(Admin admin);

}










