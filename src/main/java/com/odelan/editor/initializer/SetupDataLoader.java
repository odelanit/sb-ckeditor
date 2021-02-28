package com.odelan.editor.initializer;

import com.odelan.editor.models.Role;
import com.odelan.editor.models.User;
import com.odelan.editor.repository.PostRepository;
import com.odelan.editor.repository.RoleRepository;
import com.odelan.editor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (alreadySetup) return;

        Role adminRole = createRoleIfNotFound("admin");
        Optional<User> optionalUser = userRepository.findByUsername("admin");
        if (!optionalUser.isPresent()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setEnabled(true);

            Set<Role> set = new HashSet<>();
            set.add(adminRole);
            user.setRoles(set);

            userRepository.save(user);
        }

//        postRepository.deleteAll();

        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
        return role;
    }
}
