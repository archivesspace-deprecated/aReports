class AccessionsReceiptReport < JDBCReport 
	register_report({ 
		:uri_suffix => "accessions_receipt", 
		:description => "Accessions Receipt Report", 
	})
end 
