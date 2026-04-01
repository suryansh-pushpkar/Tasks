package com.friendbook.controller;

import com.friendbook.dto.ApiMessage;
import com.friendbook.dto.CommentResponse;
import com.friendbook.dto.HomeResponse;
import com.friendbook.dto.NotificationResponse;
import com.friendbook.dto.PostResponse;
import com.friendbook.dto.ProfilePageResponse;
import com.friendbook.dto.ProfileResponse;
import com.friendbook.dto.ProfileUpdateDTO;
import com.friendbook.dto.UserCardResponse;
import com.friendbook.entity.FriendRequest;
import com.friendbook.entity.Post;
import com.friendbook.entity.PostComment;
import com.friendbook.entity.User;
import com.friendbook.security.AuthenticatedUser;
import com.friendbook.service.SocialService;
import com.friendbook.service.StorageService;
import com.friendbook.service.UserService;
import jakarta.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.RestController;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FriendBookApiController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

    private final UserService userService;
    private final SocialService socialService;
    private final StorageService storageService;

    public FriendBookApiController(UserService userService, SocialService socialService, StorageService storageService) {
        this.userService = userService;
        this.socialService = socialService;
        this.storageService = storageService;
    }

    @GetMapping("/home")
    @Transactional(readOnly = true)
    public HomeResponse home(@RequestParam(required = false) String q, Authentication authentication) {
        User currentUser = currentUser(authentication);
        return new HomeResponse(
            toUserCard(currentUser),
            userService.search(q).stream()
                .filter(user -> !user.getId().equals(currentUser.getId()))
                .map(this::toUserCard)
                .toList(),
            socialService.getPendingRequests(currentUser).stream()
                .map(request -> toNotification(request, currentUser))
                .toList(),
            socialService.getFeed(currentUser).stream()
                .map(this::toPost)
                .toList()
        );
    }

    @GetMapping("/profile")
    @Transactional(readOnly = true)
    public ProfilePageResponse myProfile(Authentication authentication) {
        User currentUser = currentUser(authentication);
        return toProfileResponse(currentUser, currentUser);
    }

    @GetMapping("/users/{username}")
    @Transactional(readOnly = true)
    public ProfilePageResponse profile(@PathVariable String username, Authentication authentication) {
        User currentUser = currentUser(authentication);
        User profileUser = userService.getByUsername(username);
        return toProfileResponse(currentUser, profileUser);
    }

    @PostMapping("/requests/{username}/toggle")
    public ResponseEntity<ApiMessage> toggleRequest(@PathVariable String username, Authentication authentication) {
        User currentUser = currentUser(authentication);
        User target = userService.getByUsername(username);
        socialService.sendOrCancelRequest(currentUser, target);
        return ResponseEntity.ok(new ApiMessage("Request state updated"));
    }

    @PostMapping("/requests/{id}/respond")
    public ResponseEntity<ApiMessage> respond(@PathVariable Long id,
                                              @RequestParam String action,
                                              Authentication authentication) {
        socialService.respondToRequest(id, currentUser(authentication), action);
        return ResponseEntity.ok(new ApiMessage("Request updated"));
    }

    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiMessage> createPost(@RequestParam("imageFile") MultipartFile imageFile,
                                                 @RequestParam(required = false) String caption,
                                                 Authentication authentication) {
        User currentUser = currentUser(authentication);
        String imagePath = storageService.storeImage(imageFile);
        socialService.createPost(currentUser, caption, imagePath);
        return ResponseEntity.ok(new ApiMessage("Post created successfully"));
    }

    @PostMapping("/posts/{id}/like")
    public ResponseEntity<ApiMessage> toggleLike(@PathVariable Long id, Authentication authentication) {
        socialService.toggleLike(id, currentUser(authentication));
        return ResponseEntity.ok(new ApiMessage("Like updated"));
    }

    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<ApiMessage> addComment(@PathVariable Long id,
                                                 @RequestParam String content,
                                                 Authentication authentication) {
        socialService.addComment(id, currentUser(authentication), content);
        return ResponseEntity.ok(new ApiMessage("Comment added"));
    }

    @PutMapping("/users/{username}/profile")
    public ResponseEntity<ProfileResponse> updateProfile(@PathVariable String username,
                                                         @Valid @RequestBody ProfileUpdateDTO dto,
                                                         Authentication authentication) {
        User currentUser = currentUser(authentication);
        if (!currentUser.getUserName().equals(username)) {
            throw new IllegalArgumentException("You can only update your own profile");
        }
        return ResponseEntity.ok(userService.updateProfile(username, dto, null));
    }

    @PostMapping(value = "/users/{username}/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiMessage> uploadProfileImage(@PathVariable String username,
                                                         @RequestParam("profileImage") MultipartFile profileImage,
                                                         Authentication authentication) {
        User currentUser = currentUser(authentication);
        if (!currentUser.getUserName().equals(username)) {
            throw new IllegalArgumentException("You can only update your own profile");
        }

        ProfileUpdateDTO dto = new ProfileUpdateDTO();
        dto.setFullName(currentUser.getFullName());
        dto.setEmail(currentUser.getEmail());
        dto.setFavSongs(currentUser.getFavSongs());
        dto.setFavBooks(currentUser.getFavBooks());
        dto.setFavPlaces(currentUser.getFavPlaces());
        userService.updateProfile(username, dto, storageService.storeImage(profileImage));
        return ResponseEntity.ok(new ApiMessage("Profile image updated"));
    }

    private ProfilePageResponse toProfileResponse(User currentUser, User profileUser) {
        var posts = socialService.getProfilePosts(profileUser).stream()
            .map(this::toPost)
            .toList();

        return new ProfilePageResponse(
            toUserCard(currentUser),
            toUserCard(profileUser),
            currentUser.getId().equals(profileUser.getId()),
            socialService.isFollowing(currentUser, profileUser),
            socialService.hasPendingRequest(currentUser, profileUser),
            posts.size(),
            socialService.followerCount(profileUser),
            socialService.followingCount(profileUser),
            profileUser.getFavSongs(),
            profileUser.getFavBooks(),
            profileUser.getFavPlaces(),
            socialService.followersOf(profileUser).stream().map(this::toUserCard).toList(),
            socialService.followingOf(profileUser).stream().map(this::toUserCard).toList(),
            posts
        );
    }

    private NotificationResponse toNotification(FriendRequest request, User currentUser) {
        return new NotificationResponse(
            request.getId(),
            toUserCard(request.getSender()),
            socialService.isFollowing(currentUser, request.getSender()),
            DATE_TIME_FORMATTER.format(request.getCreatedAt())
        );
    }

    private PostResponse toPost(Post post) {
        List<CommentResponse> comments = post.getComments().stream()
            .map(this::toComment)
            .toList();

        return new PostResponse(
            post.getId(),
            toUserCard(post.getUser()),
            post.getCaption(),
            post.getImagePath(),
            DATE_TIME_FORMATTER.format(post.getCreatedAt()),
            post.getLikes().size(),
            comments
        );
    }

    private CommentResponse toComment(PostComment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getUser().getUserName(),
            comment.getContent(),
            DATE_TIME_FORMATTER.format(comment.getCreatedAt())
        );
    }

    private UserCardResponse toUserCard(User user) {
        return new UserCardResponse(user.getId(), user.getUserName(), user.getFullName(), user.getProfileImage());
    }

    private User currentUser(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser authenticatedUser)) {
            throw new IllegalArgumentException("Please login first");
        }
        return userService.getById(authenticatedUser.getId());
    }
}
