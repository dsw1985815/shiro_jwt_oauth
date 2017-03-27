get
===
	select
	*
	from
	user_login_type
	where
	plat_type = #platType#

update
===
	insert
	into
	user_login_type
	(plat_type, login_type, login_plat, user_role)
	values
	(#platType#, #loginType#, #loginPlat#, #userRole#)
	on duplicate key update
	login_type = #loginType#