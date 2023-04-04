package com.example.servlet.web.frontcontroller.v4;

import java.util.Map;

public interface ControllerV4 {

    String process(Map<String, String> paraMap, Map<String, Object> model); // return view Name
}
