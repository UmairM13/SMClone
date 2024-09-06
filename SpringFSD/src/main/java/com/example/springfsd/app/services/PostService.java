package com.example.springfsd.app.services;

import com.example.springfsd.app.models.Post;
import com.example.springfsd.app.models.User;
import com.example.springfsd.app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    public Long addNewPost(String text, Long authorId) throws Exception{
        User author = userService.getUserById(authorId);
        if(author == null){
            throw new Exception("Author not found");
        }

        Post post = new Post();
        post.setText(text);
        post.setAuthor(author);

        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }
}
