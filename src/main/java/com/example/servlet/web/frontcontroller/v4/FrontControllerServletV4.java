package com.example.servlet.web.frontcontroller.v4;

import com.example.servlet.web.frontcontroller.ModelView;
import com.example.servlet.web.frontcontroller.MyView;
import com.example.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import com.example.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import com.example.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// /front-controller/v4/* 경로로 들어오면 무조건 FrontController(문지기) 거쳐야 함
@WebServlet(name = "frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {

    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestURI = request.getRequestURI(); // 요청 URI

        ControllerV4 controller = controllerMap.get(requestURI); // URI에 해당하는 Controller 호출
        if(controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 페이지 없거나 못 찾을 때
            return;
        }

        // URL 파라미터 정보 생성
        Map<String, String> paramMap = createParamMap(request);

        // 모델 생성
        Map<String, Object> model = new HashMap<>();
        
        // View 이름
        String viewName = controller.process(paramMap, model);

        // View 경로 완성
        MyView view = viewResolver(viewName); // "/WEB-INF/views/new-form.jsp"ㅍ

        // View 렌더링
        view.render(model, request, response);
    }

    private static MyView viewResolver(String viewName) { // View 경로 완성
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private static Map<String, String> createParamMap(HttpServletRequest request) { // URL 파라미터 정보 생성
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName->paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
