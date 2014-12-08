class AccessionsInventoryReport < JDBCReport 
	register_report({ 
		:uri_suffix => "accessions_inventory", 
		:description => "Accessions Inventory Report", 
	})
end 
