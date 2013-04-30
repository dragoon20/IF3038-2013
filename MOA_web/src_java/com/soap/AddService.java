package com.soap;

import javax.jws.WebService;

@WebService
public interface AddService 
{
	public boolean add_new_user(String username, String email, String password, 
							String fullname, String avatar, String birthdate);
	
	public int add_new_task(String token, String app_id, String nama_task, 
							String deadline, Integer id_kategori);
	
	public boolean add_assignee(String token, String app_id, 
							Integer id_task, String username);
	
	public boolean add_tag(String token, String app_id, 
							Integer id_task, String tag_name);
	
	public boolean add_new_attachment(String token, String app_id, 
							Integer id_task, String attachment);
	
	public int add_new_category(String token, String app_id, 
							String nama_kategori, String usernames_list);
	
	public int add_new_comment(String token, String app_id, 
							Integer id_task, String komentar);
}
