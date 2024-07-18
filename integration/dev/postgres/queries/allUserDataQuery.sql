SELECT 
    "Users"."accountId",
    "Users"."userName", 
    "UserClaims"."claimValue" AS "email",
    "fullName"."claimValue" AS "fullName",
    "picture"."claimValue" AS "picture",
    "company"."claimValue" AS "company"
FROM 
    "Users"
INNER JOIN 
    "UserClaims" ON "Users"."accountId" = "UserClaims"."userId" AND "UserClaims"."claimId" = (
        SELECT "Claims"."claimId" FROM public."Claims" WHERE "Claims"."claimName" = 'email'
    )
LEFT JOIN 
    "UserClaims" AS "fullName" ON "Users"."accountId" = "fullName"."userId" AND "fullName"."claimId" = (
        SELECT "Claims"."claimId" FROM public."Claims" WHERE "Claims"."claimName" = 'name'
    )
LEFT JOIN 
    "UserClaims" AS "picture" ON "Users"."accountId" = "picture"."userId" AND "picture"."claimId" = (
        SELECT "Claims"."claimId" FROM public."Claims" WHERE "Claims"."claimName" = 'picture'
    )
LEFT JOIN 
    "UserClaims" AS "company" ON "Users"."accountId" = "company"."userId" AND "company"."claimId" = (
        SELECT "Claims"."claimId" FROM public."Claims" WHERE "Claims"."claimName" = 'company'
    );