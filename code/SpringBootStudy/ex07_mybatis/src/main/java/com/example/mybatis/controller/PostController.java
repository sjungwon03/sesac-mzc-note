package com.example.mybatis.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mybatis.dto.PageResponse;
import com.example.mybatis.dto.PostCreateRequest;
import com.example.mybatis.dto.PostResponse;
import com.example.mybatis.dto.PostUpdateRequest;
import com.example.mybatis.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
  
  private final PostService postService;

  @PostMapping
  public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostCreateRequest request) {
    PostResponse response = postService.createPost(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PostResponse> getPost(@PathVariable("id") Long id) {
    PostResponse response = postService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping
  public ResponseEntity<PageResponse<PostResponse>> getPosts(
    @RequestParam(value = "page", defaultValue = "1") int page,
    @RequestParam(value = "size", defaultValue = "2") int size,
    @RequestParam(value = "sort", defaultValue = "DESC") String sort
  ) {
    return ResponseEntity.ok(postService.getPosts(page, size, sort));
  }

  @PutMapping("/{id}")
  public ResponseEntity<PostResponse> updatePost(@PathVariable("id") Long id, @RequestBody PostUpdateRequest request) {
    PostResponse response = postService.update(id, request);
      
    return ResponseEntity.ok(response);
  }  

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePost(@PathVariable("id") Long id){
    postService.delete(id);

    return ResponseEntity.noContent().build();
  }
}
