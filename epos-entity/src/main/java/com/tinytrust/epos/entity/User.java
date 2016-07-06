package com.tinytrust.epos.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * <一句话功能简述>
 *
 * @author Owen
 * @version [版本号, 2013-5-19]
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_pos_user")
public class User implements Serializable {
    private static final long serialVersionUID = -2461513049518820093L;

    // 用户编号
    @Id
    @GeneratedValue(generator = "userCodeGenerator")
    @GenericGenerator(name = "userCodeGenerator",
            strategy = "com.tinytrust.epos.id.UserIdGenerator",
            parameters = {
                    @Parameter(name = "table_name", value = "t_pos_primary_key"),
                    @Parameter(name = "segment_column_name", value = "pk_generator_name"),
                    @Parameter(name = "segment_value", value = "PK_USER_CODE"),
                    @Parameter(name = "value_column_name", value = "pk_generator_value"),
                    @Parameter(name = "initialValue", value = "10000"),
                    @Parameter(name = "allocationSize", value = "1")})
    @Column(name = "user_code")
    private String userCode;

    // 手机号
    @Column(name = "cellphone")
    private String cellphone;

    // 密码
    @Column(name = "password")
    private String password;

    // 用户姓名
    @Column(name = "user_name")
    private String userName;

    // 身份证号
    @Column(name = "id_code")
    private String idCode;

    // 角色编号
    // 1:管理员  2:一级经销商 3:二级经销商
    @Column(name = "role_code")
    private int roleCode;

    // 到账类型
    // 1：T+0 2：T+1
    @Column(name = "tranfer_type")
    private int tranferType;

    // 上级编号
    @Column(name = "top_user_code")
    private String topUserCode;

    // 外部用户编号
    @Column(name = "outter_user_code")
    private String outterUserCode;

    // 状态 1:待审核 2:正常状态 3:停用状态
    @Column(name = "status")
    private int status;

    // 最近一次登录时间
    @Column(name = "last_time")
    private Date lastTime;

    // 最近一次登录IP
    @Column(name = "last_ip")
    private String lastIp;
}
