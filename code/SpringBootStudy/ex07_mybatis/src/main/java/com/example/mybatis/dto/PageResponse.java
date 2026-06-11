package com.example.mybatis.dto;

import java.util.List;

public record PageResponse <T> (
  List<T> lists,
  int page,
  int size,
  int totalPages,
  long totalElements
) {
}
