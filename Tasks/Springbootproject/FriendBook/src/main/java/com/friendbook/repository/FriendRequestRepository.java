package com.friendbook.repository;

import com.friendbook.entity.FollowRequestStatus;
import com.friendbook.entity.FriendRequest;
import com.friendbook.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findBySenderAndReceiverAndStatus(User sender, User receiver, FollowRequestStatus status);

    List<FriendRequest> findByReceiverAndStatusOrderByCreatedAtDesc(User receiver, FollowRequestStatus status);

    List<FriendRequest> findBySenderOrReceiverOrderByCreatedAtDesc(User sender, User receiver);
}
