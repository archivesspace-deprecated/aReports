-- Function to return the term type given a subject id
-- It currently just returns the term type for the 
-- first term.
DROP FUNCTION IF EXISTS GetTermType;

DELIMITER $$

CREATE FUNCTION GetTermType(f_subject_id INT) 
	RETURNS VARCHAR(255)
BEGIN
	DECLARE f_term_type VARCHAR(255) DEFAULT "";	
	
	SELECT enumeration_value.`value` INTO f_term_type 
	FROM term 
	INNER JOIN enumeration_value 
	ON term.`term_type_id` = enumeration_value.`id` 
	WHERE term.`id`  
	IN (SELECT subject_term.`term_id` 
		FROM subject_term 
		WHERE subject_term.`subject_id` = f_subject_id)  
	LIMIT 1;
	
	RETURN f_term_type;
END $$

DELIMITER ;

-- Function to return the parent resource record id if 
-- resource, or archival_object id is passed in.
DROP FUNCTION IF EXISTS GetResourceId;

DELIMITER $$

CREATE FUNCTION GetResourceId(f_resource_id INT, f_archival_object_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_root_record_id INT;	
	
	IF f_resource_id IS NOT NULL THEN
		SET f_root_record_id = f_resource_id;
	ELSE
		SELECT archival_object.`root_record_id` INTO f_root_record_id 
		FROM archival_object 
		WHERE archival_object.`id` = f_archival_object_id;  
	END IF;
	
	RETURN f_root_record_id;
END $$

DELIMITER ;

-- Function to return the parent digital object record id if 
-- digital_object, or digital_object_component id is passed in.
DROP FUNCTION IF EXISTS GetDigitalObjectId;

DELIMITER $$

CREATE FUNCTION GetDigitalObjectId(f_digital_object_id INT, f_digital_component_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_root_record_id INT;	
	
	IF f_digital_object_id IS NOT NULL THEN
		SET f_root_record_id = f_digital_object_id;
	ELSE
		SELECT digital_object_component.`root_record_id` INTO f_root_record_id 
		FROM digital_object_component 
		WHERE digital_object_component.`id` = f_digital_component_id;  
	END IF;
	
	RETURN f_root_record_id;
END $$

DELIMITER ;

-- Function to return a coordinate string by concating the three 
-- coordinate labels and indicators
DROP FUNCTION IF EXISTS GetCoordinate;

DELIMITER $$

CREATE FUNCTION GetCoordinate(f_location_id INT) 
	RETURNS VARCHAR(1020)
BEGIN
	DECLARE f_coordinate VARCHAR(1020);	
        DECLARE f_coordinate_1 VARCHAR(255);
        DECLARE f_coordinate_2 VARCHAR(255);
        DECLARE f_coordinate_3 VARCHAR(255);
        
        -- The three select statements can be combined into 1 query, but for clarity 
        -- are left separate
	SELECT CONCAT(location.`coordinate_1_label`, ' ', location.`coordinate_1_indicator`)  
                INTO f_coordinate_1 
		FROM location 
		WHERE location.`id` = f_location_id;
	
        SELECT CONCAT(location.`coordinate_2_label`, ' ', location.`coordinate_2_indicator`)  
                INTO f_coordinate_2 
		FROM location 
		WHERE location.`id` = f_location_id;

        SELECT CONCAT(location.`coordinate_3_label`, ' ', location.`coordinate_3_indicator`)  
                INTO f_coordinate_3 
		FROM location 
		WHERE location.`id` = f_location_id; 
        
        SET f_coordinate = CONCAT_WS('/', f_coordinate_1, f_coordinate_2, f_coordinate_3);
        
	RETURN f_coordinate;
END $$

DELIMITER ;


-- Function to return the number of resources for a particular repository
DROP FUNCTION IF EXISTS GetEnumValue;

DELIMITER $$

CREATE FUNCTION GetEnumValue(f_enum_id INT) 
	RETURNS VARCHAR(255)
BEGIN
	DECLARE f_value VARCHAR(255);	
	
	SELECT enumeration_value.`value`INTO f_value
	FROM enumeration_value
	WHERE enumeration_value.`id` = f_enum_id;
	    
	RETURN f_value;
END $$

DELIMITER ;

-- Function to return the number of resources for a particular repository
DROP FUNCTION IF EXISTS GetTotalResources;

DELIMITER $$

CREATE FUNCTION GetTotalResources(f_repo_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT;	
	
	SELECT COUNT(id) INTO f_total 
	FROM resource 
	WHERE resource.`repo_id` = f_repo_id;
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the number of resources with level = item for a 
-- particular repository
DROP FUNCTION IF EXISTS GetTotalResourcesItems;

DELIMITER $$

CREATE FUNCTION GetTotalResourcesItems(f_repo_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT;	
	
	SELECT COUNT(id) INTO f_total 
	FROM resource 
	WHERE (resource.`repo_id` = f_repo_id
	AND 
	GetEnumValue(resource.`level_id`) = 'item' COLLATE utf8_general_ci);
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the number of resources with restrictions for a 
-- particular repository
DROP FUNCTION IF EXISTS GetResourcesWithRestrictions;

DELIMITER $$

CREATE FUNCTION GetResourcesWithRestrictions(f_repo_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT;	
	
	SELECT COUNT(id) INTO f_total 
	FROM resource 
	WHERE (resource.`repo_id` = f_repo_id
	AND 
	resource.`restrictions` = 1);
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the number of resources with finding aids for a 
-- particular repository
DROP FUNCTION IF EXISTS GetResourcesWithFindingAids;

DELIMITER $$

CREATE FUNCTION GetResourcesWithFindingAids(f_repo_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT;	
	
	SELECT COUNT(id) INTO f_total 
	FROM resource 
	WHERE (resource.`repo_id` = f_repo_id
	AND 
	resource.`ead_id` IS NOT NULL);
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the number of accessions for a particular repository
DROP FUNCTION IF EXISTS GetTotalAccessions;

DELIMITER $$

CREATE FUNCTION GetTotalAccessions(f_repo_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT;	
	
	SELECT COUNT(id) INTO f_total 
	FROM accession 
	WHERE accession.`repo_id` = f_repo_id;
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the number of accessions that are processed for
-- a particular repository
DROP FUNCTION IF EXISTS GetAccessionsProcessed;

DELIMITER $$

CREATE FUNCTION GetAccessionsProcessed(f_repo_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT;	
	
	SELECT count(T1.id) INTO f_total  
	FROM accession T1 
	INNER JOIN 
		collection_management T2 ON T1.id = T2.accession_id 
	WHERE (T1.repo_id = f_repo_id  
	AND GetEnumValue(T2.processing_status_id) = 'completed' COLLATE utf8_general_ci);
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the number of accessions that are cataloged for
-- a particular repository
DROP FUNCTION IF EXISTS GetAccessionsCataloged;

DELIMITER $$

CREATE FUNCTION GetAccessionsCataloged(f_repo_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT;	
	
	SELECT count(T2.accession_id) INTO f_total  
	FROM event T1 
	INNER JOIN 
		event_link_rlshp T2 ON T1.id = T2.event_id 
	WHERE (T1.repo_id = f_repo_id  
	AND T2.accession_id IS NOT NULL 
	AND GetEnumValue(T1.event_type_id) = 'cataloged' COLLATE utf8_general_ci)
	GROUP BY T2.accession_id;
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the if an accessions has been cataloged
DROP FUNCTION IF EXISTS GetAccessionCataloged;

DELIMITER $$

CREATE FUNCTION GetAccessionCataloged(f_accession_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT DEFAULT 0;	
	
	SELECT COUNT(T1.id) INTO f_total 
	FROM event T1 
	INNER JOIN 
		event_link_rlshp T2 ON T1.id = T2.event_id 
	WHERE (
		T2.accession_id = f_accession_id 
	AND 
		GetEnumValue(T1.event_type_id) = 'cataloged' COLLATE utf8_general_ci);
	    
	RETURN f_total;
END $$

DELIMITER ;


-- Function to return the number of accessions with restrictions for a 
-- particular repository
DROP FUNCTION IF EXISTS GetAccessionsWithRestrictions;

DELIMITER $$

CREATE FUNCTION GetAccessionsWithRestrictions(f_repo_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT;	
	
	SELECT COUNT(id) INTO f_total 
	FROM accession 
	WHERE (accession.`repo_id` = f_repo_id
	AND 
	accession.`use_restrictions` = 1);
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the number of accessions that have had rights transferred
-- for a particular repository
DROP FUNCTION IF EXISTS GetAccessionsWithRightsTransfered;

DELIMITER $$

CREATE FUNCTION GetAccessionsWithRightsTransfered(f_repo_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT;	
	
	SELECT count(T2.accession_id) INTO f_total  
	FROM event T1 
	INNER JOIN 
		event_link_rlshp T2 ON T1.id = T2.event_id 
	WHERE (T1.repo_id = f_repo_id  
	AND T2.accession_id IS NOT NULL 
	AND GetEnumValue(T1.event_type_id) = 'rights_transferred' COLLATE utf8_general_ci)
	GROUP BY T2.accession_id;
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the number of personal agent records
DROP FUNCTION IF EXISTS GetAgentsPersonal;

DELIMITER $$

CREATE FUNCTION GetAgentsPersonal(f_repo_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT;	
	
	SELECT COUNT(id) INTO f_total 
	FROM agent_person
	WHERE agent_person.`id` NOT IN (
		SELECT user.`agent_record_id` 
		FROM
		user WHERE 
		user.`agent_record_id` IS NOT NULL);
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the number of corporate agent records
DROP FUNCTION IF EXISTS GetAgentsCorporate;

DELIMITER $$

CREATE FUNCTION GetAgentsCorporate(f_repo_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT;	
	
	SELECT COUNT(id) INTO f_total 
	FROM agent_corporate_entity 
	WHERE agent_corporate_entity.`publish` IS NOT NULL;
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the number of family agent records
DROP FUNCTION IF EXISTS GetAgentsFamily;

DELIMITER $$

CREATE FUNCTION GetAgentsFamily(f_repo_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT;	
	
	SELECT COUNT(id) INTO f_total 
	FROM agent_family;
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the number of software agent records
DROP FUNCTION IF EXISTS GetAgentsSoftware;

DELIMITER $$

CREATE FUNCTION GetAgentsSoftware(f_repo_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT;	
	
	SELECT COUNT(id) INTO f_total 
	FROM agent_software
	WHERE agent_software.`system_role` = 'none';
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the number of subject records
DROP FUNCTION IF EXISTS GetTotalSubjects;

DELIMITER $$

CREATE FUNCTION GetTotalSubjects(f_repo_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT;	
	
	SELECT COUNT(id) INTO f_total 
	FROM subject;
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the number of resource records for a particular finding
-- aid status
DROP FUNCTION IF EXISTS GetStatusCount;

DELIMITER $$

CREATE FUNCTION GetStatusCount(f_repo_id INT, f_status_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT;	
	
	SELECT COUNT(id) INTO f_total 
	FROM 
		resource
	WHERE 
		resource.`finding_aid_status_id` = f_status_id
		AND
		resource.`repo_id` = f_repo_id;
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the number of resource records for a particular language
-- code
DROP FUNCTION IF EXISTS GetLanguageCount;

DELIMITER $$

CREATE FUNCTION GetLanguageCount(f_repo_id INT, f_language_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT;	
	
	SELECT COUNT(id) INTO f_total 
	FROM 
		resource
	WHERE 
		resource.`language_id` = f_language_id
		AND
		resource.`repo_id` = f_repo_id;
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the number of instances for a particular instance
-- type in a repository. I coudn't find a simple way to do this counting
DROP FUNCTION IF EXISTS GetInstanceCount;

DELIMITER $$

CREATE FUNCTION GetInstanceCount(f_repo_id INT, f_instance_type_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT DEFAULT 0;
	DECLARE f_id INT;	
	DECLARE done INT DEFAULT 0;
	
	DECLARE cur CURSOR FOR SELECT T1.`id`  
	FROM 
		resource T1
	INNER JOIN
    		instance T2 ON GetResourceId(T2.`resource_id`, T2.`archival_object_id`) = T1.`id`
    WHERE 
		T1.`repo_id` = f_repo_id
		AND
    		T2.`instance_type_id` = f_instance_type_id 
	GROUP BY
		T1.`id`;	
	
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;
	
	OPEN cur;
	
	count_resource: LOOP
		FETCH cur INTO f_id;
	
		IF done = 1 THEN
			LEAVE count_resource;
		END IF;
		
		SET f_total = f_total + 1;
	
	END LOOP count_resource;
	
	CLOSE cur;
	    
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the total extent of unprocessed accessions that
-- for a particular repository
DROP FUNCTION IF EXISTS GetAccessionsExtent;

DELIMITER $$

CREATE FUNCTION GetAccessionsExtent(f_repo_id INT, f_extent_type_id INT) 
	RETURNS DECIMAL(10,2)
BEGIN
	DECLARE f_total DECIMAL(10,2);	
	
	SELECT SUM(T1.number) INTO f_total  
	FROM extent T1 
	INNER JOIN 
		accession T2 ON T1.accession_id = T2.id 
	WHERE (T2.repo_id = f_repo_id   
		AND GetAccessionCataloged(T2.id) = 0
		AND T1.extent_type_id = f_extent_type_id);
	
	-- Check for null then set it to zero
	IF f_total IS NULL THEN
		SET f_total = 0;
	END IF;
	
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the total extent of resources and its' archival objects
-- for a particular repository
DROP FUNCTION IF EXISTS GetResourcesExtent;

DELIMITER $$

CREATE FUNCTION GetResourcesExtent(f_repo_id INT, f_extent_type_id INT) 
	RETURNS DECIMAL(10,2)
BEGIN
	DECLARE f_total DECIMAL(10,2);	
	
	SELECT SUM(T1.number) INTO f_total  
	FROM extent T1 
	INNER JOIN 
		resource T2 ON GetResourceId(T1.resource_id, T1.archival_object_id) = T2.id 
	WHERE (T2.repo_id = f_repo_id   
		AND T1.extent_type_id = f_extent_type_id);
	
	-- Check for null then set it to zero
	IF f_total IS NULL THEN
		SET f_total = 0;
	END IF;
	
	RETURN f_total;
END $$

DELIMITER ;

-- Function to return the number of subject records with a certain term type
DROP FUNCTION IF EXISTS GetTermTypeCount;

DELIMITER $$

CREATE FUNCTION GetTermTypeCount(f_term_type_id INT) 
	RETURNS INT
BEGIN
	DECLARE f_total INT DEFAULT 0;	
	
	SELECT COUNT(T1.`id`) INTO f_total 
	FROM 
		term T1
	INNER JOIN
	    subject_term T2 ON T1.`id` = T2.`term_id`
	WHERE
		T1.`term_type_id` = f_term_type_id
	GROUP BY 
	    T2.`subject_id`;
	
	RETURN f_total;
END $$

DELIMITER ;
