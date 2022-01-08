package donnager.RedditClone.repositories;

import donnager.RedditClone.model.Post;
import donnager.RedditClone.model.User;
import donnager.RedditClone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepo extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);
}
