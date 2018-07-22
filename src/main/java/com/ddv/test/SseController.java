package com.ddv.test;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
public class SseController {
	
    @Autowired
    private SseService sseService;
	
    @RequestMapping(path = "/sse/subscribe", method = RequestMethod.GET)
	public SseEmitter eventSubscribe(HttpServletRequest aRequest) {
    	String sessionId = aRequest.getSession().getId();
    	System.out.println("#### eventSubscribe " + sessionId);
		return sseService.getEmitter(sessionId); 
	}
	
}