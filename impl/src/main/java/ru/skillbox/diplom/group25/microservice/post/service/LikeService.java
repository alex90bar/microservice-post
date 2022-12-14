package ru.skillbox.diplom.group25.microservice.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group25.library.core.util.TokenUtil;
import ru.skillbox.diplom.group25.microservice.post.dto.LikeDto;
import ru.skillbox.diplom.group25.microservice.post.dto.LikeType;
import ru.skillbox.diplom.group25.microservice.post.mapper.LikeMapper;
import ru.skillbox.diplom.group25.microservice.post.repository.LikeRepository;

/**
 * LikeService
 *
 * @author alex90bar
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

  private final LikeRepository likeRepository;
  private final PostService postService;
  private final CommentService commentService;
  private final LikeMapper mapper;

  /**
   * Метод создания лайка
   * */
  public void create(Long id, Long commentId) {
    log.info("create begins with id: {} commentId {} ", id, commentId);

    Long userId = TokenUtil.getJwtInfo().getId();
    LikeDto dto = new LikeDto();
    dto.setType(commentId == 0 ? LikeType.POST : LikeType.COMMENT);
    dto.setItemId(commentId == 0 ? id : commentId);
    dto.setAuthorId(userId);

    //    Проверяем, есть ли лайк в БД, если нет - то создаем.
    //    Пересчитываем количество лайков в соответствующем посте/коменте.
    if (!likeRepository.existsByAuthorIdAndTypeAndItemId(userId, dto.getType(), dto.getItemId())){
      likeRepository.save(mapper.toEntity(dto));
      if (dto.getType().equals(LikeType.POST)){
        postService.setLike(dto.getItemId());
      } else {
        commentService.setLike(dto.getItemId());
      }
    } else {
      log.info("Like already exists with: {}", dto);
    }
    log.info("create ends");
  }


  /**
   * Метод удаления лайка
   * */
  public void delete(Long id, Long commentId){
    log.info("delete begins with id: {} commentId {} ", id, commentId);

    Long userId = TokenUtil.getJwtInfo().getId();

    LikeDto dto = new LikeDto();
    dto.setType(commentId == 0 ? LikeType.POST : LikeType.COMMENT);
    dto.setItemId(commentId == 0 ? id : commentId);


    //Проверяем наличие лайка в БД, если есть - удаляем.
    //Пересчитываем количество лайков в соответствующем посте/коменте.
    if (likeRepository.existsByAuthorIdAndTypeAndItemId(userId, dto.getType(), dto.getItemId())) {
      likeRepository.deleteByAuthorIdAndTypeAndItemId(userId, dto.getType(), dto.getItemId());
      if (dto.getType().equals(LikeType.POST)) {
        postService.dislike(dto.getItemId());
      } else {
        commentService.dislike(dto.getItemId());
      }
    } else {
      log.info("Like not found with: {}", dto);
    }
    log.info("delete ends ");
  }

}


