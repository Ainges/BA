SELECT "Users"."accountId","Users"."userName", "UserClaims"."claimValue"
FROM "Users"
INNER JOIN "UserClaims" 
	ON "Users"."accountId" = "UserClaims"."userId"
WHERE "UserClaims"."claimId" = '43d81f18-a6df-46f8-9034-1e7650c7639c';
