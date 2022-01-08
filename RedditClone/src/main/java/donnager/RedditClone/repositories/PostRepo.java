package donnager.RedditClone.repositories;

import donnager.RedditClone.model.Post;
import donnager.RedditClone.model.Subreddit;
import donnager.RedditClone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Long> {

    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
