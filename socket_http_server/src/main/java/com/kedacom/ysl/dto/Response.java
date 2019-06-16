package com.kedacom.ysl.dto;

import java.io.OutputStream;
import java.io.PrintWriter;

import com.kedacom.ysl.utils.JsonUtil;

import lombok.Data;

@Data
public class Response {

	private PrintWriter writer;

	public Response(OutputStream outputStream) {
		writer = new PrintWriter(outputStream);
	}

	public void writeJson(int code, String message, Object data) {
		writer.println("HTTP/1.1 " + code + " " + message);
		writer.println("Content-type:application/json; charset=utf-8");
		writer.println();
		if (data != null) {
			writer.println(JsonUtil.toJson(data));
		}
		writer.flush();
	}

	public void writeHtml(int code, String message, String describe) {
		writer.println("HTTP/1.1 " + code + " " + message);
		writer.println("Content-type:text/html; charset=utf-8");
		writer.println();
		writer.println("<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">");
		writer.println("<html><head>");
		writer.println("<title>" + code + " " + message + "</title>");
		writer.println("</head><body>");
		writer.println("<h1>" + message + "</h1>");
		writer.println("<p>" + describe + "</p>");
		writer.println("<hr>");
		writer.println("<address>ysl Server at kedacom</address>");
		writer.println("</body></html>");
		writer.flush();
	}
}
