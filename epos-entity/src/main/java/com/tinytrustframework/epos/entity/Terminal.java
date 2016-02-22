package com.tinytrustframework.epos.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_pos_terminal")
public class Terminal implements java.io.Serializable {

    private static final long serialVersionUID = 2483602362126430917L;

    // 终端编号
    @Column(name = "terminal_code", nullable = false, length = 32)
    private String terminalCode;

    // 用户编号
    @Id
    @Column(name = "user_code", nullable = false, length = 64)
    private String userCode;

    // 用户名称
    @Transient
    private String userName;

    // 手机号码
    @Transient
    private String cellphone;
}
