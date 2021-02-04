package ominext.timetracking.service;

import ominext.timetracking.model.entity.Employee;
import ominext.timetracking.repository.IEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    IEmployeeRepository repository;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        String username = token.split(":")[0];
        String password = token.split(":")[1];
        if (repository.findByUsername(username).isPresent()) {
            Employee employee = repository.findByUsername(username).get();
            if (employee.getPassword().equals(password)) {
                List<GrantedAuthority> authorityList = new ArrayList<>();
                authorityList.add(new SimpleGrantedAuthority(employee.getRole().toString()));
                if (employee.isLogin()) {
                    return new User(employee.getUsername(), employee.getPassword(), authorityList);
                }
                return new User(employee.getUsername(), employee.getPassword(), true,
                        false, true, true, authorityList);
            }
        }
        throw new UsernameNotFoundException("User not found by " + token);
    }
}
