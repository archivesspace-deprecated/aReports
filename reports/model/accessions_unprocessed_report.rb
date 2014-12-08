class AccessionsUnprocessedReport < JDBCReport 
	register_report({ 
		:uri_suffix => "accessions_unprocessed", 
		:description => "Accessions Unprocessed Report", 
	})
end 
