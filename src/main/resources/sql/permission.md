findAll
===
	select
	@pageTag(){
		id,permission,permission_code
	@}
	from
		permission
	where
		1=1
		@if(!isEmpty(permissionCode)){
		 and `permission_code`=#permissionCode#
		@}
		@if(!isEmpty(permission)){
		 and `permission` like concat(concat('%',#permission#), '%')
		@}

selectRolePermissionByPlat
===
	select 
		id,permission_code,permission,method
	from
		permission t1,
		role_permissions t2
	where
		t1.id = t2.permission_id
		and
		t2.role_id = #id#
		and
		t2.plat_type = #platType#

selectPermissionResource
===
	select permission
	from
		permission_resources
	where
		permission_id = #id#

sample
===
* 注释

	select #use("cols")# from permission where #use("condition")#

cols
===

	id,permission_code,permission

updateSample
===

	`id`=#id#,`permission_code`=#permissionCode#,`permission`=#permission#

condition
===

	1 = 1  
	@if(!isEmpty(permissionCode)){
	 and `permission_code`=#permissionCode#
	@}
	@if(!isEmpty(permission)){
	 and `permission`=#permission#
	@}
	
