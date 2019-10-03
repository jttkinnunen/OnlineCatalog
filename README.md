# OnlineCatalog
project2018FCG







#TODO clean this stuff
#### Groovy Jenkins isntructions https://gist.github.com/arehmandev/736daba40a3e1ef1fbe939c6674d7da8
This might not work perfectly...

### wild card imports
Don't use wildcard imports, to disable automatic wild
go to Idea settings -> search kotlin -> Under codestyle/kotlin 
-> imports -> choose: single line imports.

That is kinda bad instructions for now.













# Rest Api description

**Add User**
----
  Adds a single new user. <br />
  **Requires Admin Rights**

* **URL**

  /addUser

* **Method:**

  `POST`
  
*  **URL Params**

    None

* **Data Params**

   **Required:**
 
   ``` 
   { 
        "token": [string],
        "first_name": [string],
        "last_name": [string],
        "email": [string],
        "admin": [boolean],
        "password": [string]   
   }
   ```
   
* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `{ success: true }`
 
* **Error Response:**

  * **Code:** 500 INTERNAL SERVER ERROR <br />
    **Content:** `{ error : "Database Error" }`

  OR

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** `{ error : "This requires admin permissions." }`
    
  OR

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** `{ error : "Authentication Error" }`

* **Sample Call:**

   ``` 
   { 
        "token": "db75d141-92df-4181-91a1-f775dc27fbe6",
        "first_name": "Antti",
        "last_name": "Pantti",
        "email": "elefantti@gmail.com",
        "admin": true,
        "password": "salainen salasana"   
   }
   ```
   
   **Get Users**
   ----
     Gets all Users or just those users whoms IDs were given <br />
       **Requires Admin Rights**

   
   * **URL**
   
     /getUsers
   
   * **Method:**
   
     `POST`
     
   *  **URL Params**
   
       None
   
   * **Data Params**
   
      **Required:**
    
      ``` 
      { 
           "token": [string] 
      }
      ```
    
        **Optional:**
        
      ``` 
      { 
           "ids": [ [int], [int] ],
      }
      ```
   * **Success Response:**
   
     * **Code:** 200 <br />
       **Content:**       
        ``` 
        { 
            "users": [
                { 
                    "first_name": "Antti",
                    "last_name": "Pantti",
                    "email": "elefantti@gmail.com",
                    "admin": true,
                },
                { 
                    "first_name": "Matti",
                    "last_name": "Mallikas",
                    "email": "malli@gmail.com",
                    "admin": false,
                }
            ]
        }
        ```
    
   * **Error Response:**
   
     * **Code:** 500 INTERNAL SERVER ERROR <br />
       **Content:** `{ error : "Database Error" }`
   
     OR
   
     * **Code:** 401 UNAUTHORIZED <br />
       **Content:** `{ error : "This requires admin permissions." }`
       
     OR
   
     * **Code:** 401 UNAUTHORIZED <br />
       **Content:** `{ error : "Authentication Error" }`
   
   * **Sample Call:**
   
      ``` 
      { 
           "token": "db75d141-92df-4181-91a1-f775dc27fbe6",
           "ids": [1, 2]
      }
      ```