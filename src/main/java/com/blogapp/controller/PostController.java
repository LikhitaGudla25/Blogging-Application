package com.blogapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blogapp.entity.Comment;
import com.blogapp.entity.Post;
import com.blogapp.entity.User;
import com.blogapp.repository.CommentRepository;
import com.blogapp.service.CommentService;
import com.blogapp.service.PostService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private HttpSession session;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/")
    public String publicHome(Model model) {
        model.addAttribute("posts", postService.getPublishedPosts());
        return "index";
    }



    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("posts",
                postService.getPostsByUserId(user.getId()));
        return "dashboard";
    }


    @GetMapping("/create")
    public String createPostPage(Model model) {
        model.addAttribute("post", new Post());
        return "create_post";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute Post post, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        post.setUser(user);

        // 🔴 FORCE STATUS (IMPORTANT)
        if ("PUBLISHED".equalsIgnoreCase(post.getStatus())) {
            post.setStatus("PUBLISHED");
        } else {
            post.setStatus("DRAFT");
        }

        postService.createPost(post);
        return "redirect:/dashboard";
    }


    @GetMapping("/view/{id}")
    public String viewPost(@PathVariable Integer id, Model model) {
        Post post = postService.getPostById(id);
        User user = (User) session.getAttribute("user");

        if (user == null) return "redirect:/login";

        List<Comment> comments = commentService.getCommentsByPostId(id);

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("user", user);
        model.addAttribute("commentObj", new Comment());

        return "view_post";
    }

    @PostMapping("/comment/{postId}")
    public String addComment(@PathVariable Integer postId,
                             @ModelAttribute Comment comment) {

        User currentUser = (User) session.getAttribute("user");
        Post post = postService.getPostById(postId);

        if (currentUser != null && currentUser.getId().equals(post.getUser().getId())) {
            return "redirect:/view/" + postId + "?error=cannot_comment_on_own_post";
        }

        comment.setPost(post);
        commentService.addComment(comment);

        return "redirect:/view/" + postId;
    }

    @GetMapping("/search")
    public String searchPosts(@RequestParam("query") String query, Model model) {
        List<Post> posts = postService.searchPosts(query);
        model.addAttribute("posts", posts);
        model.addAttribute("query", query);
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Integer id, Model model) {
        Post post = postService.getPostById(id);
        User user = (User) session.getAttribute("user");

        if (user == null || post == null || !post.getUser().getId().equals(user.getId())) {
            return "redirect:/dashboard";
        }

        model.addAttribute("post", post);
        return "edit_post";
    }

    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable Integer id,
                             @ModelAttribute Post post,
                             HttpSession session) {

        Post existingPost = postService.getPostById(id);
        User user = (User) session.getAttribute("user");

        if (user == null || existingPost == null ||
            !existingPost.getUser().getId().equals(user.getId())) {
            return "redirect:/dashboard";
        }

        existingPost.setTitle(post.getTitle());
        existingPost.setDescription(post.getDescription());
        existingPost.setContent(post.getContent());

        postService.createPost(existingPost);
        return "redirect:/dashboard";
    }

    @GetMapping("/comments")
    public String viewUserComments(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) return "redirect:/login";

        List<Comment> comments = commentRepository.findCommentsByPostId(loggedInUser.getId());
        model.addAttribute("comments", comments);

        return "comments";
    }

    @GetMapping("/posts/new")
    public String showNewPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "create_post";
    }

    @GetMapping("/posts")
    public String publicPosts(Model model) {
        model.addAttribute("posts", postService.getPublishedPosts());
        return "index";
    }



    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return "redirect:/dashboard";
    }
}
