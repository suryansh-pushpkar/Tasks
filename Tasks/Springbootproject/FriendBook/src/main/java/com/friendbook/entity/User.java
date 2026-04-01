package com.friendbook.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 32)
    private String userName;

    @Column(length = 255)
    private String profileImage;

    @Column(length = 255)
    private String favSongs;

    @Column(length = 255)
    private String favBooks;

    @Column(length = 255)
    private String favPlaces;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "sender")
    private List<FriendRequest> sentRequests = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<FriendRequest> receivedRequests = new ArrayList<>();

    @OneToMany(mappedBy = "follower")
    private List<Follow> followingRelationships = new ArrayList<>();

    @OneToMany(mappedBy = "following")
    private List<Follow> followerRelationships = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PostComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PostLike> likes = new ArrayList<>();

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFavSongs() {
        return favSongs;
    }

    public void setFavSongs(String favSongs) {
        this.favSongs = favSongs;
    }

    public String getFavBooks() {
        return favBooks;
    }

    public void setFavBooks(String favBooks) {
        this.favBooks = favBooks;
    }

    public String getFavPlaces() {
        return favPlaces;
    }

    public void setFavPlaces(String favPlaces) {
        this.favPlaces = favPlaces;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<FriendRequest> getSentRequests() {
        return sentRequests;
    }

    public void setSentRequests(List<FriendRequest> sentRequests) {
        this.sentRequests = sentRequests;
    }

    public List<FriendRequest> getReceivedRequests() {
        return receivedRequests;
    }

    public void setReceivedRequests(List<FriendRequest> receivedRequests) {
        this.receivedRequests = receivedRequests;
    }

    public List<Follow> getFollowingRelationships() {
        return followingRelationships;
    }

    public void setFollowingRelationships(List<Follow> followingRelationships) {
        this.followingRelationships = followingRelationships;
    }

    public List<Follow> getFollowerRelationships() {
        return followerRelationships;
    }

    public void setFollowerRelationships(List<Follow> followerRelationships) {
        this.followerRelationships = followerRelationships;
    }

    public List<PostComment> getComments() {
        return comments;
    }

    public void setComments(List<PostComment> comments) {
        this.comments = comments;
    }

    public List<PostLike> getLikes() {
        return likes;
    }

    public void setLikes(List<PostLike> likes) {
        this.likes = likes;
    }

    public void addPost(Post post) {
        posts.add(post);
        post.setUser(this);
    }

    public void addSentRequest(FriendRequest friendRequest) {
        sentRequests.add(friendRequest);
        friendRequest.setSender(this);
    }

    public void addReceivedRequest(FriendRequest friendRequest) {
        receivedRequests.add(friendRequest);
        friendRequest.setReceiver(this);
    }

    public void addFollowingRelationship(Follow follow) {
        followingRelationships.add(follow);
        follow.setFollower(this);
    }

    public void addFollowerRelationship(Follow follow) {
        followerRelationships.add(follow);
        follow.setFollowing(this);
    }

    public void addComment(PostComment comment) {
        comments.add(comment);
        comment.setUser(this);
    }

    public void addLike(PostLike like) {
        likes.add(like);
        like.setUser(this);
    }
}
