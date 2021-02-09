package com.tgfc.tw.security;

import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.po.Role;
import com.tgfc.tw.entity.model.po.User;
import com.tgfc.tw.entity.model.response.user.UserResponseV2_1;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContextHolderHandler {

	private static UserResponseV2_1 getUserResponseHandler() {
		return (UserResponseV2_1) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static UserResponseV2_1 getUserResponse() {
		UserResponseV2_1 origin = getUserResponseHandler();
		UserResponseV2_1 copy = new UserResponseV2_1();
		BeanUtils.copyProperties(origin, copy);

		return copy;
	}

	public static String getUsername() {
		return getUserResponseHandler().getUsername();
	}

	public static String getName() {
		return getUserResponseHandler().getName();
	}

	public static int getId() {
		return getUserResponseHandler().getId();
	}

	public static List<Integer> getTeamIdList() {
		return getUserResponseHandler().getTeamIdList();
	}

	public static void setUserResponse(UserResponseV2_1 userResponse) {
		Authentication auth = new UsernamePasswordAuthenticationToken(userResponse, null, AuthorityUtils.createAuthorityList(userResponse.getPermissions().toString()));
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	public static List<String> getUserPermission() {
		return getUserResponseHandler().getPermissions();
	}

	public static List<String> getPermissionsList(User user) {
		List<String> permissions = new ArrayList<>();
		List<Role> roles = user.getRoleList();

		if (roles.size() > 0) {
			permissions.addAll(roles.stream().map(Role::getCode).collect(Collectors.toList()));
		} else {
			permissions.add(PermissionEnum.Role.NORMAL);
		}

		return permissions;
	}
}
