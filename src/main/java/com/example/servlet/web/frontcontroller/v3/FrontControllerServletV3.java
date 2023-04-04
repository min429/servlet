package com.example.servlet.web.frontcontroller.v3;

import com.example.servlet.web.frontcontroller.ModelView;
import com.example.servlet.web.frontcontroller.MyView;
import com.example.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import com.example.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import com.example.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// /front-controller/v3/* 경로로 들어오면 무조건 FrontController(문지기) 거쳐야 함
@WebServlet(name = "frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {

    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestURI = request.getRequestURI(); // 요청 URI

        ControllerV3 controller = controllerMap.get(requestURI); // URI에 해당하는 Controller 호출
        if(controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 페이지 없거나 못 찾을 때
            return;
        }

        // URL 파라미터 정보 생성
        Map<String, String> paramMap = createParamMap(request);
        
        // Model and View 정보
        ModelView mv = controller.process(paramMap);

        // View 경로 완성
        String viewName = mv.getViewName(); // 논리이름 new-form
        MyView view = viewResolver(viewName); // "/WEB-INF/views/new-form.jsp"

        // View 렌더링
        view.render(mv.getModel(), request, response);
    }

    private MyView viewResolver(String viewName) { // View 경로 완성
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest request) { // URL 파라미터 정보 생성
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName->paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
