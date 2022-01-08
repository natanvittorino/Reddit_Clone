package donnager.RedditClone.repositories;

import donnager.RedditClone.model.Comment;
import donnager.RedditClone.model.Post;
import donnager.RedditClone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);

}
