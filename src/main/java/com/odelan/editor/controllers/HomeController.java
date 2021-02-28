package com.odelan.editor.controllers;

import com.odelan.editor.models.FileDB;
import com.odelan.editor.models.Post;
import com.odelan.editor.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

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
        return "redirect:/";
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

    @GetMapping("/show/{id}")
    public String show(@PathVariable Long id, Model model) {
        Post post = postRepository.findById(id).get();
        model.addAttribute(post);
        return "show";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        postRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}/files")
    @ResponseBody
    public Set<FileDB> getAllFiles(@PathVariable Long id) {
        Post post = postRepository.findById(id).get();
        return post.getFiles();
    }
}
