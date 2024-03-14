SELECT 
    "Users"."accountId",
    "Users"."userName", 
    "UserClaims"."claimValue" AS "email",
    "fullName"."claimValue" AS "fullName"
FROM 
    "Users"
INNER JOIN 
    "UserClaims" ON "Users"."accountId" = "UserClaims"."userId" AND "UserClaims"."claimId" = '43d81f18-a6df-46f8-9034-1e7650c7639c'
LEFT JOIN 
    "UserClaims" AS "fullName" ON "Users"."accountId" = "fullName"."userId" AND "fullName"."claimId" = 'a1f103b0-0648-4fd7-9c31-012396bffbf3'
;