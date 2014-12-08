class RepositoryProfileReport < JDBCReport 
	register_report({ 
		:uri_suffix => "repository_profile", 
		:description => "Repository Profile Report", 
	})
end 
