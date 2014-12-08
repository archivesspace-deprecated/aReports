class AccessionsCatalogedReport < JDBCReport 
	register_report({ 
		:uri_suffix => "accessions_cataloged", 
		:description => "Accessions Cataloged Report", 
	})
end 
