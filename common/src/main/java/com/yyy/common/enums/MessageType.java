package com.yyy.common.enums;

/**
 * 消息类别
 */
public enum MessageType {
    /**
     * 服务通知
     */
    SERVICE_NOTICE(0),
    /**
     * 系统通知
     */
    SYSTEM_NOTICE(1),
    /**
     * 支付消息
     */
    PAY_MESSAGE(2),
    /**
     * 提醒消息
     */
    REMINDER_MESSAGE(3),
    /**
     * 签约申请消息
     */
    SIGN_APPLY(5),
    /**
     * 服务申请消息
     */
    SERVICE_APPLY(6),
    /**
     * 预约申请消息
     */
    ORDER_APPLY(7),
    /**
     * 评价申请消息
     */
    COMMENT_APPLY(8),
    /**
     * 转机构申请消息
     */
    TRANSFER_APPLY(9),
    /**
     * 签约到期提醒消息
     */
    SIGN_APPLY_NOTICE(10);

    private Integer value;

    MessageType(Integer value) {
        this.value = value;
    }


    public Integer getValue() {
        return value;
    }
}
