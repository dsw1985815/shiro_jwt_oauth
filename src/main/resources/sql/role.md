find
===
	select
	@pageTag(){
		*
	@}
 	from
	(
	SELECT
		id,
			'console' plat_type,
			t2.role_name role_name
		FROM
			qh_database.qh_c_role t2
	UNION ALL
	SELECT
		id,
			'user' plat_type,
			t2.role_name role_name
		FROM
			qh_database.qh_user_role  t2
	) t
	where
		1=1
		@if(!isEmpty(roleName)){
			and role_name = #roleName#
		@}
		@if(!isEmpty(platType)){
			and plat_type = #platType#
		@}

selectRoleByPlat
===
	select
	@if(platType=='console'){
			id,
			'console' plat_type,
			t2.role_name role_name
		FROM
			qh_database.qh_c_role t2
		WHERE
			t2.id = #id#
	@}
	@if(platType=='user'){
			id,
			'user' plat_type,
			t2.role_name role_name
		FROM
			qh_database.qh_user_role  t2
		WHERE
			t2.id = #id#
	@}

findOtherRole
===
	select * from
	(
	select
		t1.role_name
	from
		qh_database.qh_c_role t1
	union all
	select
		t2.role_name
	from
		qh_database.qh_user_role t2
	) t
	where
	1 = 1  
	@if(!isEmpty(roleName)){
	 and `role_name`=#roleName#
	@}

selectUserRoleByPlat
===
	select
	@if(platType=='console'){
			t2.id,
			'console' plat_type,
			t2.role_name role_name,
			t1.user_code
		FROM
			qh_database.qh_c_account t1,
			qh_database.qh_c_role t2,
			qh_database.qh_role_account t3
		WHERE
			t1.id = t3.account_id
		AND
			t3.role_id = t2.id
		AND
			t1.id = #id#
	@}
	@if(platType=='user'){
			t2.id,
			'user' plat_type,
			t2.role_name role_name,
			t1.user_code
		FROM
			qh_database.qh_user t1,
			qh_database.qh_user_role  t2
		WHERE
			t1.user_role = t2.id
		AND
			t1.uuid = (SELECT uuid FROM usercenter.user WHERE id = #id#)
	@}
	
	
sample
===
* 注释 

	select #use("cols")# from role where #use("condition")#

cols
===

	id,create_time,role_name

updateSample
===

	`id`=#id#,`role_name`=#roleName#,`role_code`=#roleCode#

condition
===

	1 = 1  
	@if(!isEmpty(roleName)){
	 and `role_name`=#roleName#
	@}
	

