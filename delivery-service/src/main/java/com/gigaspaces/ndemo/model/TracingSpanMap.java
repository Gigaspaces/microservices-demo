package com.gigaspaces.ndemo.model;

import io.opentracing.Span;

import java.util.concurrent.ConcurrentHashMap;

public class TracingSpanMap {

    private final ConcurrentHashMap<String, Span> map = new ConcurrentHashMap<>();

    public TracingSpanMap() {

    }

    public void put(String orderId, Span activeSpan) {
        map.put(orderId, activeSpan);
    }

    public Span get(String orderId) {
        return map.get(orderId);
    }

    public void remove(String orderId){
        map.remove(orderId);
    }


}
