class AccessionsIPStatusReport < JDBCReport 
	register_report({ 
		:uri_suffix => "accessions_ip_status", 
		:description => "Accessions IP Status Report", 
	})
end 
