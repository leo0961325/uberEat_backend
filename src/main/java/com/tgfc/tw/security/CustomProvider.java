package com.tgfc.tw.security;

import com.tgfc.tw.entity.model.po.Floor;
import com.tgfc.tw.entity.model.po.Team;
import com.tgfc.tw.entity.model.po.User;
import com.tgfc.tw.entity.model.response.user.UserResponse;
import com.tgfc.tw.entity.model.response.user.UserResponseV2_1;
import com.tgfc.tw.entity.repository.FloorRepository;
import com.tgfc.tw.entity.repository.TeamRepository;
import com.tgfc.tw.entity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tw.tgfc.common.spring.ldap.model.LdapUser;
import tw.tgfc.common.spring.ldap.service.LDAPService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
@Transactional
public class CustomProvider implements AuthenticationProvider {

    @Autowired
    LDAPService ldapService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    FloorRepository floorRepository;
    @Autowired
    TeamRepository teamRepository;

    private static Logger logger = Logger.getLogger(CustomProvider.class.getName());

    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String account = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        User user = null;
        Optional<User> userOptional = userRepository.findByUsername(account);
        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (!encoder.matches(password, user.getPassword())) {
                LdapUser ldap = findEmployeeByLDAp2(account, password);
                if (ldap == null) {
                    throw new AuthenticationServiceException(String.format("please check account or password"));
                } else {
                    user.setPassword(encoder.encode(password));
                    userRepository.save(user);
                }
            }
        } else {
            user = findEmployeeByLDAp(account, password);
            if (user == null) {
                throw new BadCredentialsException("please check account or password");
            }
        }

        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("admin"));

//        List<String> permissions = ContextHolderHandler.getPermissionsList(user);
        UserResponseV2_1 principle = UserResponseV2_1.valueOfPermission(user);

        return new UsernamePasswordAuthenticationToken(principle, null, AuthorityUtils.createAuthorityList(principle.getPermissions().toArray(new String[principle.getPermissions().size()])));
    }

    private User findEmployeeByLDAp(String account, String password) {
        LdapUser ldapUser = ldapService.authenticate(account, password);
        if (ldapUser == null) {
            return null;
        }
        return addUserToDataBase(ldapUser, password);
    }

    private LdapUser findEmployeeByLDAp2(String account, String password) {
        LdapUser ldapUser = ldapService.authenticate(account, password);
        return ldapUser;
    }

    private User addUserToDataBase(LdapUser ldapUser, String password) {
        Optional<Floor> floor = floorRepository.findById(1);
        Optional<Team> team = teamRepository.findById(1);
        List<Team> teamList = new ArrayList<>();
        teamList.add(team.get());

        User user = new User();
        user.setUsername(ldapUser.getAccount());
        user.setPassword(encoder.encode(password));
        user.setName(ldapUser.getName());
        user.setEnglishName(ldapUser.getName());
        user.setFloor(floor.isPresent() ? floor.get() : null);
        user.setTeamList(teamList);
        user.setEmail(ldapUser.getEmail());
        userRepository.save(user);
        return user;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}