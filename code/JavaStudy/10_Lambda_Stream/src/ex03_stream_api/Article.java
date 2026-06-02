package ex03_stream_api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Lombok 이용해 Constructor, Getter/Setter, ToString 채우기

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Article {
  private String title;
  private int viewCount;
}
