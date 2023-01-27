-- SET REFERENTIAL_INTEGRITY FALSE;
UPDATE elimination_user SET target_subject = NULL;
UPDATE elimination_user SET eliminated_by_subject = NULL;
delete from elimination_user;
-- SET REFERENTIAL_INTEGRITY TRUE;