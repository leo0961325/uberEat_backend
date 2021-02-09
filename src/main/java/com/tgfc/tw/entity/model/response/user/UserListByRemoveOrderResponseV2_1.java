package com.tgfc.tw.entity.model.response.user;

import com.tgfc.tw.entity.model.po.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserListByRemoveOrderResponseV2_1 {

	private int id;
	private String username;
	private String name;
	private String englishName;
	private String[] teamName;

	public UserListByRemoveOrderResponseV2_1(){
	}

	public UserListByRemoveOrderResponseV2_1(User user){
		setId(user.getId());
		setName(user.getName());
		setEnglishName(user.getEnglishName());
		setUsername(user.getUsername());
		List<String> teamNameList = user.getTeamList().stream().map(t -> t.getName()).collect(Collectors.toList());
		String[] teamName = teamNameList.stream().toArray(n->new String[n]);
		setTeamName(teamName);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String[] getTeamName() {
		return teamName;
	}

	public void setTeamName(String[] teamName) {
		this.teamName = teamName;
	}
}
