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
        
        -- The three select statements can be combined into 1 query by for clarity 
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
