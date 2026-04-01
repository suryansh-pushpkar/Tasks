package com.friendbook.service;

import com.friendbook.entity.Follow;
import com.friendbook.entity.FollowRequestStatus;
import com.friendbook.entity.FriendRequest;
import com.friendbook.entity.Post;
import com.friendbook.entity.PostComment;
import com.friendbook.entity.PostLike;
import com.friendbook.entity.User;
import com.friendbook.repository.FollowRepository;
import com.friendbook.repository.FriendRequestRepository;
import com.friendbook.repository.PostCommentRepository;
import com.friendbook.repository.PostLikeRepository;
import com.friendbook.repository.PostRepository;
import com.friendbook.repository.UserRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SocialService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostCommentRepository postCommentRepository;

    public SocialService(UserRepository userRepository, FollowRepository followRepository,
                         FriendRequestRepository friendRequestRepository, PostRepository postRepository,
                         PostLikeRepository postLikeRepository, PostCommentRepository postCommentRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.postCommentRepository = postCommentRepository;
    }

    @Transactional(readOnly = true)
    public long followerCount(User user) {
        return followRepository.countByFollowing(user);
    }

    @Transactional(readOnly = true)
    public long followingCount(User user) {
        return followRepository.countByFollower(user);
    }

    @Transactional(readOnly = true)
    public List<Post> getProfilePosts(User user) {
        return postRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Transactional(readOnly = true)
    public List<Post> getFeed(User user) {
        List<User> users = new ArrayList<>();
        users.add(user);
        users.addAll(followRepository.findByFollower(user).stream().map(Follow::getFollowing).toList());
        return postRepository.findByUserInOrderByCreatedAtDesc(users);
    }

    @Transactional
    public void sendOrCancelRequest(User sender, User receiver) {
        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("You cannot follow yourself");
        }
        if (followRepository.findByFollowerAndFollowing(sender, receiver).isPresent()) {
            throw new IllegalArgumentException("You already follow this user");
        }

        friendRequestRepository.findBySenderAndReceiverAndStatus(sender, receiver, FollowRequestStatus.PENDING)
            .ifPresentOrElse(existing -> {
                existing.setStatus(FollowRequestStatus.CANCELLED);
                friendRequestRepository.save(existing);
            }, () -> {
                FriendRequest request = new FriendRequest();
                request.setSender(sender);
                request.setReceiver(receiver);
                request.setStatus(FollowRequestStatus.PENDING);
                friendRequestRepository.save(request);
            });
    }

    @Transactional(readOnly = true)
    public List<FriendRequest> getPendingRequests(User user) {
        return friendRequestRepository.findByReceiverAndStatusOrderByCreatedAtDesc(user, FollowRequestStatus.PENDING);
    }

    @Transactional
    public void respondToRequest(Long requestId, User receiver, String action) {
        FriendRequest request = friendRequestRepository.findById(requestId)
            .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

        if (!request.getReceiver().getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("You cannot manage this request");
        }
        if (request.getStatus() != FollowRequestStatus.PENDING) {
            throw new IllegalArgumentException("This request has already been processed");
        }

        if ("accept".equalsIgnoreCase(action) || "follow_back".equalsIgnoreCase(action)) {
            request.setStatus(FollowRequestStatus.ACCEPTED);
            if (followRepository.findByFollowerAndFollowing(request.getSender(), receiver).isEmpty()) {
                Follow follow = new Follow();
                follow.setFollower(request.getSender());
                follow.setFollowing(receiver);
                followRepository.save(follow);
            }
            if ("follow_back".equalsIgnoreCase(action)
                && followRepository.findByFollowerAndFollowing(receiver, request.getSender()).isEmpty()) {
                Follow reciprocal = new Follow();
                reciprocal.setFollower(receiver);
                reciprocal.setFollowing(request.getSender());
                followRepository.save(reciprocal);
            }
        } else {
            request.setStatus(FollowRequestStatus.DECLINED);
        }
        friendRequestRepository.save(request);
    }

    @Transactional
    public Post createPost(User user, String caption, String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            throw new IllegalArgumentException("Post image is required");
        }

        Post post = new Post();
        post.setUser(user);
        post.setCaption(caption);
        post.setImagePath(imagePath);
        return postRepository.save(post);
    }

    @Transactional
    public void toggleLike(Long postId, User user) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        postLikeRepository.findByPostAndUser(post, user).ifPresentOrElse(
            postLikeRepository::delete,
            () -> {
                PostLike like = new PostLike();
                like.setPost(post);
                like.setUser(user);
                postLikeRepository.save(like);
            }
        );
    }

    @Transactional
    public void addComment(Long postId, User user, String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Comment cannot be empty");
        }
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        PostComment comment = new PostComment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content.trim());
        postCommentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public boolean isFollowing(User viewer, User profileOwner) {
        return followRepository.findByFollowerAndFollowing(viewer, profileOwner).isPresent();
    }

    @Transactional(readOnly = true)
    public boolean hasPendingRequest(User viewer, User profileOwner) {
        return friendRequestRepository.findBySenderAndReceiverAndStatus(viewer, profileOwner, FollowRequestStatus.PENDING).isPresent();
    }

    @Transactional(readOnly = true)
    public List<User> followersOf(User user) {
        return followRepository.findByFollowing(user).stream()
            .map(Follow::getFollower)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<User> followingOf(User user) {
        return followRepository.findByFollower(user).stream()
            .map(Follow::getFollowing)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FriendRequest> activityFor(User user) {
        return friendRequestRepository.findBySenderOrReceiverOrderByCreatedAtDesc(user, user).stream()
            .sorted(Comparator.comparing(FriendRequest::getCreatedAt).reversed())
            .toList();
    }
}
