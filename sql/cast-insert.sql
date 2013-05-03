INSERT INTO TBL_CHARACTERS VALUES (1,'zombies','brains', 'Graargh');
INSERT INTO TBL_CHARACTERS VALUES (2,'pirates','rum','Aye Aye, Matey' );
INSERT INTO TBL_CHARACTERS VALUES (3,'vampires','blood','Scream');
INSERT INTO TBL_CHARACTERS VALUES (4,'ninjas','ramen','Sssh');

INSERT INTO TBL_COMMENTS VALUES (DEFAULT,1,'zombies','SKV','2011-09-09','A zombie is a creature that appears in books, films and popular culture');
INSERT INTO TBL_COMMENTS (MVCHAR_ID, MVCHAR_NAME, USERNAME, COMMENT_DATE, COMMENT) VALUES (4,'ninjas','aUser','2012-08-22','Ninjas are so silent');
INSERT INTO TBL_COMMENTS values (DEFAULT,3,'pirates','SKV','2011-09-09','Pirates are awesomely cool');