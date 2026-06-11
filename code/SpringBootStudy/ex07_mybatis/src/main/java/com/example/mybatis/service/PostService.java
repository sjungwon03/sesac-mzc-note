package com.example.mybatis.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.mybatis.dto.PageResponse;
import com.example.mybatis.dto.Post;
import com.example.mybatis.dto.PostCreateRequest;
import com.example.mybatis.dto.PostResponse;
import com.example.mybatis.dto.PostUpdateRequest;
import com.example.mybatis.exception.CustomException;
import com.example.mybatis.exception.ErrorCode;
import com.example.mybatis.mapper.PostMapper;

import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PostService {
  
  private final PostMapper postMapper;

  @Transactional
  public PostResponse createPost(PostCreateRequest request){
    Post post = Post.builder()
        .userId(request.userId())
        .title(request.title())
        .content(request.content())
        .build();
    
    // 제약 조건 위배를 대비한 코드 필요
    postMapper.save(post);

    System.out.println("INSERT 이후 Post: " + post);
    // post에는 어떤 정보가 저장되어 있는지?
    // userId + title + content 추가로 INSERT 쿼리 실행 시 MyBatis가 채운 id 값이 함께 존재함

    // post 리턴 시 createdAt 제외한 모든 값 리턴 가능
    // createdAt을 꼭 채워서 리턴하고 싶다면, Post의 id를 이용해 select 한 뒤 그 결과를 반환함
    return findById(post.getId());
  }

  public PostResponse findById(Long id){
    Post post = postMapper.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    return PostResponse.from(post);
  }

  public PageResponse<PostResponse> getPosts(int page, int size, String sort) {
    int offset = (page - 1) * size;
    long totalElements = postMapper.countAll();
    int totalPages = (int) Math.ceil((double) totalElements / size);

    List<Post> posts = postMapper.findAll(offset, size, sort);
    List<PostResponse> list = posts.stream().map(PostResponse::from).toList();

    return new PageResponse<PostResponse>(list, page, size, totalPages, totalElements);
  }

  @Transactional
  public PostResponse update(Long id, PostUpdateRequest request){
    Post post = postMapper.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    
    post.setTitle(request.title());
    post.setContent(request.content());
    
    postMapper.update(post);

    return PostResponse.from(post);
  }

  @Transactional
  public void delete(Long id){
    postMapper.findById(id).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    
    postMapper.deleteById(id);
  }
}
