package com.enter4ward.myserver.controllers;

import java.io.IOException;

import com.enter4ward.webserver.Response;

/**
 * The Class FileUpload.
 */
public class IndexController extends Template {


	private static final String IMAGE = "<div><img src='res/test.png'></div>";

	@Override
	public void run() throws IOException {
		Response response = createResponse(Response.CODE_OK);	
		response.setContent(getTemplate(IMAGE, getNav(getRequest().getUrl())));	
		send(response);
	}

}
