package org.apdoer.common.service.util;


import org.apdoer.common.service.code.ExceptionCode;
import org.apdoer.common.service.model.vo.ResultVo;


/**
 * @author apdoer
 */
public class ResultVoBuildUtils {
    private ResultVoBuildUtils() {
    }

    /**
     * 构造ResultVo 对象
     *
     * @param code 结果码值
     * @param msg  结果消息
     * @return ResultVo
     * 结果对象
     */
    public static ResultVo buildResultVo(int code, String msg) {
        return new ResultVo(code, msg, null);
    }

    /**
     * 构造ResultVo 对象
     *
     * @param code 结果码值
     * @param msg  结果消息
     * @param data 结果数据
     * @return ResultVo
     * 结果对象
     */
    public static ResultVo buildResultVo(int code, String msg, Object data) {
        return new ResultVo(code, msg, data);
    }

    /**
     * 构造success的ResultVo对象（org.apdoer.code=0,msg="success"）
     *
     * @param data 结果数据
     * @return ResultVo
     * 结果对象
     */
    public static ResultVo buildSuccessResultVo(Object data) {
        return new ResultVo(ExceptionCode.SUCCESS.getCode(), "success", data);
    }

    /**
     * 构造success的ResultVo对象（org.apdoer.code=0,msg="success"）
     *
     * @return ResultVo
     * 结果对象
     */
    public static ResultVo buildSuccessResultVo() {
        return new ResultVo(ExceptionCode.SUCCESS.getCode(), "success", null);
    }

    /**
     * 构造faild的ResultVo对象（org.apdoer.code=1,msg="faild"）
     *
     * @return ResultVo
     * 结果对象
     */
    public static ResultVo buildFaildResultVo() {
        return new ResultVo(ExceptionCode.FIAL.getCode(), "faild", null);
    }

    /**
     * 构造faild的ResultVo对象（org.apdoer.code=1,msg="faild"）
     *
     * @param data 结果数据
     * @return ResultVo
     * 结果对象
     */
    public static ResultVo buildFaildResultVo(Object data) {
        return new ResultVo(ExceptionCode.FIAL.getCode(), "faild", data);
    }

    /**
     * 构造faild的ResultVo对象（org.apdoer.code=1）
     *
     * @param msg
     * @return
     */
    public static ResultVo buildFaildResultVo(String msg) {
        return new ResultVo(ExceptionCode.FIAL.getCode(), msg, null);
    }
}
