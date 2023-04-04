package com.example.servlet.web.springmvc.v3;

import com.example.servlet.domain.member.Member;
import com.example.servlet.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/springmvc/v3/members") // 중복되는 경로 미리 지정
public class SpringMemberControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

//    @RequestMapping(value = "/new-form", method = RequestMethod.GET) // GET방식으로만 호출 가능
    @GetMapping("/new-form") // GET방식으로만 호출 가능
    public String newForm(){
        return "new-form";
    }

//    @RequestMapping(value = "/save", method = RequestMethod.POST) // POST방식으로만 호출 가능
    @PostMapping("/save") // POST방식으로만 호출 가능
    public String save(@RequestParam("username") String username,
                             @RequestParam("age") int age,
                             Model model) {

        Member member = new Member(username, age);
        memberRepository.save(member);

        model.addAttribute("member", member);
        return "save-result";
    }

//    @RequestMapping(method = RequestMethod.GET) // /springmvc/v2/members // GET방식으로만 호출 가능
    @GetMapping() // /springmvc/v2/members // GET방식으로만 호출 가능
    public String members(Model model) {

        List<Member> members = memberRepository.findAll();

        model.addAttribute("members", members);
        return "members";
    }
}
