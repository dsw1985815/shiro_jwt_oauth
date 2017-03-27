sample
===
* 注释

	select #use("cols")# from user_roles where #use("condition")#

cols
===

	role_id,user_id

updateSample
===

	`role_id`=#roleId#,`user_id`=#userId#

condition
===

	1 = 1  
	@if(!isEmpty(roleId)){
	 and `role_id`=#roleId#
	@}
	@if(!isEmpty(userId)){
	 and `user_id`=#userId#
	@}
	
