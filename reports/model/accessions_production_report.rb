class AccessionsProductionReport < JDBCReport 
	register_report({ 
		:uri_suffix => "accessions_production", 
		:description => "Accessions Production Report", 
	})
end 
