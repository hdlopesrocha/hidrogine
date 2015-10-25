package com.enter4ward.myserver.controllers;

import java.io.IOException;

import org.json.JSONException;

import com.enter4ward.myserver.Template;
import com.enter4ward.wavefront.MaterialLibrary;
import com.enter4ward.wavefront.WaveFront;
import com.enter4ward.webserver.Controller;
import com.enter4ward.webserver.HttpTools;
import com.enter4ward.webserver.Response;
import com.enter4ward.webserver.Upload;

/**
 * The Class FileUpload.
 */
public class ConvertController extends Controller {

	/** The Constant upload. */
	private static final String CONVERT = HttpTools.loadText("convert.html");
	
	

	@Override
	public void run() throws IOException {
		Response response = createResponse(Response.CODE_OK);	
		
		Upload file = getRequest().getUpload("file");

		if (file == null) {
			response.setContent(Template.getTemplate(CONVERT, Template.getNav(getRequest().getUrl())));
			send(response);
			return;
		}

		try {
			if ("obj".equals(file.getExtension())){
				response.setContent(new WaveFront(file.getFile()).toFile(),
						file.getName() + ".geo");
				send(response);
				return;
			}
			if ("mtl".equals(file.getExtension())){
				response.setContent(new MaterialLibrary(file.getFile()).toFile(),
						file.getName() + ".mat");
				send(response);
				return;
			}
			
		} catch (IOException | JSONException e) {
		
		}
		response.setContent("ERROR");
		send(response);
	}

}
