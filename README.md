# Spring-boot-3-jwt-security



![Screenshot from 2023-08-14 00-05-52](https://github.com/thatmayank/Spring-boot-3-jwt-security/assets/63830975/dbde025e-fd07-4d8e-928b-33205b602b74)


![Screenshot from 2023-08-15 11-43-31](https://github.com/thatmayank/Spring-boot-3-jwt-security/assets/63830975/122ed9e4-8208-41c5-a4e2-e376444b15f3)

TODO -- implementing user authentication and authorization using JSON Web Tokens (JWT) with the ability to log in on multiple devices simultaneously and log out from individual devices.

Steps: 
1. Store access token in database.
2. Generate access and refresh token for particular user on particular device.
3. If user press log out, revoke that particular access token in backend and flush refresh token in frontend. This will help in such way that user will be still logged in on other devices.
4. If user wants to reset password, then revoke all the access token for that particular user and logout from all devices.
5. Store refresh token on client side.
6. Pass access token in each request to maintain multiple users sessions.

![react-refresh-token-jwt-axios-interceptors-flow](https://github.com/thatmayank/Spring-boot-3-jwt-security/assets/63830975/925677c6-be07-4829-9626-8770d4d09ead)




   
