--
-- ER/Studio Data Architect 9.7 SQL Code Generation
-- Project :      PDAS_20_New_3.0
--
-- Date Created : Friday, October 27, 2017 16:00:43
-- Target DBMS : PostgreSQL 9.x
--

-- 
-- TABLE: access_restriction 
--

Set schema 'all_constraints';

CREATE TABLE access_restriction(
    access_restrictionid       bigserial    NOT NULL,
    desc_naid                  bigint         NOT NULL,
    access_restriction_note    text,
    auth_list_naid             bigint         NOT NULL,
    lifecycle_numberid         bigserial    NOT NULL,
    special_pjt_for_authid     bigserial    NOT NULL,
    prim_auth_naid             bigint         NOT NULL,
    CONSTRAINT access_restriction_pkey PRIMARY KEY (access_restrictionid, auth_list_naid, desc_naid, lifecycle_numberid, special_pjt_for_authid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: authority_list 
--

CREATE TABLE authority_list(
    auth_list_naid                bigint         NOT NULL,
    term_name                     text,
    auth_details                  jsonb,
    auth_type                     text,
    created_date                  timestamp,
    imported_date                 timestamp,
    last_changed_date             timestamp,
    last_approved_date            timestamp,
    last_brought_under_edit       timestamp,
    approval_history              json,
    changed_history               json,
    brought_under_edit_history    json,
    is_under_edit                 boolean,
    special_pjt_for_authid        bigserial    NOT NULL,
    lifecycle_numberid            bigserial    NOT NULL,
    desc_naid                     bigint         NOT NULL,
    prim_auth_naid                bigint         NOT NULL,
    CONSTRAINT authoritylist_pkey PRIMARY KEY (auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: broader_narrower_related_term 
--

CREATE TABLE broader_narrower_related_term(
    broader_narrower_related_termid    bigserial    NOT NULL,
    prim_auth_naid                     bigint         NOT NULL,
    bnr_term_naid                      bigint,
    term_type                          text,
    CONSTRAINT broader_narrower_related_term_pkey PRIMARY KEY (broader_narrower_related_termid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: creating_org_indv 
--

CREATE TABLE creating_org_indv(
    creating_org_indvid          bigserial    NOT NULL,
    creating_ind_creating_org    boolean,
    auth_list_naid               bigint         NOT NULL,
    prim_auth_naid               bigint         NOT NULL,
    desc_naid                    bigint         NOT NULL,
    lifecycle_numberid           bigserial    NOT NULL,
    special_pjt_for_authid       bigserial    NOT NULL,
    CONSTRAINT creating_org_indv_pkey PRIMARY KEY (creating_org_indvid, auth_list_naid, desc_naid, lifecycle_numberid, prim_auth_naid, special_pjt_for_authid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: description 
--

CREATE TABLE description(
    desc_naid                                   bigint              NOT NULL,
    desc_type                                   text,
    data_ctl_gp                                 jsonb,
    record_gp_no                                text,
    "collectionID"                              text,
    parent_naid                                 bigint,
    other_title                                 text,
    inclusive_start_date_qualifier_naid         bigint,
    inclusive_end_date_qualifier_naid           bigint,
    title                                       text,
    inclusive_start_date                        jsonb,
    coverage_start_date_qualifier_naid          bigint,
    coverage_end_date_qualifier_naid            bigint,
    inclusive_end_date                          jsonb,
    coverage_start_date                         jsonb,
    coverage_end_date                           jsonb,
    date_note                                   text,
    scope_content_note                          text,
    staff_note_only                             text,
    record_status                               boolean,
    party_designation_naid                      bigint,
    description_author                          jsonb,
    begin_congress_naid                         bigint,
    end_congress_naid                           bigint,
    arrangement                                 text,
    function_user                               text,
    general_note                                text,
    "local_Id"                                  text,
    numbering_note                              text,
    xfer_note                                   text,
    custodial_history_note                      text,
    online_resource_naid                        bigint,
    scale_note                                  text,
    edit_status_naid                            bigint,
    sound_type_naid                             bigint,
    total_footage                               numeric(10, 0),
    "a/v"                                       boolean,
    ptr_obj_availability_date                   jsonb,
    production_date                             jsonb,
    copyright_date                              jsonb,
    subtitle                                    text,
    release_date                                jsonb,
    broadcast_date                              jsonb,
    shortlist                                   text,
    broadcast_date_qualifier_naid               bigint,
    release_date_qualifier_naid                 bigint,
    copyright_date_qualifier_naid               bigint,
    production_date_qualifier_naid              float8,
    ptr_obj_availability_date_qualifier_naid    bigint,
    is_under_edit                               boolean,
    created_date                                timestamp,
    imported_date                               timestamp,
    last_approved_date                          timestamp,
    last_changed_date                           timestamp,
    last_brought_under_edit_date                timestamp,
    approval_history                            jsonb,
    change_history                              jsonb,
    brought_under_edit_history                  jsonb,
    created_user                                text,
    imported_user                               text,
    last_changed_user                           text,
    last_approved_user                          text,
    last_brought_under_edit_user                text,
    CONSTRAINT description_pkey PRIMARY KEY (desc_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: digital_object 
--

CREATE TABLE digital_object(
    digital_objectid                bigserial         NOT NULL,
    object_description              text,
    object_designator               text,
    label_flag                      text,
    locate_by                       text,
    access_filename                 text,
    access_file_size                text,
    thumbnail_filename              text,
    thumbnail_file_size             numeric(10, 0),
    "projectID"                     text,
    imported                        date,
    status                          text,
    display                         text,
    in_database                     text,
    original_process                text,
    scanning_color                  text,
    original_width                  numeric(10, 0),
    scanning_dimensions             numeric(10, 0),
    server_name                     text,
    version                         numeric(10, 0),
    scanning_process                text,
    scanning_medium                 text,
    scanning_medium_category        text,
    original_orientation            text,
    masterfile_size                 numeric(10, 0),
    master_media_backup             text,
    master_media_primary            text,
    digital_object_translation      text,
    master_derivation_file_media    text,
    master_filename                 text,
    digital_object_transcript       text,
    original_dimension              numeric(10, 0),
    original_height                 numeric(10, 0),
    original_medium                 text,
    batch_number                    numeric(10, 0),
    batch_date                      date,
    original_color                  text,
    desc_naid                       bigint              NOT NULL,
    auth_list_naid                  bigint              NOT NULL,
    lifecycle_numberid              bigserial         NOT NULL,
    special_pjt_for_authid          bigserial         NOT NULL,
    prim_auth_naid                  bigint              NOT NULL,
    CONSTRAINT digital_object_pkey PRIMARY KEY (digital_objectid, auth_list_naid, desc_naid, lifecycle_numberid, special_pjt_for_authid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: finding_aid 
--

CREATE TABLE finding_aid(
    finding_aidid            bigserial    NOT NULL,
    desc_naid                bigint         NOT NULL,
    finding_aid_type_naid    bigint,
    finding_aid_note         text,
    finding_aid_source       text,
    finding_aid_url_naid     bigint,
    object_type_naid         bigint,
    CONSTRAINT finding_aid_pkey PRIMARY KEY (finding_aidid, desc_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: general_media_type 
--

CREATE TABLE general_media_type(
    general_media_typeid      bigserial    NOT NULL,
    media_occurrenceid        bigserial    NOT NULL,
    auth_list_naid            bigint,
    lifecycle_numberid        bigserial,
    physical_occurenceid      bigserial    NOT NULL,
    desc_naid                 bigint         NOT NULL,
    special_pjt_for_authid    bigserial,
    prim_auth_naid            bigint,
    CONSTRAINT general_media_type_pkey PRIMARY KEY (general_media_typeid, media_occurrenceid, physical_occurenceid, desc_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: general_record_type 
--

CREATE TABLE general_record_type(
    general_record_typeid     bigserial    NOT NULL,
    desc_naid                 bigint         NOT NULL,
    auth_list_naid            bigint         NOT NULL,
    lifecycle_numberid        bigserial    NOT NULL,
    special_pjt_for_authid    bigserial    NOT NULL,
    prim_auth_naid            bigint         NOT NULL,
    CONSTRAINT general_record_type_pkey PRIMARY KEY (general_record_typeid, auth_list_naid, desc_naid, lifecycle_numberid, special_pjt_for_authid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: holding_measurment 
--

CREATE TABLE holding_measurment(
    holding_measurmentid        bigserial    NOT NULL,
    physical_occurenceid        bigserial    NOT NULL,
    holding_measurment_count    text,
    auth_list_naid              bigint         NOT NULL,
    lifecycle_numberid          bigserial    NOT NULL,
    desc_naid                   bigint         NOT NULL,
    special_pjt_for_authid      bigserial    NOT NULL,
    prim_auth_naid              bigint         NOT NULL,
    CONSTRAINT holding_measurment_pkey PRIMARY KEY (holding_measurmentid, auth_list_naid, lifecycle_numberid, desc_naid, physical_occurenceid, special_pjt_for_authid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: jurisdiction 
--

CREATE TABLE jurisdiction(
    jurisdictionid         bigserial    NOT NULL,
    geo_place_name_naid    bigint,
    prim_auth_naid         bigint         NOT NULL,
    CONSTRAINT jurisdiction_pkey PRIMARY KEY (jurisdictionid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: language 
--

CREATE TABLE language(
    languageid       bigserial    NOT NULL,
    language_naid    bigint,
    desc_naid        bigint         NOT NULL,
    CONSTRAINT language_pkey PRIMARY KEY (languageid, desc_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: lifecycle_number 
--

CREATE TABLE lifecycle_number(
    lifecycle_numberid       bigserial    NOT NULL,
    desc_naid                bigint         NOT NULL,
    auth_list_naid           bigint,
    lifecycle_number_type    text,
    CONSTRAINT "upk_lifecyclenumberID" PRIMARY KEY (lifecycle_numberid, desc_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: location 
--

CREATE TABLE location(
    locationid                bigserial    NOT NULL,
    note                      text,
    physical_occurrenceid     bigint,
    auth_list_naid            bigint         NOT NULL,
    lifecycle_numberid        bigserial    NOT NULL,
    desc_naid                 bigint         NOT NULL,
    special_pjt_for_authid    bigserial    NOT NULL,
    prim_auth_naid            bigint         NOT NULL,
    CONSTRAINT location_pkey PRIMARY KEY (locationid, auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: media_occurrence 
--

CREATE TABLE media_occurrence(
    media_occurrenceid                   bigserial         NOT NULL,
    physical_occurenceid                 bigserial         NOT NULL,
    specific_media_type_naid             bigint,
    containerid                          text,
    media_occurrence_note                text,
    physical_restriction_note            text,
    technical_access_requirement_note    text,
    piece_count                          numeric(10, 0),
    reproduction_count                   numeric(10, 0),
    dimension_naid                       bigint,
    height                               numeric(10, 0),
    width                                numeric(10, 0),
    depth                                numeric(10, 0),
    color_naid                           bigint,
    process_naid                         bigint,
    base_naid                            bigint,
    emulsion_naid                        bigint,
    desc_naid                            bigint              NOT NULL,
    CONSTRAINT media_occurrence_pkey PRIMARY KEY (media_occurrenceid, physical_occurenceid, desc_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: microform_pub 
--

CREATE TABLE microform_pub(
    microform_pubid       bigserial    NOT NULL,
    microform_pub_note    text,
    desc_naid             bigint         NOT NULL,
    CONSTRAINT microform_pub_pkey PRIMARY KEY (microform_pubid, desc_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: online_resource 
--

CREATE TABLE online_resource(
    "online_resourceID"       bigserial    NOT NULL,
    desc_naid                 bigint         NOT NULL,
    auth_list_naid            bigint         NOT NULL,
    lifecycle_numberid        bigserial    NOT NULL,
    special_pjt_for_authid    bigserial    NOT NULL,
    prim_auth_naid            bigint         NOT NULL,
    CONSTRAINT "upk_onlineresourceID" PRIMARY KEY ("online_resourceID", auth_list_naid, desc_naid, lifecycle_numberid, special_pjt_for_authid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: org_name_ref 
--

CREATE TABLE org_name_ref(
    org_name_refid    bigserial    NOT NULL,
    org_name_naid     bigint,
    prim_auth_naid    bigint         NOT NULL,
    CONSTRAINT org_name_ref_pkey PRIMARY KEY (org_name_refid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: org_personal_contributor 
--

CREATE TABLE org_personal_contributor(
    org_personal_contributorid                bigserial    NOT NULL,
    desc_naid                                 bigint         NOT NULL,
    personal_or_organizational_contributor    boolean,
    auth_list_naid                            bigint         NOT NULL,
    prim_auth_naid                            bigint         NOT NULL,
    lifecycle_numberid                        bigserial    NOT NULL,
    special_pjt_for_authid                    bigserial    NOT NULL,
    CONSTRAINT org_personal_contributor_pkey PRIMARY KEY (org_personal_contributorid, auth_list_naid, desc_naid, lifecycle_numberid, prim_auth_naid, special_pjt_for_authid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: organizational_reference 
--

CREATE TABLE organizational_reference(
    organizational_referenceid    bigserial    NOT NULL,
    orgref_naid                   bigint,
    prim_auth_naid                bigint         NOT NULL,
    CONSTRAINT organizational_reference_pkey PRIMARY KEY (organizational_referenceid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: other_preservation_characteristic 
--

CREATE TABLE other_preservation_characteristic(
    other_preservation_characteristicid    bigserial    NOT NULL,
    media_occurrenceid                     bigserial    NOT NULL,
    auth_list_naid                         bigint         NOT NULL,
    lifecycle_numberid                     bigserial    NOT NULL,
    desc_naid                              bigint         NOT NULL,
    physical_occurenceid                   bigserial    NOT NULL,
    special_pjt_for_authid                 bigserial    NOT NULL,
    prim_auth_naid                         bigint         NOT NULL,
    CONSTRAINT other_preservation_characteristic_pkey PRIMARY KEY (other_preservation_characteristicid, auth_list_naid, lifecycle_numberid, desc_naid, media_occurrenceid, physical_occurenceid, special_pjt_for_authid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: pers_org_topsub_geo_sppr_ref 
--

CREATE TABLE pers_org_topsub_geo_sppr_ref(
    pers_org_topsub_geo_sppr_refid    bigserial    NOT NULL,
    auth_reference_type_donor         text,
    prim_auth_naid                    bigint         NOT NULL,
    desc_naid                         bigint         NOT NULL,
    CONSTRAINT pers_org_topsub_geo_sppr_ref_pkey PRIMARY KEY (pers_org_topsub_geo_sppr_refid, desc_naid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: personal_reference 
--

CREATE TABLE personal_reference(
    personal_referenceid    bigserial    NOT NULL,
    prim_auth_naid          bigint         NOT NULL,
    person_naid             bigint,
    CONSTRAINT personal_reference_pkey PRIMARY KEY (personal_referenceid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: physical_occurrence 
--

CREATE TABLE physical_occurrence(
    physical_occurenceid       bigserial    NOT NULL,
    extent                     text,
    physical_occurance_note    text,
    gpra_indicator_naid        bigint,
    container_list             text,
    copy_status_naid           bigint,
    desc_naid                  bigint         NOT NULL,
    CONSTRAINT physical_occurence_pkey PRIMARY KEY (physical_occurenceid, desc_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: predecessor_successor 
--

CREATE TABLE predecessor_successor(
    predecessor_successorid       bigserial    NOT NULL,
    prim_auth_naid                bigint         NOT NULL,
    predecessor_successor_naid    bigint,
    predecessor_successor         text,
    CONSTRAINT predecessor_successor_pkey PRIMARY KEY (predecessor_successorid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: primary_authority 
--

CREATE TABLE primary_authority(
    prim_auth_naid                   bigint              NOT NULL,
    term_name                        text,
    auth_type                        text,
    name_heading                     text,
    full_name                        text,
    numerator                        text,
    personal_title                   text,
    birthdate_qualifier_naid         bigint,
    deathdate_qualifier_naid         bigint,
    birthdate                        jsonb,
    deathdate                        jsonb,
    biographical_note                text,
    source_note                      text,
    proposer_name                    text,
    proposal_date                    date,
    reference_unit_naid              bigint,
    record_source_naid               bigint,
    naco_submitted                   boolean,
    import_rec_ctl_no                text,
    notes                            text,
    scope_note                       text,
    saco_submitted                   boolean,
    lat_long                         numeric(10, 0),
    admin_hist_note                  text,
    establish_date                   jsonb,
    abolish_date                     jsonb,
    establish_date_qualifier_naid    bigint,
    abolish_date_qualifier_naid      bigint,
    last_changed_date                timestamp,
    last_approved_date               timestamp,
    last_brought_under_edit          timestamp,
    approval_history                 json,
    changehistory                    json,
    brought_under_edit_history       json,
    variant_person_naid              bigint,
    ispreferred                      boolean,
    variant_org_naid                 bigint,
    is_under_edit                    boolean,
    CONSTRAINT primauth_pkey PRIMARY KEY (prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: program_area_reference 
--

CREATE TABLE program_area_reference(
    program_area_referenceid    bigserial    NOT NULL,
    prim_auth_naid              bigint         NOT NULL,
    program_area_naid           bigint,
    CONSTRAINT program_area_reference_pkey PRIMARY KEY (program_area_referenceid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: reference_unit 
--

CREATE TABLE reference_unit(
    reference_unitid          bigserial    NOT NULL,
    physical_occurenceid      bigserial    NOT NULL,
    physical_occurrenceid     bigint,
    auth_list_naid            bigint         NOT NULL,
    lifecycle_numberid        bigserial    NOT NULL,
    desc_naid                 bigint         NOT NULL,
    special_pjt_for_authid    bigserial    NOT NULL,
    prim_auth_naid            bigint         NOT NULL,
    CONSTRAINT reference_unit_pkey PRIMARY KEY (reference_unitid, auth_list_naid, lifecycle_numberid, desc_naid, physical_occurenceid, special_pjt_for_authid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: see_also 
--

CREATE TABLE see_also(
    seealsoid              bigserial    NOT NULL,
    seealso_person_naid    bigint,
    prim_auth_naid         bigint         NOT NULL,
    CONSTRAINT seealso_pkey PRIMARY KEY (seealsoid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: series_former_parent 
--

CREATE TABLE series_former_parent(
    series_former_parentid    bigserial    NOT NULL,
    desc_naid                 bigint         NOT NULL,
    former_parent_naid        bigint,
    CONSTRAINT series_former_parent_pkey PRIMARY KEY (series_former_parentid, desc_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: special_pjt_for_auth 
--

CREATE TABLE special_pjt_for_auth(
    special_pjt_for_authid    bigserial    NOT NULL,
    prim_auth_naid            bigint         NOT NULL,
    CONSTRAINT special_pjt_for_auth_pkey PRIMARY KEY (special_pjt_for_authid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: special_pjt_for_desc 
--

CREATE TABLE special_pjt_for_desc(
    special_pjt_for_descid    bigserial    NOT NULL,
    auth_list_naid            bigint         NOT NULL,
    desc_naid                 bigint         NOT NULL,
    lifecycle_numberid        bigserial    NOT NULL,
    special_pjt_for_authid    bigserial    NOT NULL,
    prim_auth_naid            bigint         NOT NULL,
    CONSTRAINT special_pjt_for_desc_pkey PRIMARY KEY (special_pjt_for_descid, auth_list_naid, desc_naid, lifecycle_numberid, special_pjt_for_authid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: specific_access_restriction 
--

CREATE TABLE specific_access_restriction(
    specific_access_restrictionid       bigserial    NOT NULL,
    specific_access_restriction_naid    bigint,
    access_restrictionid                bigserial    NOT NULL,
    security_classification_naid        bigint,
    auth_list_naid                      bigint         NOT NULL,
    desc_naid                           bigint         NOT NULL,
    lifecycle_numberid                  bigserial    NOT NULL,
    special_pjt_for_authid              bigserial    NOT NULL,
    prim_auth_naid                      bigint         NOT NULL,
    CONSTRAINT specific_access_restriction_pkey PRIMARY KEY (specific_access_restrictionid, access_restrictionid, auth_list_naid, desc_naid, lifecycle_numberid, special_pjt_for_authid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: specific_use_restriction 
--

CREATE TABLE specific_use_restriction(
    specific_use_restrictionid    bigserial    NOT NULL,
    auth_list_naid                bigint         NOT NULL,
    use_restrictionid             bigserial    NOT NULL,
    desc_naid                     bigint         NOT NULL,
    lifecycle_numberid            bigserial    NOT NULL,
    special_pjt_for_authid        bigserial    NOT NULL,
    prim_auth_naid                bigint         NOT NULL,
    CONSTRAINT specific_use_restriction_pkey PRIMARY KEY (specific_use_restrictionid, use_restrictionid, desc_naid, auth_list_naid, lifecycle_numberid, special_pjt_for_authid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: use_for 
--

CREATE TABLE use_for(
    useforid          bigserial    NOT NULL,
    prim_auth_naid    bigint         NOT NULL,
    usefor_naid       bigint,
    CONSTRAINT usefor_pkey PRIMARY KEY (useforid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: use_restriction 
--

CREATE TABLE use_restriction(
    use_restrictionid         bigserial    NOT NULL,
    note                      text,
    desc_naid                 bigint         NOT NULL,
    auth_list_naid            bigint         NOT NULL,
    lifecycle_numberid        bigserial    NOT NULL,
    special_pjt_for_authid    bigserial    NOT NULL,
    prim_auth_naid            bigint         NOT NULL,
    CONSTRAINT use_restriction_pkey PRIMARY KEY (use_restrictionid, auth_list_naid, desc_naid, lifecycle_numberid, special_pjt_for_authid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- TABLE: variant_control_number 
--

CREATE TABLE variant_control_number(
    variant_control_numberid    bigserial    NOT NULL,
    variant_ctl_no              text,
    variant_ctl_no_note         text,
    auth_list_naid              bigint         NOT NULL,
    desc_naid                   bigint         NOT NULL,
    lifecycle_numberid          bigserial    NOT NULL,
    special_pjt_for_authid      bigserial    NOT NULL,
    prim_auth_naid              bigint         NOT NULL,
    CONSTRAINT variant_control_number_pkey PRIMARY KEY (variant_control_numberid, auth_list_naid, desc_naid, lifecycle_numberid, special_pjt_for_authid, prim_auth_naid) USING INDEX TABLESPACE pg_default 
)
;




-- 
-- INDEX: "Ref264" 
--

CREATE INDEX "Ref264" ON access_restriction(auth_list_naid)
;
-- 
-- INDEX: "Ref56" 
--

CREATE INDEX "Ref56" ON access_restriction(desc_naid)
;
-- 
-- INDEX: "Ref3150" 
--

CREATE INDEX "Ref3150" ON authority_list(special_pjt_for_authid)
;
-- 
-- INDEX: "Ref3993" 
--

CREATE INDEX "Ref3993" ON authority_list(lifecycle_numberid)
;
-- 
-- INDEX: "Ref2634" 
--

CREATE INDEX "Ref2634" ON broader_narrower_related_term(prim_auth_naid)
;
-- 
-- INDEX: "Ref251" 
--

CREATE INDEX "Ref251" ON creating_org_indv(auth_list_naid)
;
-- 
-- INDEX: "Ref2652" 
--

CREATE INDEX "Ref2652" ON creating_org_indv(prim_auth_naid)
;
-- 
-- INDEX: "Ref553" 
--

CREATE INDEX "Ref553" ON creating_org_indv(desc_naid)
;
-- 
-- INDEX: "Ref573" 
--

CREATE INDEX "Ref573" ON digital_object(desc_naid)
;
-- 
-- INDEX: "Ref276" 
--

CREATE INDEX "Ref276" ON digital_object(auth_list_naid)
;
-- 
-- INDEX: "Ref581" 
--

CREATE INDEX "Ref581" ON finding_aid(desc_naid)
;
-- 
-- INDEX: "Ref261" 
--

CREATE INDEX "Ref261" ON general_media_type(auth_list_naid)
;
-- 
-- INDEX: "Ref1688" 
--

CREATE INDEX "Ref1688" ON general_media_type(media_occurrenceid)
;
-- 
-- INDEX: "Ref265" 
--

CREATE INDEX "Ref265" ON general_record_type(auth_list_naid)
;
-- 
-- INDEX: "Ref558" 
--

CREATE INDEX "Ref558" ON general_record_type(desc_naid)
;
-- 
-- INDEX: "Ref267" 
--

CREATE INDEX "Ref267" ON holding_measurment(auth_list_naid)
;
-- 
-- INDEX: "Ref2486" 
--

CREATE INDEX "Ref2486" ON holding_measurment(physical_occurenceid)
;
-- 
-- INDEX: "Ref2672" 
--

CREATE INDEX "Ref2672" ON jurisdiction(prim_auth_naid)
;
-- 
-- INDEX: "Ref575" 
--

CREATE INDEX "Ref575" ON language(desc_naid)
;
-- 
-- INDEX: "Ref590" 
--

CREATE INDEX "Ref590" ON lifecycle_number(desc_naid)
;
-- 
-- INDEX: "Ref263" 
--

CREATE INDEX "Ref263" ON location(auth_list_naid)
;
-- 
-- INDEX: "Ref2479" 
--

CREATE INDEX "Ref2479" ON media_occurrence(physical_occurenceid)
;
-- 
-- INDEX: "Ref557" 
--

CREATE INDEX "Ref557" ON microform_pub(desc_naid)
;
-- 
-- INDEX: "Ref591" 
--

CREATE INDEX "Ref591" ON online_resource(desc_naid)
;
-- 
-- INDEX: "Ref292" 
--

CREATE INDEX "Ref292" ON online_resource(auth_list_naid)
;
-- 
-- INDEX: "Ref2645" 
--

CREATE INDEX "Ref2645" ON org_name_ref(prim_auth_naid)
;
-- 
-- INDEX: "Ref269" 
--

CREATE INDEX "Ref269" ON org_personal_contributor(auth_list_naid)
;
-- 
-- INDEX: "Ref2670" 
--

CREATE INDEX "Ref2670" ON org_personal_contributor(prim_auth_naid)
;
-- 
-- INDEX: "Ref59" 
--

CREATE INDEX "Ref59" ON org_personal_contributor(desc_naid)
;
-- 
-- INDEX: "Ref2646" 
--

CREATE INDEX "Ref2646" ON organizational_reference(prim_auth_naid)
;
-- 
-- INDEX: "Ref1689" 
--

CREATE INDEX "Ref1689" ON other_preservation_characteristic(media_occurrenceid)
;
-- 
-- INDEX: "Ref260" 
--

CREATE INDEX "Ref260" ON other_preservation_characteristic(auth_list_naid)
;
-- 
-- INDEX: "Ref571" 
--

CREATE INDEX "Ref571" ON pers_org_topsub_geo_sppr_ref(desc_naid)
;
-- 
-- INDEX: "Ref2648" 
--

CREATE INDEX "Ref2648" ON pers_org_topsub_geo_sppr_ref(prim_auth_naid)
;
-- 
-- INDEX: "Ref2637" 
--

CREATE INDEX "Ref2637" ON personal_reference(prim_auth_naid)
;
-- 
-- INDEX: "Ref574" 
--

CREATE INDEX "Ref574" ON physical_occurrence(desc_naid)
;
-- 
-- INDEX: "Ref2636" 
--

CREATE INDEX "Ref2636" ON predecessor_successor(prim_auth_naid)
;
-- 
-- INDEX: "Ref2635" 
--

CREATE INDEX "Ref2635" ON program_area_reference(prim_auth_naid)
;
-- 
-- INDEX: "Ref2485" 
--

CREATE INDEX "Ref2485" ON reference_unit(physical_occurenceid)
;
-- 
-- INDEX: "Ref287" 
--

CREATE INDEX "Ref287" ON reference_unit(auth_list_naid)
;
-- 
-- INDEX: "Ref2647" 
--

CREATE INDEX "Ref2647" ON see_also(prim_auth_naid)
;
-- 
-- INDEX: "Ref580" 
--

CREATE INDEX "Ref580" ON series_former_parent(desc_naid)
;
-- 
-- INDEX: "Ref2649" 
--

CREATE INDEX "Ref2649" ON special_pjt_for_auth(prim_auth_naid)
;
-- 
-- INDEX: "Ref254" 
--

CREATE INDEX "Ref254" ON special_pjt_for_desc(auth_list_naid)
;
-- 
-- INDEX: "Ref555" 
--

CREATE INDEX "Ref555" ON special_pjt_for_desc(desc_naid)
;
-- 
-- INDEX: "Ref182" 
--

CREATE INDEX "Ref182" ON specific_access_restriction(access_restrictionid)
;
-- 
-- INDEX: "Ref266" 
--

CREATE INDEX "Ref266" ON specific_use_restriction(auth_list_naid)
;
-- 
-- INDEX: "Ref3584" 
--

CREATE INDEX "Ref3584" ON specific_use_restriction(use_restrictionid)
;
-- 
-- INDEX: "Ref2644" 
--

CREATE INDEX "Ref2644" ON use_for(prim_auth_naid)
;
-- 
-- INDEX: "Ref556" 
--

CREATE INDEX "Ref556" ON use_restriction(desc_naid)
;
-- 
-- INDEX: "Ref259" 
--

CREATE INDEX "Ref259" ON use_restriction(auth_list_naid)
;
-- 
-- INDEX: "Ref277" 
--

CREATE INDEX "Ref277" ON variant_control_number(auth_list_naid)
;
-- 
-- INDEX: "Ref578" 
--

CREATE INDEX "Ref578" ON variant_control_number(desc_naid)
;
-- 
-- TABLE: access_restriction 
--

ALTER TABLE access_restriction ADD CONSTRAINT "Refauthority_list641" 
    FOREIGN KEY (auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES authority_list(auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
;

ALTER TABLE access_restriction ADD CONSTRAINT "Refdescription61" 
    FOREIGN KEY (desc_naid)
    REFERENCES description(desc_naid)
;


-- 
-- TABLE: authority_list 
--

ALTER TABLE authority_list ADD CONSTRAINT "Refspecial_pjt_for_auth501" 
    FOREIGN KEY (special_pjt_for_authid, prim_auth_naid)
    REFERENCES special_pjt_for_auth(special_pjt_for_authid, prim_auth_naid)
;

ALTER TABLE authority_list ADD CONSTRAINT "Reflifecycle_number931" 
    FOREIGN KEY (lifecycle_numberid, desc_naid)
    REFERENCES lifecycle_number(lifecycle_numberid, desc_naid)
;


-- 
-- TABLE: broader_narrower_related_term 
--

ALTER TABLE broader_narrower_related_term ADD CONSTRAINT "Refprimary_authority341" 
    FOREIGN KEY (prim_auth_naid)
    REFERENCES primary_authority(prim_auth_naid)
;


-- 
-- TABLE: creating_org_indv 
--

ALTER TABLE creating_org_indv ADD CONSTRAINT "Refauthority_list511" 
    FOREIGN KEY (auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES authority_list(auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
;

ALTER TABLE creating_org_indv ADD CONSTRAINT "Refprimary_authority521" 
    FOREIGN KEY (prim_auth_naid)
    REFERENCES primary_authority(prim_auth_naid)
;

ALTER TABLE creating_org_indv ADD CONSTRAINT "Refdescription531" 
    FOREIGN KEY (desc_naid)
    REFERENCES description(desc_naid)
;


-- 
-- TABLE: digital_object 
--

ALTER TABLE digital_object ADD CONSTRAINT "Refdescription731" 
    FOREIGN KEY (desc_naid)
    REFERENCES description(desc_naid)
;

ALTER TABLE digital_object ADD CONSTRAINT "Refauthority_list761" 
    FOREIGN KEY (auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES authority_list(auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
;


-- 
-- TABLE: finding_aid 
--

ALTER TABLE finding_aid ADD CONSTRAINT "Refdescription811" 
    FOREIGN KEY (desc_naid)
    REFERENCES description(desc_naid)
;


-- 
-- TABLE: general_media_type 
--

ALTER TABLE general_media_type ADD CONSTRAINT "Refauthority_list611" 
    FOREIGN KEY (auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES authority_list(auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
;

ALTER TABLE general_media_type ADD CONSTRAINT "Refmedia_occurrence881" 
    FOREIGN KEY (media_occurrenceid, physical_occurenceid, desc_naid)
    REFERENCES media_occurrence(media_occurrenceid, physical_occurenceid, desc_naid)
;


-- 
-- TABLE: general_record_type 
--

ALTER TABLE general_record_type ADD CONSTRAINT "Refauthority_list651" 
    FOREIGN KEY (auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES authority_list(auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
;

ALTER TABLE general_record_type ADD CONSTRAINT "Refdescription581" 
    FOREIGN KEY (desc_naid)
    REFERENCES description(desc_naid)
;


-- 
-- TABLE: holding_measurment 
--

ALTER TABLE holding_measurment ADD CONSTRAINT "Refauthority_list671" 
    FOREIGN KEY (auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES authority_list(auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
;

ALTER TABLE holding_measurment ADD CONSTRAINT "Refphysical_occurrence861" 
    FOREIGN KEY (physical_occurenceid, desc_naid)
    REFERENCES physical_occurrence(physical_occurenceid, desc_naid)
;


-- 
-- TABLE: jurisdiction 
--

ALTER TABLE jurisdiction ADD CONSTRAINT "Refprimary_authority721" 
    FOREIGN KEY (prim_auth_naid)
    REFERENCES primary_authority(prim_auth_naid)
;


-- 
-- TABLE: language 
--

ALTER TABLE language ADD CONSTRAINT "Refdescription751" 
    FOREIGN KEY (desc_naid)
    REFERENCES description(desc_naid)
;


-- 
-- TABLE: lifecycle_number 
--

ALTER TABLE lifecycle_number ADD CONSTRAINT "Refdescription901" 
    FOREIGN KEY (desc_naid)
    REFERENCES description(desc_naid)
;


-- 
-- TABLE: location 
--

ALTER TABLE location ADD CONSTRAINT "Refauthority_list631" 
    FOREIGN KEY (auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES authority_list(auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
;


-- 
-- TABLE: media_occurrence 
--

ALTER TABLE media_occurrence ADD CONSTRAINT "Refphysical_occurrence791" 
    FOREIGN KEY (physical_occurenceid, desc_naid)
    REFERENCES physical_occurrence(physical_occurenceid, desc_naid)
;


-- 
-- TABLE: microform_pub 
--

ALTER TABLE microform_pub ADD CONSTRAINT "Refdescription571" 
    FOREIGN KEY (desc_naid)
    REFERENCES description(desc_naid)
;


-- 
-- TABLE: online_resource 
--

ALTER TABLE online_resource ADD CONSTRAINT "Refdescription911" 
    FOREIGN KEY (desc_naid)
    REFERENCES description(desc_naid)
;

ALTER TABLE online_resource ADD CONSTRAINT "Refauthority_list921" 
    FOREIGN KEY (auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES authority_list(auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
;


-- 
-- TABLE: org_name_ref 
--

ALTER TABLE org_name_ref ADD CONSTRAINT "Refprimary_authority451" 
    FOREIGN KEY (prim_auth_naid)
    REFERENCES primary_authority(prim_auth_naid)
;


-- 
-- TABLE: org_personal_contributor 
--

ALTER TABLE org_personal_contributor ADD CONSTRAINT "Refauthority_list691" 
    FOREIGN KEY (auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES authority_list(auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
;

ALTER TABLE org_personal_contributor ADD CONSTRAINT "Refprimary_authority701" 
    FOREIGN KEY (prim_auth_naid)
    REFERENCES primary_authority(prim_auth_naid)
;

ALTER TABLE org_personal_contributor ADD CONSTRAINT "Refdescription92" 
    FOREIGN KEY (desc_naid)
    REFERENCES description(desc_naid)
;


-- 
-- TABLE: organizational_reference 
--

ALTER TABLE organizational_reference ADD CONSTRAINT "Refprimary_authority461" 
    FOREIGN KEY (prim_auth_naid)
    REFERENCES primary_authority(prim_auth_naid)
;


-- 
-- TABLE: other_preservation_characteristic 
--

ALTER TABLE other_preservation_characteristic ADD CONSTRAINT "Refmedia_occurrence891" 
    FOREIGN KEY (media_occurrenceid, physical_occurenceid, desc_naid)
    REFERENCES media_occurrence(media_occurrenceid, physical_occurenceid, desc_naid)
;

ALTER TABLE other_preservation_characteristic ADD CONSTRAINT "Refauthority_list601" 
    FOREIGN KEY (auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES authority_list(auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
;


-- 
-- TABLE: pers_org_topsub_geo_sppr_ref 
--

ALTER TABLE pers_org_topsub_geo_sppr_ref ADD CONSTRAINT "Refdescription711" 
    FOREIGN KEY (desc_naid)
    REFERENCES description(desc_naid)
;

ALTER TABLE pers_org_topsub_geo_sppr_ref ADD CONSTRAINT "Refprimary_authority481" 
    FOREIGN KEY (prim_auth_naid)
    REFERENCES primary_authority(prim_auth_naid)
;


-- 
-- TABLE: personal_reference 
--

ALTER TABLE personal_reference ADD CONSTRAINT "Refprimary_authority371" 
    FOREIGN KEY (prim_auth_naid)
    REFERENCES primary_authority(prim_auth_naid)
;


-- 
-- TABLE: physical_occurrence 
--

ALTER TABLE physical_occurrence ADD CONSTRAINT "Refdescription741" 
    FOREIGN KEY (desc_naid)
    REFERENCES description(desc_naid)
;


-- 
-- TABLE: predecessor_successor 
--

ALTER TABLE predecessor_successor ADD CONSTRAINT "Refprimary_authority361" 
    FOREIGN KEY (prim_auth_naid)
    REFERENCES primary_authority(prim_auth_naid)
;


-- 
-- TABLE: program_area_reference 
--

ALTER TABLE program_area_reference ADD CONSTRAINT "Refprimary_authority351" 
    FOREIGN KEY (prim_auth_naid)
    REFERENCES primary_authority(prim_auth_naid)
;


-- 
-- TABLE: reference_unit 
--

ALTER TABLE reference_unit ADD CONSTRAINT "Refphysical_occurrence851" 
    FOREIGN KEY (physical_occurenceid, desc_naid)
    REFERENCES physical_occurrence(physical_occurenceid, desc_naid)
;

ALTER TABLE reference_unit ADD CONSTRAINT "Refauthority_list871" 
    FOREIGN KEY (auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES authority_list(auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
;


-- 
-- TABLE: see_also 
--

ALTER TABLE see_also ADD CONSTRAINT "Refprimary_authority471" 
    FOREIGN KEY (prim_auth_naid)
    REFERENCES primary_authority(prim_auth_naid)
;


-- 
-- TABLE: series_former_parent 
--

ALTER TABLE series_former_parent ADD CONSTRAINT "Refdescription801" 
    FOREIGN KEY (desc_naid)
    REFERENCES description(desc_naid)
;


-- 
-- TABLE: special_pjt_for_auth 
--

ALTER TABLE special_pjt_for_auth ADD CONSTRAINT "Refprimary_authority491" 
    FOREIGN KEY (prim_auth_naid)
    REFERENCES primary_authority(prim_auth_naid)
;


-- 
-- TABLE: special_pjt_for_desc 
--

ALTER TABLE special_pjt_for_desc ADD CONSTRAINT "Refauthority_list541" 
    FOREIGN KEY (auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES authority_list(auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
;

ALTER TABLE special_pjt_for_desc ADD CONSTRAINT "Refdescription551" 
    FOREIGN KEY (desc_naid)
    REFERENCES description(desc_naid)
;


-- 
-- TABLE: specific_access_restriction 
--

ALTER TABLE specific_access_restriction ADD CONSTRAINT "Refaccess_restriction821" 
    FOREIGN KEY (access_restrictionid, auth_list_naid, desc_naid, lifecycle_numberid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES access_restriction(access_restrictionid, auth_list_naid, desc_naid, lifecycle_numberid, special_pjt_for_authid, prim_auth_naid)
;


-- 
-- TABLE: specific_use_restriction 
--

ALTER TABLE specific_use_restriction ADD CONSTRAINT "Refauthority_list661" 
    FOREIGN KEY (auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES authority_list(auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
;

ALTER TABLE specific_use_restriction ADD CONSTRAINT "Refuse_restriction841" 
    FOREIGN KEY (use_restrictionid, auth_list_naid, desc_naid, lifecycle_numberid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES use_restriction(use_restrictionid, auth_list_naid, desc_naid, lifecycle_numberid, special_pjt_for_authid, prim_auth_naid)
;


-- 
-- TABLE: use_for 
--

ALTER TABLE use_for ADD CONSTRAINT "Refprimary_authority441" 
    FOREIGN KEY (prim_auth_naid)
    REFERENCES primary_authority(prim_auth_naid)
;


-- 
-- TABLE: use_restriction 
--

ALTER TABLE use_restriction ADD CONSTRAINT "Refdescription561" 
    FOREIGN KEY (desc_naid)
    REFERENCES description(desc_naid)
;

ALTER TABLE use_restriction ADD CONSTRAINT "Refauthority_list591" 
    FOREIGN KEY (auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES authority_list(auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
;


-- 
-- TABLE: variant_control_number 
--

ALTER TABLE variant_control_number ADD CONSTRAINT "Refauthority_list771" 
    FOREIGN KEY (auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
    REFERENCES authority_list(auth_list_naid, lifecycle_numberid, desc_naid, special_pjt_for_authid, prim_auth_naid)
;

ALTER TABLE variant_control_number ADD CONSTRAINT "Refdescription781" 
    FOREIGN KEY (desc_naid)
    REFERENCES description(desc_naid)
;


