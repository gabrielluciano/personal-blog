package com.gabrielluciano.blog.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.mvc.ProxyExchange;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Value("${image.service.url}")
    private String imageApiUrl;

    @PostMapping("upload")
    @PreAuthorize("hasRole('EDITOR')")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("name") String name, ProxyExchange<byte[]> proxy) throws Exception {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.set("name", name);
        body.set("file", file.getResource());
        return proxy.uri(imageApiUrl + "/image/upload").body(body).post();
    }

    @GetMapping("{path}")
    public ResponseEntity<?> get(@PathVariable String path, ProxyExchange<byte[]> proxy) throws Exception {
        return proxy.uri(imageApiUrl + "/image/" + path).get();
    }

    @DeleteMapping("{name}")
    @PreAuthorize("hasRole('EDITOR')")
    public ResponseEntity<?> delete(@PathVariable String name, ProxyExchange<byte[]> proxy) throws Exception {
        return proxy.uri(imageApiUrl + "/image/" + name).delete();
    }
}
