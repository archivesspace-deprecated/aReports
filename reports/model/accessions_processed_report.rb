class AccessionsProcessedReport < JDBCReport 
	register_report({ 
		:uri_suffix => "accessions_processed", 
		:description => "Accessions Processed Report", 
	})
end 
