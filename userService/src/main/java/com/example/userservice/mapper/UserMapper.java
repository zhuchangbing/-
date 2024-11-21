package com.example.userservice.mapper;

import com.example.common.domain.entity.User;
import com.example.common.domain.vo.UserVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {
    //邮箱查找用户， 防止重复
    @Select({
            "select * from tb_user where email = #{email}"
    })
    public User selectUserByEmail(String email);

    @Select({
            "select * from tb_user",
            "where id = #{id}"
    })
    public User getUserById(Integer id);

    //  更新用户信息update  用户或者管理员修改
    @Update({
            "<script>",
            "update tb_user",
            "<set>",
            "<if test = 'nickName != null and nickName.length > 0'>nick_name = #{nickName},</if>",
            "<if test = 'header != null and header.length > 0'>header = #{header},</if>",
            "<if test = 'phone != null and phone.length > 0'>phone = #{phone},</if>",
            "<if test = 'idCard != null and idCard.length > 0'>id_card = #{idCard},</if>",
            "<if test = 'province != null and province.length > 0'>province = #{province},</if>",
            "<if test = 'city != null and city.length > 0'>city = #{city},</if>",
            "<if test = 'area != null and area.length > 0'>area = #{area},</if>",
            "<if test = 'detailAddr != null and detailAddr.length > 0'>detail_addr = #{detailAddr},</if>",
            "<if test = 'payPassword != null and payPassword.length > 0'>pay_password = #{payPassword},</if>",
            "<if test = 'password != null and password.length > 0'>password = #{password},</if>",
            "<if test = 'isBanned != null'>is_banned = #{isBanned},</if>",
            "<if test = 'account != null'>account = #{account},</if>",
            "</set>",
            "where id = #{id}",
            "</script>"
    })
    int updateUserInfo(User user);


    //注册时候添加用户  **注意生成一个 随机盐 **  不需要盐了 使用spring security的加密
    @Insert({
            "insert into tb_user(nick_name,header,email,password)",
            "values(#{nickName},#{header},#{email},#{password})",
    })
    int insertNewUser(User user);


    // 查询用户列表（管理员功能）  **分页查询管理** **条件查询**   ** userVo ** address
    @Select({
            "<script>",
            "select * from tb_user",
            "<where>",
            "<if test = 'nickName != null and nickName.length > 0'>and nick_name like concat('%',#{nickName},'%')</if>",
            "<if test = 'phone != null and phone.length > 0'>and phone like concat('%',#{phone},'%')</if>",
            "<if test = 'email != null and email.length > 0'>and email like concat('%',#{email},'%')</if>",
            "<if test = 'address != null and address.length > 0'>and concat(province,city,area,detail_addr) like concat('%',#{address},'%')</if>",
            "</where>",
            "</script>"
    })
    List<User> getUserByCondition(UserVo user);


}
