find
===
	select
	@pageTag(){
		*
	@}
 	from
	(
	SELECT
		t1.id id, 
		t1.password, 
		t1.user_account username,
		t1.phone,
		'user' plat_type, user_code
	FROM
		usercenter.user t1		
	UNION ALL
	SELECT
		t2.id, 
		MD5(t2.password) password, 
		t2.user_account username , 
		null,
		'console' plat_type, user_code
	FROM
		qh_database.qh_c_account t2
	) t
	where
		1=1
		@if(!isEmpty(username)){
			and username like concat(concat('%',#username#), '%')
		@}
		@if(!isEmpty(platType)){
			and plat_type = #platType#
		@}
		
selectUserByName
===
	select 
	@if(platType=='console'){
			id, MD5(password), user_account username , #platType# platType, user_code
		from
			qh_database.qh_c_account
		where user_account like concat(concat('%',#userName#), '%')
	@}
	@else{
			id, password, if(type=1, phone, user_account) username, #platType# platType ,phone, user_code
		from
			usercenter.user
		where if(type=1, phone, user_account) like concat(concat('%',#userName#), '%')
	@}
	
	
	
selectUserByPlat
===
	select 
	@if(platType=='console'){
			id, MD5(password), user_account username, #platType# platType , user_code
		from
			qh_database.qh_c_account
		where id = #id#
	@}
	@if(platType=='user'){
			id, password, if(type=1, phone, user_account) username, phone, #platType# 	platType , user_code
		from
			usercenter.user
		where id = #id#
	@}
	
sample
===
* 注释

	select #use("cols")# from user where #use("condition")#

cols
===

	id,guid,create_time,status,password,username,delete,phone,email,update_time,salt

updateSample
===

	`id`=#id#,`guid`=#guid#,`create_time`=#createTime#,`status`=#status#,`password`=#password#,`username`=#username#,`delete`=#delete#,`phone`=#phone#,`email`=#email#,`update_time`=#updateTime#,`salt`=#salt#

condition
===

	1 = 1  
	@if(!isEmpty(guid)){
	 and `guid`=#guid#
	@}
	@if(!isEmpty(createTime)){
	 and `create_time`=#createTime#
	@}
	@if(!isEmpty(status)){
	 and `status`=#status#
	@}
	@if(!isEmpty(password)){
	 and `password`=#password#
	@}
	@if(!isEmpty(username)){
	 and `username`=#username#
	@}
	@if(!isEmpty(delete)){
	 and `delete`=#delete#
	@}
	@if(!isEmpty(phone)){
	 and `phone`=#phone#
	@}
	@if(!isEmpty(email)){
	 and `email`=#email#
	@}
	@if(!isEmpty(updateTime)){
	 and `update_time`=#updateTime#
	@}
	@if(!isEmpty(salt)){
	 and `salt`=#salt#
	@}
	
