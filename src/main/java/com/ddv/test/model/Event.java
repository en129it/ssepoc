package com.ddv.test.model;

public class Event {

	private String eventType;
	private String eventValue;
	
	public Event(String anEventType, String anEventValue) {
		eventType = anEventType;
		eventValue = anEventValue;
	}

	public String getEventType() {
		return eventType;
	}

	public String getEventValue() {
		return eventValue;
	}
	
	@Override
	public String toString() {
		return "Event (type=" + eventType + ", value=" + eventValue + ")";
	}
}
