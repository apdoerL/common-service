package org.apdoer.common.service.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author apdoer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResultVo {

    private Integer code;

    private String msg;

    private Object data;

}
