package com.odelan.editor.controllers;

import com.odelan.editor.models.FileDB;
import com.odelan.editor.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class FileUploadController {
    @Autowired
    private FileStorageService storageService;

    @PostMapping("/editor-upload")
    @ResponseBody
    public Map<String, Object> handleEditorFileUpload(@RequestParam("upload") MultipartFile file) throws IOException {
        HashMap<String, Object> map = new HashMap<>();

        FileDB fileDB = storageService.store(file);

        map.put("url", "/files/" + fileDB.getId());
        map.put("uploaded", true);
        return map;
    }

    @PostMapping("/upload")
    @ResponseBody
    public Map<String, Object> handleFileUpload(@RequestParam("upload") MultipartFile file) throws IOException {
        HashMap<String, Object> map = new HashMap<>();
        FileDB fileDB = storageService.store(file);
        map.put("file", fileDB);
        return map;
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        FileDB fileDB = storageService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }
}
