package com.friendbook.controller;

import com.friendbook.entity.User;
import com.friendbook.security.AuthenticatedUser;
import com.friendbook.service.SocialService;
import com.friendbook.service.StorageService;
import com.friendbook.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class SocialController {

    private final UserService userService;
    private final SocialService socialService;
    private final StorageService storageService;

    public SocialController(UserService userService, SocialService socialService, StorageService storageService) {
        this.userService = userService;
        this.socialService = socialService;
        this.storageService = storageService;
    }

    @PostMapping("/requests/{username}/toggle")
    public ResponseEntity<Void> toggleRequest(@PathVariable String username, Authentication authentication) {
        User currentUser = currentUser(authentication);
        User target = userService.getByUsername(username);
        socialService.sendOrCancelRequest(currentUser, target);
        return redirect("/users/" + username);
    }

    @PostMapping("/requests/{id}/respond")
    public ResponseEntity<Void> respond(@PathVariable Long id, @RequestParam String action, Authentication authentication) {
        User currentUser = currentUser(authentication);
        socialService.respondToRequest(id, currentUser, action);
        return redirect("/home");
    }

    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(@RequestParam("imageFile") MultipartFile imageFile,
                                           @RequestParam(required = false) String caption,
                                           Authentication authentication) {
        User currentUser = currentUser(authentication);
        String imagePath = storageService.storeImage(imageFile);
        socialService.createPost(currentUser, caption, imagePath);
        return redirect("/home");
    }

    @PostMapping("/posts/{id}/like")
    public ResponseEntity<Void> toggleLike(@PathVariable Long id, Authentication authentication) {
        User currentUser = currentUser(authentication);
        socialService.toggleLike(id, currentUser);
        return redirect("/home");
    }

    @PostMapping("/posts/{id}/comments")
    public ResponseEntity<Void> addComment(@PathVariable Long id, @RequestParam String content, Authentication authentication) {
        User currentUser = currentUser(authentication);
        socialService.addComment(id, currentUser, content);
        return redirect("/home");
    }

    private User currentUser(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof AuthenticatedUser authenticatedUser)) {
            throw new IllegalArgumentException("Please login first");
        }
        return userService.getById(authenticatedUser.getId());
    }

    private ResponseEntity<Void> redirect(String location) {
        return ResponseEntity.status(HttpStatus.FOUND)
            .header(HttpHeaders.LOCATION, location)
            .build();
    }
}
