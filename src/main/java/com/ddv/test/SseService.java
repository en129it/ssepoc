package com.ddv.test;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ddv.test.model.Event;

import java.util.HashMap;

@Component
public class SseService {

    private final SseEmitter emitter = new SseEmitter();
    
    private HashMap<String, SseEmitter> sessionIdToEmitterMap = new HashMap<String, SseEmitter>(); 
    
    public SseEmitter getEmitter() {
        return emitter;
    }
    
    public SseEmitter getEmitter(String aSessionId) {
        SseEmitter rslt = sessionIdToEmitterMap.get(aSessionId);
        if (rslt==null) {
        	rslt = new SseEmitter();
        	sessionIdToEmitterMap.put(aSessionId, rslt);
        }
        return rslt;
    }
    
    public void emitEvent(Event anEvent) {
    	for (SseEmitter emitter : sessionIdToEmitterMap.values()) {
            try {
            	System.out.println("Emit " + anEvent);
            	emitter.send(anEvent, MediaType.APPLICATION_JSON);
            } catch (Exception e) {
            	emitter.completeWithError(e);
            }
    	}
    }
    

}