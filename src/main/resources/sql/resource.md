findResource
===
	select
	@pageTag(){
		*
	@}
	from
		resource t1,
		permission_resources t2
	where
		resource_path = resource
		@if(!isEmpty(resource)){
		 and `resource_path` like concat(concat('%',#resource#), '%')
		@}
		@if(!isEmpty(permissionId)){
		 and `permission_id`=#permissionId#
		@}

find
===
	select
	@pageTag(){
		*
	@}
	from
		resource
	where
		1 = 1  
		@if(!isEmpty(resourcePath)){
		 and `resource_path` like concat(concat('%',#resourcePath#), '%')
		@}
		@if(!isEmpty(tokenTypeId)){
		 and `token_type_id`=#tokenTypeId#
		@}

queryResourceDetail
===
	select
		t1.*,
		group_concat(t3.permission) permStr
	from
		resource t1
	left join permission_resources t2
		on t1.resource_path = t2.resource
	left join permission t3
		on t2.permission_id = t3.id
	group by
		t1.resource_path


sample
===
* 注释

	select #use("cols")# from resource where #use("condition")#

cols
===

	id,resource_path,token_type_id,allow_reset_header,default_permission

updateSample
===

	`id`=#id#,`resource_path`=#resourcePath#

condition
===

	1 = 1  
	@if(!isEmpty(resourcePath)){
	 and `resource_path`=#resourcePath#
	@}
	
getResourceDetail
===

	select 
		#use("cols")#
	from resource 
	where
	`resource_path`=#resourcePath#
	@ orm.single({"tokenTypeId":"id"},"TokenType");
