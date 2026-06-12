package com.example.jpa.ex02_persistence_context;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false)
  private String title;
  
  private String author;

  public Book(String title, String author){
    this.title = title;
    this.author = author;
  }

  @Override
  public String toString() {
    return "Book [id=" + id + ", title=" + title + ", author=" + author + "]";
  }

  // 책 이름을 바꿔주는 비지니스 메서드
  public void changeTitle(String title){
    this.title = title;
  }

}
