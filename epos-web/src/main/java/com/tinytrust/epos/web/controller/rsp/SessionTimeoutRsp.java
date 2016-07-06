package com.tinytrust.epos.web.controller.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author owen
 * @date 2016-05-07 07:10:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionTimeoutRsp {

    private String status;

    private String message;
}
