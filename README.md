 - Cinemate (Netflix Clone, REST API)
 - A service for viewing video content. Thesis on backend. IT Step(2021-2025).

-------------------------------------------------------
 - Redis for:
    - data from db: Role, UserRole, AuthProvider, UserProvider, Actor, Genre, Warning, ContentActor, ContentGenre, ContentWarning
    - access/refresh token
    - userdetails object
    - users token for reset password
 - 
 - ![Image](https://github.com/user-attachments/assets/9d6c811b-1423-44fb-8dc4-d738894f3913)
 - ![Image](https://github.com/user-attachments/assets/bc4f834c-9f5e-4093-94f3-45ff3872aae8)

-------------------------------------------------------
 - Send email for url reset password
 -
 - ![Image](https://github.com/user-attachments/assets/012d8adc-2b6a-4751-a07b-1fe82f64e3ee)

-------------------------------------------------------
 - Google authorization
 - 
 - ![Image](https://github.com/user-attachments/assets/e6a011ff-4ef6-4eb2-a51c-2a5e967ab347)

-------------------------------------------------------
 - Facebook authorization
 - 
 - ![Image](https://github.com/user-attachments/assets/fa2b3f64-df33-4e6e-b13b-a0b1e17af7fb)
-------------------------------------------------------
 --------------- (Endpoints) ---------------
 - login / register / logout / update_access_token / forgot_password / reset_password
 - get_current_user / get_user_by_id / update_current_user / update_user_by_id / add_user / delete_user_by_id
 - get_users (page, size, sortBy, isAsc, searchStr), get_all_roles / get_all_providers / get_all_content_types
 - add_content_type / get_all_warnings / add_warning / get_all_actors / add_actor / get_all_genres / add_genre
 - add_content / update_content / delete_content / get_contents (page, size, sortBy, isAsc, filters)
 - get_content_by_id / get_random_contents / get_contents_by_genre / get_contents_by_type / 
 - 
 -------------------- Admin User --------------------
 - ![Image](https://github.com/user-attachments/assets/1dd4dbf2-6e91-4afe-942a-5fd137b427e2)
 - ![Image](https://github.com/user-attachments/assets/1cc6d172-a20b-47fe-ac35-470bcdd58d2b)
 - ![Image](https://github.com/user-attachments/assets/fd19c429-dd48-4910-99a9-985bad2820e2)
 - ![Image](https://github.com/user-attachments/assets/c70f1b7c-9bf5-473a-91e2-722ebba619cb)
 - ![Image](https://github.com/user-attachments/assets/6634965a-ca90-4e5b-a242-fed5547dcc7e)
 - ![Image](https://github.com/user-attachments/assets/3d975973-108f-4965-a158-2c8bd09485ac)
 -------------------- Auth --------------------
 - ![Image](https://github.com/user-attachments/assets/b6621fdc-aad5-4f17-82e2-c40628bf69de)
 - ![Image](https://github.com/user-attachments/assets/694be405-b618-4295-9a1d-1c977028f304)
 - ![Image](https://github.com/user-attachments/assets/e25a9408-9326-4cc1-8bed-0c3fc1e8f71f)
 - ![Image](https://github.com/user-attachments/assets/7ccb9fe4-7cc3-4bc4-937c-113c75077df1)
 - ![Image](https://github.com/user-attachments/assets/11142571-02c2-476d-9fb6-fbfca03c9acd)
 - ![Image](https://github.com/user-attachments/assets/409f7669-5861-4876-bb18-96027f1790b2)
 - ![Image](https://github.com/user-attachments/assets/e8aa7510-af9a-47cc-9078-785a36d51ad7)
 -------------------- Current User --------------------
 - ![Image](https://github.com/user-attachments/assets/fc85c176-2a9e-4ba2-9fb7-1d22d992665b)
 - ![Image](https://github.com/user-attachments/assets/d27be7fe-596d-4df0-89ab-70b352007c52)
 -------------------- Contents (For Admin) --------------------
 - ![Image](https://github.com/user-attachments/assets/5c02c609-5c9e-4cf8-96fa-886574d19071)
 - ![Image](https://github.com/user-attachments/assets/4b65ba61-5963-404e-9396-cec982b55724)
 - ![Image](https://github.com/user-attachments/assets/1d43573b-5963-44e7-8b0f-6a63110c2ac3)
 - ![Image](https://github.com/user-attachments/assets/3bfa40f0-8d59-47c1-bb02-4facf5b8f70b)
 - ![Image](https://github.com/user-attachments/assets/b36c0201-933d-432b-8238-ea7fc4ea50cf)
 -------------------- Contents (For Users) --------------------
 - ![Image](https://github.com/user-attachments/assets/da7aa7e4-1fe4-4a5c-a648-c377762428d6)
 - ![Image](https://github.com/user-attachments/assets/4a57c38e-c6ea-4aca-8d80-b77de4efb4b3)
 - ![Image](https://github.com/user-attachments/assets/855d9650-0c12-4500-a7fe-7405aaf37c8b)
 -------------------- Roles / Providers --------------------
 - ![Image](https://github.com/user-attachments/assets/ee4f1642-84d6-4009-83a4-aa07baf41743)
 - ![Image](https://github.com/user-attachments/assets/ac40ed47-ab36-4a09-82d0-ed23f8d8d8d3)
 -------------------- Content Types --------------------
 - ![Image](https://github.com/user-attachments/assets/12294241-b9bb-4a16-afc2-34a8a2e34cc0)
 - ![Image](https://github.com/user-attachments/assets/f738859d-40d3-4619-8a55-a1dffc6ec197)
 -------------------- Warnings --------------------
 - ![Image](https://github.com/user-attachments/assets/56a5af99-033a-4b2d-98c0-cdd273310b5f)
 - ![Image](https://github.com/user-attachments/assets/6836b32e-4a8f-419e-b534-777c379de9dc)
 -------------------- Actors --------------------
 - ![Image](https://github.com/user-attachments/assets/5b7add82-5225-46e1-8245-5b7c9762a15b)
 - ![Image](https://github.com/user-attachments/assets/6d33d39d-8a56-4c05-aa46-1492c9a5b203)
 -------------------- Genres --------------------
 - ![Image](https://github.com/user-attachments/assets/2cd48f40-a981-47e9-8bde-7ea2e5b5b7f4)
 - ![Image](https://github.com/user-attachments/assets/86a949a8-63a2-431f-8c95-72728627fec9)
