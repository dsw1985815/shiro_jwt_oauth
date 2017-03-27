deleteResource
===
	delete
	from
	permission_resources
	where
	1 = 1
	@if(!isEmpty(permissionId)){
	 and `permission_id`=#permissionId#
	@}
	@if(!isEmpty(resource)){
	 and `resource`=#resource#
	@}

sample
===
* 注释

	select #use("cols")# from permission_resources where #use("condition")#

cols
===

	permission_id,resource_id

updateSample
===

	`permission_id`=#permissionId#,`resource_id`=#resourceId#

condition
===

	1 = 1  
	@if(!isEmpty(permissionId)){
	 and `permission_id`=#permissionId#
	@}
	@if(!isEmpty(resourceId)){
	 and `resource_id`=#resourceId#
	@}
	
