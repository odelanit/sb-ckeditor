package com.odelan.editor.controllers;

import com.odelan.editor.models.FileDB;
import com.odelan.editor.models.Post;
import com.odelan.editor.repository.FileDBRepository;
import com.odelan.editor.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class HomeController {
    @Autowired
    protected PostRepository postRepository;

    @Autowired
    protected FileDBRepository fileDBRepository;

    @GetMapping("/")
    public String index(@RequestParam(value = "sf", required = false) String sortField, @RequestParam(value = "sd", required = false) String sortDir, Model model) {
        if (sortField == null) sortField = "id";

        Sort.Direction direction;
        String reverseDir;
        if (sortDir == null) {
            direction = Sort.Direction.ASC;
            reverseDir = "desc";
        } else {
            direction = sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            reverseDir = sortDir.equals("asc") ? "desc" : "asc";
        }

        model.addAttribute("posts", postRepository.findAll(Sort.by(direction, sortField)));
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseDir);
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
        Post post = postRepository.findById(id).get();
        String editorField = post.getEditorField();
        List<String> imgSources = this.getImgSrc(editorField);
        for (String imgSrc : imgSources) {
            String[] strings = imgSrc.split("/files/");
            String uuid = strings[1];
            fileDBRepository.deleteById(uuid);
        }
        postRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}/files")
    @ResponseBody
    public Set<FileDB> getAllFiles(@PathVariable Long id) {
        Post post = postRepository.findById(id).get();
        return post.getFiles();
    }

    @PostMapping("/edit/{id}/files/{fileId}")
    @ResponseBody
    public HashMap<String, String> getAllFiles(@PathVariable Long id, @PathVariable String fileId) {
        Post post = postRepository.findById(id).get();
        FileDB fileDB = fileDBRepository.findById(fileId).get();
        post.removeFileDB(fileDB);
        fileDBRepository.deleteById(fileId);
        HashMap<String, String> map = new HashMap<>();
        map.put("message", "Deleted");
        return map;
    }

    private List<String> getImgSrc(String htmlStr) {
        if( htmlStr == null ){

            return null;
        }

        String img = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();

        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            img = img + "," + m_image.group();
            // Matcher m =
            // Pattern.compile ( "src = \" (*) (\ "|> | \\ s +)"?.?) Matcher (img);. // src match
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);

            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }
}
