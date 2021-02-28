package com.odelan.editor.controllers;

import com.odelan.editor.models.Post;
import com.odelan.editor.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class HomeController {
    @Autowired
    protected PostRepository postRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postRepository.findAll());
        return "index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("post", new Post());
        return "create";
    }

    @PostMapping("/create")
    public String store(@ModelAttribute Post post, Model model) {
        postRepository.save(post);

        model.addAttribute("post", post);
        return "create";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Post post = postRepository.findById(id).get();
        model.addAttribute(post);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, Post post, Model model) {
        postRepository.save(post);

        model.addAttribute(post);
        return "redirect:/";
    }
}
