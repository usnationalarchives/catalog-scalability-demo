-- Table: das_desc."1_access_restriction"

-- DROP TABLE das_desc."1_access_restriction";

CREATE TABLE das_desc."1_access_restriction"
(
  naid double precision, -- "Access Restriction needs its own table because each unit of AR includes 3 Authority list links and single description can have  up to 20 specific access restriction links based on current prod data. Hence,...
  access_restriction_status_naid double precision,
  access_restriction_note text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_access_restriction"
  OWNER TO das_desc;
COMMENT ON COLUMN das_desc."1_access_restriction".naid IS '"Access Restriction needs its own table because each unit of AR includes 3 Authority list links and single description can have  up to 20 specific access restriction links based on current prod data. Hence,
including it as part of description table as a json array would not be the most efficient model"
';
--------------------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_authoritylist"

-- DROP TABLE das_desc."1_authoritylist";

CREATE TABLE das_desc."1_authoritylist"
(
  naid double precision,
  term_name text,
  auth_details jsonb,
  auth_type text,
  created_date timestamp with time zone,
  imported_date timestamp with time zone,
  lastchanged_date timestamp with time zone,
  lastapproved_date timestamp with time zone,
  lastbroughtunderedit timestamp with time zone,
  approvalhistory json[],
  changedhistory json[],
  beought_under_edit_history json[]
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_authoritylist"
  OWNER TO das_desc;
----------------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_creating_org_indv"

-- DROP TABLE das_desc."1_creating_org_indv";

CREATE TABLE das_desc."1_creating_org_indv"
(
  desc_naid double precision,
  auth_naid double precision,
  creator_type_naid double precision,
  creatingind_creatingorg boolean
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_creating_org_indv"
  OWNER TO das_desc;
---------------------------------------------------------------------------------------------------------------------------
-- Table: das_desc."1_description"

-- DROP TABLE das_desc."1_description";

CREATE TABLE das_desc."1_description"
(
  naid double precision,
  data_ctl_gp jsonb,
  record_gp_no text,
  "collection_Id" text,
  other_title text,
  inclusive_start_dt_qualifier_naid double precision,
  inclusive_end_dt_qualifier_naid double precision,
  title text,
  inclusive_start_date jsonb,
  coverage_start_dt_qualifier_naid double precision,
  coverage_end_dt_qualifier_naid double precision,
  inclusive_end_date jsonb,
  coverage_start_date jsonb,
  coverage_end_date jsonb,
  date_note text,
  scope_content_note text,
  staff_only_note text,
  record_status boolean,
  party_designation_naid double precision,
  description_author jsonb[],
  begin_congress_naid double precision,
  end_congress_naid double precision,
  arrangement text,
  function_user text,
  general_note text[],
  "local_Id" text,
  numbering_note text,
  accession_number_naid double precision,
  record_ctr_xfer_number_naid double precision,
  disposition_auth_number_naid double precision,
  internal_xfer_number_naid double precision,
  xfer_note text,
  custodial_history_note text,
  online_resource_naid double precision,
  scale_note text,
  edit_status_naid double precision,
  sound_type_naid double precision,
  total_footage numeric,
  "a/v" boolean,
  ptr_obj_availability_date jsonb,
  production_date jsonb,
  copyright_date jsonb,
  subtitle text,
  release_date jsonb,
  broadcast_date jsonb,
  shortlist text,
  broadcast_date_qualifier_naid double precision,
  release_date_qualifier_naid double precision,
  copyright_date_qualifier_naid double precision,
  production_date_qualifier_naid double precision,
  ptr_obj_availability_date_qualifier_naid double precision
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_description"
  OWNER TO das_desc;
--------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_dgtobj"

-- DROP TABLE das_desc."1_dgtobj";

CREATE TABLE das_desc."1_dgtobj"
(
  naid double precision, -- "Microform publication needs its own table because single description can have  over 4000 digital objects based on current prod data. Hence,...
  "objectID" double precision,
  object_type_naid double precision,
  object_description text,
  object_designator text,
  label_flag text,
  locate_by text,
  access_filename text,
  access_file_size text,
  thumbnail_filename text,
  thumbnail_file_size numeric,
  "projectID" text,
  imported date,
  status text,
  display text,
  in_database text,
  original_process text,
  scanning_color text,
  original_width numeric,
  scanning_dimensions numeric,
  server_name text,
  version numeric,
  scanning_process text,
  scanning_medium text,
  scanning_medium_category text,
  original_orientation text,
  masterfile_size numeric,
  master_media_backup text,
  master_media_primary text,
  digital_object_translation text[],
  master_derivation_file_media text,
  master_filename text,
  digital_object_transcript text,
  original_dimension numeric,
  original_height numeric,
  original_medium text,
  batach_number numeric,
  batch_date date,
  original_color text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_dgtobj"
  OWNER TO das_desc;
COMMENT ON COLUMN das_desc."1_dgtobj".naid IS '"Microform publication needs its own table because single description can have  over 4000 digital objects based on current prod data. Hence,
including it as part of description table as a json array would not be the most efficient model"
';

--------------------------------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_finding_aid"

-- DROP TABLE das_desc."1_finding_aid";

CREATE TABLE das_desc."1_finding_aid"
(
  naid double precision, -- "Finding Aid needs its own table because each unit of FA includes 3 Authority list links and single description can have  up to 20 FA links based on current prod data. Hence,...
  finding_aid_type_naid double precision,
  finding_aid_note text,
  finding_aid_url_naid text,
  object_type_naid double precision
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_finding_aid"
  OWNER TO das_desc;
COMMENT ON COLUMN das_desc."1_finding_aid".naid IS '"Finding Aid needs its own table because each unit of FA includes 3 Authority list links and single description can have  up to 20 FA links based on current prod data. Hence,
including it as part of description table as a json array would not be the most efficient model"
';

------------------------------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_general_media_type"

-- DROP TABLE das_desc."1_general_media_type";

CREATE TABLE das_desc."1_general_media_type"
(
  desc_naid double precision,
  general_media_type_naid double precision
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_general_media_type"
  OWNER TO das_desc;

------------------------------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_general_record_type"

-- DROP TABLE das_desc."1_general_record_type";

CREATE TABLE das_desc."1_general_record_type"
(
  desc_naid double precision,
  general_record_type_naid double precision
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_general_record_type"
  OWNER TO das_desc;

----------------------------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_holding_measurment"

-- DROP TABLE das_desc."1_holding_measurment";

CREATE TABLE das_desc."1_holding_measurment"
(
  "physical_occurenceID" double precision,
  desc_naid double precision, -- It needs its separate talbe because in prod today, one description (across physical occurrences) can be linked to up to 50 holding measurements and, hence, needs to own table.
  holding_measurement_type_naid double precision,
  holding_measurment_count text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_holding_measurment"
  OWNER TO das_desc;
COMMENT ON COLUMN das_desc."1_holding_measurment".desc_naid IS 'It needs its separate talbe because in prod today, one description (across physical occurrences) can be linked to up to 50 holding measurements and, hence, needs to own table.';

---------------------------------------------------------------------------------------------------------------------------------------------------
-- Table: das_desc."1_language"

-- DROP TABLE das_desc."1_language";

CREATE TABLE das_desc."1_language"
(
  desc_naid double precision, -- It needs a separate table bacause in prod today, one description (can be linked to up to 60 languages) and hence, needs it's own table.
  language_naid double precision
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_language"
  OWNER TO das_desc;
COMMENT ON COLUMN das_desc."1_language".desc_naid IS 'It needs a separate table bacause in prod today, one description (can be linked to up to 60 languages) and hence, needs it''s own table.';

------------------------------------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_location"

-- DROP TABLE das_desc."1_location";

CREATE TABLE das_desc."1_location"
(
  desc_naid double precision,
  location_facility_naid double precision,
  note text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_location"
  OWNER TO das_desc;

----------------------------------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_media_occurence"

-- DROP TABLE das_desc."1_media_occurence";

CREATE TABLE das_desc."1_media_occurence"
(
  physical_occurence double precision,
  naid double precision,
  specific_media_type_naid double precision,
  "containerID" text,
  media_occurence_node text,
  physical_restriction_note text,
  technical_access_requirement_note text,
  piece_count numeric,
  reproduction_count numeric,
  dimension_naid double precision,
  height numeric,
  width numeric,
  depth numeric,
  color_naid double precision,
  process_naid double precision,
  base_naid double precision,
  emulsion_naid double precision
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_media_occurence"
  OWNER TO das_desc;

--------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_microform_pub"

-- DROP TABLE das_desc."1_microform_pub";

CREATE TABLE das_desc."1_microform_pub"
(
  naid double precision, -- "Microform publication needs its own table because single description can have  up to 54 MP title links based on current prod data. Hence,...
  microform_pub_title_naid double precision,
  microform_pub_note text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_microform_pub"
  OWNER TO das_desc;
COMMENT ON COLUMN das_desc."1_microform_pub".naid IS '"Microform publication needs its own table because single description can have  up to 54 MP title links based on current prod data. Hence,
including it as part of description table as a json array would not be the most efficient model"
';

---------------------------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_org_personal_contributor"

-- DROP TABLE das_desc."1_org_personal_contributor";

CREATE TABLE das_desc."1_org_personal_contributor"
(
  desc_naid double precision,
  auth_naid double precision, -- NAID of the linked contributor
  contributor_type_naid double precision, -- Indicates what type of authority reference organizational contributor or personal contributor
  personal_or_organizational_contributor boolean -- Indicates what type of authority referene prganisational contributor or personal contributor
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_org_personal_contributor"
  OWNER TO das_desc;
COMMENT ON COLUMN das_desc."1_org_personal_contributor".auth_naid IS 'NAID of the linked contributor';
COMMENT ON COLUMN das_desc."1_org_personal_contributor".contributor_type_naid IS 'Indicates what type of authority reference organizational contributor or personal contributor';
COMMENT ON COLUMN das_desc."1_org_personal_contributor".personal_or_organizational_contributor IS 'Indicates what type of authority referene prganisational contributor or personal contributor';

------------------------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_other_preservation_characteristic"

-- DROP TABLE das_desc."1_other_preservation_characteristic";

CREATE TABLE das_desc."1_other_preservation_characteristic"
(
  desc_naid double precision,
  other_pres_char_naid double precision
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_other_preservation_characteristic"
  OWNER TO das_desc;

-------------------------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_pers_org_topsub_geo_sppr_ref"

-- DROP TABLE das_desc."1_pers_org_topsub_geo_sppr_ref";

CREATE TABLE das_desc."1_pers_org_topsub_geo_sppr_ref"
(
  desc_naid double precision,
  auth_naid double precision, -- NAID of the linked authority
  auth_reference_type_donor text -- Indicates what type of authority reference or donor. Specific records type, Topical Subject, Person, Organization, Geographic Place Name, organization donor or personal donor.
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_pers_org_topsub_geo_sppr_ref"
  OWNER TO das_desc;
COMMENT ON COLUMN das_desc."1_pers_org_topsub_geo_sppr_ref".auth_naid IS 'NAID of the linked authority';
COMMENT ON COLUMN das_desc."1_pers_org_topsub_geo_sppr_ref".auth_reference_type_donor IS 'Indicates what type of authority reference or donor. Specific records type, Topical Subject, Person, Organization, Geographic Place Name, organization donor or personal donor.';

-------------------------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_physical_occurence"

-- DROP TABLE das_desc."1_physical_occurence";

CREATE TABLE das_desc."1_physical_occurence"
(
  "physical_occuranceID" double precision,
  naid double precision,
  copy_status_naid double precision,
  extent text,
  physical_occurance_note text,
  gpra_indicators boolean,
  container_list text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_physical_occurence"
  OWNER TO das_desc;

----------------------------------------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_primauth"

-- DROP TABLE das_desc."1_primauth";

CREATE TABLE das_desc."1_primauth"
(
  naid double precision,
  term_name text,
  auth_type text,
  name_heading text,
  full_name text,
  numerator text,
  personal_title text,
  birthdate_qualifier_naid double precision,
  deathdate_qualifier_naid double precision,
  birthdate jsonb,
  deathdate jsonb,
  biographical_note text,
  source_note text[],
  proposer_name text,
  proposal_date date,
  reference_unit_naid double precision,
  record_source_naid double precision,
  naco_submitted boolean,
  import_rec_ctl_no text,
  notes text,
  scope_note text,
  saco_submitted boolean,
  lat_long point,
  admin_hist_note text,
  establish_date json,
  abolish_date jsonb,
  establish_date_qualifier_naid double precision,
  abolish_date_qualifier_naid double precision,
  lastchanged_date timestamp with time zone,
  lastapproved_date timestamp with time zone,
  lastbroughtunderedit timestamp with time zone,
  approvalhistory json[],
  changehistory json[],
  brought_under_edit_history json[]
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_primauth"
  OWNER TO das_desc;

------------------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_reference_unit"

-- DROP TABLE das_desc."1_reference_unit";

CREATE TABLE das_desc."1_reference_unit"
(
  desc_naid double precision,
  reference_naid double precision
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_reference_unit"
  OWNER TO das_desc;

---------------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_special_pjt_for_auth"

-- DROP TABLE das_desc."1_special_pjt_for_auth";

CREATE TABLE das_desc."1_special_pjt_for_auth"
(
  authority_naid double precision,
  special_pjt_naid double precision
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_special_pjt_for_auth"
  OWNER TO das_desc;

------------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_special_pjt_for_desc"

-- DROP TABLE das_desc."1_special_pjt_for_desc";

CREATE TABLE das_desc."1_special_pjt_for_desc"
(
  desc_naid double precision,
  special_pjt_naid double precision
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_special_pjt_for_desc"
  OWNER TO das_desc;
---------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_specific_access_restriction"

-- DROP TABLE das_desc."1_specific_access_restriction";

CREATE TABLE das_desc."1_specific_access_restriction"
(
  desc_naid double precision,
  specific_access_restriction_naid double precision,
  "access_restriction_ID" double precision,
  security_class_naid double precision
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_specific_access_restriction"
  OWNER TO das_desc;

-------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_specific_use_restriction"

-- DROP TABLE das_desc."1_specific_use_restriction";

CREATE TABLE das_desc."1_specific_use_restriction"
(
  desc_naid double precision,
  speific_use_restriction_naid double precision,
  "use_restriction_ID" double precision
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_specific_use_restriction"
  OWNER TO das_desc;

-------------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_use_restrictions"

-- DROP TABLE das_desc."1_use_restrictions";

CREATE TABLE das_desc."1_use_restrictions"
(
  desc_naid double precision,
  use_restriction_status_naid double precision,
  note text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_use_restrictions"
  OWNER TO das_desc;

-----------------------------------------------------------------------------------------------------------------

-- Table: das_desc."1_variant_control_number"

-- DROP TABLE das_desc."1_variant_control_number";

CREATE TABLE das_desc."1_variant_control_number"
(
  naid double precision, -- "Variant Control Number needs its own table because single description can have  up to 4000 VCN links based on current prod data. Hence,...
  variant_ctl_no_type_naid double precision,
  variant_ctl_no text,
  variant_ctl_no_note text
)
WITH (
  OIDS=FALSE
);
ALTER TABLE das_desc."1_variant_control_number"
  OWNER TO das_desc;
COMMENT ON COLUMN das_desc."1_variant_control_number".naid IS '"Variant Control Number needs its own table because single description can have  up to 4000 VCN links based on current prod data. Hence,
including it as part of description table as a json array would not be the most efficient model"
';

-----------------------------------------------------------------------------------------------------------------

Table Count is ***-- 25 Create Table Statements --***




