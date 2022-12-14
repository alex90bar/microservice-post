package ru.skillbox.diplom.group25.microservice.post.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group25.microservice.post.dto.CommentDto;
import ru.skillbox.diplom.group25.microservice.post.service.CommentService;

/**
 * CommentResourceImpl
 *
 * @author alex90bar
 */

@RestController
@RequiredArgsConstructor
public class CommentResourceImpl implements CommentResource{

  private final CommentService commentService;


  @Override
  public void create(CommentDto dto, Long id) {
    commentService.create(dto, id);
  }

  @Override
  public ResponseEntity<Page<CommentDto>> getByPostId(Long id, Pageable page) {
    return ResponseEntity.ok(commentService.getAllByPostIdAndCommentId(id, 0L, page));
  }

  @Override
  public ResponseEntity<Page<CommentDto>> getByPostIdAndCommentId(Long id, Long commentId, Pageable page) {
    return ResponseEntity.ok(commentService.getAllByPostIdAndCommentId(id, commentId, page));
  }

  @Override
  public void deleteById(Long id, Long commentId) {
    commentService.deleteById(commentId);
  }

  @Override
  public void update(Long id, Long commentId, CommentDto dto) {
    commentService.update(dto, commentId);
  }

}


