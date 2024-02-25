package ru.zinoviev.resthomework.db.mapper;

import ru.zinoviev.resthomework.db.dao.entity.User;
import ru.zinoviev.resthomework.db.dao.entity.UserLike;
import ru.zinoviev.resthomework.db.dto.UserLikeDto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserLikesMapper {

    private Set<UserLikeDto> listEntityToListDto(List<UserLike> userLikes) {
        Set<UserLikeDto> userLikesDto = new HashSet<>();

        for (UserLike like : userLikes) {
            userLikesDto.add(entityToDto(like));
        }

        return userLikesDto;
    }

    private UserLikeDto entityToDto(UserLike userLike) {
        UserLikeDto likeDto = new UserLikeDto();

        likeDto.setId(userLike.getId());
        likeDto.setComment(userLike.getComment());
        if (userLike.getUser() != null) {
            likeDto.setUserId(userLike.getUser().getId());
            likeDto.setUserLogin(userLike.getUser().getLogin());
        }

        return likeDto;
    }

    private UserLike dtoToEntity(UserLikeDto userLikeDto) {
        UserLike like = new UserLike();

        like.setId(userLikeDto.getId());
        like.setComment(userLikeDto.getComment());
        if (userLikeDto.getUserId() != 0) {
            User user = new User();
            user.setId(userLikeDto.getId());
            user.setLogin(userLikeDto.getUserLogin());
            like.setUser(user);
        }

        return like;
    }
}
