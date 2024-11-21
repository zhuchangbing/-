package com.example.userservice.mapper;

import com.example.common.domain.entity.Address;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressMapper {

    // 条件查询地址
    @Select({
            "select * from tb_address",
            "where user_id = #{userId}",
            "and province = #{province}",
            "and city = #{city}",
            "and area = #{area}",
            "and detail_addr = #{detailAddr}",
    })
    Address getAddress(Address address);

    // 地址 ID 查询地址
    @Select({
            "select * from tb_address where id = #{id}"
    })
    Address getAddressById(Integer id);

    // 用户添加地址
    @Insert({
            "insert into tb_address(contact,phone,user_id,province,city,area,detail_addr)",
            "values(#{contact},#{phone},#{userId},#{province},#{city},#{area},#{detailAddr})"
    })
    int insertNewAddress(Address address);


    // 删除用户某个地址
    @Delete({
            "delete from tb_address",
            "where id = #{id}"
    })
    int deleteUselessAddress(Integer id);


    // 查找用户的所有地址
    @Select({
            "select * from tb_address",
            "where user_id = #{userId}",
            "order by is_selected desc"
    })
    List<Address> selectUserAddress(Integer userId);

    // 用户编辑地址信息
    @Update({
            "<script>",
            "update tb_address",
            "<set>",
            "<if test = 'contact != null and contact.length > 0'>contact = #{contact},</if>",
            "<if test = 'phone != null and phone.length > 0'>phone = #{phone},</if>",
            "<if test = 'province != null and province.length > 0'>province = #{province},</if>",
            "<if test = 'city != null and city.length > 0'>city = #{city},</if>",
            "<if test = 'area != null and area.length > 0'>area = #{area},</if>",
            "<if test = 'detailAddr != null and detailAddr.length > 0'>detail_addr = #{detailAddr},</if>",
            "</set>",
            "where id = #{id}",
            "</script>"
    })
    int updateUserAddress(Address address);    //根据地址的Id修改地址信息


    // 设置默认地址
    @Update({
            "update tb_address",
            "set is_selected = #{isSelected}",
            "where id = #{id}"
    })
    int updateUserDefaultAddress(Integer id, Integer isSelected);

    // 查询默认地址 id
    @Select({
            "select id from tb_address",
            "where is_selected = 1"
    })
    int selectDefaultAddressId();

}
