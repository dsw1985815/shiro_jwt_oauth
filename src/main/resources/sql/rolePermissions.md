deletePermissions
===
	delete
	from
		role_permissions
	where
		permission_id = #permissionId#
		and role_id = #roleId#
		and plat_type = #platType#
		@if(!isEmpty(method)){
		 and `method`=#method#
		@}
	
sample
===
* 注释

	select #use("cols")# from role_permissions where #use("condition")#

cols
===

	permission_id,role_id

updateSample
===

	`permission_id`=#permissionId#,`role_id`=#roleId#

condition
===

	1 = 1  
	@if(!isEmpty(permissionId)){
	 and `permission_id`=#permissionId#
	@}
	@if(!isEmpty(roleId)){
	 and `role_id`=#roleId#
	@}
	
