package br.com.rapharodrigo.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.rapharodrigo.todolist.user.IUserRepository;
import br.com.rapharodrigo.todolist.user.UserModel;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (!servletPath.equals("/tasks/")) {
            filterChain.doFilter(request, response);
            return;
        }

        var authorization = request.getHeader("Authorization");
        var encodedAuth = authorization.substring("Basic".length()).trim();
        var decodedAuth = Base64.getDecoder().decode(encodedAuth);
        var authString = new String(decodedAuth);        
        String[] credentials = authString.split(":");
        String username = credentials[0];
        String password = credentials[1];

        var user = this.userRepository.findByUsername(username);
        if (user == null) {
            response.sendError(401);
        } else {
            verifyPassword(request, response, filterChain, password, user);
        }
    }

    private void verifyPassword(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain,
            String password, UserModel user) throws IOException, ServletException {
        var verifiedPassword = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

        if (verifiedPassword.verified) {
            request.setAttribute("userId", user.getId());
            filterChain.doFilter(request, response);
        } else {
            response.sendError(401);
        }
    }

}
