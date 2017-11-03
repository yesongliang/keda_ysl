package com.kedacom.ysl.rabbitmq.sender;

import com.kedacom.ysl.util.DateUtil;

public class MessageBody {

    // 推送来源
    private String messageSource;
    // 推送目的
    private String messageTarget;
    // 推送时间戳
    private String messageTimestamp;
    // 推送类别
    private String messageType;
    // 推送信息
    private Object messageData;

    public MessageBody(String messageSource, String messageTarget, String messageType, Object data) {
	this.messageSource = messageSource;
	this.messageTarget = messageTarget;
	this.messageTimestamp = DateUtil.getCurrentDate(DateUtil.YYYY_MM_DD_HHMMSS);
	this.messageType = messageType;
	this.messageData = data;
    }

    public MessageBody() {
    }

    public String getMessageSource() {
	return messageSource;
    }

    public void setMessageSource(String messageSource) {
	this.messageSource = messageSource;
    }

    public String getMessageTarget() {
	return messageTarget;
    }

    public void setMessageTarget(String messageTarget) {
	this.messageTarget = messageTarget;
    }

    public String getMessageTimestamp() {
	return messageTimestamp;
    }

    public void setMessageTimestamp(String messageTimestamp) {
	this.messageTimestamp = messageTimestamp;
    }

    public Object getMessageData() {
	return messageData;
    }

    public void setMessageData(Object messageData) {
	this.messageData = messageData;
    }

    public String getMessageType() {
	return messageType;
    }

    public void setMessageType(String messageType) {
	this.messageType = messageType;
    }

    @Override
    public String toString() {
	return "MessageBody [messageSource=" + messageSource + ", messageTarget=" + messageTarget + ", messageTimestamp=" + messageTimestamp + ", messageType=" + messageType + ", messageData="
		+ messageData + "]";
    }

}
